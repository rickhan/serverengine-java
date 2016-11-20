package com.serverengine.serverinterface;

import com.serverengine.base.ServerInfo;
import com.serverengine.rpc.CallbackObj;
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
	 * @param callback
	 */
	void registerGame(RpcChannel channel, ServerInfo gameInfo, CallbackObj callback);
	
	/**
	 * 注册网关服
	 * 
	 * @param channel
	 * @param gateInfo
	 * @param callback
	 */
	void registerGate(RpcChannel channel, ServerInfo gateInfo, CallbackObj callback);
	
	/**
	 * 注册数据代理服
	 * 
	 * @param channel
	 * @param dbInfo
	 * @param callback
	 */
	void registerDBProxy(RpcChannel channel, ServerInfo dbInfo, CallbackObj callback);
	
	/**
	 * 注册管理员
	 * 
	 * @param channel
	 */
	void registerAdmin(RpcChannel channel, CallbackObj callback);
	
	/**
	 * 连接断开时调用
	 * 
	 * @param channel
	 */
	void handleConnectionClosed(RpcChannel channel);
	
	/**
	 * 关闭所有的进程
	 * 
	 * @param channel
	 * @param callback
	 */
	void shutDownAll(RpcChannel channel, CallbackObj callback);
}
