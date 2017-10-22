package allen.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CollectionTest {

	/**
	 * �˽ⷺ�����Զ���װ�� ����List��Set��Map���ֽṹ 
	 * 1��List���������飬ArrayList�����ã���LinkedList
	 * 2��Set��List���ƣ����ǻ�ȥ���ظ����ݣ�HashSet�����ã���TreeSet�����Զ�����
	 * 3��Map�������ֵ䣬HashMap�����ã���TreeMap���ᰴ��Keyֵ����
	 * 
	 */
	public static void main(String[] args) {
		CollectionTest tt = new CollectionTest();
		tt.listTest();
		tt.setTest();
		tt.mapSet();
	}

	public void listTest() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(new Integer(12));
		list.add(12);
		list.add(23);

		for (int a : list) {
			System.out.println(a);
		}

		System.out.println(list.get(1));

		System.out.println(list.size());
	}

	public void setTest() {
//		Set<Integer> set = new HashSet<Integer>();
		Set<Integer> set = new TreeSet<Integer>();
		set.add(new Integer(23));
		set.add(12);
		set.add(12);

		for (int a : set) {
			System.out.println(a);
		}

		System.out.println(set.size());
	}

	public void mapSet() {
		Map<String, String> map = new HashMap<String, String>();
//		Map<String, String> map = new TreeMap<String, String>();
		// map.put(null, "");
		map.put("11", "dd");
		map.put("a5", "bb");
		map.put("3", "dd");
		map.put("4", "cc");

		for (String key : map.keySet()) {
			System.out.println(map.get(key));
		}

		for (String str : map.values()) {
			System.out.println("###" + str);
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "##" + entry.getValue());
		}
	}

}
