package allen.jdk8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class TestOne {

	public static void main(String[] args) throws Exception {
		// test180222();
		// printSubPath("/Users/allen/Desktop");
		// fileOper();
		// wordCount();
		// calljs3();
		// testParallelJs();
//		othersFun();
		
		stringtest();
	}
	
	public static void stringtest() {
		String ss = "entries. Httpheaders allow more than one value for a specific key";
//		Stream.of(ss.toCharArray()
//		Stream.<String>of(ss.split("")).filter(s->!s.trim().isEmpty());
		
		IntStream.range(1, 100).parallel().forEach(System.out::println);
	}

	public static void othersFun() {
		System.out.println(String.join(",", "abc", "sdf", "1", "33"));
		System.out.printf("Integer SIZE: %d BYTES: %d\n", Integer.SIZE, Integer.BYTES);
		System.out.printf("Character SIZE: %d BYTES: %d\n", Character.SIZE, Character.BYTES);
		Random generator = new Random();

		System.out.println(generator.ints().limit(100).reduce(Integer::sum));

		System.out.println(generator.ints().limit(100).mapToObj(i -> i % 2 == 0).reduce(Boolean::logicalXor));
		System.out.println(Math.floorMod(6 + 10, 12)); // Ten hours later
		System.out.println(Math.floorMod(6 - 10, 12)); // Ten hours earlier

		List<String> ids = new ArrayList<>(ZoneId.getAvailableZoneIds());
		ids.removeIf(s -> !s.startsWith("America"));
		ids.forEach(System.out::println);
		ids.replaceAll(s -> s.replace("America/", ""));
		System.out.println("---");
		ids.forEach(System.out::println);
		BitSet bits = new BitSet();
		ids.forEach(s -> bits.set(s.length()));
		System.out.println(Arrays.toString(bits.stream().toArray()));
		
		String firstStr="ss";
		String secondStr="ss";
		int third=1;
		System.out.println(Objects.equals(firstStr, third));
	}

	public static void repeat(int n, IntConsumer action) {
		for (int i = 0; i < n; i++)
			action.accept(i);
	}

	public static void repeat(int n, Runnable action) {
		for (int i = 0; i < n; i++)
			action.run();
	}

	public static void test180222() {
		Stream.of("a", "b", "c").map(s -> s + s).forEach(System.out::println);

		Stream<Integer> values = Stream.empty();
		values = Stream.of(1, 2, 3, 4, 5);
		Optional<Integer> sum = values.reduce((x, y) -> x + y); // Or values.reduce(Integer::sum);
		System.out.println("sum: " + sum.orElse(0));

		Integer[] numbers3 = Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);
		System.out.println(numbers3); // Note it's an Integer[] array

		repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
		repeat(10, () -> System.out.println("Hello, World!"));

		LocalDate stDate = LocalDate.of(1980, 3, 19);
		LocalDate nowDate = LocalDate.now();
		System.out.println(stDate.until(nowDate, ChronoUnit.DAYS));

		// LocalDate today = LocalDate.of(2013, 11, 9); // Saturday
		// TemporalAdjuster NEXT_WORKDAY2 = TemporalAdjusters.ofDateAdjuster(w -> {
		// LocalDate result = w; // No cast
		// do {
		// result = result.plusDays(1);
		// } while (result.getDayOfWeek().getValue() >= 6);
		// return result;
		// });
		// LocalDate backToWork = today.with(NEXT_WORKDAY2);
		// System.out.println("backToWork: " + backToWork);

		LocalDate firstTuesday = LocalDate.of(2018, 2, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
		System.out.println("firstTuesday: " + firstTuesday);

		// LocalDate firstFriday = LocalDate.of(2000, 1,
		// 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		// LocalDate endDate = LocalDate.of(3000, 1, 1);
		// while (endDate.isAfter(firstFriday)) {
		// System.out.println(firstFriday);
		// firstFriday = firstFriday.plusDays(7);
		// }

		Instant start2 = Instant.now();
		calljs3();
		Instant end2 = Instant.now();
		Duration timeElapsed2 = Duration.between(start2, end2);
		System.out.println(timeElapsed2.toMillis());

	}

	public static void testParallelJs() {
		IntStream stream = IntStream.range(0, 100);
		stream.parallel().forEach(i -> {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("nashorn");
			try {
				Instant start = Instant.now();
				engine.eval("function foo(s){return Math.pow(s,3);}");
				engine.put("a", i);
				Instant end = Instant.now();
				System.out.println(
						i + " >>>> " + engine.eval("foo(a)") + " >> " + Duration.between(start, end).toMillis());
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public static void calljs() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		String funStr = "function foo(s){return s+1;} function bar(ss) {return 12;}";
		// engine.put("foo", funStr);

		try {
			engine.eval(funStr);
			engine.eval("var a=12;");
			System.out.println(engine.eval("foo(a)"));
			System.out.println(engine.eval("bar(a)"));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void calljs2() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		String funStr = "function foo(s){return s+1;} function bar(ss) {return 12;} foo(a);";

		try {
			CompiledScript c = ((Compilable) engine).compile(funStr);
			Bindings bindings = new SimpleBindings();
			bindings.put("a", 12);
			System.out.println(c.eval(bindings));

		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void calljs3() {
		TestBean bean = new TestBean();
		bean.setFirstStr("first");
		bean.setSecondStr("second");

		Map<String, Object> map = new HashMap<>();
		map.put("third", "333");
		map.put("beaninner", bean);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");

		try {
			Bindings scope = engine.createBindings();
			scope.put("bean", bean);
			scope.put("mapjs", map);
			scope.put("b", 99);

			engine.eval(new FileReader("/Users/allen/git/javatest/src/main/java/allen/jdk8/test.js"), scope);
			engine.eval(new FileReader("/Users/allen/git/javatest/src/main/java/allen/jdk8/test.js"));
			// System.out.println(
			// Paths.get(TestOne.class.getClassLoader().getResource("allen/jdk8/test.js").getPath()).toUri());
			// engine.eval(Files.newBufferedReader(
			// Paths.get(TestOne.class.getClassLoader().getResource("allen/jdk8/test.js").getPath())));

			engine.eval("var a=23;");
			engine.put("b", 99);
			engine.put("beanjs", bean);
			engine.put("mapjs", map);

			// System.out.println(engine.eval("foo(a)"));
			System.out.println(engine.eval("bar(b)", scope));
			System.out.println(engine.eval("bar(b)"));
			// System.out.println(engine.eval("showbean(1)"));
			// System.out.println(engine.eval("showbean(2)"));
			// System.out.println(engine.eval("foo(a)==24"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void printSubPath(String pathName) {
		for (File dirFile : new File(pathName).listFiles()) {
			if (dirFile.isDirectory()) {
				System.out.println(dirFile.getName());
			}
		}

		Stream.of(new File(pathName).listFiles()).filter(f -> f.isDirectory()).map(f -> f.getName())
				.forEach(System.out::println);
	}

	public static void fileOper() throws Exception {
		try (Stream<String> fstream = Files.lines(Paths.get("/Users/allen/Desktop/work/work.txt"))) {
			fstream.forEach(System.out::println);
		}
	}

	public static void wordCount() throws Exception {
		String content = new String(
				Files.readAllBytes(Paths.get("/Users/allen/Downloads/code/ch2/sec01/war-and-peace.txt")),
				StandardCharsets.UTF_8);
		List<String> wlist = Arrays.asList(content.split("[\\P{L}]+"));

		long start = System.nanoTime();
		long count = wlist.stream().filter(s -> s.length() > 12).count();
		System.out.println(count);
		System.out.println(System.nanoTime() - start);

		start = System.nanoTime();
		count = wlist.parallelStream().filter(s -> s.length() > 12).count();
		System.out.println(count);
		System.out.println(System.nanoTime() - start);

		wlist.stream().filter(s -> s.length() > 0).distinct().sorted((s1, s2) -> {
			if (s1.length() > s2.length())
				return 1;
			if (s1.length() < s2.length())
				return -1;
			return 0;
		}).limit(100).forEach(System.out::println);

		wlist.stream().filter(s -> s.length() > 0).distinct().sorted(Comparator.comparing(String::length).reversed())
				.limit(100).forEach(System.out::println);
	}

}
