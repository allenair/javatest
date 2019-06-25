package allen.tio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.RandomUtils;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import allen.tio.common.AllenPacket;
import allen.tio.common.MockClientGroup;

public class AllenServer {
	// handler, 包括编码、解码、消息处理
		public static ServerAioHandler aioHandler = new AllenServerHandler();

		// 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
		public static ServerAioListener aioListener = new AllenServerListener();

		// 一组连接共用的上下文对象
		public static ServerGroupContext serverGroupContext = new ServerGroupContext("allen-server", aioHandler, aioListener);

		// tioServer对象
		public static TioServer tioServer = new TioServer(serverGroupContext);

		// 有时候需要绑定ip，不需要则null
		public static String serverIp = null;

		// 监听的端口
		public static int serverPort = 9876;

		public static int timeOut = 60000;

		public static void main(String[] args) throws IOException {
			serverGroupContext.setHeartbeatTimeout(timeOut);

			tioServer.start(serverIp, serverPort);

			testSend();
			
			checkConnections();
		}

		private static void testSend() {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					int index = RandomUtils.nextInt(0, 50);
					
					if (index < 20) {
						String clientKey = MockClientGroup.mockClientIdList.get(index);
						ChannelContext tmp = Tio.getChannelContextByBsId(serverGroupContext, clientKey);
						
						if (tmp != null && !tmp.isClosed) {
							try {
								AllenPacket resppacket = new AllenPacket();
								resppacket.setBody((clientKey+"#服务器主动调用:" + LocalDateTime.now()).getBytes(AllenPacket.CHARSET));
								boolean flag = Tio.send(tmp, resppacket);
								if(!flag) {
									Tio.close(tmp, "send error!");
									Tio.unbindBsId(tmp);
								}
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						} 
					}
				}
			}, 10, 5000);
		}
		
		private static void checkConnections() {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					for (ChannelContext tmp : Tio.getAllChannelContexts(serverGroupContext).getObj()) {
						if (tmp != null && !tmp.isClosed) {
							try {
								AllenPacket resppacket = new AllenPacket();
								resppacket.setBody((tmp.getBsId()+"#test-check" + LocalDateTime.now()).getBytes(AllenPacket.CHARSET));
								boolean flag = Tio.send(tmp, resppacket);
								
								if(!flag) {
									Tio.close(tmp, "check error!");
									Tio.unbindBsId(tmp);
								}else {
									System.out.println("===="+tmp.getBsId()+"====OK");
								}
								
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						} 
					}
				}
				
			},10,120000);
		}
}
