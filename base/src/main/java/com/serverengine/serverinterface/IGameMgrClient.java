package com.serverengine.serverinterface;

import java.util.ArrayList;

import com.serverengine.base.ServerInfo;

/**
 * 管理服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public interface IGameMgrClient {
	
	/**
	 * 收取逻辑服列表
	 * 
	 * @param gameList
	 */
	void recevieGameList(ArrayList<ServerInfo> gameList);
	
	/**
	 * 收取数据代理服列表
	 * 
	 * @param dbList
	 */
	void receiveDBProxyList(ArrayList<ServerInfo> dbList);
	
	/**
	 * 强制关闭
	 */
	void forceShutDown();
}
