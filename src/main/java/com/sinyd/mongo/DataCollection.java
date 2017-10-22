package com.sinyd.mongo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/*
 * 处理原始数据
 * */
public class DataCollection {

    private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=M2_Manage;sendStringParametersAsUnicode=false;";
    private String userName = "sa";
    private String userPwd = "123";
    
    private Map<String, String> monitorMap = new HashMap<String, String>();
    private Map<String, String> deptMap = new HashMap<String, String>();
    public static void main(String[] args)throws Exception{
        new DataCollection().dataColl();
    }

    public void dataColl()throws Exception{
        deptMap.put("21010000A05", "001");
        deptMap.put("21010000A06", "002");
        deptMap.put("2101000AC03", "003");
        deptMap.put("21020000001","001");
        deptMap.put("21020000002","002");
        deptMap.put("21030000001","003");
        deptMap.put("21030000004","001");
        deptMap.put("21060000001","002");
        deptMap.put("21030000002","003");
        deptMap.put("21030000003","001");
        deptMap.put("21040000001","002");
        deptMap.put("21040000002","003");
        deptMap.put("21040000004","001");
        deptMap.put("21050000001","002");
        deptMap.put("21050000003","003");
        deptMap.put("21060000003","001");
        deptMap.put("21090000001","002");
        deptMap.put("21090000002","003");
        deptMap.put("21110000001","001");
        deptMap.put("21110000002","002");
        
        monitorMap.put("realMaterialWeight1", "-001");
        monitorMap.put("realMaterialWeight2", "-002");
        monitorMap.put("realMaterialWeight3", "-003");
        monitorMap.put("realMaterialWeight4", "-004");
        monitorMap.put("realMaterialWeight5", "-005");
        monitorMap.put("realMaterialWeight6", "-006");
        monitorMap.put("asphaltumWeight", "-007");
        monitorMap.put("stuffingWeight", "-008");
        monitorMap.put("blockWeight", "-009");
        monitorMap.put("cement", "-010");
        
        monitorMap.put("temperature", "-101");
        
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
        
        Class.forName("org.sqlite.JDBC");
        Connection conninsert = DriverManager.getConnection("jdbc:sqlite:sqlitetest.db");
        PreparedStatement pst = conninsert.prepareStatement(" insert into asphaltum_data (equipId, monitorId, key_name,value,day_dir,dept_dir, remark) values(?,?,?,?,?,?,?) ");
        
        Mongo mongo = new Mongo("192.168.1.219");
        DB db = mongo.getDB("asphaltum_data");
        DBCollection col = db.getCollection("asphaltum");
        
        StringBuilder sql = new StringBuilder();
        String num = "", dateStr="";
        
        
        sql = new StringBuilder();
        sql.append(" select id, corgid, uploaddate, realMaterialWeight1, realMaterialWeight2, realMaterialWeight3, realMaterialWeight4, ");
        sql.append("          realMaterialWeight5, realMaterialWeight6, asphaltumWeight, stuffingWeight, blockWeight, cement, temperature ");
        sql.append("   from producemeasuremonitor");
        
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql.toString());
        List<DBObject> docList = new ArrayList<DBObject>();
        
        int count=1;
        while(rs.next()){
            if(count<10111)
                dateStr = "20110910";
            else
                dateStr = "20111010";
            
            docList.clear();
            for(String key: monitorMap.keySet()){
                num = monitorMap.get(key);
                
                pst.setString(1, rs.getString("corgid"));
                pst.setString(2, rs.getString("corgid")+num);
                pst.setString(3, key);
                pst.setString(4, rs.getString(key)+"#"+getDateStr(rs.getString("uploaddate"),dateStr)+"#"+rs.getString("corgid"));
                pst.setString(5, dateStr);
                pst.setString(6, getDepart(rs.getString("corgid")));
                pst.setString(7, "producemeasuremonitor"+"#"+rs.getString("uploaddate"));
                pst.addBatch();
                
                BasicDBObject doc = new BasicDBObject();
                doc.put("equipId", rs.getString("corgid"));
                doc.put("monitorId", rs.getString("corgid") + num);
                doc.put("key_name", key);
                doc.put("value", rs.getString(key) + "#" + getDateStr(rs.getString("uploaddate"), dateStr) + "#" + rs.getString("corgid"));
                doc.put("day_dir", dateStr);
                doc.put("dept_dir", getDepart(rs.getString("corgid")));
                doc.put("remark", "producemeasuremonitor" + "#" + rs.getString("uploaddate"));
                docList.add(doc);
            }
            
            pst.executeBatch();
            col.insert(docList);
            
            System.out.println(count+"@"+rs.getString("id")+"#"+rs.getString("corgid"));
            count++;
        }
        
        
        
        
        
        rs.close();
        st.close();
        pst.close();
        conninsert.close();
        conn.close();
    }
    
    private String getDateStr(String realDate, String newDay){
        String rep = newDay.substring(0,4)+"-"+newDay.substring(4,6)+"-"+newDay.substring(6);
        return rep+realDate.substring(realDate.indexOf(" "));
    }
    
  
    
    private String getDepart(String equipId){
        return deptMap.get(equipId);
    }
}
