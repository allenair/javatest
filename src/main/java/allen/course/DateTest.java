package allen.course;

import java.util.Calendar;
import java.util.Date;

public class DateTest {
	private int[] days = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static void main(String[] args) {
		DateTest tt = new DateTest();

		System.out.println(tt.isLegalDate(2012, 2, 31));
		System.out.println(tt.getDateAfterDays(new Date(), 22));

		Calendar first = Calendar.getInstance();
		Calendar second = Calendar.getInstance();
		second.set(Calendar.YEAR, 2012);
		second.set(Calendar.MONTH, Calendar.MAY);
		second.set(Calendar.DAY_OF_MONTH, 22);
		System.out.println(tt.diffDays(first.getTime(), second.getTime()));
	}

	public boolean isLegalDate(int year, int month, int day) {
		if (year < 0)
			return false;

		if (month > 12 || month < 1)
			return false;

		int tmpDay = days[month];

		if (isLeapYear(year)) {
			if (month == 2 && day != 29)
				return false;
		} else {
			if (day != tmpDay)
				return false;
		}

		return true;
	}

	private boolean isLeapYear(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}

	public Date getDateAfterDays(Date today, int days) {
		Calendar cnow = Calendar.getInstance();
		cnow.setTime(today);
		cnow.add(Calendar.DATE, days);

		return cnow.getTime();
	}

	public int diffDays(Date firstDate, Date secondDate) {
		long firstMilliSeconds = firstDate.getTime();
		long secondLilliSeconds = secondDate.getTime();

		long days = (long) Math.ceil((firstMilliSeconds - secondLilliSeconds)
				/ (24 * 60 * 60 * 1000));

		return (int) days;
	}
}
