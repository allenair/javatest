package com.sinyd.activemq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;

public class SecuritySendTest {

	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private AtomicBoolean isConnect = new AtomicBoolean(false);
//	private BrokerService brokerService;
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		SecuritySendTest test = new SecuritySendTest();
		test.init();

		int i=1000;
		while(i<1001){
			try{
//				Thread.sleep(5000);
				if(test.isConeection()){
//					test.sendMessage("{\"message\":{\"value\":"+i+",\"timeStampLong\":"+System.currentTimeMillis()+",\"innerCode\":\"LN_JLSD_2_LISTEN_001\"},\"type\":\"\",\"opt\":\"2\"}");
					test.sendMessage("");
					System.out.println("222");
					i++;
				}else{
					
				}
				
			}catch(Exception e){
				continue;
			}
		}
		
		
//		test.close();
		System.out.println(System.currentTimeMillis()-start);
	}

	public void init() throws Exception {
//		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://192.168.1.118:61617");
		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://192.168.1.219:61617");
//		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("failover:(ssl://192.168.1.219:61617)");
//		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("ssl://59.46.65.183:61617");
//		ActiveMQSslConnectionFactory connectionFactory = new ActiveMQSslConnectionFactory("tcp://192.168.1.219:61616");

		connectionFactory.setKeyAndTrustManagers(
				SecurityTools.getKeyManagers(),
				SecurityTools.getTrustManagers(),
				new java.security.SecureRandom());

		connection = connectionFactory.createConnection();
		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//		Destination destination = session.createQueue("channel_receive_queue");
		Destination destination = session.createQueue("test_queue");

		producer = session.createProducer(destination);
		isConnect.set(true);
		
		
		
		
		
		
		
		
//		brokerService = new BrokerService();
//		// 配置监听相关的信息
//		configureBroker(brokerService);
//		// 启动Broker的启动
//		brokerService.start();
	}

//	protected void configureBroker(BrokerService answer) throws Exception {
//		// 创建持久化信息
//		answer.setPersistent(false);
//		// 设置采用JMX管理
//		answer.setUseJmx(true);
//		ConstantPendingMessageLimitStrategy strategy = new ConstantPendingMessageLimitStrategy();
//		strategy.setLimit(10);
//		PolicyEntry tempQueueEntry = createPolicyEntry(strategy);
//		tempQueueEntry.setTempQueue(true);
//		PolicyEntry tempTopicEntry = createPolicyEntry(strategy);
//		tempTopicEntry.setTempTopic(true);
//		PolicyMap pMap = new PolicyMap();
//		final List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();
//		policyEntries.add(tempQueueEntry);
//		policyEntries.add(tempTopicEntry);
//		pMap.setPolicyEntries(policyEntries);
//		answer.setDestinationPolicy(pMap);
//		// 绑定url
//		answer.addConnector(bindAddress);
//		answer.setDeleteAllMessagesOnStartup(true);
//	}
	
	public boolean isConeection(){
		return isConnect.get();
	}
	
	public boolean sendMessage(String msg) {
		TextMessage message;
		try {
			message = session.createTextMessage();

			message.setText(msg);

			producer.send(message);
			System.out.println("###" + message);
			
		} catch (JMSException e) {
			try {
				isConnect.set(false);
				session.close();
				connection.close();
			} catch (JMSException e1) {
				session=null;
				connection=null;
				return false;
			}
		}
		return true;
	}

	private void getQueueNum(){
		
	}
	
	public void close() throws JMSException {
		System.out.println("Producer:->Closing connection");
		if (producer != null)
			producer.close();
		if (session != null)
			session.close();
		if (connection != null)
			connection.close();
	}
}
