package allen.iotplatform.newrule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.gson.Gson;

public class ScriptTest {
	private static ConcurrentHashMap<String, Integer> cache = new ConcurrentHashMap<>();
	private static Map<String, ScriptBean> scriptBeanMap = new HashMap<>();
	static{
		try {
			List<String> lines = Files.readAllLines(Paths.get("/Users/allen/Desktop/aaa.json"), StandardCharsets.UTF_8);
			String json = lines.get(0);
			load(json, "version1");
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static synchronized void load(String json, String scriptName){
		ScriptBean scriptBean = new Gson().fromJson(json, ScriptBean.class);
		scriptBean.init();
		scriptBeanMap.put(scriptName, scriptBean);
	}
	
	/**
	 * 此函数每次收到上传参数会执行一次
	 * 整体思想是:
	 * (1) 从scriptBean中取得参数配置的模板，此Bean是公用的，任何内容都不允许修改，简单来说调用此Bean的任何set方法都是错的
	 * (2) 每次收到上传参数，需要配置inputMap，然后传入，此为输入参数目的是为初始化脚本环境服务，也是不允许修改的
	 * (3) 所有的修改都是在全局的cache中，使用hardCode做头来区分不同设备，cache是存储内部状态，服务于多次接收数据以判断报警的情况
	 * */
	public static List<String> dealIotAlarm(String scriptName, String hardCode, Map<String, Object> inputMap){
		List<String> errStateList = new ArrayList<>();
		ScriptBean scriptBean = scriptBeanMap.get(scriptName);
		
		// =========脚本环境初始化==================
		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
		// 将input参数放入本次脚本执行环境
		for (String key : inputMap.keySet()) {
			script.put(key, inputMap.get(key));
		}
		// 将缓存中的内部状态信息放入本次脚本执行环境，此处实现了将上次逻辑判断结果应用于本次逻辑判断的目的
		updateInnerState(hardCode, scriptBean.getInner_state(), script);
		
		// =========逻辑单元执行====================
		// 所有逻辑单元都需要执行一遍，由于会有依赖性，因此需要控制执行顺序，控制方法是采用递归调用的方式
		// 此处需要记录一下逻辑单元是否已经执行完毕
		Map<String, Boolean> finishedMap = new HashMap<>();
		for (LogicUnitBean logicCell : scriptBean.getLogic_unit()) {
			callLogicUnit(scriptBean, hardCode, logicCell.getLg_name(), script, finishedMap);
		}
		
		// =========本次执行完成后，查看预警状态=======
		// 由于所有逻辑单元都会执行，因此可能最终的报警状态不止一个，此处根据优先级别顺序进行排序输出
		// 另一点，如果出现报警，那么缓存需要清空，所有状态归为起始状态
		// 如果存在优先级则按照优先级要求将结果存入列表，如果不存在则将结果按照“state”中的标记进行显示（无特定顺序）
		
		if (scriptBean.getState_priority().keySet().size() > 0) {
			String[] priorityArr = scriptBean.getState_priority().keySet().toArray(new String[0]);
			Arrays.sort(priorityArr); // 由于是字符串排序，因此请注意"12"与"2"的大小问题
			for (String priorityName : priorityArr) {
				for (String stateName : scriptBean.getState_priority().get(priorityName)) {
					if (cache.get(hardCode + "#S#" + stateName) != null && cache.get(hardCode + "#S#" + stateName) == 1) {
						errStateList.add(scriptBean.getState_description().get(stateName));
						cache.put(hardCode + "#S#" + stateName, 0);
						cleanCache(hardCode, scriptBean.getInner_state());
					}
				}
			}
		}else {
			for(String stateName: scriptBean.getState().keySet()) {
				if (cache.get(hardCode + "#S#" + stateName) != null && cache.get(hardCode + "#S#" + stateName) == 1) {
					errStateList.add(scriptBean.getState_description().get(stateName));
					cache.put(hardCode + "#S#" + stateName, 0);
					cleanCache(hardCode, scriptBean.getInner_state());
				}
			}
		}
		
		

		return errStateList;
	}
	
	/**
	 * 每个逻辑单元的执行都是独立的，执行参数来源于输入参数和内部状态，逻辑单元的执行结果是改变内部状态(inner_state)和最终状态(state)
	 * 因此逻辑单元之间的互动是通过内部状态的改变实现的，
	 * 一些逻辑单元依赖于其他逻辑单元，是指这些被依赖的逻辑单元需要被先执行，处理方式是使用递归的方式先执行依赖逻辑单元
	 * */
	private static void callLogicUnit(ScriptBean scriptBean, String hardCode, String logicName, ScriptEngine script, Map<String, Boolean> finishedMap){
		if (finishedMap.get(logicName) == null) {
			finishedMap.put(logicName, false);
		}
		if (finishedMap.get(logicName)) {
			return;
		}
		
		LogicUnitBean logic = scriptBean.getBeanMap().get(logicName);
		for (String deptLogicName : logic.getDep_logic()) {
			callLogicUnit(scriptBean, hardCode, deptLogicName, script, finishedMap);
		}
		
		boolean tmp;
		Map<String, String> conditionMap = logic.getCondition();
		for (String conditionName : conditionMap.keySet()) {
			tmp = false;
			try {
				tmp = Boolean.parseBoolean(script.eval(conditionMap.get(conditionName)).toString());
			} catch (ScriptException e) {
				e.printStackTrace();
			}
			script.put(conditionName, tmp);
		}
		
		Map<String, Map<String, Object>> logicMap = logic.getLogic();
		for (String logicCellName : logicMap.keySet()) {
			tmp = false;
			try {
				tmp = Boolean.parseBoolean(script.eval(logicCellName).toString());
				if (tmp) {
					Map<String, Object> setValFunMap = logicMap.get(logicCellName);
					for (String paramName : setValFunMap.keySet()) {
						cache.put(hardCode + "#S#" + paramName, Float.valueOf(script.eval(setValFunMap.get(paramName).toString()).toString()).intValue());
					}
				}
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
		
		// 本次逻辑单元执行完毕，更新内部状态和脚本环境
		updateInnerState(hardCode, scriptBean.getInner_state(), script);
		
		finishedMap.put(logicName, true);
	}
	
	// 使用cache中的内部状态值，更新脚本执行环境，已确保逻辑单元执行顺利
	private static void updateInnerState(String hardCode, Map<String, Integer> innerStateMap, ScriptEngine script){
		for (String innerStateName : innerStateMap.keySet()) {
			if(cache.get(hardCode+"#S#"+innerStateName)==null){
				script.put(innerStateName, 0);
			}else{
				script.put(innerStateName, cache.get(hardCode+"#S#"+innerStateName));
			}
		}
	}
	
	// 将缓存中内部状态初始化
	private static void cleanCache(String hardCode, Map<String, Integer> innerStateMap){
		for (String key : innerStateMap.keySet()) {
			cache.put(hardCode+"#S#"+key, 0);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
		script.put("a", "E02");
		
		System.out.println(script.eval(" a=='E02'"));
		
		String res="q";
		script.put("res",res);
		script.put("cal", 12);
		script.put("cal1", 12);
		script.put("ca5l2", 12);
		script.put("ca4l3", 122);
		script.eval("res=cal+'AAA'");
		
		System.out.println(script.eval("cal+1"));
		System.out.println(script.eval("ca4l3+1"));
		script.put("__ROOMMAINTAIN", "1");
		System.out.println(script.eval("cal+1"));
		
		boolean con1 = Boolean.parseBoolean(script.eval("__ROOMMAINTAIN == '1'").toString());
		boolean con2 = Boolean.parseBoolean(script.eval("(ca5l2+1)>12").toString());
		script.put("con1", con1);
		script.put("con2", con2);
		System.out.println(script.eval("con1 && con2"));
		
		script.put("ca222", 13);
		System.out.println(Float.valueOf(script.eval("ca222/3").toString()).intValue());
		
		System.out.println(Float.valueOf(script.eval("9999").toString()).intValue());
//		cal();
	}
	
	

}
