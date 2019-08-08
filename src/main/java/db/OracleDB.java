package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleDB {

	public static void main(String[] args) throws Exception{

		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@192.168.1.71:1521:orcl","zallen", "zallen");
		
		conn.setAutoCommit(false);
		
		
		
		
//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager.getConnection(
//						"jdbc:postgresql://192.168.1.67:5432/essorgtestdb","essdb", "essdb@zxtech");
	}

}
