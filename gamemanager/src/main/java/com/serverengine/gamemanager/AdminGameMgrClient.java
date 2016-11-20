package com.serverengine.gamemanager;

import java.util.ArrayList;

import com.serverengine.base.ServerInfo;
import com.serverengine.serverinterface.IGameMgrClient;

/**
 * 管理工具客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class AdminGameMgrClient implements IGameMgrClient {

	public void recevieGameList(ArrayList<ServerInfo> gameList) {
		assert(false);
	}

	public void forceShutDown() {
		
	}

	public void receiveDBProxyList(ArrayList<ServerInfo> dbList) {
		assert(false);
	}

}
