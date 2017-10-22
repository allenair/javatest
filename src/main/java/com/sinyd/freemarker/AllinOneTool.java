package com.sinyd.freemarker;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class AllinOneTool {
	private final static String DIR_NAME = "sqltemplate";
    private static Logger log = LoggerFactory.getLogger(AllinOneTool.class);
    private static HashMap<String, String> sqlMap;
    private static Configuration cfg = null;
	private static StringTemplateLoader sTmpLoader = null;
	
	static{
		realinit();
	}
	
	private static void realinit() {
		sqlMap = new HashMap<String, String>();
        try {
        	File dir = new File(Thread.currentThread().getContextClassLoader().getResource(DIR_NAME).getFile());
        	if(dir.isDirectory()){
        		for(File file: dir.listFiles()){
        			Document doc = null;
                    SAXReader read = new SAXReader();
                    doc = read.read(file);
        
                    Element root = doc.getRootElement();
                    String key;
                    for (Iterator<Element> element =root.elementIterator();element.hasNext();) {
                    	Element sql = element.next();
                    	if("sqlElement".equals(sql.getName())){
                    		key = sql.attribute("key").getValue();
                            sqlMap.put(key, sql.getText());
                    	}
					}
        		}
        	}
        	
        	cfg = new Configuration();
    		sTmpLoader = new StringTemplateLoader();
    		for (String sqlKey : sqlMap.keySet()) {
    			sTmpLoader.putTemplate(sqlKey, sqlMap.get(sqlKey));
    			cfg.setTemplateLoader(sTmpLoader);  
                cfg.setDefaultEncoding("UTF-8");
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(e.getMessage());
        }
    }

	public static String getSqlString(String sqlKey){
		return getSqlString(sqlKey, null);
	}
	public static String getSqlString(String sqlKey, Map<String, Object> paraMap){
		if(paraMap==null)
			return sqlMap.get(sqlKey);
		
		try {
			Template template = cfg.getTemplate(sqlKey);
			StringWriter writer = new StringWriter();      
	        template.process(paraMap, writer);      
	        return writer.toString();  
		} catch (Exception e) {
			log.error(e.getMessage());
			return sqlMap.get(sqlKey);
		}      
	}
	
    
    
}
