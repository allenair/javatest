package com.sinyd.freemarker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainTest {
	private static String[] tbname = {"vehicle_flow_rate","vehicle_speed_60","vehicle_speed_6080","vehicle_speed_80100","vehicle_speed_100120","vehicle_speed_120","outside_brightness","electric_quantity_dan","electric_quantity_hai","model_change_time"};
	private static String[] prename = {"vfr","vs60","vs6080","vs80100","vs100120","vs120","ob","eqd","eqh","mct"};
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
		test0();
//		test1();
//		test2();
//		new MainTest().test3();
	}
	
	public static void test0(){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("date", new Date());
		paraMap.put("aaaa", 111);
		paraMap.put("table_name", tbname[4]);
		paraMap.put("column_prefix", prename[4]);
//		paraMap.put("where_flag", 111);
		paraMap.put("where_flag", new Date());
		
		System.out.println(AllinOneTool.getSqlString("freemarker_insert_before",paraMap));
	}
	
	public static void test1(){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("date", new Date());
		paraMap.put("aaaa", 111);
		
		int rnd=0;
		String s1,s2;
		long startLong = System.currentTimeMillis();
		for(int i=1;i<10000;i++){
			rnd = (int)Math.round(Math.random()*9);
			s1 = SqlTemplateUtil.getSqlString(tbname[rnd]+"_insert_before");
			s2 = SqlTemplateUtil.getSqlString(tbname[rnd]+"_insert");
		}
		System.out.println(System.currentTimeMillis()-startLong);
		
		System.out.println("==============================================================");
		
		startLong = System.currentTimeMillis();
		for(int i=1;i<10000;i++){
			rnd = (int)Math.round(Math.random()*9);
			paraMap.put("table_name", tbname[rnd]);
			paraMap.put("column_prefix", prename[rnd]);
			s1 = FreemarkerTool.getSql("freemarker_insert_before", paraMap);
			s2 = FreemarkerTool.getSql("freemarker_insert", paraMap);
		}
		System.out.println(System.currentTimeMillis()-startLong);
	}

	public static void test2(){
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("date", new Date());
		paraMap.put("aaaa", 111);
		
		int rnd=0;
		String s1,s2;
		long startLong = System.currentTimeMillis();
		for(int i=1;i<10000;i++){
			rnd = (int)Math.round(Math.random()*9);
			paraMap.put("table_name", tbname[rnd]);
			paraMap.put("column_prefix", prename[rnd]);
			s1 = AllinOneTool.getSqlString(tbname[rnd]+"_insert_before", paraMap);
			s2 = AllinOneTool.getSqlString(tbname[rnd]+"_insert", paraMap);
			s2 = AllinOneTool.getSqlString("freemarker_insert", paraMap);
		}
		System.out.println(System.currentTimeMillis()-startLong);
	}
	
	public void test3(){
		new Thread(new AA()).start();
		new Thread(new AA()).start();
	}
	
	class AA implements Runnable{
		@Override
		public void run() {
			int rnd=0;
			String s1,s2;
			for(int i=1;i<10;i++){
				rnd = (int)Math.round(Math.random()*9);
				Map<String, Object> paraMap1 = new HashMap<String, Object>();
				paraMap1.put("table_name", tbname[rnd]);
				paraMap1.put("column_prefix", prename[rnd]);
				s1 = AllinOneTool.getSqlString(tbname[rnd]+"_insert_before", paraMap1);
				s2 = AllinOneTool.getSqlString("freemarker_insert", paraMap1);
				System.out.println("##"+s1+"@@@@"+tbname[rnd]+"@@"+prename[rnd]+s2);
			}
		}
		
	}
}
