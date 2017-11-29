package allen.iotplatform.newrule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.gson.Gson;

public class RuleTest {

	public static void main(String[] args) throws Exception {
//		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
//		script.put("a", "E02");
//		
//		System.out.println(script.eval(""));
//		
//		String res="q";
//		script.put("res",res);
//		script.put("cal", 12);
//		script.put("cal1", 12);
//		script.put("ca5l2", 12);
//		script.put("ca4l3", 122);
//		script.eval("res=cal+'AAA'");
//		
//		System.out.println(script.eval("cal+1"));
//		System.out.println(script.eval("ca4l3+1"));
//		script.put("__ROOMMAINTAIN", "1");
//		System.out.println(script.eval("cal+1"));
//		
//		boolean con1 = Boolean.parseBoolean(script.eval("__ROOMMAINTAIN == '1'").toString());
//		boolean con2 = Boolean.parseBoolean(script.eval("(ca5l2+1)>12").toString());
//		script.put("con1", con1);
//		script.put("con2", con2);
//		System.out.println(script.eval("con1 && con2"));
//		
//		script.put("ca222", 13);
//		System.out.println(Float.valueOf(script.eval("ca222/3").toString()).intValue());
//		
//		System.out.println(Float.valueOf(script.eval("9999").toString()).intValue());
		
//		calArr();
		
		calHitachi();
	}

	private static void calArr() {
		String str = IotAlarmEngine.init("/Users/allen/git/javatest/src/main/java/allen/iotplatform/newrule/newrulewitharray.json");
		IotAlarmEngine.load(str, "arrayTest");
		
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("__SPEED", "1.2");
		inputMap.put("__WEIGHT", "800.0");
		inputMap.put("__FLOOR", "130");
		inputMap.put("__SCREEN_FLAG", "1");
		inputMap.put("__CAMERA_FLAG", "1");
		
		List<Map<String, String>> egResList = IotAlarmEngine.dealIotAlarm("arrayTest", "Allen_ARR_cache", inputMap);

		for (Map<String, String> row : egResList) {
			System.out.println(new Gson().toJson(row));
		}
	}
	
	private static void calHitachi() {
		String str = IotAlarmEngine.init("/Users/allen/git/javatest/src/main/java/allen/iotplatform/newrule/hitachi_plan.json");
		IotAlarmEngine.load(str, "hitachi");
		
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("__PERIOD", "12");
		
		List<Map<String, String>> egResList = IotAlarmEngine.dealIotAlarm("hitachi", "hitachi_ARR_cache", inputMap);

		for (Map<String, String> row : egResList) {
			System.out.println(new Gson().toJson(row));
		}
	}
}
