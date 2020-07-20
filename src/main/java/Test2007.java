import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test2007 {

	public static void main(String[] args) {
		test0711();
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
