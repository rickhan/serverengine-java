package com.serverengine.gate;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;
import com.serverengine.rpc.RpcClient;
import com.serverengine.serverinterface.IGameMgrClient;
import com.serverengine.serverinterface.IGameService;

/**
 * 管理服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class GameMgrClient implements IGameMgrClient {
	/**
	 * 单例
	 */
	private static GameMgrClient instance;

	private static ConcurrentHashMap<ServerInfo, RpcClient> games;

	public GameMgrClient() {
		instance = this;
		games = new ConcurrentHashMap<ServerInfo, RpcClient>();
	}

	public void recevieGameList(ArrayList<ServerInfo> gameList) {
		connectGames(gameList);
	}

	public void forceShutDown() {
		App.getInstance().stop();
	}

	public void connectGames(ArrayList<ServerInfo> gameList) {
		for (final ServerInfo game : gameList) {
			if (games.contains(game)) {
				continue;
			}

			try {
				RpcClient client = new RpcClient(game.getHost(), game.getPort(), GameClient.class, IGameService.class,
						new Runnable() {

							public void run() {
								RpcClient temp = games.get(game);
								if (temp == null) {
									Log.error("无法建立连接 game=" + game);
									return;
								}

								IGameService service = temp.getClient();
								service.registerGate(temp.getChannel(), AppStartupInfo.currentServerInfo(),
										new CallbackObj() {
											@SuppressWarnings("unused")
											public void onCallback(RpcChannel channel, boolean result) {
												if (result == false) {
													Log.error("注册失败！");
													System.exit(1);
												}
											}
										});
							}
						});
				games.put(game, client);
				client.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static GameMgrClient getInstance() {
		return instance;
	}

	public void receiveDBProxyList(ArrayList<ServerInfo> dbList) {
		assert(false);
	}
}
