package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class SqlserverTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new SqlserverTest().test();
	}

	public void test()throws Exception{
		Connection conn = this.getConn();
		List<String> tableNameList = new ArrayList<String>();
		DatabaseMetaData metaData = conn.getMetaData();
//		ResultSet rs = metaData.getSchemas();
//		ResultSet rs = metaData.getCatalogs();
//		ResultSet rs = metaData.getTableTypes();
		ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
		while(rs.next()){
			tableNameList.add(rs.getString("TABLE_NAME"));
		}
		rs.close();
		
		Map<String, String> pkMapList = new HashMap<String, String>();
		String tmp="";
		for (String tableName : tableNameList) {
			tmp="";
			rs = metaData.getPrimaryKeys(null, "Test1166", tableName);
			while(rs.next()){
				tmp = tmp + "##" + rs.getString(4);
			}
			pkMapList.put(tableName, tmp);
			rs.close();
		}
		
		for (String key : pkMapList.keySet()) {
			System.out.println(key+"\t\t"+pkMapList.get(key));
			
		}
		
//		
//		rs = metaData.getColumns(null, "dbo", "T110401Data1", "TestData1ID");
//		while(rs.next()){
//			System.out.println(rs.getInt(5));
//		}
//		
//		this.fillTempContentFromDb("TestSpecificInfoManage", conn);
	}
	
	private void fillTempContentFromDb(String tabName, Connection conn)throws Exception{
		ResultSet rs=null;
		Statement st=null;
		List<String> dateColArr = new ArrayList<String>();
		

		st = conn.createStatement();
		StringBuffer sql = new StringBuffer();
		sql.append("select top 1 * from ").append(tabName);
		rs = st.executeQuery(sql.toString());
		
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			System.out.println(meta.getColumnName(i)+"##"+meta.isAutoIncrement(i));
		}
		
		
		rs.close();
		st.close();
	}
	
	public Connection getConn() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager
				.getConnection(
						"jdbc:sqlserver://192.168.1.202;databaseName=Test20;sendStringParametersAsUnicode=false;",
						"Test1166", "ZB@2008");

//		Class.forName("org.postgresql.Driver");
//		Connection conn = DriverManager
//				.getConnection(
//						"jdbc:postgresql://192.168.1.229:5432/qaspdb",
//						"qasp_dev", "sinyd@2012");

		
		System.out.println(StringUtils.center("Get Connection Success!", 40, "="));
		return conn;
	}
	
}
