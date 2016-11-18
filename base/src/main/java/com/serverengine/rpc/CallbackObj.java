package com.serverengine.rpc;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.channel.Channel;

/**
 * 回调
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class CallbackObj {

	/**
	 * 回调id
	 */
	private long callbackId;

	/**
	 * 通信通道
	 */
	private Channel channel;

	/**
	 * 生成时间
	 */
	private long generateTime;

	{
		callbackId = 0L;
		channel = null;
		generateTime = System.currentTimeMillis();
	}

	public long getCallbackId() {
		return callbackId;
	}

	public void setCallbackId(long callbackId) {
		this.callbackId = callbackId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public long getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}

	public void done(Object... args) {
		if (channel == null)
		{
			return;
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Kryo kryo = ThreadSafeKryo.getKryo();
		Output output = new Output(stream);
		kryo.writeObjectOrNull(output, "__rpccallback", String.class); // 第一个参数必定为函数名
		for (int i = 0; i < args.length; ++i)
		{
			Object arg = args[i];
			Class<?> clazz = arg.getClass();
			if (clazz == RpcChannel.class)
			{
				continue;
			}
			
			if (clazz == CallbackObj.class) // 回调中嵌回调，这是不允许的
			{
				return;
			}
			
			kryo.writeObjectOrNull(output, arg, clazz);
		}
		output.flush();
		
		Message msg = Message.bulidMessage(stream.toByteArray());
		channel.writeAndFlush(msg);
	}
}
