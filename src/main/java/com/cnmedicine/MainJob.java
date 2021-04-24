package com.cnmedicine;

import java.util.stream.IntStream;

public class MainJob {
	private static TaskJob task = new TaskJob();

	public static void main(String[] args) throws Exception {
		task.startSmsQueue();
		
		Thread.sleep(2000);
		
		new Thread(() -> {
			IntStream.range(1, 10).forEach(num->{
				task.sendSms("num>>"+num);
			});
		}).start();

		new Thread(() -> {
			IntStream.range(100, 110).forEach(num->{
				task.sendSms("num>>"+num);
			});
		}).start();

		
		Thread.sleep(20000);
	}

}
