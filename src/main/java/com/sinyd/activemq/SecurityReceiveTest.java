package com.sinyd.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSslConnectionFactory;

public class SecurityReceiveTest {

	public static void main(String[] args)throws Exception  {
//		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://localhost:61617");  
		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://192.168.1.219:61617");  
		connectionFactory.setKeyAndTrustManagers(
				SecurityTools.getKeyManagers(),
				SecurityTools.getTrustManagers(),
				new java.security.SecureRandom());
		
	    Connection connection = connectionFactory.createConnection();  
	    connection.start();  
	  
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
//	    Destination destination = session.createQueue("channel_receive_queue");  
	    Destination destination = session.createQueue("channel_send_queue");  
	  
	    MessageConsumer consumer = session.createConsumer(destination);  
	    //listener
	    consumer.setMessageListener(new MessageListener() {
	 
	        public void onMessage(Message msg){ 
	            TextMessage message = (TextMessage) msg; 
	            try {
					System.out.println("收到消息：" + message.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				} 
	        } 
	    }); 
	}

}
