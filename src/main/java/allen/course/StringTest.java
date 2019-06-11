package allen.course;

public class StringTest {

	public static void main(String[] args) {
		StringTest tt = new StringTest();

		System.out.println(tt.reverseStr("123456789asd"));
		System.out.println(tt.deleteSpace(" abc   a bc   123 a#  "));
	}

	public String reverseStr(String srcStr) {
		StringBuilder sb = new StringBuilder();
		if (srcStr == null || srcStr.trim().length() == 0)
			return "";

		for (int i = srcStr.trim().length() - 1; i >= 0; i--) {
			sb.append(srcStr.charAt(i));
		}

		return sb.toString();
	}

	public String deleteSpace(String src) {
		StringBuilder sb = new StringBuilder();

		String[] strArray = src.trim().split("\\s");
		for (String string : strArray) {
			if (string != null && string.length() > 0) {
				sb.append(string.substring(0, 1).toUpperCase());
				sb.append(string.substring(1));
				sb.append(" ");
			}
		}

		return sb.toString();
	}
}
