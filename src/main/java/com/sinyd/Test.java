package com.sinyd;

import java.util.concurrent.atomic.AtomicBoolean;

public class Test {
	public static AtomicBoolean flag = new AtomicBoolean(true);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					flag.set(false);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}).start();
		
		int count=1;
		while(flag.get()){
			System.out.println("#"+(count++));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
