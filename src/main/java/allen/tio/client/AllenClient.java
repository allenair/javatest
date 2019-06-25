package allen.tio.client;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

import allen.tio.TioTestApp;
import allen.tio.common.AllenPacket;

public class AllenClient {
	public static Node serverNode = new Node("172.16.5.230", 9876);
//	public static Node serverNode = new Node("127.0.0.1", 9876);
	
	private static ReconnConf reconnConf = new ReconnConf(3000L);
	
	public static void send() throws Exception {
		for (int i = 0; i < 10; i++) {
			final int k = i;
			new Thread(() -> {
				try {
					ClientAioHandler tioClientHandler = new AllenClientHandler();
					ClientAioListener aioListener = new AllenClientListener();

					ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);
					clientGroupContext.setHeartbeatTimeout(60000);
					TioClient tioClient = new TioClient(clientGroupContext);
					
					ClientChannelContext clientChannelContext = tioClient.connect(serverNode);
					String clientId = TioTestApp.mockClientIdList.get(k);
					clientChannelContext.setBsId(clientId);
					
					while (true) {
						AllenPacket packet = new AllenPacket();
						String msg =  clientId + "#client#" + System.currentTimeMillis();
						packet.setBody(msg.getBytes(AllenPacket.CHARSET));
						Tio.send(clientChannelContext, packet);

						Thread.sleep(3000);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}).start();
		}
		
	}
}
