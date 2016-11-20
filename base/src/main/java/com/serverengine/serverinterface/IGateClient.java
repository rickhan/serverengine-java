package com.serverengine.serverinterface;

/**
 * 网关客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGateClient {
	/**
	 * 发送消息
	 * 
	 * @param args
	 */
	void sendMessage(byte[] args);
}
