package com.zxtech.json;

import java.util.HashMap;

import com.google.gson.Gson;

public class MyJSONObject extends HashMap<String, Object> {
	public static String fromObject(Object obj) {
		if(obj==null) {
			return "";
		}
		return new Gson().toJson(obj);
	}
}
