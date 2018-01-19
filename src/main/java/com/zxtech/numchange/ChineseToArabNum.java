package com.zxtech.numchange;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ChineseToArabNum {
	private static String[] POST_NAME = new String[] { "亿", "万", "千", "百", "十" };
	private static Map<String, Integer> NUMBER_Map = new HashMap<>();
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
	}

	public static void main(String[] args) {
		System.out.println(cal("一百九十五万三千五百二十三"));
	}

	public static long cal(String str) {
		String cstr = normalizeStr(str);
		return chinese2ArabNum(cstr, 0, 0);
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

		if (cstr.length() == 2 && +cstr.charAt(0) == '零') {
			return NUMBER_Map.get(cstr.substring(1)) * (long) (Math.pow(10, postNum));
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
