package allen.tio.common;

import org.tio.core.intf.Packet;

public class AllenPacket extends Packet {
	public static final int HEADER_LENGHT = 4;// 消息头的长度
	public static final String CHARSET = "utf-8";
	private byte[] body;

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

}
