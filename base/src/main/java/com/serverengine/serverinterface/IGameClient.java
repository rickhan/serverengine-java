package com.serverengine.serverinterface;

import com.serverengine.base.MailBox;

/**
 * 逻辑服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGameClient {
	/**
	 * 转发逻辑服之间的消息
	 * 
	 * @param caller
	 * @param target
	 * @param methodName
	 * @param args
	 */
	void forwardServerMessage(MailBox caller, MailBox target, String methodName, Object... args);
	
	/**
	 * 发消息到客户端
	 * 
	 * @param entityId
	 * @param args
	 */
	void sendMessageToClient(String entityId, byte[] args);
}
