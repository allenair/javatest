package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public class MysqlDB {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://192.168.1.78:3306/cnpehabit?useUnicode=true&characterEncoding=utf8&useSSL=false", "cnpe",
				"cnpe");

		dealSql(conn);
	}

	private static void dealSql(Connection conn) throws Exception {
		final AtomicInteger count = new AtomicInteger(0);
		Statement st = conn.createStatement();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("d:/temp.sql")))) {
			in.lines().filter(StringUtils::isNotBlank).forEach(line -> {
				try {
					st.execute(line);
					System.out.println(count.addAndGet(1));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		System.out.println("==========FIN===========");
	}

}
