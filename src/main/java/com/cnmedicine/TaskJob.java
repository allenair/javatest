package com.cnmedicine;

import java.util.concurrent.LinkedBlockingQueue;


public class TaskJob {
	private LinkedBlockingQueue<String> smsQueue = new LinkedBlockingQueue<>();
	
	public void startSmsQueue() {
		System.out.println("=================SMS Queue Start===========================");

		new Thread(() -> {
			while(true) {
				try {
					String str = smsQueue.take();
					System.out.println("====="+str);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void sendSms(String smsBean) {
		smsQueue.offer(smsBean);
	}
}
