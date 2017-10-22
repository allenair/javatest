package com.sinyd.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.TreeSet;

public class SqliteTest {

	private Connection conn;

	/**
	 * the longest of these insert commands is 52ms(0.052s), 
	 * so use sqlite as a method for persistent message is a good work.
	 * 
	 * if want to handle a batch of sql command(update or insert), u need use transaction.
	 * for example: insert 100 records
	 * set transaction need about 210ms(0.21s)
	 * set No-transaction need about 4800ms(4.8s)
	 * 
	 * by the way, using preparestatement's addBatch and excuteBatch method also need about 4800ms when set autocommit true.
	 * @param args
	 */
	public static void main(String[] args) throws Exception  {
		long start = System.currentTimeMillis();
//		new SqliteTest().sqliteTest();
		new SqliteTest().batchInsertSqlite();
		System.out.println(System.currentTimeMillis()-start);
	}

	public void sqliteTest() throws Exception {
		initSqliteDb();
//		conn.setAutoCommit(false);
		
		String sql = "insert into test_tb values (?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		
		String id="id_", name="name_";
		long start = System.currentTimeMillis(), end=System.currentTimeMillis();
		TreeSet<Long> diffSet = new TreeSet<Long>();
		for(int i=0;i<100;i++){
			diffSet.add(end-start);
			start = end;
			pst.setString(1, id+i);
			pst.setString(2, name+i);
			pst.setString(3, ""+System.currentTimeMillis());
			pst.execute();
			
//			conn.commit();
			end = System.currentTimeMillis();
		}
		System.out.println("BigDiff:###"+diffSet.pollLast()+"##"+diffSet.pollLast()+"##"+diffSet.pollLast());
	}

	public void batchInsertSqlite() throws Exception{
		initSqliteDb();
		conn.setAutoCommit(false);
		
		String sql = "insert into test_tb values (?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		
		String id="id_", name="name_";
		for(int i=0;i<100;i++){
			pst.setString(1, id+i);
			pst.setString(2, name+i);
			pst.setString(3, ""+System.currentTimeMillis());
			pst.addBatch();
//			pst.execute();
		}
		pst.executeBatch();
		conn.commit();
	}
	
	private void initSqliteDb() throws Exception {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:test_sqlite.db");
		Statement st = conn.createStatement();
		st.executeUpdate("create table if not exists test_tb (id TEXT, name TEXT, finish_time TEXT)");
		st.executeUpdate("delete from test_tb");
		st.close();
	}
}
