package com.zxtech.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class JsonDealCompare {
	public final static String ERROR_FLG = "E";

	private static String BAD_NULL_FILE = "d:/withnull.txt";

	public static void main(String[] args) throws Exception {
		StringBuilder resultStr = new StringBuilder();
		try (BufferedReader fin = new BufferedReader(new FileReader(BAD_NULL_FILE))) {
			fin.lines().forEach(resultStr::append);
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			System.out.println("====" + i + "=====");
//			doPostWbWithParamBAD(resultStr.toString());
			filterNullDealMap(resultStr.toString());
//			filterNullStack(resultStr.toString());
		}
		System.out.println("=====" + (System.currentTimeMillis() - start));
	}

	/*
	 * 现有方法
	 * ============================================================================
	 * */
	public static Map<String, Object> filterNullDealMap(String resultStr) {
		Map<String, Object> map = new HashMap<>();
		map = new Gson().fromJson(resultStr, map.getClass());
		for (String key : map.keySet()) {
			Object obj = map.get(key);
			if (obj == null) {
				obj = "";
			} else {
				obj = dealNull(obj);
			}
			map.put(key, obj);
		}
		return map;
	}

	private static Object dealNull(Object obj) {
		if (obj == null) {
			return "";
		} else if (obj instanceof List) {
			List newObj = (List) obj;
			for (int i = 0; i < newObj.size(); i++) {
				newObj.set(i, dealNull(newObj.get(i)));
			}
		} else if (obj instanceof Map) {
			Map newMap = (Map) obj;
			for (Object key : newMap.keySet()) {
				newMap.put(key, dealNull(newMap.getOrDefault(key, "")));
			}
		}
		return obj;
	}

	/*
	 * 由于没有指针，此处无法实现设想的操作
	 * 赋值完成后无法将结果绑定回Map或List对象里的value，需要set操作
	 * */
	public static Map<String, Object> filterNullStack(String resultStr) {
		Map<String, Object> map = new HashMap<>();
		map = new Gson().fromJson(resultStr, map.getClass());
		
		Stack<Object[]> dealStack = new Stack<>();
		for (String key : map.keySet()) {
			if(map.get(key)==null) {
				map.put(key, "");
			}else if(map.get(key) instanceof List || map.get(key) instanceof Map) {
				dealStack.add(new Object[] {map, key, map.get(key)});
			}
		}
		
		while(!dealStack.isEmpty()) {
			Object[] objArr = dealStack.pop();
			if (objArr[2] == null) {
				if(objArr[1] instanceof Integer) {
					((List)objArr[0]).set((int)objArr[1], "");				
				}else {
					((Map)objArr[0]).put(objArr[1], "");	
				}
				
			} else if (objArr[2] instanceof List) {
				List newObj = (List) objArr[2];
				for (int i = 0; i < newObj.size(); i++) {
					if(newObj.get(i)==null) {
						newObj.set(i, "");
					}else if(newObj.get(i) instanceof List || newObj.get(i) instanceof Map) {
						dealStack.add(new Object[] {newObj, i, newObj.get(i)});
					}
				}
			} else if (objArr[2] instanceof Map) {
				Map newMap = (Map) objArr[2];
				for (Object key : newMap.keySet()) {
					if(newMap.get(key)==null) {
						newMap.put(key, "");
					}else if(newMap.get(key) instanceof List || newMap.get(key) instanceof Map) {
						dealStack.add(new Object[] {newMap, key, newMap.get(key)});
					}
				}
			}
		}
		
		return map;
	}
	
	/*
	 * 原有方法 =======================================================================
	 */
	public static JSONObject doPostWbWithParamBAD(String returnStr) throws Exception {
		String result = returnStr;
		JSONObject resultObj = resultToJson(result);
		return resultObj;
	}

	private static JSONObject filterNull(JSONObject jsonObj) {
		Iterator<String> it = jsonObj.keys();
		Object obj = null;
		String key = null;
		while (it.hasNext()) {
			key = it.next();
			obj = jsonObj.get(key);
			if (obj instanceof JSONObject) {
				filterNull((JSONObject) obj);
			}
			if (obj instanceof JSONArray) {
				JSONArray objArr = (JSONArray) obj;
				for (int i = 0; i < objArr.size(); i++) {
					filterNull(objArr.getJSONObject(i));
				}
			}
			if (obj == null || obj instanceof JSONNull) {
				jsonObj.put(key, "");
			}
			if (obj.equals(null)) {
				jsonObj.put(key, "");
			}
		}
		return jsonObj;
	}

	private static JSONObject resultToJson(String result) {
		JSONObject resultObj = null;
		try {
			resultObj = JSONObject.fromObject(result);
			resultObj = filterNull(resultObj);
			if (!resultObj.containsKey("rtnData") || "".equals(resultObj.get("rtnData"))) {
				resultObj.put("rtnData", new HashMap<>());
			}
		} catch (Exception e) {
			resultObj = new JSONObject();
			resultObj.put("rtnCode", ERROR_FLG);
			resultObj.put("rtnMsg", "JSONObject转换失败！json字符串：" + result);
			resultObj.put("rtnData", new HashMap<>());
		}
		return resultObj;
	}

}
