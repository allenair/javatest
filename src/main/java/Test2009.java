import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Test2009 {

	public static void main(String[] args) {
		t0912();

	}

	private static void t0912() {
		BigDecimal zz = new BigDecimal(12.3);
		System.out.println(zz.setScale(2,RoundingMode.HALF_UP).toString());
		
		BigDecimal aa = new BigDecimal(100);
		BigDecimal bb = new BigDecimal(20.5);
		
		System.out.println(aa.add(bb.negate()));
		
		LocalDate createDate = LocalDate.now().minusMonths(4).withDayOfMonth(1);
		System.out.println(createDate);
		
		LocalDateTime createTime = LocalDateTime.now().minusMonths(4).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
		System.out.println(createTime);
		
		System.out.println(String.format("%.2f", 5*100.0/7)+"%");
		
		System.out.println(String.format("%.2f", new BigDecimal("5.00").doubleValue()/new BigDecimal(7).doubleValue())+"%");
	}
}
