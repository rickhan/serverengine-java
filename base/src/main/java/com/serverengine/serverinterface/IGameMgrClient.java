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
	 * 注册逻辑服返回
	 * 
	 * @param result
	 */
	void onRegisterGame(boolean result);
	
	/**
	 * 注册网关服返回
	 * 
	 * @param result
	 */
	void onRegisterGate(boolean result);
	
	/**
	 * 注册管理员返回
	 * 
	 * @param result
	 */
	void onRegisterAdmin(boolean result);
	
	/**
	 * 收取逻辑服列表
	 * 
	 * @param gameList
	 */
	void recevieGameList(ArrayList<ServerInfo> gameList);
}
