package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class OracleDB {

	public static void main(String[] args) throws Exception{

//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		Connection conn = DriverManager.getConnection(
//						"jdbc:oracle:thin:@192.168.1.71:1521:orcl","zallen", "zallen");
		
//		conn.setAutoCommit(false);
		
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection(
//						"jdbc:postgresql://192.168.1.67:5432/essorgtestdb","essdb", "essdb@zxtech");
		
//		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://139.9.139.49:3306/tdmdb?useUnicode=true&characterEncoding=utf8&useSSL=false","tdmuser", "wxjk1234");
		
		ResultSet rst = conn.createStatement().executeQuery("select now()");
		while(rst.next()) {
			System.out.println(rst.getDate(1));
		}
		
	}

}
