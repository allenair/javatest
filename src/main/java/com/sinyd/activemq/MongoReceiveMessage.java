package com.sinyd.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MongoReceiveMessage {
	private final static int THREAD_COUNT=10;
	private final static boolean QUEUE_MULTI_FLAG=false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MongoReceiveMessage().doGet();
	}
	
	public void doGet(){
		for(int i=0; i<THREAD_COUNT;i++){
			ReceiveThread thread;
			if(QUEUE_MULTI_FLAG)
				thread = new ReceiveThread("sinyd.test_"+i);
			else
				thread = new ReceiveThread();
			thread.start();
		}
		
		
	}
	
	private class ReceiveThread extends Thread {
		private String queqeName;
		public ReceiveThread(String queqeName){
			this.queqeName = queqeName;
		}
		public ReceiveThread(){
			this.queqeName = "sinyd.test";
		}
		@Override
		public void run() {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");

			Connection connection;
			try {
				connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination destination = session.createQueue(queqeName);
				MessageConsumer consumer = session.createConsumer(destination);
				consumer.setMessageListener(new MessageListener() {
					public void onMessage(Message msg) {
						TextMessage message = (TextMessage) msg;
						try {
							System.out.println(System.currentTimeMillis()+"#" + message.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}

	}
}
