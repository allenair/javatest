package allen.iotplatform.newrule;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RuleTest {

	public static void main(String[] args) throws Exception {
		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
		script.put("a", "E02");
		
		System.out.println(script.eval(" a=='E02'"));
		
		String res="q";
		script.put("res",res);
		script.put("cal", 12);
		script.put("cal1", 12);
		script.put("ca5l2", 12);
		script.put("ca4l3", 122);
		script.eval("res=cal+'AAA'");
		
		System.out.println(script.eval("cal+1"));
		System.out.println(script.eval("ca4l3+1"));
		script.put("__ROOMMAINTAIN", "1");
		System.out.println(script.eval("cal+1"));
		
		boolean con1 = Boolean.parseBoolean(script.eval("__ROOMMAINTAIN == '1'").toString());
		boolean con2 = Boolean.parseBoolean(script.eval("(ca5l2+1)>12").toString());
		script.put("con1", con1);
		script.put("con2", con2);
		System.out.println(script.eval("con1 && con2"));
		
		script.put("ca222", 13);
		System.out.println(Float.valueOf(script.eval("ca222/3").toString()).intValue());
		
		System.out.println(Float.valueOf(script.eval("9999").toString()).intValue());
//		cal();
	}

}
