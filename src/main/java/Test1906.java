import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.LongestCommonSubsequence;

import com.google.common.base.Strings;
import com.google.gson.Gson;

public class Test1906 {

	public static void main(String[] args) {
//		new Test1906().test0617();
		Test tt = new Test();
	}

	public void test0628() {
		String[][] tableDataArray = { {"01tyu","02bnm", "03tyu"}, {"11qwe","12ghj","13ghjk"}, {"21ghj","22ui","23fgh"}, {"31yui","32ghn","33yui"} };
		int firstRow = 1;
		String[][] flatArray = new String[firstRow + 2][(tableDataArray.length - firstRow) * tableDataArray[0].length];
		for (int i = firstRow; i < tableDataArray.length; i++) {
			for (int k = 0; k < tableDataArray[i].length; k++) {
				flatArray[firstRow + 1][(i - firstRow) * tableDataArray[0].length + k] = tableDataArray[i][k];
			}
		}
		tableDataArray = flatArray;
		
		
	}
	public void test0627() {
		System.out.println(Pattern.matches("\\[.*\\]", "[-oo,12]"));
		System.out.println(Pattern.matches("\\[.*\\]", "[-12,12]"));
		System.out.println(Pattern.matches("\\[.*\\]", "[-oo,oo]"));
		System.out.println(Pattern.matches("\\[.*\\]", "[-23,12]"));
	}
	
	public void test0619() {
		String[] strArr = new String[]{"其余层门套","额定载重","层站数站数","开门尺寸","轿内报站钟","额定速度","开门尺寸开门高度","厅外指示器显示器","层站数","其余层门套类别","层门材质","层站数层数","并联控制","厅外指示器","精确再平层","轿门","操纵箱按钮排布","机房类型","轿门材质","开门尺寸开门宽度","控制系统","操纵箱","井道尺寸井道深度","产品类型","电梯数量","轿底材质","首层门套","轿底","井道尺寸井道宽度","其余层门套材质","层门","首层门套类别","停电自动平层","提升高度","轿壁材质","操纵箱操纵箱按钮","首层门套材质","开门方式","本层开门","轿壁","首层门套颜色","厅外指示器按钮","井道尺寸"}; 
	
		stringArraySimilar("轿厢类型",strArr);
		
		double[] darr = new double[] {100,200,3000};
		
		aa19test(darr);
		
		String a="11";
		String b = null;
		
		System.out.println(joinStr("",a,b));
		
	}
	
	public static String joinStr(String delimiter, String... str) {
		return Stream.<String>of(str).map(s-> s==null?"":s).reduce((x, y)->String.join(delimiter, x, y)).get();
	}
	
	private void aa19test(double... aa) {
		
		Double[] bb = Arrays.stream(aa).map(Double::new).boxed().toArray(Double[]::new);
//		List<Double> al = Arrays.asList(aa);
//		Stream.of(aa).flatMap(x->System.out.println(x));
	}
	
	public void test0618() {
		String[] arr = "".split(";");
//		System.out.println(arr.length);
//		Stream.<String>of(arr).forEach(System.out::println);
//		System.out.println(String.format("%.4f", 1234/100000.0));
		
		String[] aa = findNumStr("3456*768");
		Stream.of(aa).forEach(System.out::println);
		
		System.out.println(Pattern.matches("\\d+\\D*\\*\\d+\\D*", "4567kg*098765m"));
		Stream.of("4567kg*098765m".split("\\*")).forEach(System.out::println);
		
		
		
		List<Map<String, String>> resultCache = new ArrayList<>();
		
		Map<String, String> row = new HashMap<>();
		resultCache.add(row);
		row.put("井道尺寸","井道宽度:2250;井道深度:2100");
		row.put("产品类型","住宅电梯");
		row.put("停电自动平层","Y");
		row.put("其余层门套","材质:发纹不锈钢;类别:小门套");
		row.put("厅外指示器","按钮:触点式按钮;显示器:液晶显示器");
		row.put("层站数","层数:33;站数:33");
		row.put("层门","材质:发纹不锈钢");
		row.put("并联控制","Y");
		row.put("开门尺寸","开门宽度:900;开门高度:2100");
		row.put("开门方式","中分自动");
		row.put("控制系统","32位微机VVVF系统");
		row.put("提升高度","102.3");
		row.put("操纵箱","操纵箱按钮:触点式按钮;按钮排布:两侧");
		row.put("机房类型","小机房");
		row.put("电梯数量","5");
		row.put("轿内报站钟","Y");
		row.put("本层开门","Y");
		row.put("精确再平层","Y");
		row.put("停电自动平层","Y");
		row.put("轿壁","材质:发纹不锈钢");
		row.put("轿底","材质:PVC");
		row.put("轿门","材质:发纹不锈钢");
		row.put("额定载重","800");
		row.put("额定速度","1.75");
		row.put("首层门套","材质:喷涂钢板;类别:小门套;颜色:象牙白");
		
		List<Map<String, String>> newShapeCacheData = reshapeCacheData(resultCache);
		System.out.println("newShapeCacheData === " + new Gson().toJson(newShapeCacheData));
		
		
		Map<String, List<String>> upDataMap = new HashMap<>();
//		upDataMap.put("Ele_Type", getList("住宅电梯使用"));
//		upDataMap.put("Ele_Type", getList("乘客电梯3"));
		upDataMap.put("Ele_Type", getList("测试使用的值应该匹配不上"));
		
		upDataMap.put("开门尺寸", getList("900*2000mm","950*2200mm"));
		upDataMap.put("载重", getList("700kg","810kg","900kg"));
		upDataMap.put("层门材质", getList("喷涂钢板","木板","发纹不锈钢"));
		upDataMap.put("自动平层", getList("YES","NO"));
		upDataMap.put("额定速度", getList("1.5ms","1.8ms"));
		upDataMap.put("消防员专用", getList("YES","NO"));
		System.out.println(new Gson().toJson(upDataMap));
	}
	private List<String> getList(String... val) {
		List<String> resList = new ArrayList<>();
		for (String string : val) {
			resList.add(string);
		}
		return resList;
	}
	
	private List<Map<String, String>> reshapeCacheData(List<Map<String, String>> resultCache) {
		List<Map<String, String>> resList = new ArrayList<>();
		
		for (Map<String, String> rowMap : resultCache) {
			Map<String, String> newRow = new HashMap<>();
			resList.add(newRow);
			
			for (String key : rowMap.keySet()) {
				String value = rowMap.get(key);
				newRow.put(key, value);

				String[] propertyArr = value.split(";");
				if (propertyArr.length > 0) {
					for (String pStr : propertyArr) {
						String[] nameValueArr = pStr.split(":");
						if (nameValueArr.length > 1) {
							newRow.put(key + nameValueArr[0], nameValueArr[1]);
						}
					}
				}
			}
		}

		return resList;
	}
	
	
	
	private String[] findNumStr(String val) {
		String[] resArr = new String[]{"",""};
		Pattern patternReal = Pattern.compile("[0-9]+[.]?[0-9]*");
		Matcher matcherReal = patternReal.matcher(val);
		if (matcherReal.find()) {
			resArr[0] = matcherReal.group();
			matcherReal = patternReal.matcher(matcherReal.replaceFirst(" "));
			if (matcherReal.find()) {
				resArr[1] = matcherReal.group();
			}
		}
		return resArr;
	}
	
	public void test0617() {
//		System.out.println(numberSimilar(1000, 1200));
		Stream.<int[]>of(numberArraySimilar(1000, new double[] { 800, 900, 1120, 1300 }))
				.forEach(a -> System.out.println(a[0] + " == " + a[1]));
		
		System.out.println(Strings.repeat("=", 100));
//		strSimilar();
//		System.out.println(stringSimilar("首层门套", "其余层"));
		Stream.<int[]>of(stringArraySimilar("首层门套", new String[]{"werty层层门", "门套", "其余层"})).forEach(a -> System.out.println(a[0] + " == " + a[1]));
	
		System.out.println(isNumber("123.0"));
		System.out.println(extractNumber("123.0kg"));
	}

	private static void strSimilar() {
		String one = "首层门套";
		String two = "层门";

//		System.out.println(new JaccardSimilarity().apply(one, two));
//		System.out.println(new LongestCommonSubsequence().apply(one, two));
		System.out.println(new LevenshteinDistance().apply(one, two));
//		System.out.println(new CosineDistance().apply(one, two));
//		System.out.println(new HammingDistance().apply(one, two));
	}

	public static boolean isNumber(String src) {
		return Pattern.matches("[0-9]+[.]?[0-9]*", src);
	}
	public static String extractNumber(String src) {
		Pattern pattern = Pattern.compile("[0-9]+[.]?[0-9]*");
		Matcher matcher = pattern.matcher(src);
		if (matcher.find()) {
			return matcher.group();
		} 
		return "";
	}
	
	// ***************************************************
	public static int numberSimilar(double one, double two) {
		return (int) Math.ceil(10000 * (1 - Math.abs(one - two) / (Math.abs(one) + Math.abs(two))));
	}
	
	public static int[][] numberArraySimilar(double stdNum, double... numArr) {
		return arraySimilar((x, y) -> numberSimilar(x, y), new Double(stdNum), Arrays.stream(numArr).boxed().toArray(Double[]::new));
	}

	public static int stringSimilar(String one, String two) {
		return (int) Math.ceil(10000 * (1 - new LevenshteinDistance().apply(one, two) * 1.0 / Math.max(one.length(), two.length())));
	}
	
	public static int[][] stringArraySimilar(String stdNum, String... numArr) {
		return arraySimilar((x, y) -> stringSimilar(x, y), stdNum, numArr);
	}
	
	@SafeVarargs
	private static <T> int[][] arraySimilar(BiFunction<T, T, Integer> fun, T stdNum, T... numArr) {
		int[][] resArr = new int[numArr.length][2];
		for (int i = 0; i < numArr.length; i++) {
			resArr[i][0] = i;
			resArr[i][1] = fun.apply(stdNum, numArr[i]);
		}

		Comparator<int[]> cp = (arr1, arr2) -> arr1[1] < arr2[1] ? -1 : arr1[1] == arr2[1] ? 0 : 1;
		Arrays.sort(resArr, cp.reversed());
		
//		resArr[0] = Arrays.stream(resArr).max(cp).get();
		
		return resArr;
	}
}
