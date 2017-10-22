package com.sinyd.activemq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class MongoSocketReceiveMsg {

	private final static int THREAD_COUNT = 10;
	private LinkedBlockingQueue<String> myQueue = new LinkedBlockingQueue<String>();

	public static void main(String[] args) {
		new MongoSocketReceiveMsg().doGet();
	}

	public void doGet() {
		try {
			for (int i = 0; i < THREAD_COUNT; i++) {
				ReceiveThread thread = new ReceiveThread();
				thread.start();
			}
			
			ServerSocket server = new ServerSocket(8319);
			while (true) {
				Socket client = server.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String msg = "";

				while ((msg = in.readLine()) != null) {
					myQueue.offer(msg);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ReceiveThread extends Thread {
		@Override
		public void run() {
			String msg;
			try {
				while(true){
					msg = myQueue.take();
					System.out.println(this.getId() + "@@" + msg + "##" + System.currentTimeMillis());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
