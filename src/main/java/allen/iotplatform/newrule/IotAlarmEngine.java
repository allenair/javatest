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


public class IotAlarmEngine {
	private static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
	private static Map<String, ScriptBean> scriptBeanMap = new HashMap<>();
	private static Map<String, String> errorDescriptMap = new HashMap<>();
	
	static {
		init("d:/iot_alarm_template.json");
	}
	
	private static void init(String fileName) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			StringBuilder jsonBuilder = new StringBuilder();
			
			for (String str : lines) {
				jsonBuilder.append(str);
			}
			load(jsonBuilder.toString(), "xio_alarm");
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 用于更换预警解析引擎模板
	 * */
	public static synchronized void load(String json, String scriptName){
		ScriptBean scriptBean = new Gson().fromJson(json, ScriptBean.class);
		scriptBean.init();
		dealPriority(scriptBean);
		cleanAllCache();
		
		scriptBeanMap.put(scriptName, scriptBean);
		errorDescriptMap = scriptBean.getState_description();
	}
	
	public static String getErrorContentByCode(String errorCode) {
		return errorDescriptMap.get(errorCode);
	}
	
	/**
	 * 此函数每次收到上传参数会执行一次
	 * 整体思想是:
	 * (1) 从scriptBean中取得参数配置的模板，此Bean是公用的，任何内容都不允许修改，简单来说调用此Bean的任何set方法都是错的
	 * (2) 每次收到上传参数，需要配置inputMap，然后传入，此为输入参数目的是为初始化脚本环境服务，也是不允许修改的
	 * (3) 所有的修改都是在全局的cache中，使用hardCode做头来区分不同设备，cache是存储内部状态，服务于多次接收数据以判断报警的情况
	 * */
	public static List<Map<String, String>> dealIotAlarm(String scriptName, String hardCode, Map<String, Object> inputMap){
		List<Map<String, String>> errStateList = new ArrayList<>();
		ScriptBean scriptBean = scriptBeanMap.get(scriptName);
		
		// =========脚本环境初始化=====？？？此处需要考虑是否可以按照硬件编码给出固定的解析引擎，增加速度？？？=====
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
		boolean cleanFlag = false;
		Map<String, String> resMap;
		if (scriptBean.getState_priority().keySet().size() > 0) {
			String[] priorityArr = scriptBean.getState_priority().keySet().toArray(new String[0]);
			Arrays.sort(priorityArr); // 由于是字符串排序，因此请注意"12"与"2"的大小问题
			for (String priorityName : priorityArr) {
				for (String stateName : scriptBean.getState_priority().get(priorityName)) {
					resMap = new HashMap<>();
					if (cache.get(hardCode + "#S#" + stateName) != null) {
						resMap.put(stateName, cache.get(hardCode + "#S#" + stateName));
						
						if(!cleanFlag) {
							cleanFlag = isFinished(stateName, scriptBean);
						}
					}else {
						resMap.put(stateName,"0");
					}
					errStateList.add(resMap);
				}
			}
		}
		
		// 如果此处标记清理标记，说明已经产生预警或本次计算完成，需要将本系列计算中的状态清除，以便开始下次计算
		if(cleanFlag) {
			cleanCache(hardCode, scriptBean.getInner_state());
			cleanCache(hardCode, scriptBean.getState());
		}

		return errStateList;
	}
	
	// 此处处理结束计算的问题，由于存在两种情况，一是模板执行一遍就出结果，二是模板需要执行多次（次数不定）需要等到出现符合条件的时候才能结束
	// 因此此处控制方式是，判断输出的变量是否在停止标记中，如果停止标记列表为空，则模板只执行一次
	private static boolean isFinished(String stateName, ScriptBean scriptBean) {
		if(scriptBean.getStop_condition()==null || scriptBean.getStop_condition().size()==0) {
			return true;
		}else {
			if(scriptBean.getStop_condition().contains(stateName)) {
				return true;
			}
		}
		return false;
	}
	
	// 此处处理一个问题，就是如果模板优先级列表中没有数据或是数据少于state中的要求，则此处默认将所有state中数据加入，
	// 此处是对模板的修正，此处只修改模板的内存对象，并不变动模板文件
	// 把所有不包括在priority中的state都默认增加到，最小优先级（99）的列表中，以保证都能输出
	private static void dealPriority(ScriptBean scriptBean) {
		List<String> lastPriorityList = new ArrayList<>();
		
		Map<String, String> allPriorityMap = new HashMap<>();
		for (String priorityKey : scriptBean.getState_priority().keySet()) {
			for (String priorityState : scriptBean.getState_priority().get(priorityKey)) {
				allPriorityMap.put(priorityState, "1");
			}
		}
		
		for (String stateKey : scriptBean.getState().keySet()) {
			if(!allPriorityMap.containsKey(stateKey)) {
				lastPriorityList.add(stateKey);
			}
		}
		
		scriptBean.getState_priority().put("99", lastPriorityList);
	}
	
	// 将缓存中内部状态初始化
	private static void cleanCache(String hardCode, Map<String, Integer> innerStateMap){
		for (String key : innerStateMap.keySet()) {
			cache.put(hardCode+"#S#"+key, "0");
		}
	}
	
	// 清空缓存中所有内容，在更换解析模板的时候这很重要
	private static void cleanAllCache() {
		cache = new ConcurrentHashMap<>();
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
						cache.put(hardCode + "#S#" + paramName, script.eval(setValFunMap.get(paramName).toString()).toString());
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
				script.put(innerStateName, getValueByString(cache.get(hardCode+"#S#"+innerStateName)));
			}
		}
	}
	
	// 原则上模板目的是进行计算操作，因此将变量载入js引擎环境的时候，需要将之转换为数值类型double，以支持js正确的计算
	private static double getValueByString(String str) {
		double res = 0.0;
		if(str==null || "".equals(str) || "NA".equalsIgnoreCase(str)) {
			return 0.0;
		}
		try {
			res = Double.parseDouble(str);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
