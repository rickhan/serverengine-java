package com.serverengine.gamemanager;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcClient;
import com.serverengine.serverinterface.IGameMgrService;

/**
 * 管理工具
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class AdminTool extends App {
	
	public AdminTool(String[] args) throws Exception {
		super(args);
	}

	@Override
	public boolean init() {
		try {
			ServerInfo serverInfo = AppStartupInfo.gameManagerInfo();
			gamemanagerClient = new RpcClient(serverInfo.getHost(), serverInfo.getPort(), AdminGameMgrClient.class, IGameMgrService.class,
					new Runnable() {

						public void run() {
							IGameMgrService service = getGameMgrService();
							service.registerAdmin(gamemanagerClient.getChannel(), new CallbackObj() {
								
								@SuppressWarnings("unused")
								public void onCallback(boolean result) {
									Log.error("操作结果返回=" + result);

									if (result == false) {
										Log.error("注册失败，可能有多人同时在进行!");
										System.exit(1);
									}
									
									Log.info("连接gamemanager成功！");
								}
							});
						}
					});
			gamemanagerClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stop() {
		App.getInstance().getMainGroup().shutdownGracefully();
		return true;
	}

	public static void main(String[] args) throws Exception {
		//args = new String[] { "servers.xml", "gamemanager" };
		new AdminTool(args);
	}
}
