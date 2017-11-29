package allen.iotplatform.newrule;

import java.io.IOException;
import java.math.BigDecimal;
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

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;


public class IotAlarmEngine {
	private static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
	private static Map<String, ScriptBean> scriptBeanMap = new HashMap<>();
	private static Map<String, String> errorDescriptMap = new HashMap<>();
	
	static {
//		load(init("d:/iot_alarm_template.json"), "xio_alarm");
	}
	
	public static String init(String fileName) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			StringBuilder jsonBuilder = new StringBuilder();
			
			for (String str : lines) {
				jsonBuilder.append(str);
			}
			
			return jsonBuilder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return "";
	}
	
	/**
	 * 用于更换预警解析引擎模板
	 * */
	public static synchronized void load(String json, String scriptName){
		ScriptBean scriptBean = new Gson().fromJson(json, ScriptBean.class);
		
		// 初始化、规范化模板
		scriptBean.init();
		// 清除缓存
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
		// 将input参数放入本次脚本执行环境，输入参数也放入缓存中
		for (String key : inputMap.keySet()) {
			script.put(key, inputMap.get(key));
			cache.put(hardCode + "#S#" + key, inputMap.get(key).toString());
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
	
	
	
	// 将缓存中内部状态初始化
	private static void cleanCache(String hardCode, Map<String, Integer> innerStateMap){
		for (String key : innerStateMap.keySet()) {
			cache.remove(hardCode+"#S#"+key);
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
		
		// 1、将单位条件判断结果存入脚本环境以便进行后续逻辑的判断计算
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
		
		// 2、根据单位条件（脚本环境中）计算逻辑输出值
		Map<String, Map<String, Object>> logicMap = logic.getLogic();
		Map<String, Object> elseValFunMap = null;
		boolean elseFlag = true;
		try {
			for (String logicCellName : logicMap.keySet()) {
				if ("else".equalsIgnoreCase(logicCellName.trim())) {
					elseValFunMap = logicMap.get(logicCellName);
					continue;
				}
				
				tmp = false;
				tmp = Boolean.parseBoolean(script.eval(logicCellName).toString());
				if (tmp) {
					elseFlag = false;// 如果已经有条件生效，则else不生效
					Map<String, Object> setValFunMap = logicMap.get(logicCellName);
					for (String paramName : setValFunMap.keySet()) {
						cache.put(hardCode + "#S#" + paramName, script.eval(setValFunMap.get(paramName).toString()).toString());
					}
				}
			}

			if (elseFlag && elseValFunMap != null) {
				for (String paramName : elseValFunMap.keySet()) {
					cache.put(hardCode + "#S#" + paramName, script.eval(elseValFunMap.get(paramName).toString()).toString());
				}
			}
		} catch (ScriptException e) {
			System.err.println("==ERROR=IN=LOGIC=>>>"+logicName+"<<<========");;
		}
		
		// 3、计算查表计算的值（value_map区域）
		Map<String, List<Map<String, String>>> valueMap = logic.getValue_map();
		String rowRes;
		for (String key : valueMap.keySet()) {
			List<Map<String, String>> rowList = valueMap.get(key);
			String calVal="";
			for (Map<String, String> row : rowList) {
				rowRes = calArrayOneRow(row, hardCode, script);
				if(StringUtils.isNotEmpty(rowRes)) {
					try {
						calVal = script.eval(rowRes).toString();
					} catch (ScriptException e) {
						calVal = "NA";
						System.err.println("==ERROR=IN=CALCULATE=>>>"+key+"<<<========");
					}
					break;
				}else {
					calVal = "NA";
				}
			}
			
			// 此处处理是支持多重赋值的情况
			String[] keyArr = key.split(",");
			for (String keySingle : keyArr) {
				if(StringUtils.isNotEmpty(keySingle)) {
					keySingle = keySingle.trim();
					cache.put(hardCode + "#S#" + keySingle, calVal);
				}
			}
		}
		
		// 4、本次逻辑单元执行完毕，将缓存中的内容更新脚本环境中的内部状态的值，以便其他单元使用
		updateInnerState(hardCode, scriptBean.getInner_state(), script);
		
		// 清除当前脚本环境中的条件单元（设置成false），避免对其他逻辑单元计算造成影响(因为一般条件的名称都相同，例如con1)，
		// 一般情况不会影响其他单元，因为每个单元计算时候使用到的值都会被覆盖，此处是防御性编程考虑
		for (String conditionName : conditionMap.keySet()) {
			script.put(conditionName, false);
		}
		
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
	
	// 逐行查看表中数据是否符合条件
	private static String calArrayOneRow(Map<String, String> row, String hardCode, ScriptEngine script) {
		boolean flag = true;
		String res = "";
		try {
			for (String key : row.keySet()) {
				if("__val__".equals(key)) {
					if("NA".equals(row.get(key))) {
						res = "NA";
					}else {
						res = script.eval(row.get(key)).toString();
					}
					
				}else {
					flag = calConditionStr(cache.get(hardCode+"#S#"+key), row.get(key));
					if(!flag) {
						break;
					}
				}
			}
			
			if(flag) {
				return res;
			}
			
		}catch(ScriptException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	// 此方法是对条件的解析，目前支持的条件类型是：
	// 单条件：1.0  指val=1.0，此处也支持val是字符串的情况
	// 范围值：(1,8] 指 1<val<=8，$代表无穷，在左为负无穷，在右为正无穷
	// 单独值：1,2,4,5,6 值val取值在此列表中，此处也支持val是字符串的情况
	private static boolean calConditionStr(String val, String conditionStr) {
		boolean flag = false;
		
		// 如果没有值则认为该条件不成立
		if(StringUtils.isEmpty(val)) {
			return false;
		}
		// 如果没有条件描述，则认为该条件成立
		conditionStr = conditionStr.trim();
		if(StringUtils.isEmpty(conditionStr) || "NA".equalsIgnoreCase(conditionStr)) {
			return true;
		}
		
		BigDecimal valNum = null;
		String valStr = null;
		boolean valNumFlag;
		if(isNumber(val)) {
			valNum = new BigDecimal(val);
			valNumFlag = true;
		}else {
			valStr = val;
			valNumFlag = false;
		}
		
		String firstStr = conditionStr.substring(0, 1);
		String lastStr = conditionStr.substring(conditionStr.length()-1);
		if("(".equals(firstStr) || "[".equals(firstStr)) {
			String[] arr = conditionStr.substring(1,conditionStr.length()-1).split(",");
			arr[0] = arr[0].trim();
			arr[1] = arr[1].trim();
			BigDecimal leftNum, rightNum;
			if("$".equals(arr[0])) {
				leftNum = new BigDecimal(Integer.MIN_VALUE);
			}else {
				leftNum = new BigDecimal(arr[0]);
			}
			if("$".equals(arr[1])) {
				rightNum = new BigDecimal(Integer.MAX_VALUE);
			}else {
				rightNum = new BigDecimal(arr[1]);
			}
			
			if("(".equals(firstStr) && ")".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) < 0) && (rightNum.compareTo(valNum) > 0);
			}
			if("[".equals(firstStr) && "]".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) <= 0) && (rightNum.compareTo(valNum) >= 0);
			}
			if("(".equals(firstStr) && "]".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) < 0) && (rightNum.compareTo(valNum) >= 0);
			}
			if("[".equals(firstStr) && ")".equals(lastStr)) {
				flag = (leftNum.compareTo(valNum) <= 0) && (rightNum.compareTo(valNum) > 0);
			}
			
		}else if(conditionStr.contains(",")) {
			String[] arr = conditionStr.split(",");
			BigDecimal tmpNum;
			for (String string : arr) {
				string = string.trim();
				if(valNumFlag) {
					tmpNum = new BigDecimal(string);
					if(tmpNum.compareTo(valNum)==0) {
						flag = true;
						break;
					}
				}else {
					if(string.equalsIgnoreCase(valStr)) {
						flag = true;
						break;
					}
				}
			}
			
		}else {
			if(valNumFlag) {
				BigDecimal singleNum = new BigDecimal(conditionStr);
				flag = (singleNum.compareTo(valNum)==0);
			}else {
				flag = valStr.equalsIgnoreCase(conditionStr);
			}
			
		}
		
		return flag;
	}
	
	private static boolean isNumber(String str){  
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    }  
}
