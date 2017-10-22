package allen.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
	public final static int GROUP_NUM=10;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception  {
		new Main().test();
	}

	private void test()throws Exception {
		Random rnd = new Random(System.currentTimeMillis());
		
		List<List<Integer>> numList = new ArrayList<List<Integer>>();

		for (int i = 0; i < GROUP_NUM; i++) {
			List<Integer> innerList = new ArrayList<Integer>();
			for (int k = 0; k < 10; k++) {
				innerList.add(rnd.nextInt(10000));
			}
			numList.add(innerList);
		}
		
		printList(numList);
		
		new MyThread().startTest(numList);
		Thread.sleep(3000);
		
		printResult();
	}
	
	private void printList(List<List<Integer>> numList){
		for (List<Integer> list : numList) {
			for (Integer num : list) {
				System.out.print(num+"\t");
			}
			System.out.println();
		}
	}
	
	private void printResult(){
		System.out.println("===============result====================");
		for (int num : MyThread.maxArray) {
			System.out.print(num+"\t");
		}
		System.out.println("\n===============sorted====================");
		Arrays.sort(MyThread.maxArray);
		for (int num : MyThread.maxArray) {
			System.out.print(num+"\t");
		}
		System.out.println();
		System.out.println(MyThread.maxNum);
	}
}
