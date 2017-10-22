package com.zxtech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Location {

	private String url = "http://restapi.amap.com/v3/geocode/geo?key=fbc7338e0edbd1e583b8965271355fa9&address=";
	private String inputFile = "";
	private String outputFile = "";
	private Connection connection;

	public static void main(String[] args) throws Exception {
		
		
		Location location = new Location();
		location.dealAddress();

	}

	public void dealAddress() throws Exception {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(
						"jdbc:postgresql://localhost:5432/essdb_1127","essdb", "essdb@zxtech");
		
		List<String> outputList = new ArrayList<>();
		
		List<Map<String, String>> rowList = this.getInputList();

		for (Map<String, String> row : rowList) {
			URL url = new URL(this.url + row.get("address"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(10000);// 连接超时 单位毫秒
			conn.setReadTimeout(2000);// 读取超时 单位毫秒

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("HTTP GET Request Failed with Error code : " + conn.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String resStr = responseBuffer.readLine();
			HashMap<String, Object> resMap = new Gson().fromJson(resStr, HashMap.class);
			
			
			StringBuilder sb = new StringBuilder();
			sb.append(row.get("id")).append(",").append(row.get("address")).append(",");
			sb.append(row.get("longitude")).append(",");
			sb.append(row.get("latitude")).append(",");
			this.saveOutputList(resMap, outputList, sb.toString(),row);
		}
		this.realSave(outputList);
	}

	private List<Map<String, String>> getInputList() throws Exception {
		List<Map<String, String>> resList = new ArrayList<>();
		HashMap<String, String> row;
		
		BufferedReader br = new BufferedReader(new FileReader("/Users/allen/Desktop/aawwwwwwwwwwww.csv"));
		String tmp;
		List<String> list;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);	
			row = new HashMap<>();
			row.put("address", arr[0]);
			row.put("id", arr[1]);
			
			list = this.getPosition(arr[1]);
			if(list.size()==2){
				row.put("longitude", list.get(0));
				row.put("latitude", list.get(1));
			}
			
			resList.add(row);
		}
		br.close();

		return resList;
	}

	private List<String> getPosition(String projId) throws Exception {
		ArrayList<String> resList = new ArrayList<>();
		PreparedStatement pst = this.connection.prepareStatement("select longitude,latitude from  sys_map_position where position_type='2'and table_id=? ");
		pst.setString(1, projId);
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()){
			resList.add(rst.getString("longitude"));
			resList.add(rst.getString("latitude"));
		}
		pst.close();
		return resList;
	}
	
	private void saveOutputList(Map<String, Object> row, List<String> outputList, String tmp, Map<String, String> inputRow) throws Exception {
		List<Map<String, Object>> list = (List<Map<String, Object>>) row.get("geocodes");
		if(list.size()==0){
			return;
		}
		Map<String, Object> map = (Map<String, Object>) list.get(0);
		System.out.println(">>" + tmp + "@@" + map.get("location"));
		
		
		outputList.add(tmp+map.get("location"));
	}
	
	private void realSave(List<String> outputList)throws Exception {
		PrintWriter fw = new PrintWriter("/Users/allen/Desktop/1128res-wwwwwwww.csv");
		for (String str : outputList) {
			fw.println(str);
		}
		
		fw.close();
	}
}
