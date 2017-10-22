package com.sinyd.freemarker;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTemplateUtil {
    private final static String DIR_NAME = "sqltemplate";
    private static Logger log = LoggerFactory.getLogger(SqlTemplateUtil.class);
    private static HashMap<String, String> sqlMap;

    static {
        sqlMap = new HashMap<String, String>();
        realinit();
    }
    
    private static void realinit() {
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
        	

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String getSqlString(String key) {
        return sqlMap.get(key);
    }
    
    public static Set<String> getAllSqlKey(){
    	return sqlMap.keySet();
    }
}
