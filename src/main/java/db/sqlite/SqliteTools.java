package db.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteTools {
	private static String dbPathName = "/Users/allen/sample.db";
	public static Connection getConnection() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbPathName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static void main(String[] arg) throws Exception{
		new SqliteTools().test();
	}
	
	private void test() {
		try {
			Connection conn = SqliteTools.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate("drop table if exists person");
		    st.executeUpdate("create table person (id integer, name string)");
		    
		    st.execute("insert into person values(12,'Allen王')");
		    st.execute("insert into person values(19,'Bella王')");
		    
		    ResultSet rs = st.executeQuery("select * from person");
		    
		    while(rs.next()) {
		    		System.out.println(rs.getString("name"));
		    }
			
		    conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
