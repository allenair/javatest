package com.zxtech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class EmpPosition {
	private Connection conn;
	public static void main(String[] args) throws Exception{
		new EmpPosition().start();
	}

	public void start() throws Exception{
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(
						"jdbc:postgresql://192.168.1.130:5432/essdb_1025","essdb", "essdb@zxtech");
		
		
		PreparedStatement pst = conn.prepareStatement("select emp_id, max(longitude) as longitude, max(latitude) as latitude from sys_emp_position group by emp_id having max(record_timestamp)>'2016-10-24 00:00:00'");
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()){
			String id = rst.getString("emp_id");
			String longitude = rst.getString("longitude");
			String latitude = rst.getString("latitude");
			
			RedisUtil.set("@"+id, "{\"longitude\":\""+longitude+"\",\"latitude\":\""+latitude+"\"}");
		}
	
	
	}
}
