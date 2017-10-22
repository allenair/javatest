package com.zxtech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvDeal {
//	private String fileName="/Users/allen/Desktop/LCA-base.csv";
//	private String fileName="/Users/allen/Desktop/MCA-base.csv";
//	private String fileName="/Users/allen/Desktop/HGP-base.csv";
//	private String fileName="/Users/allen/Desktop/HGE-base.csv";
	
//	private String fileName="/Users/allen/Desktop/LCA.csv";
//	private String fileName="/Users/allen/Desktop/MCA.csv";
//	private String fileName="/Users/allen/Desktop/HGP.csv";
	private String fileName="/Users/allen/Desktop/HGE.csv";
	
//	private int[] modelArr={2}; // LCA
//	private int[] modelArr={5,12,21,31}; // MCA
//	private int[] modelArr={6,9,11}; // HGP
	private int[] modelArr={8}; // HGE
	
	private Connection conn;
	
	private Map<String, Integer> loadTypeMap = new HashMap<>();
	private Map<String, Integer> speedTypeMap = new HashMap<>();
	private Map<String, Integer> dooropenTypeMap = new HashMap<>();
	private Map<String, Integer> doordirectionTypeMap = new HashMap<>();
	private Map<String, Integer> throughTypeMap = new HashMap<>();
	private Map<String, Integer> cwTypeMap = new HashMap<>();
	private Map<String, Integer> specTypeMap = new HashMap<>();
	
	
	private List<Map<String, String>> getRecord() throws Exception{
		List<Map<String, String>> result = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);
			
			for (int modelId : modelArr) {
				HashMap<String, String> row = new HashMap<>();
				row.put("CR_MODEL_ID", ""+modelId);
				row.put("CR_LOAD_TYPE_ID", getValue(arr[1]));
				row.put("CR_SPEED_TYPE_ID", getValue(arr[2]));
				row.put("CR_DOOR_OPENING_TYPE_ID", getValue(arr[3]));
				row.put("CR_DOOR_OPENING_DIRECTION_TYPE_ID", getValue(arr[4]));
				row.put("CR_THROUGH_TYPE_ID", getValue(arr[5]));
				row.put("CR_CWT_TYPE_ID", getValue(arr[6]));
				row.put("CR_SPECIFICATION_ID", getValue(arr[7]));
				row.put("CN_jdkd", getValue(arr[8]));
				row.put("CN_jdsd", getValue(arr[9]));
				row.put("CN_jnkd", getValue(arr[10]));
				row.put("CN_jnsd", getValue(arr[11]));
				row.put("CN_kmkd", getValue(arr[12]));
				row.put("CN_kmgd", getValue(arr[13]));
				row.put("CN_mdbccl1", getValue(arr[14]));
				row.put("CN_mdbccl2", getValue(arr[15]));
				row.put("CN_dcgd", getValue(arr[16]));
				row.put("CN_dksd", getValue(arr[17]));
				row.put("CN_jdgd", getValue(arr[18]));
				row.put("CN_jfkd", getValue(arr[19]));
				row.put("CN_jfsd", getValue(arr[20]));
				row.put("CN_jfgd", getValue(arr[21]));
				
				result.add(row);
			}
			
		}
		br.close();
		return result;
	}
	
	private List<Map<String, String>> getRecordScope() throws Exception{
		List<Map<String, String>> result = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);
			
			for (int modelId : modelArr) {
				int index=0;
				HashMap<String, String> row = new HashMap<>();
				row.put("CR_MODEL_ID", ""+modelId);
				
				row.put("CR_LOAD_TYPE_ID", getValue(arr[index++]));
				row.put("CR_SPEED_TYPE_ID", getValue(arr[index++]));

				row.put("CR_DOOR_OPENING_TYPE_ID", getValue(arr[index++]));
				row.put("CR_DOOR_OPENING_DIRECTION_TYPE_ID", getValue(arr[index++]));
				row.put("CR_THROUGH_TYPE_ID", getValue(arr[index++]));
				row.put("CR_CWT_TYPE_ID", getValue(arr[index++]));
				row.put("CR_SPECIFICATION_ID", getValue(arr[index++]));
				
				row.put("CN_jxmj", getValue(arr[index++]));
				row.put("CN_kmcc", getValue(arr[index++]));
				row.put("CN_kmcc_min", getValue(arr[index++]));
				row.put("CN_kmcc_max", getValue(arr[index++]));
				
				row.put("CN_jxkd", getValue(arr[index++]));
				row.put("CN_jxsd", getValue(arr[index++]));
				row.put("CN_jxkd_min", getValue(arr[index++]));
				row.put("CN_jxkd_max", getValue(arr[index++]));
				row.put("CN_jxsd_min", getValue(arr[index++]));
				row.put("CN_jxsd_max", getValue(arr[index++]));
				
				row.put("CN_jxwb_jdb_left_min", getValue(arr[index++]));
				row.put("CN_jxwb_jdb_left_max", getValue(arr[index++]));
				row.put("CN_jxwb_jdb_right_min", getValue(arr[index++]));
				row.put("CN_jxwb_jdb_right_max", getValue(arr[index++]));
				row.put("CN_jxwb_jdb_back_min", getValue(arr[index++]));
				row.put("CN_jxwb_jdb_back_max", getValue(arr[index++]));
				
				row.put("CN_dzzx_jxzx", getValue(arr[index++]));
				row.put("CN_dzzx_hb_min", getValue(arr[index++]));

				String dkMin = getValue(arr[index++]);
				if(dkMin.indexOf(":")>0){
					row.put("CN_dk_min_type", "1");
				}else{
					row.put("CN_dk_min_type", "0");
				}
				row.put("CN_dk_min", dkMin);
				
				String dcMin = getValue(arr[index++]);
				if(dcMin.indexOf(":")>0){
					row.put("CN_dc_min_type", "1");
				}else{
					row.put("CN_dc_min_type", "0");
				}
				row.put("CN_dc_min", dcMin);
				
				result.add(row);
			}
			
		}
		br.close();
		return result;
	}
	
	private void modifyResultMap(List<Map<String, String>> resMap){
		for (Map<String, String> row : resMap) {
			String ok = "1";
			int id = getId(row.get("CR_LOAD_TYPE_ID"), this.loadTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_LOAD_TYPE_ID");
			}else{
				row.put("CR_LOAD_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_SPEED_TYPE_ID"), this.speedTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_SPEED_TYPE_ID");
			}else{
				row.put("CR_SPEED_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_DOOR_OPENING_TYPE_ID"), this.dooropenTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_DOOR_OPENING_TYPE_ID");
			}else{
				row.put("CR_DOOR_OPENING_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_DOOR_OPENING_DIRECTION_TYPE_ID"), this.doordirectionTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_DOOR_OPENING_DIRECTION_TYPE_ID");
			}else{
				row.put("CR_DOOR_OPENING_DIRECTION_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_THROUGH_TYPE_ID"), this.throughTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_THROUGH_TYPE_ID");
			}else{
				row.put("CR_THROUGH_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_CWT_TYPE_ID"), this.cwTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_CWT_TYPE_ID");
			}else{
				row.put("CR_CWT_TYPE_ID", ""+id);
			}
			
			id = getId(row.get("CR_SPECIFICATION_ID"), this.specTypeMap);
			if(id==0){
				ok="0";
				row.put("ERROR_PROPERTY", "CR_SPECIFICATION_ID");
			}else{
				row.put("CR_SPECIFICATION_ID", ""+id);
			}
			
			row.put("OK", ok);
		}
	}
	
	private int getId(String name, Map<String, Integer> map){
		if(map.get(name)==null){
			return 0;
		}else{
			return map.get(name);
		}
	}
	
	private String getValue(String tmp){
		if(tmp==null){
			return "";
		}else{
			return tmp.trim();
		}
	}
	
	private void initData() throws Exception{
		Statement st = this.conn.createStatement();
		ResultSet rst;
		String name;
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_load_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.loadTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_speed_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.speedTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_door_opening_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.dooropenTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_door_opening_direction_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.doordirectionTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_through_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.throughTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_cwt_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.cwTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		rst = st.executeQuery("select distinct cn_id, cn_name from tn_specification_type ");
		while(rst.next()){
			name = getValue(rst.getString("cn_name"));
			this.specTypeMap.put(name, rst.getInt("cn_id"));
		}
		rst.close();
		
		st.close();
	}
	
	private void saveData(List<Map<String, String>> rowList) throws Exception{
		PreparedStatement pst = conn.prepareStatement("INSERT INTO TN_MODEL_STANDARD_PARAMETER (CR_MODEL_ID, CR_LOAD_TYPE_ID, CR_SPEED_TYPE_ID, CR_DOOR_OPENING_TYPE_ID, CR_DOOR_OPENING_DIRECTION_TYPE_ID, CR_THROUGH_TYPE_ID, CR_CWT_TYPE_ID, CR_SPECIFICATION_ID, CN_jdkd, CN_jdsd, CN_jnkd, CN_jnsd, CN_kmkd, CN_kmgd, CN_mdbccl1, CN_mdbccl2, CN_dcgd, CN_dksd, CN_jdgd, CN_jfkd, CN_jfsd, CN_jfgd) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		for (Map<String, String> row : rowList) {
			if("0".equals(row.get("OK"))){
				continue;
			}
			pst.setInt(1, Integer.valueOf(row.get("CR_MODEL_ID")));
			pst.setInt(2, Integer.valueOf(row.get("CR_LOAD_TYPE_ID")));
			pst.setInt(3, Integer.valueOf(row.get("CR_SPEED_TYPE_ID")));
			pst.setInt(4, Integer.valueOf(row.get("CR_DOOR_OPENING_TYPE_ID")));
			pst.setInt(5, Integer.valueOf(row.get("CR_DOOR_OPENING_DIRECTION_TYPE_ID")));
			pst.setInt(6, Integer.valueOf(row.get("CR_THROUGH_TYPE_ID")));
			pst.setInt(7, Integer.valueOf(row.get("CR_CWT_TYPE_ID")));
			pst.setInt(8, Integer.valueOf(row.get("CR_SPECIFICATION_ID")));
			pst.setInt(9, Integer.valueOf(row.get("CN_jdkd")));
			pst.setInt(10, Integer.valueOf(row.get("CN_jdsd")));
			pst.setInt(11, Integer.valueOf(row.get("CN_jnkd")));
			pst.setInt(12, Integer.valueOf(row.get("CN_jnsd")));
			pst.setInt(13, Integer.valueOf(row.get("CN_kmkd")));
			pst.setInt(14, Integer.valueOf(row.get("CN_kmgd")));
			pst.setInt(15, Integer.valueOf(row.get("CN_mdbccl1")));
			pst.setInt(16, Integer.valueOf(row.get("CN_mdbccl2")));
			pst.setInt(17, Integer.valueOf(row.get("CN_dcgd")));
			pst.setInt(18, Integer.valueOf(row.get("CN_dksd")));
			pst.setInt(19, Integer.valueOf(row.get("CN_jdgd")));
			pst.setInt(20, Integer.valueOf(row.get("CN_jfkd")));
			pst.setInt(21, Integer.valueOf(row.get("CN_jfsd")));
			pst.setInt(22, Integer.valueOf(row.get("CN_jfgd")));
			
			pst.executeUpdate();
		}
	}
	
	private void saveDataScope(List<Map<String, String>> rowList) throws Exception{
		PreparedStatement pst = conn.prepareStatement("INSERT INTO TN_MODEL_PARAMETER_SCOPE (CR_MODEL_ID, CR_LOAD_TYPE_ID, CR_SPEED_TYPE_ID, CR_DOOR_OPENING_TYPE_ID, CR_DOOR_OPENING_DIRECTION_TYPE_ID, CR_THROUGH_TYPE_ID, CR_CWT_TYPE_ID, CR_SPECIFICATION_ID, CN_jxmj, CN_kmcc_min, CN_kmcc_max, CN_jxkd_min, CN_jxkd_max, CN_jxsd_min, CN_jxsd_max, CN_jxwb_jdb_left_min, CN_jxwb_jdb_left_max, CN_jxwb_jdb_right_min, CN_jxwb_jdb_right_max, CN_jxwb_jdb_back_min, CN_jxwb_jdb_back_max, CN_dk_min_type, CN_dk_min, CN_dc_min_type, CN_dc_min, CN_dzzx_jxzx, CN_dzzx_hb_min, CN_kmcc, CN_jxkd, CN_jxsd) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		for (Map<String, String> row : rowList) {
			if("0".equals(row.get("OK"))){
				continue;
			}
			pst.setInt(1, Integer.valueOf(row.get("CR_MODEL_ID")));
			pst.setInt(2, Integer.valueOf(row.get("CR_LOAD_TYPE_ID")));
			pst.setInt(3, Integer.valueOf(row.get("CR_SPEED_TYPE_ID")));
			pst.setInt(4, Integer.valueOf(row.get("CR_DOOR_OPENING_TYPE_ID")));
			pst.setInt(5, Integer.valueOf(row.get("CR_DOOR_OPENING_DIRECTION_TYPE_ID")));
			pst.setInt(6, Integer.valueOf(row.get("CR_THROUGH_TYPE_ID")));
			pst.setInt(7, Integer.valueOf(row.get("CR_CWT_TYPE_ID")));
			pst.setInt(8, Integer.valueOf(row.get("CR_SPECIFICATION_ID")));
			pst.setInt(9, Integer.valueOf(row.get("CN_jxmj")));
			pst.setInt(10, Integer.valueOf(row.get("CN_kmcc_min")));
			pst.setInt(11, Integer.valueOf(row.get("CN_kmcc_max")));
			pst.setInt(12, Integer.valueOf(row.get("CN_jxkd_min")));
			pst.setInt(13, Integer.valueOf(row.get("CN_jxkd_max")));
			pst.setInt(14, Integer.valueOf(row.get("CN_jxsd_min")));
			pst.setInt(15, Integer.valueOf(row.get("CN_jxsd_max")));
			pst.setInt(16, Integer.valueOf(row.get("CN_jxwb_jdb_left_min")));
			pst.setInt(17, Integer.valueOf(row.get("CN_jxwb_jdb_left_max")));
			pst.setInt(18, Integer.valueOf(row.get("CN_jxwb_jdb_right_min")));
			pst.setInt(19, Integer.valueOf(row.get("CN_jxwb_jdb_right_max")));
			pst.setInt(20, Integer.valueOf(row.get("CN_jxwb_jdb_back_min")));
			pst.setInt(21, Integer.valueOf(row.get("CN_jxwb_jdb_back_max")));
			pst.setInt(22, Integer.valueOf(row.get("CN_dk_min_type")));
			pst.setString(23, row.get("CN_dk_min"));
			pst.setInt(24, Integer.valueOf(row.get("CN_dc_min_type")));
			pst.setString(25, row.get("CN_dc_min"));
			pst.setInt(26, Integer.valueOf(row.get("CN_dzzx_jxzx")));
			pst.setInt(27, Integer.valueOf(row.get("CN_dzzx_hb_min")));
			pst.setInt(28, Integer.valueOf(row.get("CN_kmcc")));
			pst.setInt(29, Integer.valueOf(row.get("CN_jxkd")));
			pst.setInt(30, Integer.valueOf(row.get("CN_jxsd")));
			
			pst.executeUpdate();
		}
	}
	
	private void printWrongRecord(List<Map<String, String>> rowList) throws Exception{
		
		for (Map<String, String> row : rowList) {
			if("1".equals(row.get("OK"))){
				continue;
			}
			
			StringBuilder sb = new StringBuilder();
			for (String key : row.keySet()) {
				sb.append(key).append("->").append(row.get(key)).append("##");
			}
			System.out.println(sb.toString());
		}
		
	}
	
	public void start() throws Exception{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		this.conn = DriverManager.getConnection(
						"jdbc:sqlserver://192.168.0.161:1433;DatabaseName=SaleSupportDB;","elevatortraining", "a12345678!");
		
		// === TN_MODEL_STANDARD_PARAMETER =====
//		this.initData();
//		List<Map<String, String>> rowList = this.getRecord();
//		this.modifyResultMap(rowList);
//		this.printWrongRecord(rowList);
//		this.saveData(rowList);

		
		// === TN_MODEL_PARAMETER_SCOPE =====
		this.initData();
		List<Map<String, String>> rowList = this.getRecordScope();
		this.modifyResultMap(rowList);
		this.printWrongRecord(rowList);
		this.saveDataScope(rowList);
		
		
		System.out.println("======Finished======");
	}
	
	public static void main(String[] args) throws Exception{
		CsvDeal deal = new CsvDeal();
		deal.start();
	}

}
