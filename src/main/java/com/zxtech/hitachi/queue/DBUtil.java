package com.zxtech.hitachi.queue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class DBUtil {
	public static String HOST = "192.168.8.200";
	public static int PORT = 1433;
	public static String USERNAME = "sa";
	public static String PASSWORD = "a123456!";
	public static String DBNAME = "hess_back1";

	public static String getSetting() {
		return "DBUtil [\n\tHOST= " + HOST + " \n\tPORT= " + PORT + " \n\tUSERNAME= " + USERNAME + " \n\tPASSWORD= " + PASSWORD + " \n\tDBNAME " + DBNAME + "\n]";
	}

	public static int saveApiOperationLog(int category, String url, String requestData, String responseData, String apiName) throws Exception {
		Connection conn = getConn();
		conn.setAutoCommit(true);
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO dbo.api_operation_log                      ");
		sql.append(" (category, url, request_data, response_data, api_name) ");
		sql.append(" VALUES(?, ?, ?, ?, ?);                             ");
		PreparedStatement pst = conn.prepareStatement(sql.toString());
		pst.setInt(1, category);
		pst.setString(2, url);
		pst.setString(3, requestData);
		pst.setString(4, responseData);
		pst.setString(5, apiName);
		return pst.executeUpdate();
	}
	
	public static int[] saveApiOperationLogBatch(List<Map<String, String>> rows)throws Exception {
		Connection conn = getConn();
//		conn.setAutoCommit(f);
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO dbo.api_operation_log                      ");
		sql.append(" (category, url, request_data, response_data, api_name) ");
		sql.append(" VALUES(?, ?, ?, ?, ?);                             ");
		PreparedStatement pst = conn.prepareStatement(sql.toString());
		
		for (Map<String, String> row : rows) {
			pst.setInt(1, Integer.parseInt(row.get("category")));
			pst.setString(2, row.get("url"));
			pst.setString(3, row.get("requestData"));
			pst.setString(4, row.get("responseData"));
			pst.setString(5, row.get("apiName"));
			
			pst.addBatch();
		}
		
		return pst.executeBatch();
	}

	private static Connection getConn() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager
				.getConnection("jdbc:sqlserver://" + HOST + ":" + PORT + ";databaseName=" + DBNAME, USERNAME, PASSWORD);
		return conn;
	}
}
