package com.zxtech.xio;

import java.io.BufferedReader;
import java.io.FileReader;
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

public class XioWorker {

	private String fileName="e:/worker-utf.csv";
	private List<Map<String, String>> allWorker = new ArrayList<>();
	private List<Map<String, String>> realWorker = new ArrayList<>();
	
	public static void main(String[] args)throws Exception{
		XioWorker tt = new XioWorker();
		tt.init();
		tt.checkWorkerList();
		tt.genUpdateSql();
		
		System.out.println("======Finished======");
	}

	public Connection getCon() throws Exception{
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(
						"jdbc:postgresql://192.168.1.67:5432/essdb_0427","essdb", "essdb@zxtech");

		return conn;
	}
	
	private void init()throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);
			HashMap<String, String> row = new HashMap<>();
			row.put("code", arr[0].trim());
			row.put("type", arr[1]);
			row.put("name", arr[2]);
			row.put("company", arr[3]);
			row.put("level", arr[4]);
			row.put("emp_entrance_date", arr[5]);
			row.put("member_date", arr[6]);
			this.allWorker.add(row);
		}
		br.close();
	}
	
	private void checkWorkerList()throws Exception{
		List<String> errorList = new ArrayList<>();
		Connection conn = this.getCon();
		PreparedStatement pst = conn.prepareStatement("select 1 from base_employee where emp_code=?");
		ResultSet rst;
		for (Map<String, String> row : allWorker) {
			pst.setString(1, row.get("code"));
			rst = pst.executeQuery();
			if(rst.next()){
				this.realWorker.add(row);
			}else{
				System.out.println(row.get("code"));
				String tmp=row.get("code")+","+row.get("type")+","+row.get("name")+","+row.get("company")+","+row.get("level")+","+row.get("emp_entrance_date")+","+row.get("member_date");
				errorList.add(tmp);
			}
		}
		
		this.realSave(errorList,"e:/error.csv");
	}
	
	private void genUpdateSql()throws Exception {
		List<String> updateSql = new ArrayList<>();
		for (Map<String, String> row : realWorker) {
			String sql = "update base_employee set emp_level='"+row.get("level");
			sql = sql + "', emp_entrance_date='"+row.get("emp_entrance_date")+"' ";
			if(StringUtils.isNotBlank(row.get("member_date"))){
				sql = sql + ", member_date='"+row.get("member_date")+"' ";
			}
			sql = sql + "  where emp_code='"+row.get("code")+"'; ";
			
			updateSql.add(sql);
		}
		
		this.realSave(updateSql,"e:/update.sql");
	}
	
	private void realSave(List<String> list, String filename)throws Exception {
		PrintWriter fw = new PrintWriter(filename);
		for (String str : list) {
			fw.println(str);
		}
		
		fw.close();
	}
}
