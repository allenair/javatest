package com.sinyd.activemq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.ObjectName;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.transport.TransportListener;

public class SendTest {

	/**
	 * @param args
	 */
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private BrokerService brokerService;
	private String bindAddress = "tcp://192.168.1.219:61616";
	private String queueName = "my-queue";

	public static void main(String[] args) throws Exception {
		SendTest test = new SendTest();
		test.init();

		for (int i = 0; i < 1; i++) {
//			test.sendMessage("{\"type\":\"jms_message_volt\",\"value\":\"" + (int)(Math.random()*5) + "\"}");
			test.sendMessage("");
		}

		test.close();
	}

	public void init() throws Exception {
//		brokerService = new BrokerService();
		// 配置监听相关的信息
//		configureBroker(brokerService);
		// 启动Broker的启动
//		brokerService.start();
		
		
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				bindAddress);
		connection = connectionFactory.createConnection();
		
		// 添加Connection 的状态监控的方法
//		monitorConnection(connection);
		
		
		
		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queueName);

		producer = session.createProducer(destination);
	}


	public void sendMessage(String msg) throws Exception {
		// MapMessage message = session.createMapMessage();
		// message.setString("allen_str", msg);
//		getQueues();
		TextMessage message = session.createTextMessage();
		message.setText(msg);

		producer.send(message);
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

	
	
	
	
	
	
	private void getQueues() throws Exception {
		BrokerViewMBean broker = brokerService.getAdminView();
		if (broker == null) {
			return;
		}
		ObjectName[] queues = broker.getQueues();
		for (ObjectName objectName : queues) {
			System.out.println("###"+objectName.toString());
		}
	}
	
	
	private PolicyEntry createPolicyEntry(
			ConstantPendingMessageLimitStrategy strategy) {
		PolicyEntry policy = new PolicyEntry();
		policy.setAdvisdoryForFastProducers(true);
		policy.setAdvisoryForConsumed(true);
		policy.setAdvisoryForDelivery(true);
		policy.setAdvisoryForDiscardingMessages(true);
		policy.setAdvisoryForSlowConsumers(true);
		policy.setAdvisoryWhenFull(true);
		policy.setProducerFlowControl(false);
		policy.setPendingMessageLimitStrategy(strategy);
		return policy;
	}
	private void configureBroker(BrokerService answer) throws Exception {
		// 创建持久化信息
		answer.setPersistent(false);
		// 设置采用JMX管理
		answer.setUseJmx(true);
		ConstantPendingMessageLimitStrategy strategy = new ConstantPendingMessageLimitStrategy();
		strategy.setLimit(10);
		PolicyEntry tempQueueEntry = createPolicyEntry(strategy);
		tempQueueEntry.setTempQueue(true);
		PolicyEntry tempTopicEntry = createPolicyEntry(strategy);
		tempTopicEntry.setTempTopic(true);
		PolicyMap pMap = new PolicyMap();
		final List<PolicyEntry> policyEntries = new ArrayList<PolicyEntry>();
		policyEntries.add(tempQueueEntry);
		policyEntries.add(tempTopicEntry);
		pMap.setPolicyEntries(policyEntries);
		answer.setDestinationPolicy(pMap);
		// 绑定url
		answer.addConnector(bindAddress);
		answer.setDeleteAllMessagesOnStartup(true);
	}
	
	private void monitorConnection(Connection connection) {
		ActiveMQConnection activemqconnection = (ActiveMQConnection) connection;
		// 添加ActiveMQConnection的监听类
		activemqconnection.addTransportListener(new TransportListener() {

			public void onCommand(Object object) {
				System.out.println("onCommand  object " + object);

			}

			public void onException(IOException ex) {
				System.out.println("onException =" + ex.getMessage());
			}

			public void transportInterupted() {
				System.out.println("transportInterupted =");
			}

			public void transportResumed() {
				System.out.println("transportResumed .........");
			}
		});
	}
	
}
