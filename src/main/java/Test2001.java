import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.log.SysoCounter;

public class Test2001 {

	public static void main(String[] args) throws Exception{
		int a=12;
		f0205();
//		int aa = Long.valueOf(Math.round(12*100.0/2343)).intValue();
//		System.out.println(aa);
		System.out.println("sssFlagw".endsWith("Flag"));
		System.out.println(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
	}
	private static void f17() {
		String key = "aaa";
		switch (key) {
		case "bbb":
			System.out.println(111);
			break;
		case "aaa":
		case "ccc":
			System.out.println(222);
			break;
		case "cccs":
			System.out.println(333);
			break;
		default:
			break;
		}
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
	
	private static void f0205() throws FileNotFoundException, IOException {
		List<List<String>> siteList = new ArrayList<>();
		List<List<String>> itemList = new ArrayList<>();
		
		String siteStr = "INSERT INTO run_site_info(id, part_type, part_name, site_catalogy, site_name, site_code, site_sort) VALUES(";
		String itemStr = "INSERT INTO run_check_item(id, site_id, item_content, item_sort)  VALUES(";
		
		try(BufferedReader fin = new BufferedReader(new FileReader("d:/ssss.csv")); PrintWriter fout = new PrintWriter("d:/out.sql");){
			fin.lines().filter(line->StringUtils.isNotBlank(line)).forEach(line->{
				String[] arr = line.split(",");
				
				List<String> site = new ArrayList<>();
				List<String> item = new ArrayList<>();
				
				String siteId = UUID.randomUUID().toString();
				String itemId = UUID.randomUUID().toString();
				
				site.add(siteId);
				site.add(arr[0].trim());
				site.add(arr[1].trim());
				site.add(arr[2].trim());
				site.add(arr[4].trim());
				
				item.add(itemId);
				item.add(siteId);
				item.add(arr[3].trim());
				
				siteList.add(site);
				itemList.add(item);
			});

			for (int i = 0; i < siteList.size(); i++) {
				List<String> site = siteList.get(i);
				List<String> item = itemList.get(i);
				
				StringBuilder sb = new StringBuilder();
				sb.append(siteStr);
				sb.append("'").append(site.get(0)).append("', ");
				sb.append("'0', ");
				sb.append("'").append(site.get(1)).append("', ");
				sb.append("'").append(site.get(2)).append("', ");
				sb.append("'").append(site.get(3)).append("', ");
				sb.append("'").append(site.get(4)).append("', ");
				sb.append(i+8).append("); ");
				fout.println(sb.toString());
				
				sb = new StringBuilder();
				sb.append(itemStr);
				sb.append("'").append(item.get(0)).append("', ");
				sb.append("'").append(item.get(1)).append("', ");
				sb.append("'").append(item.get(2)).append("', ");
				sb.append("1); ");
				fout.println(sb.toString());
				
				fout.println();
			}
			fout.flush();
		}
	}
}
