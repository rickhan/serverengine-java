package com.serverengine.serverinterface;

import com.serverengine.base.ServerInfo;
import com.serverengine.rpc.RpcChannel;

/**
 * 服务器管理器
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGameMgrService {
	
	/**
	 * 注册逻辑服
	 * 
	 * @param channel
	 * @param serverInfo
	 */
	void registerGame(RpcChannel channel, ServerInfo gameInfo);
	
	/**
	 * 注册网关服
	 * 
	 * @param channel
	 * @param gateInfo
	 */
	void registerGate(RpcChannel channel, ServerInfo gateInfo);
	
	/**
	 * 注册管理员
	 * 
	 * @param channel
	 */
	void registerAdmin(RpcChannel channel);
	
	/**
	 * 连接断开时调用
	 * 
	 * @param channel
	 */
	void handleConnectionClosed(RpcChannel channel);
}
