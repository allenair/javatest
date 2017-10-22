package allen.thread;

import java.util.ArrayList;
import java.util.List;

public class MyThread {
	public static int[] maxArray = new int[Main.GROUP_NUM];
	public static int maxNum = 0;

	public void startTest(List<List<Integer>> numList) {
		List<Runnable> taskList = new ArrayList<Runnable>();
		for (int i = 0; i < numList.size(); i++) {
			taskList.add(new Task(i, numList.get(i)));
		}
		
		for (Runnable runnable : taskList) {
			new Thread(runnable).start();
		}
	}

}
