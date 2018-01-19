package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;


public class SetHoliday {

	private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> holiMap = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
	private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> workMap = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception  {
		new SetHoliday().start();
	}

	public void start() throws Exception {
		Connection conn = this.getConn();
		
		initMap();
		
		deleteOldRecord(2013, conn);
		insertHoliday(2013,conn);
		
		deleteOldRecord(2014, conn);
		insertHoliday(2014,conn);
		
		deleteOldRecord(2015, conn);
		insertHoliday(2015,conn);
	}
	
	private void initMap() {
		holiMap.put(2013, new HashMap<Integer, ArrayList<Integer>>());
		workMap.put(2013, new HashMap<Integer, ArrayList<Integer>>());
		for (int i = 1; i <= 12; i++) {
			holiMap.get(2013).put(i, new ArrayList<Integer>());
			workMap.get(2013).put(i, new ArrayList<Integer>());
		}

		holiMap.get(2013).get(1).add(1);
		holiMap.get(2013).get(1).add(2);
		holiMap.get(2013).get(1).add(3);
		workMap.get(2013).get(1).add(5);
		workMap.get(2013).get(1).add(6);

		holiMap.get(2013).get(2).add(9);
		holiMap.get(2013).get(2).add(10);
		holiMap.get(2013).get(2).add(11);
		holiMap.get(2013).get(2).add(12);
		holiMap.get(2013).get(2).add(13);
		holiMap.get(2013).get(2).add(14);
		holiMap.get(2013).get(2).add(15);
		workMap.get(2013).get(2).add(16);
		workMap.get(2013).get(2).add(17);

		holiMap.get(2013).get(4).add(4);
		holiMap.get(2013).get(4).add(5);
		holiMap.get(2013).get(4).add(6);
		holiMap.get(2013).get(4).add(29);
		holiMap.get(2013).get(4).add(30);
		workMap.get(2013).get(4).add(7);
		workMap.get(2013).get(4).add(27);
		workMap.get(2013).get(4).add(28);

		holiMap.get(2013).get(5).add(1);

		holiMap.get(2013).get(6).add(10);
		holiMap.get(2013).get(6).add(11);
		holiMap.get(2013).get(6).add(12);
		workMap.get(2013).get(6).add(8);
		workMap.get(2013).get(6).add(9);

		holiMap.get(2013).get(9).add(19);
		holiMap.get(2013).get(9).add(20);
		holiMap.get(2013).get(9).add(21);
		workMap.get(2013).get(9).add(22);
		workMap.get(2013).get(9).add(29);

		holiMap.get(2013).get(10).add(1);
		holiMap.get(2013).get(10).add(2);
		holiMap.get(2013).get(10).add(3);
		holiMap.get(2013).get(10).add(4);
		holiMap.get(2013).get(10).add(5);
		holiMap.get(2013).get(10).add(6);
		holiMap.get(2013).get(10).add(7);
		workMap.get(2013).get(10).add(12);
		
		holiMap.get(2013).get(12).add(30);
		holiMap.get(2013).get(12).add(31);
		workMap.get(2013).get(12).add(28);
		workMap.get(2013).get(12).add(29);
		
		
		
		

		
		
		
		holiMap.put(2014, new HashMap<Integer, ArrayList<Integer>>());
		workMap.put(2014, new HashMap<Integer, ArrayList<Integer>>());
		for (int i = 1; i <= 12; i++) {
			holiMap.get(2014).put(i, new ArrayList<Integer>());
			workMap.get(2014).put(i, new ArrayList<Integer>());
		}
		
		holiMap.get(2014).get(1).add(1);
		holiMap.get(2014).get(1).add(30);
		holiMap.get(2014).get(1).add(31);
		workMap.get(2014).get(1).add(26);
		
		holiMap.get(2014).get(2).add(1);
		holiMap.get(2014).get(2).add(2);
		holiMap.get(2014).get(2).add(3);
		holiMap.get(2014).get(2).add(4);
		holiMap.get(2014).get(2).add(5);
		workMap.get(2014).get(2).add(8);
		
		holiMap.get(2014).get(4).add(5);
		holiMap.get(2014).get(4).add(6);
		holiMap.get(2014).get(4).add(7);
		
		holiMap.get(2014).get(5).add(1);
		holiMap.get(2014).get(5).add(2);
		holiMap.get(2014).get(5).add(3);
		holiMap.get(2014).get(5).add(31);
		workMap.get(2014).get(5).add(4);
		
		holiMap.get(2014).get(6).add(1);
		holiMap.get(2014).get(6).add(2);
		
		holiMap.get(2014).get(9).add(6);
		holiMap.get(2014).get(9).add(7);
		holiMap.get(2014).get(9).add(8);
		workMap.get(2014).get(9).add(28);
		
		holiMap.get(2014).get(10).add(1);
		holiMap.get(2014).get(10).add(2);
		holiMap.get(2014).get(10).add(3);
		holiMap.get(2014).get(10).add(4);
		holiMap.get(2014).get(10).add(5);
		holiMap.get(2014).get(10).add(6);
		holiMap.get(2014).get(10).add(7);
		workMap.get(2014).get(10).add(11);
	}

	private void insertHoliday(int year, Connection conn) throws Exception {
		if(holiMap.get(year)==null || workMap.get(year)==null){
			holiMap.put(year, new HashMap<Integer, ArrayList<Integer>>());
			workMap.put(year, new HashMap<Integer, ArrayList<Integer>>());
			for (int i = 1; i <= 12; i++) {
				holiMap.get(year).put(i, new ArrayList<Integer>());
				workMap.get(year).put(i, new ArrayList<Integer>());
			}
		}
		
		PreparedStatement pst = conn
				.prepareStatement("insert into sys_holiday (holiday_date, holiday_year, holiday_month, holiday_day) values (?, ?, ?, ?)");
		Calendar cal = Calendar.getInstance();
		cal.set(year, Calendar.JANUARY, 1);

		while (cal.get(Calendar.YEAR) == year) {
			if(isHoliday(cal)){
				pst.setDate(1, new Date(cal.getTimeInMillis()));
				pst.setInt(2, cal.get(Calendar.YEAR));
				pst.setInt(3, cal.get(Calendar.MONTH)+1);
				pst.setInt(4, cal.get(Calendar.DATE));
				pst.executeUpdate();
			}
			cal.add(Calendar.DATE, 1);
		}
		
		pst.close();
	}

	private void deleteOldRecord(int year, Connection conn) throws Exception {
		PreparedStatement pst = conn
				.prepareStatement("delete from sys_holiday where holiday_year=?");
		pst.setInt(1, year);
		pst.executeUpdate();
		pst.close();
	}
	
	private boolean isHoliday(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);

		if (workMap.get(year).get(month).contains(day)) {
			return false;
		} else if (cal.get(Calendar.DAY_OF_WEEK) == 1
				|| cal.get(Calendar.DAY_OF_WEEK) == 7
				|| holiMap.get(year).get(month).contains(day)) {
			return true;
		}

		return false;
	}


	public Connection getConn() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://192.168.1.229:5432/soadb", "soa_dev",
				"soa_dev@2013");

		System.out.println(StringUtils.center("Get Connection Success!", 40,
				"="));
		return conn;
	}
}
