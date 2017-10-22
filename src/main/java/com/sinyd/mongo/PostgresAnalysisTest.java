package com.sinyd.mongo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresAnalysisTest {

	private Connection conn;
	private Statement st;
	private long startlong;

	public void test07() throws Exception {
		this.start();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT                                                         "); 
		sql.append("		T .monitor_id,                                          ");
		sql.append("		SUM (1) AS COUNT,                                ");
		sql.append("		MIN (T .real_vlaue) AS MIN,                    ");
		sql.append("		MAX (T .real_vlaue) AS MAX,                  ");
		sql.append("		AVG (T .real_vlaue) AS AVG,                   ");
		sql.append("		SUM (T .real_vlaue) AS total                   ");
		sql.append("	FROM                                                          ");
		sql.append("		test_table T                                              ");
		sql.append("	WHERE                                                         ");
		sql.append("		T .equip_id = '21040000002'                  ");
		sql.append("	AND T .real_vlaue != 0                                ");
		sql.append("	AND (                                                           ");
		sql.append("		T .real_day BETWEEN '2011-05-01'         ");
		sql.append("		AND '2011-09-10'                                   ");
		sql.append("	)                                                                   ");
		sql.append("	GROUP BY                                                   ");
		sql.append("		T .monitor_id                                           ");
		sql.append("	ORDER BY                                                    ");
		sql.append("		T .monitor_id                                           ");
				
		ResultSet rs = st.executeQuery(sql.toString());
		while (rs.next()) {
			System.out.println("monitorId: " + rs.getString("monitor_id")
									+ ", count: " + rs.getString("count")
									+ ", min: " + rs.getString("min")
									+ ", max: " + rs.getString("max") + ", total: "
									+ rs.getString("total"));
		}
		System.out.println("Elapse time is " + this.stopAndGet());
	}
	public void test09() throws Exception {
		this.start();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT                                                                                  ");
		sql.append("		A .monitor_id,                                                                   ");
		sql.append("		A .total,                                                                             ");
		sql.append("		A .w_count AS count,                                                    ");
		sql.append("		A .zerocount,                                                                    ");
		sql.append("		A .w_count*1.0 / A .total AS rate                                      ");
		sql.append("	FROM                                                                                   ");
		sql.append("		(                                                                                        ");
		sql.append("			SELECT                                                                          ");
		sql.append("				T .monitor_id,                                                           ");
		sql.append("				SUM (1) AS total,                                                      ");
		sql.append("				SUM (                                                                       ");
		sql.append("					CASE                                                                     ");
		sql.append("					WHEN s.standard_value > 0                                 ");
		sql.append("					AND (                                                                    ");
		sql.append("						T .real_vlaue > s.standard_value * 1.1              ");
		sql.append("						OR T .real_vlaue < s.standard_value * 0.9        ");
		sql.append("					) THEN                                                                  ");
		sql.append("						1                                                                        ");
		sql.append("					WHEN s.standard_value < 0                                 ");
		sql.append("					AND (                                                                    ");
		sql.append("						T .real_vlaue < s.standard_value * 1.1              ");
		sql.append("						OR T .real_vlaue > s.standard_value * 0.9        ");
		sql.append("					) THEN                                                                  ");
		sql.append("						1                                                                        ");
		sql.append("					ELSE                                                                      ");
		sql.append("						0                                                                        ");
		sql.append("					END                                                                      ");
		sql.append("				) AS w_count,                                                            ");
		sql.append("				SUM (                                                                       ");
		sql.append("					CASE                                                                     ");
		sql.append("					WHEN T .real_vlaue = 0 THEN                             ");
		sql.append("						1                                                                        ");
		sql.append("					ELSE                                                                      ");
		sql.append("						0                                                                        ");
		sql.append("					END                                                                      ");
		sql.append("				) AS zerocount                                                          ");
		sql.append("			FROM                                                                           ");
		sql.append("				test_table T,                                                              ");
		sql.append("				standard_table s                                                       ");
		sql.append("			WHERE                                                                          ");
		sql.append("				T .monitor_id = s.monitor_id                                   ");
		sql.append("			AND T .equip_id = '21040000002'                               ");
		sql.append("			AND T .real_day BETWEEN '2011-05-01'                     ");
		sql.append("			AND '2011-09-10'                                                        ");
		sql.append("			GROUP BY                                                                    ");
		sql.append("				T .monitor_id                                                            ");
		sql.append("			ORDER BY                                                                     ");
		sql.append("				T .monitor_id                                                            ");
		sql.append("		) A                                                                                     ");

		ResultSet rs = st.executeQuery(sql.toString());
		while (rs.next()) {
			System.out.println("monitorId: " + rs.getString("monitor_id")
									+ ", count: " + rs.getString("count")
									+ ", zeroCount: " + rs.getString("zerocount")
									+ ", total: " + rs.getString("total") + ", rate: "
									+ rs.getString("rate"));
		}
		System.out.println("Elapse time is " + this.stopAndGet());
	}

	private void start() throws Exception {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(
				"jdbc:postgresql://192.168.1.229:5432/test", "postgres",
				"postgresql");
		st = conn.createStatement();
		this.startlong = System.currentTimeMillis();
	}

	private int stopAndGet() throws Exception {
		long t = System.currentTimeMillis() - this.startlong;
		st.close();
		conn.close();
		return (int) t;
	}
}
