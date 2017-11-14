package com.zxtech.xio;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XioWeek {

	// 2016-01-04    1024
	public static void main(String[] args)throws IOException {
//		LocalDate startMonday = LocalDate.now();
//		System.out.println(startMonday.getDayOfWeek().getValue());
//		
//		System.out.println(startMonday.plusDays(-1).getDayOfWeek().getValue());
//		;
		
		// TODO Auto-generated method stub
		LocalDate startMonday = LocalDate.of(2016, 1, 4);
		LocalDate startSunday = LocalDate.of(2016, 1, 10);
		XioWeek test = new XioWeek();
		List<Map<String, String>> list = test.getAllWeekList(startMonday, startSunday, 1024, 100);
		for (Map<String, String> row : list) {
			System.out.println(row.get("week")+"  "+row.get("start")+"  "+row.get("end"));
		}
		test.writeToFile(list);
	}

	private List<Map<String, String>> getAllWeekList(LocalDate startMonday, LocalDate startSunday, int start, int weeks){
		ArrayList<Map<String, String>> weekList = new ArrayList<>();
		for(int i=0; i<weeks;i++){
			HashMap<String, String> row = new HashMap<>();
			row.put("week", ""+(start-i));
			row.put("start", startMonday.plusWeeks(0-i).toString());
			row.put("end", startSunday.plusWeeks(0-i).toString());
			weekList.add(row);
		}
		
		
		
		return weekList;
	}
	
	private void writeToFile(List<Map<String, String>> list)throws IOException{
		PrintWriter fw = new PrintWriter("/Users/allen/Desktop/xio-week.sql");
		String insertStr = " insert into mt_xio_week(xio_week, start_date, end_date) values(";
		for (Map<String, String> row : list) {
			StringBuilder sb = new StringBuilder();
			sb.append(insertStr);
			sb.append(row.get("week")).append(",'");
			sb.append(row.get("start")).append("','");
			sb.append(row.get("end")).append("'); ");
			fw.println(sb.toString());
		}
		
		fw.close();
	}
}
