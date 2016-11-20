package com.serverengine.dbproxy;

import java.util.ArrayList;

import com.serverengine.base.App;
import com.serverengine.base.ServerInfo;
import com.serverengine.serverinterface.IGameMgrClient;

public class GameMgrClient implements IGameMgrClient{

	public void recevieGameList(ArrayList<ServerInfo> gameList) {
		assert(false);
	}

	public void receiveDBProxyList(ArrayList<ServerInfo> dbList) {
		assert(false);
	}

	public void forceShutDown() {
		App.getInstance().stop();
	}

}
