import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1909 {

	public static void main(String[] args) {
		test0909();
	}

	public static void test0909() {
		String line = "111. 每台梯配3条层门钥匙。";
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(line);
		if(m.find()) {
			System.out.println(m.start());
			System.out.println(m.end());
		}
			
		
	}
	
	public static void test0905() {
		int aa = 12;

		System.out.println((123) + aa * 3 + 3.0 / ( aa ));
		System.out.println(123 + aa * 3 + 3.0 / ( - aa ));
		System.out.println(Math.sqrt(3 * ((aa))));
		System.out.println(( - 23 + 12));

//		System.out.println("asd%fghjk#dsa#345bn%asd%fghjaswcvbn%asd%hjkl".replaceAll("asd", "@"));
		
		String expStr = "asd^\times% f_ghjk345bn%^asd%_fghjas\timeswcvbn_%  ^asd^_%hjkl";
		
		System.out.println(expStr.indexOf("asd",0));
		System.out.println(expStr.charAt(expStr.indexOf("asd",0)+1));
//		System.out.println(expStr.replaceAll("\\times", "@@@"));
		System.out.println(expStr.replaceAll("\\s", ""));
		
		expStr="$I_{CW}\\times N_{CW}\\times\\left(\\frac{D_M}{D_{CW}}\\right)^2\\times\\left(\\frac{1}{R_{SP}}\\right)^2$";
		
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
		System.out.println(expStr.substring(startIndex, endIndex+1));
		
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
