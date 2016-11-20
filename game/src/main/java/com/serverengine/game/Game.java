package com.serverengine.game;

import java.util.ArrayList;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcClient;
import com.serverengine.rpc.RpcServer;
import com.serverengine.serverinterface.IGameClient;
import com.serverengine.serverinterface.IGameMgrService;

/**
 * 游戏逻辑服
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class Game extends App {
	public Game(String[] args) throws Exception {
		super(args);
	}

	@Override
	public boolean init() {
		ServerInfo currentServerInfo = AppStartupInfo.currentServerInfo();
		ServerInfo gamemanagerInfo = AppStartupInfo.gameManagerInfo();
		try {
			mainServer = new RpcServer(currentServerInfo.getHost(), currentServerInfo.getPort(), GameService.class, IGameClient.class);
			
			gamemanagerClient = new RpcClient(gamemanagerInfo.getHost(), gamemanagerInfo.getPort(), GameMgrClient.class,
					IGameMgrService.class, new Runnable() {

						public void run() {
							IGameMgrService service = gamemanagerClient.getClient();
							service.registerGame(gamemanagerClient.getChannel(), AppStartupInfo.currentServerInfo(),
									new CallbackObj() {
										@SuppressWarnings("unused")
										public void onCallback(ArrayList<ServerInfo> dbProxyList) {
											Log.info("逻辑服注册成功 dblist=" + dbProxyList);
											GameMgrClient.getInstance().connectDbProxys(dbProxyList);
										}
									});
						}
					});
			gamemanagerClient.start();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		App.getInstance().getMainGroup().shutdownGracefully();
		return true;
	}

	public static void main(String[] args) throws Exception {
		new Game(args);
	}
}
