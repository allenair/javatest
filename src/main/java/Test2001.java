import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import com.itextpdf.text.log.SysoCounter;

public class Test2001 {

	public static void main(String[] args) {
		f15();
//		int aa = Long.valueOf(Math.round(12*100.0/2343)).intValue();
//		System.out.println(aa);
	}

	private static void f15() {
		final Map<Integer, Integer> nummap = new HashMap<Integer, Integer>();
		final Random rnd = new Random();

		IntStream.range(1, 100).forEach(i -> {
			int num = rnd.nextInt(10) + 1;
			if (nummap.get(num) == null) {
				nummap.put(num, 1);
			} else {
				nummap.put(num, nummap.get(num) + 1);
			}
		});

		nummap.keySet().forEach(key -> {
//			System.out.println(key + "==" + nummap.get(key));
		});

		nummap.clear();
		
		List<Integer> srcList = Arrays.asList(12, 23, 14, 25, 36);
		IntStream.range(1, 10000).forEach(i -> {
			int num = randomIndexByWeight(srcList);
			if (nummap.get(num) == null) {
				nummap.put(num, 1);
			} else {
				nummap.put(num, nummap.get(num) + 1);
			}
		});

		nummap.keySet().forEach(key -> {
			System.out.println(key + "==" + nummap.get(key));
		});
	}

	private static int randomIndexByWeight(List<Integer> weightList) {
		List<Integer> realWeightList = new ArrayList<>();

		int sum = weightList.stream().reduce(0, (a, b) -> a + b);
		weightList.stream().forEach(val -> realWeightList.add(Long.valueOf(Math.round(val * 100.0 / sum)).intValue()));

		int rndNum = new Random().nextInt(100) + 1;
		int accu = 0;
		for (int i = 0; i < realWeightList.size(); i++) {
			accu = accu + realWeightList.get(i);
			if (rndNum <= accu) {
				return i;
			}
		}

		return realWeightList.size() - 1;
	}
}
