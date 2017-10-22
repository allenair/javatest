package com.sinyd.activemq;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.mail.Flags.Flag;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiveTest {
	private String bindAddress = "tcp://localhost:61616";
	private String queueName = "my-queue";
	private ConcurrentLinkedQueue<String> queueList = new ConcurrentLinkedQueue<String>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception  {
		new ReceiveTest().init();
		System.out.println("Main thread end!!");
	}
	
	public void init()throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(bindAddress);  
		  
	    Connection connection = connectionFactory.createConnection();  
	    connection.start();  
	  
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
	    Destination destination = session.createQueue(queueName);  
	  
	    MessageConsumer consumer = session.createConsumer(destination);  
	    //listener
	    consumer.setMessageListener(new MessageListener() {
	 
	        public void onMessage(Message msg){ 
	            TextMessage message = (TextMessage) msg; 
	            try {
	            	queueList.add(message.getText());
//	            	System.out.println("收到消息：" + message.getText());
//					session.commit(); 
				} catch (JMSException e) {
					e.printStackTrace();
				} 
	        } 
	    }); 

	    new Thread(new Runnable() {
			@Override
			public void run() {
				Set<String> msgSet = new HashSet<String>();
				while(true){
					int len = queueList.size();
					for(int i=0;i<len;i++){
						msgSet.add(queueList.poll());
					}
					for (String msg : msgSet) {
						System.out.println("收到消息：" + msg);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					msgSet.clear();
				}
			}
		}).start();
	    
	}
}
