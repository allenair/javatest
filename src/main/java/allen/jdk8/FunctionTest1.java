package allen.jdk8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionTest1 {

	public static void main(String[] args) {
		Predicate<Integer> isRight = x -> x > 5;
		System.out.println(isRight.test(12));
		testPredict(6, x -> x > 5);
		
		System.out.println("=================================================");
		BinaryOperator<Integer> mulFun = (x, y) -> x * y;
		System.out.println(mulFun.apply(12, 9));
		System.out.println(testBinOper("abc", "123", (x, y) -> {
			return x + ";;;" + y;
		}));

		System.out.println("=================================================");
		testFilter();
		
		System.out.println("=================================================");
		Stream.of("asd","xcv","345","dfg").map(x -> x.toUpperCase()).forEach(System.out::println);
		
		System.out.println("=================================================");
		testFlatmap();
		
		System.out.println("=================================================");
		testMaxMin();
		
		System.out.println("=================================================");
		testSpplier(()->"Allen ZZZZXXXXXCCCCC");
		testIntSpplier(()->123);
		
		System.out.println("=================================================");
		List<String> slist = Arrays.asList("asd","a","567","dfghjk","hj","4567");
		System.out.println(slist.stream().collect(Collectors.maxBy(Comparator.<String, Integer>comparing(s->s.length()))).get());
		
		Map<Boolean, List<String>> resMap = slist.stream().collect(Collectors.partitioningBy(s->isNumber(s)));
		System.out.println("=================================================");
		
		Set<String> set = slist.stream().map(String::toUpperCase).collect(Collectors.toCollection(TreeSet::new));
		String joined = slist.stream().map(String::toUpperCase).collect(Collectors.joining(","));
		int total = slist.stream().collect(Collectors.summingInt(String::length));
		System.out.println("================================================="+total);
		
		Map<String, List<String>> byDept = slist.stream().collect(Collectors.groupingBy(String::toUpperCase));
		Map<Integer, List<String>> byDept2 = slist.stream().collect(Collectors.groupingBy(String::length));
		Map<String, Integer> totalByDept = slist.stream().collect(Collectors.groupingBy(String::toUpperCase, Collectors.summingInt(String::length)));
		System.out.println("=================================================");
		
		
		System.out.println(fiboFun(8));
		cache.forEach((key,value)->System.out.println(key+">>>"+value));
	}

	private static Map<Integer, Integer> cache = new HashMap<>();
	public static int fiboFun(int n) {
		if(n<3) {
			return 1;
		}
		return cache.computeIfAbsent(n-1, FunctionTest1::fiboFun) + cache.computeIfAbsent(n-2, FunctionTest1::fiboFun);
	}
	
	public static void testSpplier(Supplier<String> sfun) {
		System.out.println(sfun.get().toUpperCase());
	}
	
	public static void testIntSpplier(IntSupplier ifun) {
		System.out.println(ifun.getAsInt()*10);
	}
	
	public static <T> void testPredict(T num, Predicate<T> pFun) {
		System.out.println(pFun.test(num));
	}

	public static <T> T testBinOper(T one, T two, BinaryOperator<T> bfun) {
		return bfun.apply(one, two);
	}

	public static void testFilter() {
		Integer[] arr = { 12, 22, 1, 23, 14, 16, 17, 39, 24, 34, 36 };
		
//		int sum = Stream.<Integer>of(arr).reduce((x, y) -> x + y).get();
//		int sum = Stream.<Integer>of(arr).reduce(0, (x, y) -> x + y);
		int sum = Stream.<Integer>of(arr).mapToInt(x->x).sum();
		
		System.out.println(sum);
		
		double mean = sum * 1.0 / arr.length;
		System.out.println(mean);

		Stream.of(arr).filter(x -> x > mean).forEach(System.out::println);

		List<Integer> arrList = Arrays.asList(arr);
		List<Double> resList = arrList.stream().map(x -> x * 1.1).filter(x -> x > mean).collect(Collectors.toList());
		resList.stream().forEach(System.out::println);
		System.out.println(">>>"+resList.stream().count());

		Stream.of("asd","123","34.5","45.5.6").filter(x -> isNumber(x)).forEach(System.out::println);
		
		System.out.println(">>>"+arrList.stream().mapToDouble(x->x*1.0).average().getAsDouble());
		
		arrList.stream().mapToInt(x->x).sorted().forEach(System.out::println);
	}
	
	public static boolean isNumber(String src) {
		return Pattern.matches("[0-9]+[.]?[0-9]*", src);
	}
	
	public static void testFlatmap() {
		List<Integer> together = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4)).flatMap(nums->nums.stream()).collect(Collectors.toList());
		together.forEach(System.out::println);
	}
	
	public static void testMaxMin() {
		List<InnerBean> innList = Arrays.asList(new InnerBean("AAA",1234), new InnerBean("BBBB", 456), new InnerBean("CCC", 341));
		
		System.out.println(innList.stream().min(Comparator.comparing((InnerBean bean)->bean.getValue())).get().getName());
		
		System.out.println(innList.stream().max((x, y) -> {
			if (x.getValue() > y.getValue()) {
				return 1;
			} else if (x.getValue() < y.getValue()) {
				return -1;
			} else {
				return 0;
			}
		}).get().getName());
	}
	
	private static class InnerBean{
		String name;
		int value;
		public InnerBean(String name, int value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public int getValue() {
			return value;
		}
	}
}
