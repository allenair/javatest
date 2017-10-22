package com.sinyd.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String json = toJson();
		toBean("{\"name\":\"aaa\"}");
		
		Type listType = new TypeToken<List<PojoBean>>() {}.getType();
		List<PojoBean> list = new Gson().fromJson(json, listType);
		System.out.println("sss");
	}

	private static String toJson(){
		List<PojoBean> list = new ArrayList<PojoBean>();
		PojoBean bean = new PojoBean();
		bean.setName("aaa");
		bean.setSex("f");
		bean.setAge(23);
		list.add(bean);
		
		bean = new PojoBean();
		bean.setName("bbb");
		bean.setSex("g");
		bean.setAge(33);
		list.add(bean);
		
		System.out.println(new Gson().toJson(list));
		return new Gson().toJson(list);
	}
	
	private static void toBean(String json){
		PojoBean bean = new Gson().fromJson(json, PojoBean.class);
		System.out.println(bean);
	}
}
