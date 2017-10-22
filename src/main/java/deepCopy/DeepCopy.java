package deepCopy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class DeepCopy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<MenuBean> menuList = new ArrayList<MenuBean>();

		MenuBean bean = new MenuBean("菜单", "");
		TreeBean tree = new TreeBean("系统管理", "");
		tree.addChild(new TreeBean("单位管理", "").addChild(new TreeBean("业主单位管理","/demo/resources/html/sysmanage/customorg_manage.html")));
		bean.addChildTree(tree);

		tree = new TreeBean("基础数据管理", "");
		tree.addChild(new TreeBean("运输单位管理","/demo/resources/html/basemanage/transportcompany_manage.html"));
		tree.addChild(new TreeBean("车辆管理", "/demo/resources/html/basemanage/vehicle_manage.html"));
		tree.addChild(new TreeBean("仓库管理", "/demo/resources/html/basemanage/warehouse_manage.html"));
		bean.addChildTree(tree);

		menuList.add(bean);
		test(menuList);
	}
	
	public static void test(List<MenuBean> srcList){
		Map<String, List<MenuBean>> map = new HashMap<String, List<MenuBean>>();
		map.put("json", srcList);
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(map);
		
		List<MenuBean> resList = (List) gson.fromJson(jsonStr, HashMap.class).get("json");
		System.out.println(11);
	}
	
	public static void test2(List<MenuBean> srcList){
		
	}
}
