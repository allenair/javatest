package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class SqlServerStat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SqlServerStat().statAll();
	}

	public void statAll(){
		String[] allDb = {"IBMIS","M2_Manage","QAIS_DEV","Test20Manage","TestManage"};
		
		for (String dbName : allDb) {
			statDb(dbName);
		}
		
	}
	
	private void statDb(String dbName){
		Connection conn=null;
		try{
			conn = this.getConn(dbName);
			List<String> tableNameList = new ArrayList<String>();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
			while(rs.next()){
				tableNameList.add(rs.getString("TABLE_NAME"));
			}
			rs.close();
			
			Statement st = conn.createStatement();
			Map<String, Integer> countMapList = new HashMap<String, Integer>();
			for (String tableName : tableNameList) {
				rs = st.executeQuery("select count(1)  from ["+tableName+"]");
				while(rs.next()){
					countMapList.put(tableName, rs.getInt(1));
				}
				rs.close();
			}
			st.close();
			
			System.out.println(dbName+"===========================================\n");
			for (String resStr : sort(countMapList)) {
				System.out.println(resStr);
				
			}
			System.out.println(dbName+"===========================================\n\n");
			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private List<String> sort(Map<String, Integer> countMapList){
		ArrayList<String> resList = new ArrayList<String>();
		String[] keyList = countMapList.keySet().toArray(new String[0]);
		int max = 0, tmp=0;
		String tmpStr;
		for (int i=0;i<keyList.length;i++) {
			max = countMapList.get(keyList[i]);
			for(int k=i+1;k<keyList.length;k++){
				tmp = countMapList.get(keyList[k]);
				if(tmp>max){
					max = tmp;
					tmpStr = keyList[i];
					keyList[i] = keyList[k];
					keyList[k] = tmpStr;
				}
			}
		}
		
		for (String key : keyList) {
			resList.add(key+","+countMapList.get(key));
		}
		
		return resList;
	}
	
	private Connection getConn(String dbName) throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager
				.getConnection(
						"jdbc:sqlserver://59.46.65.183;databaseName="+dbName+";sendStringParametersAsUnicode=false;",
						"Test1166", "ZB@2008");

		
		System.out.println(StringUtils.center("Get Connection Success!", 40, "="));
		return conn;
	}
}
