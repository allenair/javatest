import java.time.DayOfWeek;
import java.time.LocalDate;

public class Test2008 {

	public static void main(String[] args) {
		genWorkDay(2022);
	}

	public static void genWorkDay(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year + 1, 1, 1);
		String weight = "1.0";

		while (start.isBefore(end)) {
			weight = "1.0";
			if (start.getDayOfWeek().equals(DayOfWeek.SUNDAY) || start.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				weight = "0.0";
			}
			System.out.println("insert into zx_workday(work_date, weight) values('" + start + "', " + weight + ");");
			start = start.plusDays(1);
		}
	}
}
