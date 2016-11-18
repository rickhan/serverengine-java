package com.serverengine.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * rpc通道
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class RpcChannel extends SimpleChannelInboundHandler<Message> {

	private SocketChannel socketChannel;
	private final MethodMgr serverMethods;
	private Object clientObj;
	private Object owner;

	public RpcChannel(final MethodMgr serverMethods, Object clientObj,
			SocketChannel socketChannel) {
		this.serverMethods = serverMethods;
		this.socketChannel = socketChannel;
		this.clientObj = clientObj;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		if (serverMethods.handleMessage(this, msg) == false) {
			// TODO: add log here!!
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		socketChannel = (SocketChannel) ctx.channel();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		socketChannel = null;
		serverMethods.handleEvent("handleConnectionClosed", this);

		if (owner instanceof RpcClient) {
			((RpcClient) owner).start();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public MethodMgr getServerMethod() {
		return serverMethods;
	}

	public void write(Message msg) {
		socketChannel.writeAndFlush(msg);
	}

	@SuppressWarnings("unchecked")
	public <T> T getClientObj() {
		return (T) clientObj;
	}

	public void setClientObj(Object clientObj) {
		this.clientObj = clientObj;
	}

	public Object getOwner() {
		return owner;
	}

	public void setOwner(Object owner) {
		this.owner = owner;
	}

}
