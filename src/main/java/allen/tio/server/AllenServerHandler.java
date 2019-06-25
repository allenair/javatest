package allen.tio.server;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import allen.tio.common.AllenPacket;

public class AllenServerHandler implements ServerAioHandler {

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
			System.out.println("[Server" + LocalDateTime.now() + "]收到：" + str);

			String[] strArr = str.split("#");
			
			if(StringUtils.isEmpty(channelContext.getBsId())) {
				Tio.bindBsId(channelContext, strArr[0]);
			}

			AllenPacket resppacket = new AllenPacket();
			resppacket.setBody(("收到数据").getBytes(AllenPacket.CHARSET));
			Tio.send(channelContext, resppacket);

		}

	}

}
