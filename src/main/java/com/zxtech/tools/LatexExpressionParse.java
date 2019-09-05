package com.zxtech.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatexExpressionParse {
	private static List<String[]> replaceList = new ArrayList<>();
	
	static {
		replaceList.add(new String[] {"\\times", " * "});
		replaceList.add(new String[] {"\\div", " / "});
		replaceList.add(new String[] {"\\left(", " ( "});
		replaceList.add(new String[] {"\\right)", " ) "});
		replaceList.add(new String[] {"\\left|", " #ABS( "});
		replaceList.add(new String[] {"\\right|", " ) "});
		replaceList.add(new String[] {"\\alpha", "alpha"});
		replaceList.add(new String[] {"\\beta", "beta"});
		replaceList.add(new String[] {"\\gamma", "gamma"});
		replaceList.add(new String[] {"\\delta", "delta"});
		replaceList.add(new String[] {"\\epsilon", "epsilon"});
		replaceList.add(new String[] {"\\zeta", "zeta"});
		replaceList.add(new String[] {"\\eta", "eta"});
		replaceList.add(new String[] {"\\theta", "theta"});
		replaceList.add(new String[] {"\\iota", "iota"});
		replaceList.add(new String[] {"\\kappa", "kappa"});
		replaceList.add(new String[] {"\\lambda", "lambda"});
		replaceList.add(new String[] {"\\mu", "mu"});
		replaceList.add(new String[] {"\\nu", "nu"});
		replaceList.add(new String[] {"\\xi", "xi"});
		replaceList.add(new String[] {"\\pi", "#PI"});
		replaceList.add(new String[] {"\\rho", "rho"});
		replaceList.add(new String[] {"\\sigma", "sigma"});
		replaceList.add(new String[] {"\\tau", "tau"});
		replaceList.add(new String[] {"\\upsilon", "upsilon"});
		replaceList.add(new String[] {"\\phi", "phi"});
		replaceList.add(new String[] {"\\chi", "chi"});
		replaceList.add(new String[] {"\\psi", "psi"});
		replaceList.add(new String[] {"\\omega", "omega"});
		
		replaceList.add(new String[] {"\\cos", "#COS"});
		replaceList.add(new String[] {"\\sin", "#SIN"});
		replaceList.add(new String[] {"\\tan", "#TAN"});
		replaceList.add(new String[] {"\\cot", "#COT"});
		replaceList.add(new String[] {"\\sqrt", "#SQRT"});
		replaceList.add(new String[] {"\\log", "#LOG"});
		replaceList.add(new String[] {"\\ln", "#LN"});
	}
	
	public static Map<String, String> parse(String srcExpression) {
		Map<String, String> resMap = new HashMap<>();
		String expressStr = "";

		expressStr = preDealNormalSign(srcExpression);

		resMap.put("expression", expressStr);
		resMap.put("parameters", getParameters("************"));
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
	 * 9、将数字与非数字字符（字母或#）紧邻的情况中间加入 * ，举例：2a, 2alpha 转换为 2*a, 2*alpha
	 */
	private static String preDealNormalSign(String expressStr) {
		String expStr = expressStr;

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
		expStr = expStr.replaceAll("+", " + ");
		expStr = expStr.replaceAll("-", " - ");

		// 处理9
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
		if (index < expStr.length()) {
			sb.append(expStr.substring(index));
		}
		expStr = sb.toString();

		return expStr;
	}

	// 处理幂，开根号与开高次方
	// \sqrt{ab}    \sqrt[23]{df}    sdf^3
	private static String dealPower(String expressStr) {
		String expStr = expressStr;
		
		return expStr;
	}
	
	// 处理log和ln
	// \log\left(sd\right)     \log_{22}\left(222\right)     \ln\left(sff\right)
	private static String dealLog(String expressStr) {
		String expStr = expressStr;
		
		return expStr;
	}
	
	// 处理分数形式
	// \frac{D_t}{d_r}
	private static String dealFrac(String expressStr) {
		String expStr = expressStr;
		
		return expStr;
	}
	
	// 从公式中获取变量
	private static String getParameters(String expressStr) {

		return "";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
