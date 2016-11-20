package com.serverengine.gamemanager;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.App;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;
import com.serverengine.serverinterface.IGameMgrClient;
import com.serverengine.serverinterface.IGameMgrService;

/**
 * 管理服的服务函数
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class GameMgrService implements IGameMgrService {

	private ConcurrentHashMap<RpcChannel, ServerInfo> gameClients = new ConcurrentHashMap<RpcChannel, ServerInfo>();
	private ConcurrentHashMap<RpcChannel, ServerInfo> gateClients = new ConcurrentHashMap<RpcChannel, ServerInfo>();
	private ConcurrentHashMap<RpcChannel, ServerInfo> dbClients = new ConcurrentHashMap<RpcChannel, ServerInfo>();
	private ConcurrentHashMap<ServerInfo, RpcChannel> infoToChannels = new ConcurrentHashMap<ServerInfo, RpcChannel>();
	private IGameMgrClient adminClient;

	public void registerGame(RpcChannel channel, ServerInfo gameInfo, CallbackObj callback) {
		gameClients.put(channel, gameInfo);
		infoToChannels.put(gameInfo, channel);

		ArrayList<ServerInfo> gameList = new ArrayList<ServerInfo>();
		gameList.add(gameInfo);
		for (RpcChannel gate : gateClients.keySet()) {
			IGameMgrClient gateClient = gate.getClientObj();
			gateClient.recevieGameList(gameList);
		}

		ArrayList<ServerInfo> dbProxyList = new ArrayList<ServerInfo>();
		for (ServerInfo db : dbClients.values()) {
			dbProxyList.add(db);
		}

		callback.done(dbProxyList);
		Log.info("regsterGame, gameInfo=" + gameInfo);
	}

	public void registerGate(RpcChannel channel, ServerInfo gateInfo, CallbackObj callback) {
		infoToChannels.put(gateInfo, channel);
		gateClients.put(channel, gateInfo);

		ArrayList<ServerInfo> gameList = new ArrayList<ServerInfo>();
		for (ServerInfo info : gameClients.values()) {
			gameList.add(info);
		}

		callback.done(gameList);
	}

	public void registerDBProxy(RpcChannel channel, ServerInfo dbInfo, CallbackObj callback) {
		dbClients.put(channel, dbInfo);
		callback.done(true);

		ArrayList<ServerInfo> dbProxyList = new ArrayList<ServerInfo>();
		dbProxyList.add(dbInfo);

		for (RpcChannel game : gameClients.keySet()) {
			IGameMgrClient client = game.getClientObj();
			client.receiveDBProxyList(dbProxyList);
		}
	}

	public void registerAdmin(RpcChannel channel, CallbackObj callback) {
		IGameMgrClient registerClient = channel.getClientObj();
		if (adminClient != null) {
			// registerClient.onRegisterAdmin(false);
			callback.done(false);
			Log.error("registerAdmin failed!");
			return;
		}

		// registerClient.onRegisterAdmin(true);
		callback.done(true);
		adminClient = registerClient;
		Log.info("registerAdmin succeed!");
	}

	public void handleConnectionClosed(RpcChannel channel) {
		ServerInfo game = gameClients.remove(channel);
		if (game != null) {
			infoToChannels.remove(game);
		} else {
			ServerInfo gate = gateClients.remove(channel);
			if (gate != null) {
				infoToChannels.remove(gate);
			}
		}
	}

	public void shutDownAll(RpcChannel channel, CallbackObj callback) {
		if (adminClient != (IGameMgrClient) channel.getClientObj()) {
			Log.error("同时有多人偿试关进程！");
			callback.done(false);
			return;
		}

		for (RpcChannel connectedChannel : infoToChannels.values()) {
			IGameMgrClient client = connectedChannel.getClientObj();
			client.forceShutDown();
		}

		// adminClient.forceShutDown();
		callback.done(true);

		App.getInstance().stop();
	}
}
