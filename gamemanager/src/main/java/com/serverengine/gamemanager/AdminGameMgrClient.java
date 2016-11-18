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

	public void onRegisterGame(boolean result) {
		assert(false);
	}

	public void onRegisterGate(boolean result) {
		assert(false);
	}

	public void onRegisterAdmin(boolean result) {
		System.out.print("onRegisterAdmin result=" + result);
		System.exit(1);
	}

	public void recevieGameList(ArrayList<ServerInfo> gameList) {
		assert(false);
	}

}
