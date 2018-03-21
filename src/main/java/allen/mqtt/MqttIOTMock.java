package allen.mqtt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttIOTMock {

	private int qos = 0;
	private String broker = "tcp://localhost:1883";
	private String topic = "/iotdata/ft/";
	private final int deviceCount = 1000;
	private final int threadCount = 100;

	public static void main(String[] args) {
		new MqttIOTMock().runMock();
	}

	public void runMock() {
		final String json = "{\"elevatorId\":\"allentest-123\",\"parameterStr\":\"ExX+D31ubgAAY3oBAA==\",\"time\":\"12345678\"}";
		for (int k = 0; k < deviceCount; k++) {
			new Thread(() -> {
				try {
					MqttClient mqttClient = connect("" + UUID.randomUUID());
					int clientCode = new Random().nextInt(deviceCount * 10);
					for (int n = 0; n < threadCount; n++) {
						Thread.sleep(500);
						pub(mqttClient, json, topic + clientCode);
//						System.out.println("====" + n);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();

			
		}
	}

	private MqttClient connect(String clientId) throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setConnectionTimeout(10);
		connOpts.setKeepAliveInterval(20);
		MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
		mqttClient.connect(connOpts);
		return mqttClient;
	}

	private void pub(MqttClient sampleClient, String msg, String topic)
			throws MqttPersistenceException, MqttException, UnsupportedEncodingException {
		MqttMessage message = new MqttMessage(msg.getBytes("UTF-8"));
		message.setQos(this.qos);
		message.setRetained(false);
		sampleClient.publish(topic, message);
	}

}
