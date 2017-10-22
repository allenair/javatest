package com.sinyd.activemq;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoSendMessage {

	/**
	 * <P>
	 * Function: main
	 * <P>
	 * Description:
	 * <P>
	 * Others:
	 * 
	 * @author: Allen
	 * @CreateTime: 2012-3-15 ����11:34:20
	 * @param args
	 *            void
	 */
	private DBCollection coll;
	private Mongo mongo;
	private final static int EACH_MESS_COUNT = 100;
	private final static int THREAD_COUNT = 10;
	private final static boolean QUEUE_MULTI_FLAG=false;
	
	private Connection connection;

	public static void main(String[] args) throws Exception {

		new MongoSendMessage().sendMessage();
	}

	public MongoSendMessage() throws Exception {
		mongo = new Mongo("192.168.1.219");
		DB db = mongo.getDB("asphaltum_test1");
		coll = db.getCollection("asphaltum");

	}

	public void sendMessage() {
		List<ArrayList<String>> sendList = getMsgList();

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		// ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.155:61616");
		
		try {
			connection = connectionFactory.createConnection();
			connection.start();

			System.out.println("==START==" + System.currentTimeMillis());

			ArrayList<SendThread> pool = new ArrayList<SendThread>();
			for (int i = 0; i < THREAD_COUNT; i++) {
				SendThread thread;
				if(QUEUE_MULTI_FLAG)
					thread = new SendThread(sendList.get(i), "sinyd.test_"+i);
				else
					thread = new SendThread(sendList.get(i));
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
					connection.close();
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private List<ArrayList<String>> getMsgList(){
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
		private Session session;
		private MessageProducer producer;
		private List<String> list = null;
		private String queqeName;
		public SendThread(List<String> list) {
			this.list = list;
			this.queqeName = "sinyd.test";
			try {
				init();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		public SendThread(List<String> list, String queqeName) {
			this.list = list;
			this.queqeName = queqeName;
			try {
				init();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				for (String msg : list) {
					sendMessage(msg + "," + System.currentTimeMillis());
				}
				 System.out.println(System.currentTimeMillis());
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void init() throws JMSException {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(this.queqeName);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}

		private void sendMessage(String msg) throws JMSException {
			TextMessage message = session.createTextMessage();
			message.setText(msg);
			producer.send(message);
		}

		private void close() throws JMSException {
			if (producer != null)
				producer.close();
			if (session != null)
				session.close();
		}

	}
}
