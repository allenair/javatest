package com.zxtech;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.greenpineyu.fel.parser.FelParser.expressionList_return;

public class XioProject {
	private Connection conn;
	private Map<Integer, String> mydate = new HashMap<>();
	private List<String> lineList = new ArrayList<>();
	private Map<String, List<String>> personMap = new HashMap<>();
	private List<List<String>> elevatorList = new ArrayList<>();
	
	
	private void getLineList() throws Exception{
		PreparedStatement pst = conn.prepareStatement("select distinct r.line_id from base_elevator e join mt_line_proj_rel r on e.proj_id=r.proj_id ");
		ResultSet rst = pst.executeQuery();
		while(rst.next()){
			lineList.add(rst.getString(1));
		}
		pst.close();
	}
	
	private void getAllPerson()throws Exception{
		for (String lineId : lineList) {
			personMap.put(lineId, new ArrayList<String>());
		}
		
		PreparedStatement pst = conn.prepareStatement("select line_id, emp_id from mt_line_emp_rel ");
		ResultSet rst = pst.executeQuery();
		while(rst.next()){
			if(personMap.get(rst.getString(1))!=null)
				personMap.get(rst.getString(1)).add(rst.getString(2));
		}
		pst.close();
	}
	
	private String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().toUpperCase().replaceAll("-", "");
	}
	
	private String getPersonStr(List<String> personList){
		String pid="";
		int len = personList.size();
		if(len>0){
			int index = (int)(Math.round(Math.random()*(len-1)));
			pid = personList.get(index);
		}
		return pid;
	}
	
	private void getAllElevator() throws Exception{
		PreparedStatement pst = conn.prepareStatement("select distinct e.id, r.line_id from base_elevator e join mt_line_proj_rel r on e.proj_id=r.proj_id join mt_line_emp_rel rel on r.line_id=rel.line_id ");
		ResultSet rst = pst.executeQuery();
		while(rst.next()){
			ArrayList<String> row = new ArrayList<>();
			row.add(rst.getString(1));
			row.add(rst.getString(2));
			elevatorList.add(row);
		}
	}
	
	private void writeTxt() throws Exception{
		PrintWriter fw = new PrintWriter("/Users/allen/Desktop/result.sql");
		for (List<String> row : elevatorList) {
			String elevatorId = row.get(0);
			String lineId = row.get(1);
			String personId = getPersonStr(personMap.get(lineId));
			for (Integer xioweek : mydate.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(" insert into mt_work_plan (id, elevator_id, line_id, work_type, xio_week, ");
				sb.append("    plan_date, real_date, sign_date, emp_one_id, status) ");
				sb.append("  values('").append(this.getUUID()).append("','").append(elevatorId).append("','");
				sb.append(lineId).append("','1',").append(xioweek).append(",'").append(mydate.get(xioweek));
				sb.append("','").append(mydate.get(xioweek)).append("','").append(mydate.get(xioweek));
				sb.append("','").append(personId).append("','5'); ");
				
				fw.println(sb.toString());
			}
		}
		
		fw.close();
	}
	
	public void start() throws Exception{
		mydate.put(1635, "2016-08-26");
		mydate.put(1637, "2016-09-09");
		mydate.put(1639, "2016-09-23");
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(
						"jdbc:postgresql://localhost:5432/essdb","essdb", "essdb@zxtech");
		
		this.getLineList();
		this.getAllPerson();
		this.getAllElevator();
		this.writeTxt();
		
		System.out.println("======Finished======");
		this.conn.close();
	}
	public static void main(String[] args) throws Exception{
		new XioProject().start();

	}

}
