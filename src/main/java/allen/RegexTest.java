package allen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("额\\w*定");
		String src = "1.1额定载重800kg;1额de定速度：1.75m/s;1.2额aaa定速度：1m/s额aaa定";
		Matcher matcher = pattern.matcher(src);
		while (matcher.find()) {
			System.out.println(matcher.group());
			System.out.println(matcher.start()+" == "+matcher.end());
			System.out.println(src.substring(matcher.end()));
		}
		
		System.out.println(src.substring(18,32));
	}

}
