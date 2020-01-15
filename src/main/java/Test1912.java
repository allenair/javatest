import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class Test1912 {

	public static void main(String[] args) {
//		f1231();
//		System.out.println(diffDescription("13","11.025","0"));
		
		System.out.println((int)'A');
		System.out.println((int)'a');
		System.out.println(StringUtils.capitalize("1adfg"));
	}
	
	
	private static void f1231() {
		LocalDate today = LocalDate.now().plusDays(1);
		File dirs = new File("c:/temp");
		if (dirs.exists()) {
			Stream.<File>of(dirs.listFiles()).filter(f -> {
				String fileName = f.getName().split("_")[0];
				return f.isFile() && LocalDate.parse(fileName).isBefore(today);
			}).forEach(f -> {
				f.delete();
			});
		}
	}
	private static void f1230() {
		LocalDate today = LocalDate.now();
		today = today.plusDays(5);
		String dayStr = today.format(DateTimeFormatter.ofPattern("yyyy/M/d"));
		
		String timeStr = "08:12:56";
		timeStr = timeStr.substring(0, timeStr.lastIndexOf(":"));
		
		String fullStr = dayStr + " " + timeStr;
		
		System.out.println(fullStr);
	}
	private static void f1227() {
//		String expressStr = "phase_num+cold_type+circle_num+wire_material+adjust_type+\"-\"+ele_vol+\"/\"+volt_level";
//		Map<String, String> inputMap = new HashMap<>();
//		inputMap.put("phase_num", "S");
//		inputMap.put("cold_type", "A");
//		inputMap.put("circle_num", "100");
//		inputMap.put("wire_material", "");
//		inputMap.put("adjust_type", "B");
//		inputMap.put("ele_vol", "50000");
//		inputMap.put("volt_level", "110");
		
		String expressStr = "\"d087fe59-e888-4388-94e1-6f5f47876918\"";
		Map<String, String> inputMap = new HashMap<>();
		
		try {
			System.out.println(calExpressValueWithJs(expressStr, inputMap));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String calExpressValueWithJs(String expressStr, Map<String, String> inputMap) throws ScriptException {
		ScriptEngine script = new ScriptEngineManager().getEngineByName("js");
		// 将input参数放入本次脚本执行环境
		for (String key : inputMap.keySet()) {
			script.put(key, inputMap.get(key));
		}
		
		return script.eval(expressStr).toString();
	}
	
	private static void f1214() {
		List<Map<String, Object>> tempList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("name", "allen");
		map.put("age", 23);
		tempList.add(map);
		
		map = new HashMap<>();
		map.put("name", "bella");
		map.put("age", 12);
		tempList.add(map);
		
		map = new HashMap<>();
		map.put("name", "aileen");
		map.put("age", 23);
		tempList.add(map);
		
//		tempList = new ArrayList<>();
		
//		String aa = tempList.stream().map(row->row.get("name").toString()).reduce("", (a,b)->a+","+b);
		String aa = tempList.stream().map(row->row.get("name").toString()).reduce((a,b)->a+","+b).orElse("ss");
		System.out.println(aa);
		
//		int bb = tempList.stream().map(row->Integer.parseInt(row.get("age").toString())).reduce(0, (a,b)->a+b);
		int bb = tempList.stream().map(row->Integer.parseInt(row.get("age").toString())).reduce((a,b)->a+b).orElse(0);
		System.out.println(bb);
	}
	private static void f1211() {
		String insertSql = "INSERT INTO base_dictionary(id, dict_type, dict_type_name, dict_name, dict_code, dict_sort)\r\n" + 
				"VALUES('#1#', 'product_type_four', '产品型号第4部分', 'code', '#2#', #3#);";
		
		int[] vals = {30,50,63,80,100,125,160,200,250,315,400,500,630,800,1000,1250,1600,2000,1000,1250,1600,2000,2500,3150,4000,5000,6300,8000,10000,12500,16000,20000,20000,25000,31500,40000,50000,630000};
		
		Set<Integer> valSet = new HashSet<>();
		
		IntStream.of(vals).forEach(valSet::add);
		
		List<Integer> valList = valSet.stream().sorted().collect(Collectors.toList());
		int index=1;
		for (Integer num : valList) {
			String tmp = insertSql.replaceAll("#1#", UUID.randomUUID().toString());
			tmp = tmp.replaceAll("#2#", num.toString());
			tmp = tmp.replaceAll("#3#", index+"");
			index++;
			
			System.out.println(tmp);
		}
		
	}
	private static String diffDescription(String realData, String upper, String down) {
		BigDecimal realVal = new BigDecimal(realData);
		BigDecimal upperVal = new BigDecimal(upper);
		BigDecimal downVal = new BigDecimal(down);

		if (downVal.equals(BigDecimal.ZERO)) {
			if (realVal.compareTo(upperVal) > 0) {
				BigDecimal diff = realVal.subtract(upperVal).divide(upperVal, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
				return "实测值高于标准值" + diff.setScale(2).toString() + "%";
			}
		} else {
			if (realVal.compareTo(upperVal) > 0) {
				BigDecimal diff = realVal.subtract(upperVal).divide(upperVal, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
				return "实测值高于标准值上限" + diff.setScale(2).toString() + "%";
			}
			if (realVal.compareTo(downVal) < 0) {
				BigDecimal diff = downVal.subtract(realVal).divide(downVal, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
				return "实测值低于标准值下限" + diff.setScale(2).toString() + "%";
			}
		}

		return "";
	}
	
	public static void f1210() {
		
		System.out.println(Pattern.matches("\\d+", "5"));
		System.out.println(Pattern.matches("\\d+", "6"));
		System.out.println(Pattern.matches("\\d+", "5.1"));
		System.out.println(Pattern.matches("\\d+", "10"));
	}
	public static void f1205() {
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		System.out.println(LocalDateTime.now());
		System.out.println(timeFormat.format(LocalDateTime.now()));
		
		String productType = "SFSZ11-50000/110";
		String[] tmpArr = productType.split("/");
		System.out.println("product_type_4 " + tmpArr[1]);
		
		tmpArr = tmpArr[0].split("-");
		System.out.println("product_type_3 " + tmpArr[1]);
		
		int index=0;
		for(int i=0;i<tmpArr[0].length();i++) {
			char c = tmpArr[0].charAt(i);
			if(c>='0' && c<='9') {
				index = i;
				break;
			}
		}
		System.out.println("product_type_1 " + tmpArr[0].substring(0,index));
		System.out.println("product_type_2 " + tmpArr[0].substring(index));
		
	}
	
	public static void f1204() {
		String res = "{\"aaa\":{\"model_line\":\"1\",\"model_point\":\"1\"},\"bbb\":\"ddddfasdfgh\",\"cccc\":[1,2,3,4,5],\"ddd\":[{\"kkk\":44567},{\"ppp\":\"poi\"}]}";
		Map<String, Object> resMap = new Gson().fromJson(res, new TypeToken<Map<String, Object>>() {
		}.getType());
		
		Map<String, String> map = new HashMap<>();
		resMap.entrySet().stream().filter(entity->entity.getValue()!=null).forEach(entity->map.put(entity.getKey(), entity.getValue().toString()));

		System.out.println(map);
	}
	
	public static void f1204_1() {
//		LocalDate today = LocalDate.now();
		LocalDate today = LocalDate.parse("2019-12-01");
		
		
		LocalDate begin = today.minusDays(today.getDayOfWeek().getValue()-1);
		LocalDate end = today.plusDays(7-today.getDayOfWeek().getValue());
		
		
		
		System.out.println(today);
		System.out.println(today.getDayOfWeek().getValue());
		
		System.out.println(begin);
		System.out.println(begin.getDayOfWeek().getValue());
		
		System.out.println(end);
		System.out.println(end.getDayOfWeek().getValue());
		
		System.out.println(today.plusMonths(1).withDayOfMonth(1).minusDays(1));
	}
	
	public static String[] f1204_2(String flag) {
		String[] res = {"",""};
		LocalDate today = LocalDate.now();
		
		if(flag.equals("week")) {
			res[0] = today.minusDays(today.getDayOfWeek().getValue()-1).toString() + " 00:00:00";
			res[1] = today.plusDays(7-today.getDayOfWeek().getValue()).toString() + " 23:59:59";
			
		}else if(flag.equals("month")) {
			res[0] = today.withDayOfMonth(1).toString() + " 00:00:00";
			res[1] = today.plusMonths(1).withDayOfMonth(1).minusDays(1).toString() + " 23:59:59";
			
		}else {
			res[0] = today.toString() + " 00:00:00";
			res[1] = today.toString() + " 23:59:59";
		}
		
		return res;
	}
}
