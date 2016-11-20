package com.serverengine.dbproxy;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcClient;
import com.serverengine.rpc.RpcServer;
import com.serverengine.serverinterface.IDbClient;
import com.serverengine.serverinterface.IGameMgrService;

/**
 * db代理
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class DbProxy extends App {
	public DbProxy(String[] args) throws Exception {
		super(args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean init() {
		ServerInfo gamemanagerInfo = AppStartupInfo.gameManagerInfo();
		final ServerInfo currentServerInfo = AppStartupInfo.currentServerInfo();
		try {
			gamemanagerClient = new RpcClient(gamemanagerInfo.getHost(), gamemanagerInfo.getPort(), GameMgrClient.class,
					IGameMgrService.class, new Runnable() {

						public void run() {
							IGameMgrService service = gamemanagerClient.getClient();
							service.registerDBProxy(gamemanagerClient.getChannel(), currentServerInfo,
									new CallbackObj() {
										@SuppressWarnings("unused")
										public void onCallback(boolean result) {
											Log.info("注册结果返回result=" + result);
											if (result == false) {
												System.exit(1);
											}
										}
									});
						}

					});
			gamemanagerClient.start();

			mainServer = new RpcServer(currentServerInfo.getHost(), currentServerInfo.getPort(), DbService.class,
					IDbClient.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		gamemanagerClient.close();
		mainServer.close();
		getMainGroup().shutdownGracefully();
		return true;
	}

	public static void main(String[] args) throws Exception {
		new DbProxy(args);
	}
}
