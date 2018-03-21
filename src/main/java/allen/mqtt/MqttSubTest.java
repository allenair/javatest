package allen.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubTest {

	public static void main(String[] args) throws MqttException {
		 runsub("client-id-1", "/iotdata/ft/+");  
	}
	 private static MqttClient connect(String clientId) throws MqttException{  
         MemoryPersistence persistence = new MemoryPersistence();  
         MqttConnectOptions connOpts = new MqttConnectOptions();  
//       String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.206:1883"};  
         connOpts.setCleanSession(false);  
//         connOpts.setUserName(userName);  
//         connOpts.setPassword(passWord.toCharArray());  
         connOpts.setConnectionTimeout(10);  
         connOpts.setKeepAliveInterval(20);  
//         connOpts.setServerURIs(uris);  
//         connOpts.setWill(topic, "close".getBytes(), 2, true);  
         MqttClient mqttClient = new MqttClient("tcp://localhost:1883", clientId, persistence);  
         mqttClient.connect(connOpts);  
         return mqttClient;  
     }  
       
     public static void sub(MqttClient mqttClient,String topic) throws MqttException{  
//         int[] Qos  = {0};  
//         String[] topics = {topic};  
//         mqttClient.subscribe(topics, Qos); 
         
         mqttClient.subscribe(topic, 0, (top, msg)->{
        	 System.out.println(new String(msg.getPayload(),"UTF-8"));
         });
     }  
       
       
    private static void runsub(String clientId, String topic) throws MqttException{  
        MqttClient mqttClient = connect(clientId);  
        if(mqttClient != null){  
            sub(mqttClient,topic);  
        }  
    }  
}
