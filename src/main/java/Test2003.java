import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import cn.hutool.core.io.FileUtil;

public class Test2003 {

	public static void main(String[] args)throws Exception {
//		dealZtTaskFact();
		
//		double ss = 1/3.0;
//		System.out.println(new BigDecimal(ss).setScale(2, RoundingMode.HALF_UP));
//		Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-25").getTime());
//		System.out.println(date);

//		IntStream.range(0, 10).forEach(i->{
//			LocalDate now = LocalDate.now().plusDays(i);
//			System.out.print(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//			System.out.println("   " + now.getDayOfWeek());
//			
//		});
		
//		tt0311();
		
//		System.out.println(new File("D:/code/gitee/bi_research/document/design/笔记.txt").getParent());
		System.out.println(new File("D:/code/gitee/bi_research/document").getAbsolutePath());
		
//		FileUtil.mkdir("d:/zzz/sssd");
//		FileUtil.copy(new File("D:/code/gitee/bi_research/document/design/笔记.txt"), new File("d:/zzz/sssd/"), true);
		
		File srcPath = new File("D:/code/gitee/bi_research/document");
		copyFile(srcPath, srcPath.getAbsolutePath(), "d:/zzz/");
	}

	private static void copyFile(File srcFile, String srcPath, String desPath) {
		if(srcFile.isFile()) {
			String nowPath = srcFile.getParent();
			nowPath = nowPath.replace(srcPath, "");
			FileUtil.copy(srcFile, new File(desPath + nowPath), true);
		}else {
			String nowPath = srcFile.getAbsolutePath();
			nowPath = nowPath.replace(srcPath, "");
			FileUtil.mkdir(desPath + nowPath);
			for (File file : srcFile.listFiles()) {
				copyFile(file, srcPath, desPath);
			}
		}
	}
	
	private static void tt0311() {
		Map<Integer, String> nameMap = new HashMap<>();
		nameMap.put(12, "aaaa");
		nameMap.put(23, "bbbb");
		String[] arr = ",,23,12,".split(",");
		String ss= String.join("/", Stream.<String>of(arr).filter(str -> StringUtils.isNotBlank(str.trim()))
				.map(str -> nameMap.get(Integer.parseInt(str))).collect(Collectors.toList()));
		System.out.println(ss);
	}
	
	private static void createWorkday0304() throws Exception {
		Connection conn = Test2003.getConn();
		
		LocalDate end = LocalDate.of(2021, 1, 1);
		LocalDate date = LocalDate.of(2019, 4, 1);
	
		Statement st = conn.createStatement();
		
		while(date.isBefore(end)) {
			String sql = "insert into zx_workday(work_date, weight) values('#1#', #2#);";
			String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String weight = "1.0";
			if(date.getDayOfWeek()==DayOfWeek.SATURDAY || date.getDayOfWeek()==DayOfWeek.SUNDAY) {
				weight = "0";
			}
			
			sql = sql.replaceAll("#1#", dateStr);
			sql = sql.replaceAll("#2#", weight);
			
			System.out.println(">>>"+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			st.execute(sql);
			
			date = date.plusDays(1);
		}
		
		System.out.println("===============");
		conn.close();
	}
	
	// 处理任务统计事实表
	private static void dealZtTaskFact() throws Exception {
		Connection conn = getConn();
		ResultSet rs;
		
		// 1、得到全部人员所属部门
		Map<String, Integer> userDeptMap = new HashMap<>();
		String sql = "select account, dept from zt_user";
		rs = conn.createStatement().executeQuery(sql);		
		while(rs.next()) {
			userDeptMap.put(rs.getString("account"), rs.getInt("dept"));
		}
		rs.close();
		
		// 2、得到全部task对应的project
		Map<Integer, Map<String, Object>> taskProjectMap = new HashMap<>();
		sql = "select id, project, estimate from zt_task;";
		rs = conn.createStatement().executeQuery(sql);		
		while(rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("project", rs.getInt("project"));
			map.put("estimate", rs.getBigDecimal("estimate"));
			taskProjectMap.put(rs.getInt("id"), map);
		}
		rs.close();
		
		// 3、处理所有日期，实际使用应该指定日期
		List<Map<String, Object>> dateList = new ArrayList<>();
		
		rs = conn.createStatement().executeQuery("select work_date, weight from zx_workday order by id;");
		while(rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("work_date", rs.getString("work_date"));
			map.put("weight", rs.getBigDecimal("weight"));
			dateList.add(map);
		}
		rs.close();
		
//		Map<String, Object> map = new HashMap<>();
//		map.put("work_date", "2020-02-25");
//		map.put("weight", new BigDecimal("1.0"));
//		dateList.add(map);
		
		dateList.stream().limit(1000).forEach(row->{
			try {
				realUpdateFact(row.get("work_date").toString(), (BigDecimal)row.get("weight"), userDeptMap, taskProjectMap, conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		
		conn.close();
	}
	
	private static void realUpdateFact(String workDateStr, BigDecimal weight, Map<String, Integer> userDeptMap, Map<Integer, Map<String, Object>> taskProjectMap, Connection conn) throws Exception {
		Statement st = conn.createStatement();
		String sql ="";
		ResultSet rs;
		Date workDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(workDateStr).getTime());
		
		int totleCount, nowCount=1;
		
		// 1、得到指定日期，按照任务与人员分组的情况
		List<Map<String, Object>> teList = new ArrayList<>();
		sql = "select task, account, format(SUM(consumed),1) consumed from zt_taskestimate where date='"+workDateStr+"' GROUP BY task, account order by task, account; ";
		rs = st.executeQuery(sql);		
		while(rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("task_id", rs.getInt("task"));
			map.put("account", rs.getString("account"));
			map.put("work_date", workDate);
			map.put("consumed", rs.getBigDecimal("consumed"));
			
			teList.add(map);
		}
		rs.close();
		
		totleCount = teList.size();
		// 2、将以上分组处理得到按照人分组当天的实际总消耗
		PreparedStatement tepst = conn.prepareStatement("insert into zx_taskestimate_fact(task_id, account, work_date, project_id, dept_id, consumed, date_total_consumed, consumed_ratio) values(?,?,?,?,?,?,?,?);");
		PreparedStatement taskpst = conn.prepareStatement("insert into zx_task_fact(task_id, account, project_id, dept_id, estimate) values(?,?,?,?,?);");
		PreparedStatement taskCheckPst = conn.prepareStatement("select count(*) num from zx_task_fact where task_id=?");
		
		Map<String, List<Map<String, Object>>> listByPersonMap = teList.stream().collect(Collectors.groupingBy(map->map.get("account").toString()));
		for (String personName : listByPersonMap.keySet()) {
			List<Map<String, Object>> personTaskList = listByPersonMap.get(personName);
			double total = personTaskList.stream().mapToDouble(row->Double.parseDouble(row.get("consumed").toString())).sum();
			
			for (Map<String, Object> row : personTaskList) {
				BigDecimal ratio = BigDecimal.ZERO;
				// 判断等于0
				if(total>0.001) {
					ratio = ((BigDecimal)row.get("consumed")).divide(new BigDecimal(total), 4, RoundingMode.HALF_UP);
				}
				
				int projectId = Integer.parseInt(taskProjectMap.get((Integer)row.get("task_id")).get("project").toString());
				BigDecimal estimate = new BigDecimal(taskProjectMap.get((Integer)row.get("task_id")).get("estimate").toString());
				
				tepst.setInt(1, (Integer)row.get("task_id"));
				tepst.setString(2, row.get("account").toString());
				tepst.setDate(3, workDate);
				tepst.setInt(4, projectId);
				tepst.setInt(5, userDeptMap.get(row.get("account").toString()));
				tepst.setBigDecimal(6, (BigDecimal)row.get("consumed"));
				tepst.setBigDecimal(7, new BigDecimal(total));
				tepst.setBigDecimal(8, ratio);
				tepst.execute();
				
				int count=1;
				taskCheckPst.setInt(1, (Integer)row.get("task_id"));
				rs = taskCheckPst.executeQuery();
				if(rs.next()) {
					count = rs.getInt("num");
				}
				rs.close();
				
				if(count==0) {
					taskpst.setInt(1, (Integer)row.get("task_id"));
					taskpst.setString(2, row.get("account").toString());
					taskpst.setInt(3, projectId);
					taskpst.setInt(4, userDeptMap.get(row.get("account").toString()));
					taskpst.setBigDecimal(5, estimate);
					taskpst.execute();
				}
				
				System.out.println(workDateStr+" == "+totleCount+": "+ (nowCount++) +" >>>"+row.get("account"));
			}
		}
		tepst.close();
		taskpst.close();
		taskCheckPst.close();
		
		
		
		st.close();
	}
	
	
	private static Connection getConn() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.147:3306/zentao?useUnicode=true;", "root",
				"asddemo@zxtech");

		conn.setAutoCommit(true);
		return conn;
	}
}
