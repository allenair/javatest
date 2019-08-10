import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Test1908 {

	public static void main(String[] args) {
		test0810();
	}

	private static void test0810() {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> mockData = new HashMap<>();
		mockData.put("items_id", "1");
		mockData.put("item_content", "");
		mockData.put("measure_result", "AA1111111111111111");
		list.add(mockData);
		
		mockData = new HashMap<>();
		mockData.put("items_id", "2");
		mockData.put("item_content", "BBBBBBB");
		mockData.put("measure_result", "BB2222222222222");
		list.add(mockData);
		
		
		System.out.println(new Gson().toJson(list));
	}
}
