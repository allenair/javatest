import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.zxtech.bean.UpHardAnalysisVO;

import db.RedisUtil;

public class Test {
	private static Logger log = LoggerFactory.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String ss="ss";
		char gg='7';
		gg='5';
		Test tt = new Test();
		tt.tt190614();
	}

	private void tt190614() {
		int c=11;
		int sd=12;
		List<Integer> sortedList = new ArrayList<>();
		sortedList.add(11000);
		sortedList.add(9001);
		sortedList.add(13002);
		sortedList.add(7003);

		sortedList.stream().sorted(Comparator.<Integer>reverseOrder()).forEach(System.out::println);

		Map<String, String> matchMap = new HashMap<>();
		matchMap.put("轿门", "12345");

		String key = matchMap.keySet().stream().max(Comparator.comparing(String::length)).get();

		System.out.println(key);
	}

	private void tt190613() {
		String[][] arr = new String[][] { { "1", "2", "3", "4" }, {} };
		System.out.println(String.join(";", arr[1]));

		String[][] lastArray = new String[][] { { "q1", "q2", "q3", "q4" }, { "1", "2", "3", "4" },
				{ "1a", "2a", "3a", "4a" }, { "1", "2", "3", "4" } };

		String[][] currentArray = new String[][] { { "1", "2", "3", "4" }, { "1a", "2a", "3a", "4a" },
				{ "1b", "2b", "3b", "4b" } };

		Arrays.<String[]>stream(dealTableContentArray(currentArray, lastArray)).forEach(val -> {
			System.out.println(String.join(";", val));
		});

	}

	public String[][] dealTableContentArray(String[][] currentArray, String[][] lastArray) {
		int index = 0, count = 0;
		for (int i = lastArray.length - 1; i > 0; i--) {
			if (String.join(";", lastArray[i]).equals(String.join(";", currentArray[0]))) {
				index = i;
				for (int k = index; k < lastArray.length; k++) {
					if (!String.join(";", lastArray[k]).equals(String.join(";", currentArray[k - index]))) {
						count = 0;
						break;
					}
					count++;
				}
				break;
			}
		}

		int rowNum = lastArray.length + currentArray.length - count;
		int colNum = currentArray[0].length;

		String[][] resArr = new String[rowNum][colNum];
		for (int i = 0; i < lastArray.length; i++) {
			for (int k = 0; k < colNum; k++) {
				resArr[i][k] = lastArray[i][k];
			}
		}
		for (int i = count; i < currentArray.length; i++) {
			for (int k = 0; k < colNum; k++) {
				resArr[i + lastArray.length - count][k] = currentArray[i][k];
			}
		}

		return resArr;
	}

	private boolean isStringEqual(String one, String two) {
		String oneStr = CharMatcher.anyOf("：，。.！？,!?:").or(CharMatcher.whitespace()).removeFrom(one).trim();
		String twoStr = CharMatcher.anyOf("：，。.！？,!?:").or(CharMatcher.whitespace()).removeFrom(two).trim();

		return oneStr.equals(twoStr);
	}

	private void tt181114() {
		final int count = 5;
		for (int i = 0; i < 10; i++) {
			final int num = i * count;
			new Thread(() -> {
				for (int k = num; k < num + count; k++) {
					System.out.println(k);
				}

			}).start();
		}
	}

	private void tt181021() {
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");

		for (String item : list) {
			System.out.println(item);
			if (item.equals("2")) {
				list.remove(item);
			}
		}

		for (String string : list) {
			System.out.println(">>   " + string);
		}
	}

	private static void testNumber() {
		System.out.println(Pattern.matches("[0-9]+", "123"));
		System.out.println(Pattern.matches("[0-9]+", "a234"));
		System.out.println(Pattern.matches("[0-9]+", "90009"));
		System.out.println(Pattern.matches("[0-9]+", "12j33"));
		System.out.println(Pattern.matches("[0-9]+", "1.33"));
		System.out.println(Pattern.matches("[0-9]+", "1.33"));
		System.out.println(Pattern.matches("[0-9]+", "133 "));
	}

	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}

	private int getRandom(int scope) {
		return new Random().nextInt(scope);
	}

	public int getIntByString(String str) {
		int len = str.length();
		int byteNum = len / 8;
		StringBuilder sb = new StringBuilder();

		for (int i = byteNum - 1; i >= 0; i--) {
			sb.append(str.substring(i * 8, (i + 1) * 8));
		}

		return Integer.parseInt(sb.toString(), 2);
	}

	public String getMonthAndDay(String startDate, String endDate) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar tmp = start;
		start.setTime(format.parse(startDate));
		end.setTime(format.parse(endDate));

		int monthCount = 0;
		while (true) {
			tmp.add(Calendar.MONTH, 1);
			if (end.compareTo(tmp) > 0) {
				tmp.add(Calendar.MONTH, -1);
				break;
			}
			monthCount++;
		}

		int tmpMonth = tmp.get(Calendar.MONTH) + 1;
		int tmpDay = tmp.get(Calendar.DAY_OF_MONTH);
		int endMonth = end.get(Calendar.MONTH) + 1;
		int endDay = end.get(Calendar.DAY_OF_MONTH);
		int dayCount = 0;
		if (tmpMonth == endMonth) {
			dayCount = end.get(Calendar.DAY_OF_MONTH) - tmp.get(Calendar.DAY_OF_MONTH) + 1;
		} else {
			if (endMonth == 4 || endMonth == 6 || endMonth == 9 || endMonth == 11) {
				if (tmpDay == 30) {
					monthCount++;
				} else {

				}
			} else if (endMonth == 2) {

			}
		}

		int endYear = end.get(Calendar.YEAR);

		if (endYear % 4 == 0 && endYear % 100 != 0 || endYear % 400 == 0) {

		}

		return "";
	}

	public static final TypeAdapter<java.sql.Date> DATE = new TypeAdapter<java.sql.Date>() {
		@Override
		public java.sql.Date read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(in.nextString()).getTime());
			} catch (Exception e) {
				throw new JsonSyntaxException(e);
			}
		}

		@Override
		public void write(JsonWriter out, java.sql.Date value) throws IOException {
			out.value(new SimpleDateFormat("yyyy-MM-dd").format(value));
		}
	};
	public static final TypeAdapter<java.sql.Time> TIME = new TypeAdapter<java.sql.Time>() {
		@Override
		public java.sql.Time read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				return new java.sql.Time(new SimpleDateFormat("HH:mm:ss").parse(in.nextString()).getTime());
			} catch (Exception e) {
				throw new JsonSyntaxException(e);
			}
		}

		@Override
		public void write(JsonWriter out, java.sql.Time value) throws IOException {
			out.value(new SimpleDateFormat("HH:mm:ss").format(value));
		}
	};

	public int hex2Int(Object s) {
		return Integer.parseInt(s.toString(), 16);
	}

	public static String byte2Str(byte b) {
		String tmp = Integer.toHexString(byte2int(b)).toUpperCase();
		return tmp.length() == 1 ? "0" + tmp : tmp;
	}

	public static int byte2int(byte b) {
		int tmp = (int) b;
		tmp = tmp & 0xff;
		return tmp;
	}

	public void test1225() {
		int a = 257;
		byte b = (byte) a;

		System.out.println(a + "  " + Integer.toBinaryString(a));
		System.out.println(b + "  " + Integer.toBinaryString((int) b));

	}

	public void test0819() {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.getMostSignificantBits());
		System.out.println(uuid.getLeastSignificantBits());
//		System.out.println(uuid.timestamp());
		System.out.println(uuid.toString());
	}

	public void test1104() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 3);
		cal.set(Calendar.HOUR, 11);

		Date firdate = new Date();
		Date secdate = cal.getTime();
		System.out.println(firdate);
		System.out.println(secdate);
		System.out.println(this.twoDaysNum(firdate, secdate));
	}

	private int twoDaysNum(Date firstDate, Date secondDate) {
		int res = 0;
		long firstMill = firstDate.getTime();
		long secondMill = secondDate.getTime();
		long differ = firstMill > secondMill ? firstMill - secondMill : secondMill - firstMill;

		res = (int) Math.ceil(differ * 1.0 / (24 * 60 * 60 * 1000));
		return res;
	}
}
