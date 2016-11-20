package com.serverengine.gate;

import java.util.ArrayList;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcClient;
import com.serverengine.rpc.RpcServer;
import com.serverengine.serverinterface.IGameMgrService;
import com.serverengine.serverinterface.IGateClient;

/**
 * 网关服
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class Gate extends App {
	public Gate(String[] args) throws Exception {
		super(args);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean init() {
		final ServerInfo currentServerInfo = AppStartupInfo.currentServerInfo();
		ServerInfo gamemanagerInfo = AppStartupInfo.gameManagerInfo();
		try {
			gamemanagerClient = new RpcClient(gamemanagerInfo.getHost(), gamemanagerInfo.getPort(), GameMgrClient.class,
					IGameMgrService.class, new Runnable() {

						public void run() {
							IGameMgrService service = gamemanagerClient.getClient();
							service.registerGate(gamemanagerClient.getChannel(), currentServerInfo, new CallbackObj() {

								@SuppressWarnings("unused")
								public void onCallback(ArrayList<ServerInfo> gameList) {
									Log.info("连接管理服返回：gameList=" + gameList);
									GameMgrClient.getInstance().connectGames(gameList);
								}
							});
						}
					});
			gamemanagerClient.start();

			mainServer = new RpcServer("0.0.0.0", currentServerInfo.getPort(), GateService.class,
					IGateClient.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		mainServer.close();
		gamemanagerClient.close();
		getMainGroup().shutdownGracefully();
		return true;
	}

	public static void main(String[] args) throws Exception {
		new Gate(args);
	}
}
