package com.serverengine.rpc;

import java.lang.reflect.Proxy;

import com.serverengine.base.App;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * rpc 服务器
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class RpcServer {

	private ChannelFuture channelFuture;
	private MethodMgr serverMethods;
	private Object serverInstance;

	public RpcServer(String host, int port, Class<?> server,
			final Class<?> client) throws Exception {
		serverInstance = server.newInstance();
		serverMethods = new MethodMgr();
		serverMethods.extractPublicVoidMethod(serverInstance);

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(App.getInstance().getMainGroup(), App.getInstance()
				.getMainGroup());
		bootstrap.channel(NioServerSocketChannel.class);

		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 8 * 1024);
		bootstrap.option(ChannelOption.SO_BACKLOG, 128);
		bootstrap.option(ChannelOption.ALLOCATOR,
				PooledByteBufAllocator.DEFAULT);

		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childOption(ChannelOption.SO_LINGER, 0);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 8 * 1024);
		bootstrap.childOption(ChannelOption.ALLOCATOR,
				PooledByteBufAllocator.DEFAULT);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				Object clientObj = Proxy.newProxyInstance(
						client.getClassLoader(), new Class<?>[] { client },
						new RpcInvocationHandler(serverMethods, channel));
				RpcChannel rpcChannel = new RpcChannel(serverMethods, clientObj, channel);
				channel.pipeline().addLast("decoder", new MessageDecoder())
						.addLast("encoder", new MessageEncoder())
						.addLast("message", rpcChannel);
			}
		});

		channelFuture = bootstrap.bind(host, port);
		channelFuture.sync();
	}

	public void close() {
		if (channelFuture != null) {
			channelFuture.channel().close().syncUninterruptibly();
			channelFuture.channel().closeFuture().syncUninterruptibly();
			channelFuture = null;
		}
	}
	
	public Object getServerInstance()
	{
		return serverInstance;
	}
}
