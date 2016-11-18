package com.serverengine.gamemanager;

import com.serverengine.base.App;
import com.serverengine.rpc.RpcClient;
import com.serverengine.serverinterface.IGameMgrService;

/**
 * 管理工具
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class AdminTool extends App {

	private RpcClient gameMgrClient;

	public AdminTool(String[] args) throws Exception {
		super(args);
	}

	@Override
	public boolean init() {
		try {
			gameMgrClient = new RpcClient("127.0.0.1", 8888,
					AdminGameMgrClient.class, IGameMgrService.class);
			gameMgrClient.start();
			while (gameMgrClient.getClient() == null);
			IGameMgrService service = gameMgrClient.getClient();
			service.registerAdmin(gameMgrClient.getChannel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "servers.xml", "gamemanager" };
		new AdminTool(args);
	}
}
