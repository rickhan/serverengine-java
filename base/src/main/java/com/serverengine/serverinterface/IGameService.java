package com.serverengine.serverinterface;

import com.serverengine.base.ServerInfo;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;

/**
 * 逻辑服接口
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGameService {
	/**
	 * 注册网关服
	 * 
	 * @param channel
	 * @param gateInfo
	 * @param callback
	 */
	void registerGate(RpcChannel channel, ServerInfo gateInfo, CallbackObj callback);
	
	/**
	 * 处理来自客户端的信息
	 * 
	 * @param channel
	 * @param entityId
	 * @param args
	 */
	void handleClientMessage(RpcChannel channel, String entityId, byte[] args);
	
	/**
	 * 客户端连接断开
	 * 
	 * @param entityId
	 */
	void handleClientDisconnected(String entityId);
	
	/**
	 * 来自服务器的消息
	 * 
	 * @param from
	 * @param to
	 * @param entityId
	 * @param args
	 */
	void handleServerMessage(ServerInfo from, ServerInfo to, String entityId, byte[] args);
}
