package org.tio.examples.im.client;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.examples.im.client.handler.AuthRespHandler;
import org.tio.examples.im.client.handler.ChatRespHandler;
import org.tio.examples.im.client.handler.ExitGroupNotifyRespHandler;
import org.tio.examples.im.client.handler.HandshakeRespHandler;
import org.tio.examples.im.client.handler.ImAioHandlerIntf;
import org.tio.examples.im.client.handler.JoinGroupNotifyRespHandler;
import org.tio.examples.im.client.handler.JoinRespHandler;
import org.tio.examples.im.client.handler.LoginRespHandler;
import org.tio.examples.im.common.CommandStat;
import org.tio.examples.im.common.ImPacket;
import org.tio.examples.im.common.ImSessionContext;
import org.tio.examples.im.common.packets.Command;

import cn.hutool.core.util.ZipUtil;

/**
 *
 * @author tanyaowu
 *
 */
public class ImClientAioHandler implements ClientAioHandler {
	private static Logger log = LoggerFactory.getLogger(ImClientAioHandler.class);

	private static Map<Command, ImAioHandlerIntf> handlerMap = new HashMap<>();
	static {
		handlerMap.put(Command.COMMAND_AUTH_RESP, new AuthRespHandler());
		handlerMap.put(Command.COMMAND_CHAT_RESP, new ChatRespHandler());
		handlerMap.put(Command.COMMAND_JOIN_GROUP_RESP, new JoinRespHandler());
		handlerMap.put(Command.COMMAND_HANDSHAKE_RESP, new HandshakeRespHandler());
		handlerMap.put(Command.COMMAND_LOGIN_RESP, new LoginRespHandler());

		handlerMap.put(Command.COMMAND_JOIN_GROUP_NOTIFY_RESP, new JoinGroupNotifyRespHandler());
		handlerMap.put(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, new ExitGroupNotifyRespHandler());
	}

	private static ImPacket handshakeRespPacket = new ImPacket(Command.COMMAND_HANDSHAKE_RESP);

	private static ImPacket heartbeatPacket = new ImPacket(Command.COMMAND_HEARTBEAT_REQ);

	/**
	 * @param args
	 *
	 * @author tanyaowu
	 * 2016年11月18日 上午9:13:15
	 *
	 */
	public static void main(String[] args) {
	}

	/**
	 *
	 *
	 * @author tanyaowu
	 * 2016年11月18日 上午9:13:15
	 *
	 */
	public ImClientAioHandler() {
	}

	@Override
	public ImPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
		ImSessionContext imSessionContext = (ImSessionContext) channelContext.getAttribute();
		byte firstbyte = buffer.get();

		if (!imSessionContext.isHandshaked()) {
			if (ImPacket.HANDSHAKE_BYTE == firstbyte) {
				return handshakeRespPacket;
			} else {
				throw new AioDecodeException("还没握手");
			}
		}

		buffer.position(buffer.position() - 1);//位置复元

		int headerLength = ImPacket.LEAST_HEADER_LENGHT;
		ImPacket imPacket = null;
		firstbyte = buffer.get();
		@SuppressWarnings("unused")
		byte version = ImPacket.decodeVersion(firstbyte);
		boolean isCompress = ImPacket.decodeCompress(firstbyte);
		boolean hasSynSeq = ImPacket.decodeHasSynSeq(firstbyte);
		boolean is4ByteLength = ImPacket.decode4ByteLength(firstbyte);
		if (hasSynSeq) {
			headerLength += 4;
		}
		if (is4ByteLength) {
			headerLength += 2;
		}
		if (readableLength < headerLength) {
			return null;
		}
		Byte code = buffer.get();
		Command command = Command.forNumber(code);
		int bodyLength = 0;
		if (is4ByteLength) {
			bodyLength = buffer.getInt();
		} else {
			bodyLength = buffer.getShort();
		}

		if (bodyLength > ImPacket.MAX_LENGTH_OF_BODY || bodyLength < 0) {
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}

		int seq = 0;
		if (hasSynSeq) {
			seq = buffer.getInt();
		}

		//		@SuppressWarnings("unused")
		//		int reserve = buffer.getInt();//保留字段

		//		PacketMeta<ImPacket> packetMeta = new PacketMeta<>();
		int neededLength = headerLength + bodyLength;
		int test = readableLength - neededLength;
		if (test < 0) // 不够消息体长度(剩下的buffe组不了消息体)
		{
			//			packetMeta.setNeededLength(neededLength);
			return null;
		} else {
			imPacket = new ImPacket();
			imPacket.setCommand(command);

			if (seq != 0) {
				imPacket.setSynSeq(seq);
			}

			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				if (isCompress) {
					try {
						byte[] unGzippedBytes = ZipUtil.unGzip(dst);//.unGZip(dst);
						imPacket.setBody(unGzippedBytes);
						//						imPacket.setBodyLen(unGzippedBytes.length);
					} catch (Exception e) {
						throw new AioDecodeException(e);
					}
				} else {
					imPacket.setBody(dst);
					//					imPacket.setBodyLen(dst.length);
				}
			}

			//			packetMeta.setPacket(imPacket);
			return imPacket;

		}

	}

	/**
	 * @see org.tio.core.intf.AioHandler#encode(org.tio.core.intf.Packet)
	 *
	 * @param packet
	 * @return
	 * @author tanyaowu
	 * 2016年11月18日 上午9:37:44
	 *
	 */
	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
		ImPacket imPacket = (ImPacket) packet;
		if (imPacket.getCommand() == Command.COMMAND_HEARTBEAT_REQ) {
			ByteBuffer buffer = ByteBuffer.allocate(1);
			buffer.put(ImPacket.HEARTBEAT_BYTE);
			return buffer;
		}
		if (imPacket.getCommand() == Command.COMMAND_HANDSHAKE_REQ) {
			ByteBuffer buffer = ByteBuffer.allocate(1);
			buffer.put(ImPacket.HANDSHAKE_BYTE);
			return buffer;
		}

		byte[] body = imPacket.getBody();
		int bodyLen = 0;
		boolean isCompress = false;
		boolean is4ByteLength = false;
		if (body != null) {
			bodyLen = body.length;

			if (bodyLen > 200) {
				try {
					byte[] gzipedbody = ZipUtil.gzip(body);
					if (gzipedbody.length < body.length) {
						log.error("压缩前:{}, 压缩后:{}", body.length, gzipedbody.length);
						body = gzipedbody;
						bodyLen = gzipedbody.length;
						isCompress = true;
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

			if (bodyLen > Short.MAX_VALUE) {
				is4ByteLength = true;
			}
		}

		int allLen = imPacket.calcHeaderLength(is4ByteLength) + bodyLen;

		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(groupContext.getByteOrder());

		byte firstbyte = ImPacket.encodeCompress(ImPacket.VERSION, isCompress);
		firstbyte = ImPacket.encodeHasSynSeq(firstbyte, packet.getSynSeq() > 0);
		firstbyte = ImPacket.encode4ByteLength(firstbyte, is4ByteLength);
		//		String bstr = Integer.toBinaryString(firstbyte);
		//		log.error("二进制:{}",bstr);

		buffer.put(firstbyte);
		buffer.put((byte) imPacket.getCommand().getNumber());

		//GzipUtils

		if (is4ByteLength) {
			buffer.putInt(bodyLen);
		} else {
			buffer.putShort((short) bodyLen);
		}

		if (packet.getSynSeq() != null && packet.getSynSeq() > 0) {
			buffer.putInt(packet.getSynSeq());
		}
		//		else
		//		{
		//			buffer.putInt(0);
		//		}
		//
		//		buffer.putInt(0);

		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}

	/**
	 * @see org.tio.core.intf.AioHandler#handler(org.tio.core.intf.Packet)
	 *
	 * @param packet
	 * @return
	 * @throws Exception
	 * @author tanyaowu
	 * 2016年11月18日 上午9:37:44
	 *
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		ImPacket imPacket = (ImPacket) packet;
		Command command = imPacket.getCommand();
		ImAioHandlerIntf handler = handlerMap.get(command);
		if (handler != null) {
			Object obj = handler.handler(imPacket, channelContext);
			CommandStat.getCount(command).handled.incrementAndGet();
			return;
		} else {
			CommandStat.getCount(command).handled.incrementAndGet();
			log.warn("找不到对应的命令码[{}]处理类", command);
			return;
		}

	}

	/**
	 * @see org.tio.client.intf.ClientAioHandler#heartbeatPacket()
	 *
	 * @return
	 * @author tanyaowu
	 * 2016年12月6日 下午2:18:16
	 *
	 */
	@Override
	public ImPacket heartbeatPacket() {
		return heartbeatPacket;
	}

}
