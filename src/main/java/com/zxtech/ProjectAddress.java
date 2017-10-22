package com.zxtech;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectAddress {

	
	public static void main(String[] args) throws Exception{
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection(
//						"jdbc:postgresql://localhost:5432/essdb_1127","essdb", "essdb@zxtech");
//		
//		PreparedStatement pst = conn.prepareStatement("select id from mt_project where proj_name=? ");
//		ResultSet rst;
//		
//		BufferedReader br = new BufferedReader(new FileReader("/Users/allen/Desktop/address.csv"));
//		PrintWriter fw = new PrintWriter("/Users/allen/Desktop/address-reslit.csv");
//		
//		String tmp;
//		StringBuilder sb = new StringBuilder();
//		while((tmp=br.readLine())!=null){
//			String[] arr = tmp.trim().split(";",-1);	
//			pst.setString(1, arr[3]);
//			rst = pst.executeQuery();
//			
//			sb = new StringBuilder();
//			sb.append(tmp);
//			if(rst.next()){
//				sb.append(";").append(rst.getString("id"));
//				
//			}
//			fw.println(sb.toString());
//			
//		}
//		br.close();
//		fw.close();
		new ProjectAddress().changeFile();
	}

	public void changeFile()throws Exception{
		BufferedReader br = new BufferedReader(new FileReader("/Users/allen/Desktop/1128res-wwwwwwww.csv"));
		PrintWriter fw = new PrintWriter("/Users/allen/Desktop/1128res-wwKKKKK.sql");
		
		String tmp;
		StringBuilder sb = new StringBuilder();
		while((tmp=br.readLine())!=null){
			sb = new StringBuilder();
			String[] arr = tmp.trim().split(",",-1);	
			
			sb.append("update sys_map_position set longitude='").append(arr[4]).append("', latitude='").append(arr[5]).append("' where position_type='2' ");
			sb.append(" and table_id='").append(arr[0]).append("';");
			fw.println(sb.toString());
			
		}
		br.close();
		fw.close();
	}
	
}
