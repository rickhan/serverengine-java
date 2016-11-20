package com.serverengine.serverinterface;

import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;

/**
 * 网关服
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGateService {
	/**
	 * 连接服务器
	 * 
	 * @param channel
	 * @param deviceId
	 * @param callback
	 */
	void connectServer(RpcChannel channel, String deviceId, CallbackObj callback);
	
	/**
	 * 接收客户端消息
	 * 
	 * @param channel
	 * @param args
	 */
	void handleClientMessage(RpcChannel channel, byte[] args);
}
