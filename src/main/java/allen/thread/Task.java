package allen.thread;

import java.util.List;

public class Task implements Runnable {
	private int index;
	private List<Integer> numList;

	public Task(int index, List<Integer> numList) {
		this.index = index;
		this.numList = numList;
	}

	@Override
	public void run() {
		int max = 0;
		for (int num : numList) {
			if (num > max) {
				max = num;
			}
		}

		MyThread.maxArray[index] = max;
		
//		synchronized (MyThread.class) {
			if(max>MyThread.maxNum){
				try {
					long a = (long)(Math.random()*50);
					Thread.sleep(a);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MyThread.maxNum = max;
			}
//		}
		
	}

}
