import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1909 {

	public static void main(String[] args) {
		test0905();
	}

	public static void test0905() {
		int aa = 12;

		System.out.println((123) + aa * 3 + 3.0 / ( aa ));
		System.out.println(123 + aa * 3 + 3.0 / ( - aa ));
		System.out.println(Math.sqrt(3 * ((aa))));
		System.out.println(( - 23 + 12));

		System.out.println("asd%fghjk#dsa#345bn%asd%fghjaswcvbn%asd%hjkl".replaceAll("asd", "@"));
		
		String expStr = "asd^%f_ghjk345bn%^asd%_fghjaswcvbn_%^asd^_%hjkl";
		
		System.out.println(expStr.indexOf("asd",0));
		System.out.println(expStr.charAt(expStr.indexOf("asd",0)+1));
		
		StringBuilder sb = new StringBuilder();
		char c;
		for(int i=0; i<expStr.length(); i++) {
			c = expStr.charAt(i);
			if((c=='^' || c=='_') && expStr.charAt(i+1)!='{') {
				sb.append(c);
				sb.append("{");
				sb.append(expStr.charAt(i+1));
				sb.append("}");
				i++;
			}else {
				sb.append(c);
			}
		}
		expStr = sb.toString();
		
//		while ((index = expStr.indexOf("^", index)) > -1 || (index = expStr.indexOf("_", index)) > -1) {
//			if(expStr.charAt(index+1)!='{') {
//				expStr = expStr.substring(0, index+1)+"{"+expStr.charAt(index+1)+"}"+expStr.substring(index+2);
//			}
//			index++;
//		}
		
		expStr = "asd^%f_ghjk345 bn%^a23 sd%_fghjas2 wcvbn_%^a456 Asd^_78 #PI%hjkl";
		Pattern pattern = Pattern.compile("\\d+[a-z,A-Z,#]");
		Matcher matcher = pattern.matcher(expStr);
		int index = 0;
		sb = new StringBuilder();
		while (matcher.find(index)) {
			String tmp = matcher.group();
			tmp = tmp.substring(0, tmp.length() - 1) + " * " + tmp.substring(tmp.length() - 1);
			sb.append(expStr.substring(index, matcher.start()));
			sb.append(tmp);
			index = matcher.end();
		}
		if(index<expStr.length()) {
			sb.append(expStr.substring(index));
		}
		System.out.println(expStr);
		System.out.println(sb.toString());
		
//		if (matcher.find(1)) {
//			System.out.println("=================");
//			System.out.println(matcher.group());
//			System.out.println(matcher.start());
//			System.out.println(matcher.end());
//		} 

		
	}

}
