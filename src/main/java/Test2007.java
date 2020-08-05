import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test2007 {

	public static void main(String[] args) throws Exception {
		test0724();
	}
	
	private static void test0724() {
		List<String> aaList = new ArrayList<String>();
		aaList.add("AAA");
		aaList.add("bbb");
		aaList.add("vcc");
		
		System.out.println(String.join(",", aaList));
		
		String[] roleArr = "asd, 123, 56 ".split(",");
		List<String> roleList = Arrays.asList(roleArr).stream().map(String::trim).collect(Collectors.toList());
		
		roleList.forEach(System.out::println);
		
		System.out.println(Math.round(23.54));
	}
	
	public static void genTableId() {
		IntStream.range(0, 200).forEach(i->{
			System.out.println(UUID.randomUUID().toString());
		});
		
	}
	
	private static void test0723() {
		int digit = 9;
		
		String serialStr = String.format("%0"+digit+"d", 123456);
		serialStr = serialStr.substring(serialStr.length()-digit);
		System.out.println(serialStr);
	}
	private static void test0711() {
		long start = System.currentTimeMillis();
		String expressStr = "parseInt(aa)<2";
		IntStream.range(0, 100).forEach(i->{
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put("aa", "13");
			try {
//				calExpressValueWithJs(expressStr, inputMap);
				System.out.println(calExpressValueWithJs(expressStr, inputMap));
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		});
		
		System.out.println("====="+(System.currentTimeMillis()-start));
	}
	
	private static String calExpressValueWithJs(String expressStr, Map<String, String> inputMap) throws ScriptException {
		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
		// 将input参数放入本次脚本执行环境
		for (String key : inputMap.keySet()) {
			script.put(key, inputMap.get(key));
		}

		return script.eval(expressStr).toString();
	}
}
