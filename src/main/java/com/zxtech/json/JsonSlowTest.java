package com.zxtech.json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class JsonSlowTest {
	public final static String ERROR_FLG = "E";

	private static String BAD_NULL_FILE = "d:/withnull.txt";

	private static String GOOD_FILE = "d:/good.txt";

	public static void main(String[] args) throws Exception {
		String paramStr = "{\"Mp_Template_Id\":\"1001\",\"Activity_Times\":\"3T7UF7Y\"}";
		JSONObject paramObj = JSONObject.fromObject(paramStr);
		StringBuilder resultStr = new StringBuilder();
		try (BufferedReader fin = new BufferedReader(new FileReader(GOOD_FILE))) {
			fin.lines().forEach(resultStr::append);
		}

		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			System.out.println("====" + i + "=====");
//			doPostWbWithParamBAD(paramObj, resultStr.toString());
			doPostWbWithParamGOOD(paramObj, resultStr.toString());
		}
		System.out.println("=====" + (System.currentTimeMillis() - start));
	}

	public static JSONObject doPostWbWithParamBAD(JSONObject param, String returnStr) throws Exception {
		param = filterNull(param);

		String result = returnStr;

		JSONObject resultObj = resultToJson(result);

		return resultObj;
	}

	public static JSONObject doPostWbWithParamGOOD(JSONObject param, String returnStr) throws Exception {
		String result = returnStr;

		return JSONObject.fromObject(result);
	}

	public static JSONObject resultToJson(String result) {
		JSONObject resultObj = null;
		try {
			resultObj = JSONObject.fromObject(result);
			resultObj = filterNull(resultObj);
			if (!resultObj.containsKey("rtnData") || "".equals(resultObj.get("rtnData"))) {
				resultObj.put("rtnData", new HashMap<>());
			}
		} catch (Exception e) {
//			System.out.println("------JSONObject转换失败！json串：" + result + "------");
			resultObj = new JSONObject();
			resultObj.put("rtnCode", ERROR_FLG);
			resultObj.put("rtnMsg", "JSONObject转换失败！json字符串：" + result);
			resultObj.put("rtnData", new HashMap<>());
		}
		return resultObj;
	}

	public static JSONObject filterNull(JSONObject jsonObj) {
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
}
