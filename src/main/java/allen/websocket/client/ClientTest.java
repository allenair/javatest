package allen.websocket.client;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ClientTest {

	public static void main(String[] args) {
		try {
			String url = "ws://127.0.0.1:8080/myHandler/ID=123";
			URI uri = new URI(url);
			WebSocketClient mWs = new WebSocketClient(uri) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					System.out.println("=====连接服务器====");
				}

				@Override
				public void onMessage(String s) {
					System.out.println(s);
				}

				@Override
				public void onClose(int i, String s, boolean b) {
					System.out.println("=====服务器关闭====");
				}

				@Override
				public void onError(Exception e) {
					System.out.println(e);
				}
			};
			mWs.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
