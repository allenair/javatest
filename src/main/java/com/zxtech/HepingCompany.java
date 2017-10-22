package com.zxtech;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class HepingCompany {
	private Connection conn;
	private Map<String, List<String>> nameList=new HashMap<>();
	private Map<String, String> nameIdPair = new HashMap<>();
	private Map<String, Map<String, String>> idMap=new HashMap<>();
	
	private List<String> updateSql = new ArrayList<>();
	private List<String> deleteSql = new ArrayList<>();
	
	
	private void setNameIdPairMap() throws Exception{
		PreparedStatement pst = conn.prepareStatement("select comp_name, max(id) as id from base_company  group by comp_name");
		ResultSet rst = pst.executeQuery();
		while(rst.next()){
			this.nameIdPair.put(rst.getString("comp_name"), rst.getString("id"));
		}
		pst.close();
	}
	
	private void setRelationMap() throws Exception{
		PreparedStatement pst = conn.prepareStatement("select * from base_company  order by comp_name");
		ResultSet rst = pst.executeQuery();
		while(rst.next()){
			String id = rst.getString("id");
			String comp_name = rst.getString("comp_name");
		
			HashMap<String, String> row = new HashMap<>();
			row.put("relation_person", rst.getString("relation_person"));
			row.put("relation_phone", rst.getString("relation_phone"));
			row.put("comp_name", comp_name);
			
			this.idMap.put(id, row);
			
			List<String> idList;
			if(this.nameList.get(comp_name)==null){
				idList = new ArrayList<>();
				nameList.put(comp_name, idList);
			}else{
				idList = this.nameList.get(comp_name);
			}
			idList.add(id);
		}
		pst.close();
	}
	
	private void getSqlList(){
		for (String nameKey : this.nameList.keySet()) {
			if(this.nameList.get(nameKey).size()<2){
				continue;
			}
			
			String newId = this.nameIdPair.get(nameKey);
			String relation_person = this.idMap.get(newId).get("relation_person");
			String relation_phone = this.idMap.get(newId).get("relation_phone");
			
			for (String idKey : this.nameList.get(nameKey)) {
				String oldId = idKey;
				if(oldId.equals(newId)){
					continue;
				}
				StringBuilder sql = new StringBuilder();
				sql.append("update mt_proj_comp_rel set comp_id='").append(newId).append("' where comp_id='").append(oldId).append("'; \n");
				
				if(StringUtils.isEmpty(relation_person) && StringUtils.isNotEmpty(this.idMap.get(oldId).get("relation_person"))){
					relation_person = this.idMap.get(oldId).get("relation_person");
					relation_phone = this.idMap.get(oldId).get("relation_phone");
					sql.append("update base_company set relation_person='").append(relation_person)
						.append("',relation_phone='").append(relation_phone).append("'  where id='").append(newId).append("'; \n");
				}
				this.updateSql.add(sql.toString());
				
				sql = new StringBuilder();
				sql.append("delete from base_company where id='").append(oldId).append("'; \n");
				this.deleteSql.add(sql.toString());
			}
		}
	}
	
	private void writeToFile(List<String> sqlList, String fileName)throws Exception{
		PrintWriter fw = new PrintWriter(fileName);
		for (String str : sqlList) {
			fw.println(str);
		}
		fw.close();
	}
	
	
	
	
	
	public void start() throws Exception{
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(
						"jdbc:postgresql://192.168.1.67:5432/essorgtestdb","essdb", "essdb@zxtech");
		
		
		this.setNameIdPairMap();
		this.setRelationMap();
		this.getSqlList();
		this.writeToFile(this.updateSql, "/Users/allen/Desktop/update.sql");
		this.writeToFile(this.deleteSql, "/Users/allen/Desktop/delete.sql");
		
		System.out.println("======Finished======");
		this.conn.close();
	}
	public static void main(String[] args) {
		try {
			new HepingCompany().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
