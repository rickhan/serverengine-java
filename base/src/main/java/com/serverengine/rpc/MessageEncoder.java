package com.serverengine.rpc;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 消息编码
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class MessageEncoder extends MessageToMessageEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message packet,
			List<Object> out) throws Exception {
		if (packet.getMessageLength() <= 0) {
			return;
		}

		ByteBuf buffer = ctx.alloc().buffer(packet.getMessageLength());
		buffer.writeInt(packet.getMessageLength());
		buffer.writeBytes(packet.getMessageData());
		out.add(buffer);
	}

}
