package com.serverengine.gamemanager;

import com.serverengine.base.App;
import com.serverengine.rpc.RpcServer;
import com.serverengine.serverinterface.IGameMgrClient;

/**
 * 管理服
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class GameManager extends App {
	public GameManager(String[] args) throws Exception {
		super(args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean init() {
		try {
			mainServer = new RpcServer("127.0.0.1", 8888, GameMgrService.class,
					IGameMgrClient.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}
	

	public static void main(String[] args) throws Exception {
		args = new String[] {"servers.xml", "gamemanager"};
		new GameManager(args);
	}
}
