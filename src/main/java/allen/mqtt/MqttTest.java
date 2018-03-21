package allen.mqtt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.CharSet;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttTest {

	public static void main(String[] args) throws Exception {
		publish("message content", "client-id-0", "/server/public/a");
	}

	private static MqttClient connect(String clientId, String userName, String password) throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		// connOpts.setUserName(userName);
		// connOpts.setPassword(password.toCharArray());
		connOpts.setConnectionTimeout(10);
		connOpts.setKeepAliveInterval(20);
		// String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
		// connOpts.setServerURIs(uris); //起到负载均衡和高可用的作用
		MqttClient mqttClient = new MqttClient("tcp://localhost:1883", clientId, persistence);
//		mqttClient.setCallback(new PushCallback("test"));
		mqttClient.connect(connOpts);
		return mqttClient;
	}

	private static void pub(MqttClient sampleClient, String msg, String topic)
			throws MqttPersistenceException, MqttException, UnsupportedEncodingException {
		MqttMessage message = new MqttMessage("ertwersdfas中文".getBytes("UTF-8"));
		message.setQos(0);
		message.setRetained(false);
		sampleClient.publish(topic, message);
	}

	private static void publish(String str, String clientId, String topic) throws MqttException, UnsupportedEncodingException {
		MqttClient mqttClient = connect(clientId, "", "");

		if (mqttClient != null) {
			pub(mqttClient, str, topic);
			System.out.println("pub-->" + str);
		}

		if (mqttClient != null) {
			mqttClient.disconnect();
		}
	}

}

class PushCallback implements MqttCallback {
	private String threadId;

	public PushCallback(String threadId) {
		this.threadId = threadId;
	}

	public void connectionLost(Throwable cause) {

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// System.out.println("deliveryComplete---------" + token.isComplete());
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String msg = new String(message.getPayload());
		System.out.println(threadId + " " + msg);
	}
}
