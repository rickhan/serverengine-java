package com.serverengine.rpc;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 消息解码
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class MessageDecoder  extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if (in.readableBytes() < Integer.SIZE)
		{
			return;
		}
		
		ByteBuf buf = in.slice();
		int length = buf.readInt();
		if (length <= 0)
		{
			ctx.channel().close();
			return;
		}
		
		if (length > buf.readableBytes())
		{
			return;
		}
		
		byte[] data = new byte[length - Integer.SIZE];
		buf.readBytes(data);
		in.readerIndex(in.readerIndex() + buf.readerIndex());
		out.add(Message.bulidMessage(data));
	}

}
