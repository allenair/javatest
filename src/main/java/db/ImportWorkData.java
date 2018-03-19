package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

public class ImportWorkData {
	public static Map<String, Integer> checkMap = new HashMap<String, Integer>();
//	private String url = "jdbc:postgresql://192.168.1.229:5432/soadb";
	private String url = "jdbc:postgresql://192.168.1.47:5432/soadb";
//	private String url = "jdbc:postgresql://localhost:5433/soadb";
	
	public static void main(String[] args)  throws Exception {
		checkMap.put("tDept", 13);
		checkMap.put("tUser", 85);
		checkMap.put("tUnit", 12);
		checkMap.put("tproject", 90);
		checkMap.put("tProjectPhases", 104);
		new ImportWorkData().startImport();
	}
	
	public void startImport() throws Exception {
		System.out.println(StringUtils.center("check", 40, "="));
		if(!this.checkData()){
			return;
		}
		
		System.out.println(StringUtils.center("delete old data", 40, "="));
		this.deleteWorkData();
		
		System.out.println(StringUtils.center("Workload", 40, "="));
		List<Map<String, Object>> rowList = this.exportWorkloadData();
		this.importWorkloadData(rowList);
		System.out.println(StringUtils.center("Workload Success!", 40, "="));
		
		System.out.println(StringUtils.center("WorkloadReply", 40, "="));
		rowList = this.exportWorkloadReplyData();
		this.importWorkloadReplyData(rowList);
		System.out.println(StringUtils.center("WorkloadReply Success!", 40, "="));
	}

	private boolean checkData() throws Exception {
		boolean flag = true;
		Connection mscon = this.getMSsqlConn();
		String sql = "select count(1) from ";
		for (String tabKey : checkMap.keySet()) {
			Statement st = mscon.createStatement();
			ResultSet rs = st.executeQuery(sql+tabKey);
			int num = 0;
			if(rs.next()){
				num = rs.getInt(1);
			}
			st.close();
			
			if(checkMap.get(tabKey)!=num){
				System.out.println(StringUtils.center(tabKey+" wrong", 40, "="));
				flag = false;
				break;
			}
		}
		
		mscon.close();
		return flag;
	}
	
	private void deleteWorkData() throws Exception {
		Connection pqcon = this.getPQConn();
		pqcon.createStatement().execute("delete from pm_work_reply");
		pqcon.createStatement().execute("delete from pm_work_load");
		pqcon.close();
	}
	
	private List<Map<String, Object>> exportWorkloadData() throws Exception {
		List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();
		
		Connection mscon = this.getMSsqlConn();
		StringBuilder sql = new StringBuilder();
		sql.append("select  w.iWorkloadID as id, u.iID+1000 as user_id, w.iUnitID as bu_id, w.iProjectID as proj_id,                                                      ");
		sql.append("            w.iProjectPhasesID as phases_id, w.dWork as work_date, w.nWorkTime as work_time,                                                     ");
		sql.append("            w.cOvertime as over_time, w.cLeave as leave_time, w.cWork as work_content,                                                             ");
		sql.append("            w.cWorkExplain as work_explain, w.cMemo as memo, w.dOperate as op_date,                                                                ");
		sql.append("            w.iAudit as if_audit, w.cAuditor as audit_man, w.dAudit as audit_time, '1' as enable_flag,                                                 ");
		sql.append("            '' as create_user, null as create_timestamp, '' as last_update_user, null as last_update_timestamp, '' as last_update_remark   ");
		sql.append("  from tWorkload w join  tUser u on w.cUserID=u.cUserID                                                                                                        ");
		
		PreparedStatement mspst = mscon.prepareStatement(sql.toString());
		ResultSet rst = mspst.executeQuery();
		
		while(rst.next()){
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", rst.getObject("id"));
			row.put("user_id", rst.getObject("user_id"));
			row.put("bu_id", rst.getObject("bu_id"));
			row.put("proj_id", rst.getObject("proj_id"));
			row.put("phases_id", rst.getObject("phases_id"));
			row.put("work_date", rst.getDate("work_date"));
			row.put("work_time", this.getDecimalVal(rst.getBigDecimal("work_time")));
			row.put("over_time", this.getDecimalVal(rst.getBigDecimal("over_time")));
			row.put("leave_time", this.getDecimalVal(rst.getBigDecimal("leave_time")));
			row.put("work_content", rst.getString("work_content"));
			row.put("work_explain", rst.getString("work_explain"));
			row.put("memo", rst.getString("memo"));
			row.put("op_date", rst.getTimestamp("op_date"));
			row.put("if_audit", rst.getInt("if_audit"));
			row.put("audit_man", rst.getString("audit_man"));
			row.put("audit_time", rst.getTimestamp("audit_time"));
			row.put("enable_flag", "1");
			row.put("create_user", null);
			row.put("create_timestamp", null);
			row.put("last_update_user", null);
			row.put("last_update_timestamp", null);
			row.put("last_update_remark", null);
			
			resList.add(row);
		}
		mscon.close();

		return resList;
	}

	private void importWorkloadData(List<Map<String, Object>> rowList) throws Exception {
		Connection pqcon = this.getPQConn();
		PreparedStatement pqpst = pqcon.prepareStatement("insert into pm_work_load values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		for (Map<String, Object> row : rowList) {
			pqpst.setObject(1, row.get("id"));
			pqpst.setObject(2, row.get("user_id"));
			pqpst.setObject(3, row.get("bu_id"));
			pqpst.setObject(4, row.get("proj_id"));
			pqpst.setObject(5, row.get("phases_id"));
			
			pqpst.setObject(6, row.get("work_date"));
			pqpst.setBigDecimal(7, (BigDecimal)row.get("work_time"));
			pqpst.setBigDecimal(8, (BigDecimal)row.get("over_time"));
			pqpst.setBigDecimal(9, (BigDecimal)row.get("leave_time"));
			pqpst.setObject(10, row.get("work_content"));
			
			pqpst.setObject(11, row.get("work_explain"));
			pqpst.setObject(12, row.get("memo"));
			pqpst.setObject(13, row.get("op_date"));
			pqpst.setObject(14, row.get("if_audit"));
			pqpst.setObject(15, row.get("audit_man"));
			
			pqpst.setObject(16, row.get("audit_time"));
			pqpst.setObject(17, row.get("enable_flag"));
			pqpst.setObject(18, row.get("create_user"));
			pqpst.setObject(19, row.get("create_timestamp"));
			pqpst.setObject(20, row.get("last_update_user"));
			pqpst.setObject(21, row.get("last_update_timestamp"));
			
			pqpst.setObject(22, row.get("last_update_remark"));
		
			
			pqpst.executeUpdate();
		}
		
		pqpst.close();
		pqcon.close();
	}
	
	private List<Map<String, Object>> exportWorkloadReplyData() throws Exception {
		List<Map<String, Object>> resList = new ArrayList<Map<String,Object>>();
		
		Connection mscon = this.getMSsqlConn();
		StringBuilder sql = new StringBuilder();
		sql.append("   select w.iID as id, w.iWorkloadID as work_load_id, u.iID+1000 as user_id,                                                     ");
		sql.append("            w.Reply as reply_content, '1' as enable_flag, w.cCreator as create_user,                                                     ");
		sql.append("            w.tCreatedTime as create_timestamp, '' as last_update_user,                                                             ");
		sql.append("            null as last_update_timestamp, '' as last_update_remark                                                 ");
		sql.append("    from tWorkloadReply w join  tUser u on w.cUserID=u.cUserID                                                                    ");
		
		PreparedStatement mspst = mscon.prepareStatement(sql.toString());
		ResultSet rst = mspst.executeQuery();
		
		while(rst.next()){
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", rst.getObject("id"));
			row.put("work_load_id", rst.getObject("work_load_id"));
			row.put("user_id", rst.getObject("user_id"));
			row.put("reply_content", rst.getString("reply_content"));
			row.put("enable_flag", "1");
			row.put("create_user", rst.getString("create_user"));
			row.put("create_timestamp", rst.getTimestamp("create_timestamp"));
			row.put("last_update_user", null);
			row.put("last_update_timestamp", null);
			row.put("last_update_remark", null);
			
			resList.add(row);
		}
		mscon.close();

		return resList;
	}
	  
	private void importWorkloadReplyData(List<Map<String, Object>> rowList) throws Exception {
		Connection pqcon = this.getPQConn();
		PreparedStatement pqpst = pqcon.prepareStatement("insert into pm_work_reply values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		for (Map<String, Object> row : rowList) {
			pqpst.setObject(1, row.get("id"));
			pqpst.setObject(2, row.get("work_load_id"));
			pqpst.setObject(3, row.get("user_id"));
			pqpst.setObject(4, row.get("reply_content"));
			pqpst.setObject(5, row.get("enable_flag"));
			
			pqpst.setObject(6, row.get("create_user"));
			pqpst.setObject(7, row.get("create_timestamp"));
			pqpst.setObject(8, row.get("last_update_user"));
			pqpst.setObject(9, row.get("last_update_timestamp"));
			pqpst.setObject(10, row.get("last_update_remark"));

			pqpst.executeUpdate();
		}
		
		pqpst.close();
		pqcon.close();
	}
	
	private Connection getMSsqlConn() throws Exception {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager
				.getConnection(
						"jdbc:sqlserver://192.168.1.223;databaseName=workmanage;sendStringParametersAsUnicode=false;",
						"sa", "sa@2008");

		return conn;
	}
	
	private Connection getPQConn() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(url, "soa_dev", "soa_dev@2013");
		
		return conn;
	}
	
	private BigDecimal getDecimalVal(BigDecimal val){
		if(val==null){
			return BigDecimal.ZERO;
		}
		return val;
	}
	
	private String getJsonStr(Object obj){
		return new Gson().toJson(obj);
	}
}
