package com.serverengine.rpc;

import io.netty.channel.Channel;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

/**
 * rpc调用处理
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class RpcInvocationHandler implements InvocationHandler {

	/**
	 * 序列化工具
	 */
	private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			return kryo;
		}
	};

	private Channel channel;

	public RpcInvocationHandler(Channel channel) {
		this.channel = channel;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName = method.getName();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Kryo kryo = kryos.get();
		Output output = new Output(stream);
		kryo.writeObjectOrNull(output, methodName, String.class); // 第一个参数，必须是函数名
		for (int i = 0; i < args.length; ++i)
		{
			Object arg = args[i];
			Class<?> clazz = arg.getClass();
			if (clazz == RpcChannel.class)
			{
				continue;
			}
			
			if (clazz == CallbackObj.class)
			{
				CallbackObj obj = (CallbackObj)arg;
				kryo.writeObjectOrNull(output, obj.getCallbackId(), long.class);
				continue;
			}
			
			kryo.writeObjectOrNull(output, arg, clazz);
		}
		output.flush();
		
		Message msg = Message.bulidMessage(stream.toByteArray());
		channel.writeAndFlush(msg);
		return null;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
