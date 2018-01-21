package com.zxtech.numchange;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ChineseToArabNum {
	private static String[] ALL_KEYWORD = new String[] { "亿", "万", "千", "百", "十", "零", "0", "九", "9", "八", "8", "七",
			"7", "六", "6", "五", "5", "四", "4", "三", "3", "二", "2", "一", "1" };
	private static String[] POST_NAME = new String[] { "亿", "万", "千", "百", "十" };
	private static Map<String, Integer> NUMBER_Map = new HashMap<>();
	private static Map<String, String> CHN_NUMBER_Map = new HashMap<>();
	private static Map<String, String> STR_MAP = new HashMap<>();
	private static Map<String, Integer> POST_MAP = new HashMap<>();
	static {
		STR_MAP.put("億", "亿");
		STR_MAP.put("萬", "万");
		STR_MAP.put("仟", "千");
		STR_MAP.put("佰", "百");
		STR_MAP.put("拾", "十");
		STR_MAP.put("两", "二");
		STR_MAP.put("俩", "二");

		POST_MAP.put("亿", 8);
		POST_MAP.put("万", 4);
		POST_MAP.put("千", 3);
		POST_MAP.put("百", 2);
		POST_MAP.put("十", 1);

		NUMBER_Map.put("零", 0);
		NUMBER_Map.put("九", 9);
		NUMBER_Map.put("八", 8);
		NUMBER_Map.put("七", 7);
		NUMBER_Map.put("六", 6);
		NUMBER_Map.put("五", 5);
		NUMBER_Map.put("四", 4);
		NUMBER_Map.put("三", 3);
		NUMBER_Map.put("二", 2);
		NUMBER_Map.put("一", 1);
		NUMBER_Map.put("0", 0);
		NUMBER_Map.put("9", 9);
		NUMBER_Map.put("8", 8);
		NUMBER_Map.put("7", 7);
		NUMBER_Map.put("6", 6);
		NUMBER_Map.put("5", 5);
		NUMBER_Map.put("4", 4);
		NUMBER_Map.put("3", 3);
		NUMBER_Map.put("2", 2);
		NUMBER_Map.put("1", 1);

		CHN_NUMBER_Map.put("0", "零");
		CHN_NUMBER_Map.put("9", "九");
		CHN_NUMBER_Map.put("8", "八");
		CHN_NUMBER_Map.put("7", "七");
		CHN_NUMBER_Map.put("6", "六");
		CHN_NUMBER_Map.put("5", "五");
		CHN_NUMBER_Map.put("4", "四");
		CHN_NUMBER_Map.put("3", "三");
		CHN_NUMBER_Map.put("2", "二");
		CHN_NUMBER_Map.put("1", "一");
	}

	public static void main(String[] args) {
		// System.out.println(cal("一百九十五万三千五百二十三"));
		System.out.println(getNumStr("gjhjk一百95万三千五百二十三dadas"));
	}

	public static long cal(String str) {
		String cstr = normalizeStr(str);
		return chinese2ArabNum(cstr, 0, 0);
	}

	public static long getNumStr(String srcStr) {
		StringBuilder sb = new StringBuilder();
		String tmp;
		for (int i = 0; i < srcStr.length(); i++) {
			tmp = "" + srcStr.charAt(i);
			if (isInArray(tmp, ALL_KEYWORD)) {
				sb.append(tmp);
			} else {
				if (sb.toString().length() > 0) {
					break;
				}
			}
		}
		tmp = sb.toString();
		for (String post : POST_NAME) {
			if (tmp.contains(post)) {
				break;
			}
		}

		return cal(tmp);
	}

	private static boolean isInArray(String src, String[] all_keyword) {
		for (int i = 0; i < src.length(); i++) {
			String tmp = "" + src.charAt(i);
			for (String keyWord : all_keyword) {
				if (keyWord.equals(tmp)) {
					return true;
				}
			}
		}

		return false;
	}

	private static String normalizeStr(String str) {
		StringBuilder sb = new StringBuilder();
		String tmp;
		for (int i = 0; i < str.length(); i++) {
			tmp = "" + str.charAt(i);
			for (String key : STR_MAP.keySet()) {
				if (key.equals(tmp)) {
					tmp = STR_MAP.get(key);
					break;
				}
			}
			sb.append(tmp);
		}
		return sb.toString();
	}

	private static long chinese2ArabNum(String cstr, int postNum, long sum) {
		if (StringUtils.isEmpty(cstr)) {
			return 0;
		}

		if (cstr.length() == 1) {
			return NUMBER_Map.get(cstr) * (long) (Math.pow(10, postNum));
		}

		if (cstr.length() == 2 && cstr.charAt(0) == '零') {
			return NUMBER_Map.get(cstr.substring(1)) * (long) (Math.pow(10, postNum));
		}

		if (!isInArray(cstr, POST_NAME)) {
			try {
				return Long.parseLong(cstr)* (long) (Math.pow(10, postNum));
			} catch (Exception e) {
				return 0;
			}
		}

		for (String post : POST_NAME) {
			if (cstr.contains(post)) {
				String[] arrs = cstr.split(post);
				String left, right;
				left = arrs[0];
				if (arrs.length < 2 || StringUtils.isEmpty(arrs[1])) {
					right = "零";
				} else {
					right = arrs[1];
				}
				sum += chinese2ArabNum(left, POST_MAP.get(post) + postNum, sum) + chinese2ArabNum(right, postNum, sum);
				break;
			}
		}
		return sum;
	}
}
