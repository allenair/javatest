package com.zxtech.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class JsonSlowTest {
	public final static String ERROR_FLG = "E";

	private static String BAD_NULL_FILE = "d:/withnull.txt";

	private static String GOOD_FILE = "d:/good.txt";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		String paramStr = "{\"Mp_Template_Id\":\"1001\",\"Activity_Times\":\"3T7UF7Y\"}";
		JSONObject paramObj = JSONObject.fromObject(paramStr);
		StringBuilder resultStr = new StringBuilder();
		try (BufferedReader fin = new BufferedReader(new FileReader(BAD_NULL_FILE))) {
			fin.lines().forEach(resultStr::append);
		}
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			System.out.println("====" + i + "=====");
//			doPostWbWithParamBAD(paramObj, resultStr.toString());
			filterNullDealMap(resultStr.toString());
			
//			doPostWbWithParamGOOD(paramObj, resultStr.toString());
//			doGsonObject(resultStr.toString());
			
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
		return JSONObject.fromObject(returnStr);
	}
	
	public static Map<String, Object> filterNullDealMap(String resultStr){
		Map<String, Object> map = new HashMap<>();
		map = new Gson().fromJson(resultStr, map.getClass());
		
		for (String key : map.keySet()) {
			Object obj = map.get(key);
			if(obj==null) {
				obj = "";
			}else {
				obj = dealNull(obj);
			}
			map.put(key, obj);
		}
		return map;
	}
	
	private static Object dealNull(Object obj) {
		if(obj==null) {
			return "";
		}else if(obj instanceof List) {
			List newObj = (List)obj;
			for (int i=0; i<newObj.size(); i++) {
				newObj.set(i, dealNull(newObj.get(i)));
			}
		}else if(obj instanceof Map) {
			Map newMap = (Map)obj;
			for (Object key: newMap.keySet()) {
				newMap.put(key, dealNull(newMap.getOrDefault(key, "")));
			}
		}
		
		return obj;
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
	
	
	
	
	
	
	
	public  void doGsonObject(String returnStr) throws Exception {
		Map<String, Object> map = new HashMap<>();
		Gson gson = new GsonBuilder().serializeNulls().create();
		map = gson.fromJson(returnStr, map.getClass());
	}

	private  Map<String, Object> filterNullNew(Map<String, Object> map){
		map = new HashMap<>();
		map.put("aaa", "a");
		map.put("bbb", null);
		Map<String, Object> innerMap = new HashMap<>();
		map.put("inner", innerMap);
		innerMap.put("ccc", "c");
		innerMap.put("ddd", null);
		
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(String.class, new TypeAdapter<String>() {
			@Override
			public void write(JsonWriter out, String value) throws IOException {
				if (value == null) {//序列化使用的是adapter的write方法
		            //jsonWriter.nullValue();//这个方法是错的，而是应该将null转成""
					out.value("");
		            return;
		        }
				out.value(value);
			}

			@Override
			public String read(JsonReader jsonReader) throws IOException {
				if (jsonReader.peek() == JsonToken.NULL) {//反序列化使用的是read方法
		            jsonReader.nextNull();
		            return "";
		        }
		        return jsonReader.nextString();
			}
		});
		
		Gson gson = new GsonBuilder().serializeNulls().create();
		String str = gson.toJson(map);
		
		Map<String, Object> newMap = new HashMap<>();
		newMap = gson.fromJson(str, newMap.getClass());
		return newMap;
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
}

