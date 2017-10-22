package com.sinyd.activemq;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoSocketSendMsg {

	private final static int EACH_MESS_COUNT = 100;
	private final static int THREAD_COUNT = 10;
	private Mongo mongo;
	private DBCollection coll;

	public static void main(String[] args) throws Exception  {
		new MongoSocketSendMsg().sendMessage();
	}

	public MongoSocketSendMsg() throws Exception {
		mongo = new Mongo("192.168.1.219");
		DB db = mongo.getDB("asphaltum_test1");
		coll = db.getCollection("asphaltum");
	}

	public void sendMessage() {
		try {
			List<ArrayList<String>> sendList = getMsgList();
			
			System.out.println("==START==" + System.currentTimeMillis());
			
			List<Socket> socketPool = getSocketList(THREAD_COUNT);
			
			ArrayList<SendThread> pool = new ArrayList<SendThread>();
			for (int i = 0; i < THREAD_COUNT; i++) {
				SendThread thread = new SendThread(sendList.get(i), socketPool.get(i));
				thread.start();
				pool.add(thread);
			}
			while (true) {
				int running = 0;
				for (SendThread myThread : pool) {
					if (myThread.isAlive())
						running++;
				}
				if (running <= 0) {
					System.out.println("#END#" + System.currentTimeMillis());
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Socket> getSocketList(int count)throws Exception{
		List<Socket> list = new ArrayList<Socket>();
		for(int i=0;i<count;i++){
			Socket sendSocket= new Socket("localhost", 8319);
			list.add(sendSocket);
		}
		return list;
	}
	
	private List<ArrayList<String>> getMsgList() {
		DBCursor ret = coll.find();
		// DBCursor ret = coll.find(new BasicDBObject("equipId","21040000002"));

		List<ArrayList<String>> sendList = new LinkedList<ArrayList<String>>();
		ArrayList<String> tmpList = null;

		int count = 0;
		while (ret.hasNext()) {
			DBObject obj = ret.next();
			if (count % EACH_MESS_COUNT == 0) {
				tmpList = new ArrayList<String>();
				sendList.add(tmpList);
			}

			tmpList.add(obj.get("monitorId") + "," + obj.get("real_vlaue")
					+ "#" + obj.get("equipId") + ","
					+ obj.get("real_date_long"));

			count++;
			if (count >= EACH_MESS_COUNT * THREAD_COUNT)
				break;
		}
		mongo.close();
		return sendList;
	}

	private class SendThread extends Thread {
		private List<String> list = null;
		private Socket sendSocket;
		private PrintWriter out;

		public SendThread(List<String> list, Socket sendSocket) throws Exception {
			this.list = list;
			this.sendSocket= sendSocket;
			out = new PrintWriter(sendSocket.getOutputStream());
		}

		@Override
		public void run() {
			for (String msg : list) {
				out.println(msg + "," + System.currentTimeMillis());
				out.flush();
			}
			
			System.out.println(System.currentTimeMillis());
			
			try {
				out.close();
				sendSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
