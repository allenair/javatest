import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;

public class Test1909 {

	public static void main(String[] args) throws Exception  {
		doPostWithParam();
	}

	public static void doPostWithParam() throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("id", 123456789);
		param.put("name", "asdfg");
		
		
		String jsonStr = new Gson().toJson(param);
		System.out.println(jsonStr);
		
		StringEntity entity = new StringEntity(jsonStr, "utf-8");// 解决中文乱码问题
		
		byte[] b = new byte[2048];
		int num = entity.getContent().read(b);
		byte[] c = new byte[num];
		System.arraycopy(b, 0, c, 0, num);
		System.out.println(new String(c, "utf-8"));
		
	}

	public static void test0919() {
		String ss = "啊";
		System.out.println(ss.length());
		System.out.println(ss.getBytes().length);

		Random rnd = new Random(System.nanoTime());
		List<Double> numList = rnd.doubles(500000).boxed().collect(Collectors.toList());

		List<Double> numList2 = new ArrayList<>();
		for (Double num : numList) {
			numList2.add(num);
		}

		long start = System.currentTimeMillis();
		Collections.sort(numList);
		numList.stream().limit(5).forEach(System.out::println);
		System.out.println("=====Total=====" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		numList2.stream().sorted().limit(5).forEach(System.out::println);
		System.out.println("=====Total=====" + (System.currentTimeMillis() - start));

//		List<Integer> numList3 = rnd.ints(500000).boxed().collect(Collectors.toList());
//		start = System.currentTimeMillis();
//		numList3.removeIf(s -> s.equals(23));
//		System.out.println("=====Total=====" + (System.currentTimeMillis() - start));

		PriorityQueue<Double> plist = new PriorityQueue<>(5);
		for (Double double1 : numList2) {
			plist.add(double1);
		}
		start = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			System.out.println(plist.poll());
		}
		System.out.println("=====Total=====" + (System.currentTimeMillis() - start));

	}

	public static void test0909() {
		String line = "111. 每台梯配3条层门钥匙。";
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(line);
		if (m.find()) {
			System.out.println(m.start());
			System.out.println(m.end());
		}

	}

	public static void test0905() {
		int aa = 12;

		System.out.println((123) + aa * 3 + 3.0 / (aa));
		System.out.println(123 + aa * 3 + 3.0 / (-aa));
		System.out.println(Math.sqrt(3 * ((aa))));
		System.out.println((-23 + 12));

//		System.out.println("asd%fghjk#dsa#345bn%asd%fghjaswcvbn%asd%hjkl".replaceAll("asd", "@"));

		String expStr = "asd^\times% f_ghjk345bn%^asd%_fghjas\timeswcvbn_%  ^asd^_%hjkl";

		System.out.println(expStr.indexOf("asd", 0));
		System.out.println(expStr.charAt(expStr.indexOf("asd", 0) + 1));
//		System.out.println(expStr.replaceAll("\\times", "@@@"));
		System.out.println(expStr.replaceAll("\\s", ""));

		expStr = "$I_{CW}\\times N_{CW}\\times\\left(\\frac{D_M}{D_{CW}}\\right)^2\\times\\left(\\frac{1}{R_{SP}}\\right)^2$";

		expStr = expStr.replace('\\', '#');

		expStr = expStr.replaceAll("\\s", "");
		expStr = expStr.replaceAll("\\$", "");
		expStr = expStr.replaceAll("#times", " * ");
		expStr = expStr.replaceAll("#div", " / ");
		expStr = expStr.replaceAll("#left\\(", " ( ");
		expStr = expStr.replaceAll("#left\\|", " | ");
		expStr = expStr.replaceAll("\\+", " + ");
		expStr = expStr.replaceAll("\\-", " - ");

		System.out.println(expStr);

		int startIndex = expStr.indexOf('{');
		int endIndex = findPair(expStr, startIndex, '{', '}');
		System.out.println(expStr.substring(startIndex, endIndex + 1));

	}

	private static String dealFrac(String expressStr) {
		String expStr = expressStr;
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while ((index = expStr.indexOf("#FRAC")) > -1) {
			int startIndex = expStr.indexOf('{', index);
			int endIndex = findPair(expStr, startIndex, '{', '}');

			int startIndex2 = expStr.indexOf('{', endIndex + 1);
			int endIndex2 = findPair(expStr, startIndex2, '{', '}');

			sb = new StringBuilder();
			sb.append(expStr.substring(0, index));
			sb.append(" (");
			sb.append(expStr.substring(startIndex + 1, endIndex));
			sb.append(") / (");
			sb.append(expStr.substring(startIndex2 + 1, endIndex2));
			sb.append(") ");
			sb.append(expStr.substring(endIndex2 + 1));

			expStr = sb.toString();
		}

		return expStr;
	}

	private static int findPair(String expressStr, int startIndex, char left, char right) {
		int endIndex = startIndex;
		Stack<Character> checkStack = new Stack<>();
		checkStack.push(left);

		for (int i = startIndex + 1; i < expressStr.length(); i++) {
			if (right == expressStr.charAt(i)) {
				checkStack.pop();
			} else if (left == expressStr.charAt(i)) {
				checkStack.push(left);
			}

			if (checkStack.isEmpty()) {
				endIndex = i;
				break;
			}
		}

		return endIndex;
	}
}
