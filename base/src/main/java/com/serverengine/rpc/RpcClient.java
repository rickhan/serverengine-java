package com.serverengine.rpc;

import java.lang.reflect.Proxy;

import com.serverengine.base.App;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * rpc客户端
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class RpcClient {
	private ChannelFuture future;
	private RpcChannel channel;
	private String host;
	private int port;
	private Object serverInstance;
	private MethodMgr serverMethods;
	private RpcInvocationHandler invokeHandler;
	private Object clientObj;
	private Bootstrap bootstrap;
	private boolean shouldStop = false;

	public RpcClient(String host, int port, Class<?> server, Class<?> client)
			throws Exception {
		this.host = host;
		this.port = port;
		serverInstance = server.newInstance();
		serverMethods = new MethodMgr();
		serverMethods.extractPublicVoidMethod(serverInstance);
		invokeHandler = new RpcInvocationHandler(null);
		clientObj = Proxy.newProxyInstance(client.getClassLoader(),
				new Class<?>[] { client }, invokeHandler);

		bootstrap = new Bootstrap();
		bootstrap.group(App.getInstance().getMainGroup());

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 8);
		bootstrap.option(ChannelOption.SO_SNDBUF, 1024 * 1024 * 8);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

		final RpcClient owner = this;
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				invokeHandler.setChannel(ch);
				channel = new RpcChannel(serverMethods, clientObj, ch);
				ch.pipeline().addLast("encoder", new MessageEncoder())
						.addLast("decoder", new MessageDecoder())
						.addLast("message", channel);
				channel.setOwner(owner);
			}
		});
	}

	public void start() {
		if (shouldStop) {
			return;
		}

		if (future != null) {
			return;
		}

		future = bootstrap.connect(host, port);
		future.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture cf) throws Exception {
				if (cf.isSuccess()) {

				} else if (shouldStop == false) {
					stop();
				}
			}
		});
	}

	public void stop() {
		if (future == null) {
			return;
		}

		future.channel().close();
		channel = null;
		future = null;
		invokeHandler.setChannel(null);
	}

	public void close() {
		stop();
		shouldStop = true;
	}

	@SuppressWarnings("unchecked")
	public <T> T getClient() {
		if (channel == null) { // 连接还没建立的时候，返回接口也是白搭
			return null;
		}

		return (T) clientObj;
	}

	public RpcChannel getChannel() {
		return channel;
	}
}
