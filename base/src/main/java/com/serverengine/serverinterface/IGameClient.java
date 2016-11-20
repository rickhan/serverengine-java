package com.serverengine.serverinterface;

import com.serverengine.base.ServerInfo;

/**
 * 逻辑服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGameClient {
	/**
	 * 转发逻辑服之间的通信
	 * 
	 * @param from
	 * @param to
	 * @param entityId
	 * @param args
	 */
	void forwardServerMessage(ServerInfo from, ServerInfo to, String entityId, byte[] args);
	
	/**
	 * 发消息到客户端
	 * 
	 * @param entityId
	 * @param args
	 */
	void sendMessageToClient(String entityId, byte[] args);
}
