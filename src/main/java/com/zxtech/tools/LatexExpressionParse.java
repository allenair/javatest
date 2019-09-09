package com.zxtech.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatexExpressionParse {
	private static List<String[]> replaceList = new ArrayList<>();
	private static List<String[]> languageMapList = new ArrayList<>();
	
	static {
		replaceList.add(new String[] { "\\s", "" });
		replaceList.add(new String[] { "\\$", "" });
		replaceList.add(new String[] { "#mathrm", "" });

		replaceList.add(new String[] { "#times", " * " });
		replaceList.add(new String[] { "#div", " / " });
		replaceList.add(new String[] { "#left\\(", " ( " });
		replaceList.add(new String[] { "#left#\\{", " ( " });
		replaceList.add(new String[] { "#right\\)", " ) " });
		replaceList.add(new String[] { "#right#\\}", " ) " });
		replaceList.add(new String[] { "#left\\|", " #FABS( " });
		replaceList.add(new String[] { "#right\\|", " ) " });
		replaceList.add(new String[] { "#frac", "#FRAC" });

		replaceList.add(new String[] { "#cos", "#COS" });
		replaceList.add(new String[] { "#sin", "#SIN" });
		replaceList.add(new String[] { "#tan", "#TAN" });
		replaceList.add(new String[] { "#cot", "#COT" });
		replaceList.add(new String[] { "#sqrt", "#SQRT" });
		replaceList.add(new String[] { "#log", "#LOG" });
		replaceList.add(new String[] { "#ln", "#LN" });

		replaceList.add(new String[] { "#alpha", "alpha" });
		replaceList.add(new String[] { "#beta", "beta" });
		replaceList.add(new String[] { "#gamma", "gamma" });
		replaceList.add(new String[] { "#delta", "delta" });
		replaceList.add(new String[] { "#epsilon", "epsilon" });
		replaceList.add(new String[] { "#zeta", "zeta" });
		replaceList.add(new String[] { "#eta", "eta" });
		replaceList.add(new String[] { "#theta", "theta" });
		replaceList.add(new String[] { "#iota", "iota" });
		replaceList.add(new String[] { "#kappa", "kappa" });
		replaceList.add(new String[] { "#lambda", "lambda" });
		replaceList.add(new String[] { "#mu", "mu" });
		replaceList.add(new String[] { "#nu", "nu" });
		replaceList.add(new String[] { "#xi", "xi" });
		replaceList.add(new String[] { "#pi", "#FPI" });
		replaceList.add(new String[] { "#rho", "rho" });
		replaceList.add(new String[] { "#sigma", "sigma" });
		replaceList.add(new String[] { "#tau", "tau" });
		replaceList.add(new String[] { "#upsilon", "upsilon" });
		replaceList.add(new String[] { "#phi", "phi" });
		replaceList.add(new String[] { "#chi", "chi" });
		replaceList.add(new String[] { "#psi", "psi" });
		replaceList.add(new String[] { "#omega", "omega" });

		languageMapList.add(new String[] { "#FSQRT", "System.Math.Sqrt" });
		languageMapList.add(new String[] { "#FPOW", "System.Math.Pow" });
		languageMapList.add(new String[] { "#FLOG", "System.Math.Log" });
		languageMapList.add(new String[] { "#FSIN", "System.Math.Sinh" });
		languageMapList.add(new String[] { "#FCOS", "System.Math.Cosh" });
		languageMapList.add(new String[] { "#FTAN", "System.Math.Tanh" });
		languageMapList.add(new String[] { "#FCOT", "System.Math.Coth" });
		languageMapList.add(new String[] { "#FABS", "System.Math.Abs" });
		languageMapList.add(new String[] { "#FPI", "System.Math.PI" });
	}
	
	public static Map<String, String> parse(String srcExpression) {
		Map<String, String> resMap = new HashMap<>();
		String expressStr = srcExpression;

		// 1、预处理
		expressStr = preDealNormalSign(expressStr);

		// 2、处理分数，处理后分数会变为括号包裹的除法单元
		expressStr = dealFrac(expressStr);

		// 3、处理开方sqrt、sqrt[...]，处理后会变为#FSQRT()或#FPOW()包裹的单元
		expressStr = dealPower(expressStr);

		// 4、处理log、log[...]、ln，处理后会变为#FLOG()包裹的单元
		expressStr = dealLog(expressStr);

		// 5、处理三角函数，sin、cos、tan、cot、以及sin^{...}、cos^{...}、tan^{...}、cot^{...}
		expressStr = dealAngleFunction(expressStr);

		// 6、处理乘方 ^ ，由于操作数在左右两边，因此此部分需要最后处理（此时左右两边的公式会有规律性）
		expressStr = dealUpPower(expressStr);

		// 7、删除多余的大括号（此时大括号中应该仅剩字符串变量），然后提取参数（参数是指非关键字，并且是字母开头的字符串）
		expressStr = expressStr.replaceAll("\\{", "");
		expressStr = expressStr.replaceAll("\\}", "");
		String parameters = getParameters(expressStr);

		// 处理数字与字符紧邻的情况
		expressStr = dealNumberAndLetter(expressStr);
				
		// 8、使用目标语言的函数替换占位符
		expressStr = lastDealLanguageSign(expressStr);

		// 9、去掉多余的空格
		expressStr = expressStr.replaceAll("\\s", "");

		resMap.put("expression", expressStr);
		resMap.put("parameters", parameters);
		return resMap;
	}

	/*
	 * 先处理常规符号，以便简化表达式 
	 * 1、使用 * 替换 \times 
	 * 2、使用 / 替换 \div 
	 * 3、使用 ( 替换 \left( 
	 * 4、使用 ) 替换 \right) 
	 * 5、对常用的希腊字符进行替换（主要是去掉 \ 变为普通字符，例如 \alpha 转变为 alpha）
	 * 6、使用 #ABS(...) 替换 \left|...\right| 
	 * 7、将 ^ 和 _ 紧邻的后续字符使用 { } 包裹（默认一个字符时候会有不包裹的情况）
	 * 8、将 + 和 - 左右增加空格，以便后续处理
	 */
	private static String preDealNormalSign(String expressStr) {
		String expStr = expressStr;

		expStr = expStr.replace('\\', '#');
		// 处理1-6
		for (String[] arr : replaceList) {
			expStr = expStr.replaceAll(arr[0], arr[1]);
		}

		// 处理7
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < expStr.length(); i++) {
			c = expStr.charAt(i);
			if ((c == '^' || c == '_') && expStr.charAt(i + 1) != '{') {
				sb.append(c).append("{").append(expStr.charAt(i + 1)).append("}");
				i++;
			} else {
				sb.append(c);
			}
		}
		expStr = sb.toString();

		// 处理8
		expStr = expStr.replaceAll("\\+", " + ");
		expStr = expStr.replaceAll("\\-", " - ");

		return expStr;
	}

	private static String lastDealLanguageSign(String expressStr) {
		String expStr = expressStr;
		for (String[] arr : languageMapList) {
			expStr = expStr.replaceAll(arr[0], arr[1]);
		}
		return expStr;
	}
	
	// 处理开根号与开高次方
	// \sqrt{ab}    \sqrt[23]{df}    
	private static String dealPower(String expressStr) {
		String expStr = expressStr;

		int index = 0;
		StringBuilder sb = new StringBuilder();
		while ((index = expStr.indexOf("#SQRT")) > -1) {
			int startIndex = expStr.indexOf('{', index);
			int endIndex = findPair(expStr, startIndex, '{', '}');

			// 判断是否为\sqrt[23]{df}模式
			String otherPower = "";
			int otherStartIndex = expStr.indexOf("[", index);
			if (otherStartIndex < startIndex && otherStartIndex > index) {
				int otherEndIndex = findPair(expStr, otherStartIndex, '[', ']');
				otherPower = expStr.substring(otherStartIndex + 1, otherEndIndex);
			}

			sb = new StringBuilder();
			sb.append(expStr.substring(0, index));
			if ("".equals(otherPower)) {
				sb.append("#FSQRT(");
			} else {
				sb.append("#FPOW(");
			}

			sb.append(expStr.substring(startIndex + 1, endIndex));

			if ("".equals(otherPower)) {
				sb.append(") ");
			} else {
				sb.append(", ");
				sb.append(" 1.0 / " + otherPower);
				sb.append(") ");
			}

			sb.append(expStr.substring(endIndex + 1));
			expStr = sb.toString();
		}

		return expStr;
	}
	
	// 处理幂，sdf^3
	private static String dealUpPower(String expressStr) {
		String expStr = expressStr;

		int index = 0;
		StringBuilder sb = new StringBuilder();
		while ((index = expStr.indexOf("^")) > -1) {
			// 先找后半部分（一定是以大括号圈定）
			int lastStartIndex = expStr.indexOf('{', index);
			int lastEndIndex = findPair(expStr, lastStartIndex, '{', '}');
			String powStr = expStr.substring(lastStartIndex + 1, lastEndIndex);

			// 再找前半部分（可能是一个数字，一个变量，或是以小括号圈定）
			int preStartIndex = 0;
			int preEndIndex = index - 1;
			for (int i = index - 1; i >= 0; i--) {
				if (expStr.charAt(i) != ' ') {
					preEndIndex = i;
					break;
				}
			}
			String numStr = "";
			if (expStr.charAt(preEndIndex) == ')') {
				preStartIndex = findPairReverse(expStr, preEndIndex - 1, '(', ')');
				numStr = expStr.substring(preStartIndex + 1, preEndIndex);
			} else {
				for (int i = preEndIndex; i >= 0; i--) {
					if (expStr.charAt(i) == ' ') {
						break;
					}
					preStartIndex = i;
				}
				numStr = expStr.substring(preStartIndex, preEndIndex + 1);
			}

			sb = new StringBuilder();
			sb.append(expStr.substring(0, preStartIndex));
			sb.append("#FPOW( ");
			sb.append(numStr);
			sb.append(" , ");
			sb.append(powStr);
			sb.append(" ) ");

			sb.append(expStr.substring(lastEndIndex + 1));
			expStr = sb.toString();
		}

		return expStr;
	}
	
	// 处理log和ln    \log\left(sd\right)     \log_{22}\left(222\right)     \ln\left(sff\right)
	private static String dealLog(String expressStr) {
		String expStr = expressStr;

		int index = 0;
		StringBuilder sb = new StringBuilder();

		while ((index = expStr.indexOf("#LOG")) > -1) {
			String base = "10";
			int startIndex = expStr.indexOf('(', index);
			int endIndex = findPair(expStr, startIndex, '(', ')');

			// 判断是否为\log_{22}模式
			if (expStr.charAt(index + 4) == '_') {
				int otherStartIndex = expStr.indexOf("{", index);
				if (otherStartIndex < startIndex && otherStartIndex > index) {
					int otherEndIndex = findPair(expStr, otherStartIndex, '{', '}');
					base = expStr.substring(otherStartIndex + 1, otherEndIndex);
				}
			}

			sb = new StringBuilder();
			sb.append(expStr.substring(0, index));
			sb.append("#FLOG( ");
			sb.append(expStr.substring(startIndex + 1, endIndex));
			sb.append(" , ");
			sb.append(base);
			sb.append(" ) ");

			sb.append(expStr.substring(endIndex + 1));
			expStr = sb.toString();
		}
		while ((index = expStr.indexOf("#LN")) > -1) {
			int startIndex = expStr.indexOf('(', index);
			int endIndex = findPair(expStr, startIndex, '(', ')');

			sb = new StringBuilder();
			sb.append(expStr.substring(0, index));
			sb.append("#FLOG( ");
			sb.append(expStr.substring(startIndex + 1, endIndex));
			sb.append(" ) ");

			sb.append(expStr.substring(endIndex + 1));
			expStr = sb.toString();
		}

		return expStr;
	}

	// 处理三角函数
	private static String dealAngleFunction(String expressStr) {
		String expStr = expressStr;
		List<String[]> mapList = new ArrayList<>();
		mapList.add(new String[] { "#SIN", "#FSIN" });
		mapList.add(new String[] { "#COS", "#FCOS" });
		mapList.add(new String[] { "#TAN", "#FTAN" });
		mapList.add(new String[] { "#COT", "#FCOT" });

		int index = 0;
		StringBuilder sb = new StringBuilder();
		for (String[] arr : mapList) {
			while ((index = expStr.indexOf(arr[0])) > -1) {
				// 处理 \cos^{2}(34+s)的问题
				String powStr = "";
				if (expStr.charAt(index + 4) == '^') {
					int endPowIndex = findPair(expStr, index + 5, '{', '}');
					powStr = expStr.substring(index + 4, endPowIndex + 1);
				}

				int startIndex = expStr.indexOf('(', index);
				int endIndex = findPair(expStr, startIndex, '(', ')');

				sb = new StringBuilder();
				sb.append(expStr.substring(0, index));

				if (!"".equals(powStr)) {
					sb.append(" (");
				}

				sb.append(arr[1]);
				sb.append("( ");
				sb.append(expStr.substring(startIndex + 1, endIndex));
				sb.append(" ) ");

				if (!"".equals(powStr)) {
					sb.append(" ) ");
					sb.append(powStr);
				}

				sb.append(expStr.substring(endIndex + 1));
				expStr = sb.toString();
			}
		}

		return expStr;
	}
	
	// 处理分数形式     \frac{D_t}{d_r}
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
			sb.append(" (( ");
			sb.append(expStr.substring(startIndex + 1, endIndex));
			sb.append(" ) / ( ");
			sb.append(expStr.substring(startIndex2 + 1, endIndex2));
			sb.append(" )) ");

			sb.append(expStr.substring(endIndex2 + 1));
			expStr = sb.toString();
		}

		return expStr;
	}
	
	// 9、将数字与非数字字符（字母或#）紧邻的情况中间加入 * ，举例：2a, 2alpha 转换为 2*a, 2*alpha
	private static String dealNumberAndLetter(String expressStr) {
		Pattern pattern = Pattern.compile("\\s\\d+[a-zA-Z#]");
		Matcher matcher = pattern.matcher(expressStr);
		int index = 0;
		StringBuilder sb = new StringBuilder();
		while (matcher.find(index)) {
			String tmp = matcher.group();
			tmp = tmp.substring(0, tmp.length() - 1) + " * " + tmp.substring(tmp.length() - 1);
			sb.append(expressStr.substring(index, matcher.start()));
			sb.append(tmp);
			index = matcher.end();
		}
		if (index < expressStr.length()) {
			sb.append(expressStr.substring(index));
		}
		expressStr = sb.toString();
		
		return expressStr;
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
	
	private static int findPairReverse(String expressStr, int startIndex, char left, char right) {
		int endIndex = startIndex;
		Stack<Character> checkStack = new Stack<>();
		checkStack.push(right);

		for (int i = startIndex; i >= 0; i--) {
			if (left == expressStr.charAt(i)) {
				checkStack.pop();
			} else if (right == expressStr.charAt(i)) {
				checkStack.push(right);
			}

			if (checkStack.isEmpty()) {
				endIndex = i;
				break;
			}
		}

		return endIndex;
	}
	
	// 从公式中获取变量
	private static String getParameters(String expressStr) {
		Set<String> paramSet = new HashSet<>();
		Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*");
		String[] arr = expressStr.split("\\s+");
		for (String str : arr) {
			str = str.replace('(', ' ').trim();
			str = str.replace(')', ' ').trim();
			if (pattern.matcher(str).find()) {
				paramSet.add(str);
			}
		}

		List<String> paramList = new ArrayList<>();
		paramSet.forEach(param->paramList.add(param));
		Collections.sort(paramList);
		return String.join(",", paramList);
	}

	public static void main(String[] args) throws Exception {
		try (BufferedReader fin = new BufferedReader(new FileReader("d:/latex.txt"));
				PrintWriter fout = new PrintWriter("d:/out.txt", "utf-8");) {
			fin.lines().limit(1000).forEach(line -> {
				System.out.println(line);
				Map<String, String> resMap = LatexExpressionParse.parse(line);
				System.out.println(">>>:  " + resMap.get("expression"));
				System.out.println(">>>:  " + resMap.get("parameters"));
				System.out.println("========================================================");
				
				fout.println(line);
				fout.println(resMap.get("expression"));
				fout.println(resMap.get("parameters"));
				fout.println("========================================================");
			});
		}
	}

}
