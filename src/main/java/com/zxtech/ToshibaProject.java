package com.zxtech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ToshibaProject {
	private Connection conn;
	private String fileName = "/Users/allen/Desktop/cc.csv";
	
	
	
	private void saveData() throws Exception{
		PreparedStatement pst = conn.prepareStatement("insert into ns3_machine_spec_mst values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?)");
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String tmp;
		while((tmp=br.readLine())!=null){
			String[] arr = tmp.trim().split(",",-1);	
			pst.setInt(1, Integer.parseInt(getValue(arr[0])));
			for(int i=2;i<=31;i++){
				pst.setString(i, getValue(arr[i-1]));
			}
			pst.executeUpdate();
		}
		br.close();
	}
	
	private String getValue(String tmp){
		if(tmp==null){
			return "";
		}else{
			return tmp.trim();
		}
	}
	public void start() throws Exception{
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(
						"jdbc:postgresql://192.168.1.71:5432/toshibadb","allen", "allen");
		this.conn.setAutoCommit(false);
		
		this.saveData();
		
		System.out.println("======Finished======");
		this.conn.commit();
		this.conn.close();
	}
	public static void main(String[] args) throws Exception{
		new ToshibaProject().start();

	}

}
