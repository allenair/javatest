package allen.tio.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import allen.tio.common.AllenPacket;

public class AllenClientHandler implements ClientAioHandler {

	@Override
	public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
			throws AioDecodeException {
		AllenPacket imPacket = new AllenPacket();
		byte[] dst = new byte[readableLength];
		buffer.get(dst);
		imPacket.setBody(dst);
		return imPacket;
	}

	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		AllenPacket helloPacket = (AllenPacket) packet;
		byte[] body = helloPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		// 创建一个新的bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(bodyLen);
		// 设置字节序
		buffer.order(groupContext.getByteOrder());

		// 写入消息体
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		AllenPacket helloPacket = (AllenPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			String str = new String(body, AllenPacket.CHARSET);
			System.out.println("[Client] 收到："+channelContext.getBsId()+ "%%%" + str);
		}
		return;
	}

	@Override
	public Packet heartbeatPacket(ChannelContext channelContext) {
		AllenPacket heartbeatPacket = new AllenPacket();
		String msg =  channelContext.getBsId() + "#heart!!!!!!!!!!!!!!!!!!!!!!!!!!!#";
		try {
			heartbeatPacket.setBody(msg.getBytes(AllenPacket.CHARSET));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return heartbeatPacket;
	}

}
