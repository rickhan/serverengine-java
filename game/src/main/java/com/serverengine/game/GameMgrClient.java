package com.serverengine.game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcClient;
import com.serverengine.serverinterface.IDbService;
import com.serverengine.serverinterface.IGameMgrClient;

/**
 * 管理服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class GameMgrClient implements IGameMgrClient {

	private static GameMgrClient instance;

	/**
	 * 数据代理服列表
	 */
	private ConcurrentHashMap<ServerInfo, RpcClient> dbProxyList;

	public GameMgrClient() {
		dbProxyList = new ConcurrentHashMap<ServerInfo, RpcClient>();
		instance = this;
	}

	public void recevieGameList(ArrayList<ServerInfo> gameList) {
		assert (false);
	}

	public void forceShutDown() {
		App.getInstance().stop();
	}

	public void receiveDBProxyList(ArrayList<ServerInfo> dbList) {
		connectDbProxys(dbList);
	}

	public void connectDbProxys(ArrayList<ServerInfo> dbList) {
		for (final ServerInfo db : dbList) {
			try {
				RpcClient client = new RpcClient(db.getHost(), db.getPort(), DbClient.class, IDbService.class,
						new Runnable() {

							public void run() {
								RpcClient temp = dbProxyList.get(db);
								if (temp == null) {
									Log.error("连接数据服失败!");
									return;
								}

								IDbService service = temp.getClient();
								service.registerGame(temp.getChannel(), AppStartupInfo.currentServerInfo(),
										new CallbackObj() {
											@SuppressWarnings("unused")
											public void onCallback(boolean result) {
												Log.error("连接数据服结果 result=" + result);
												if (result == false) {
													System.exit(1);
												}
												
												Log.info("连接数据服成功,db=" + db);
											}
										});
							}

						});
				dbProxyList.put(db, client);
				client.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static GameMgrClient getInstance() {
		return instance;
	}
}
