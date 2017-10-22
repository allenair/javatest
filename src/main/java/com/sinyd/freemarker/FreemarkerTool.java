package com.sinyd.freemarker;

import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTool {
	private static Configuration cfg = null;
	private static StringTemplateLoader sTmpLoader = null;
	
	static{
		init();
	}
	
	private static void init(){
		cfg = new Configuration();
		sTmpLoader = new StringTemplateLoader();
		for (String sqlKey : SqlTemplateUtil.getAllSqlKey()) {
			sTmpLoader.putTemplate(sqlKey, SqlTemplateUtil.getSqlString(sqlKey));
			cfg.setTemplateLoader(sTmpLoader);  
            cfg.setDefaultEncoding("UTF-8");
		}
	}
	
	public static String getSql(String sqlKey){
		return getSql(sqlKey, null);
	}
	public static String getSql(String sqlKey, Map<String, Object> paraMap){
		if(paraMap==null)
			return SqlTemplateUtil.getSqlString(sqlKey);
		
		try {
			Template template = cfg.getTemplate(sqlKey);
			StringWriter writer = new StringWriter();      
	        template.process(paraMap, writer);      
	        return writer.toString();  
		} catch (Exception e) {
			e.printStackTrace();
			return SqlTemplateUtil.getSqlString(sqlKey);
		}      
	}
	
}
