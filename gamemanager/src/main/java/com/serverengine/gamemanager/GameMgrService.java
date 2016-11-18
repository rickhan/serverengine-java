package com.serverengine.gamemanager;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.ServerInfo;
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
	private ConcurrentHashMap<ServerInfo, RpcChannel> infoToChannels = new ConcurrentHashMap<ServerInfo, RpcChannel>();
	private IGameMgrClient adminClient;

	public void registerGame(RpcChannel channel, ServerInfo gameInfo) {
		gameClients.put(channel, gameInfo);
		infoToChannels.put(gameInfo, channel);
		
		ArrayList<ServerInfo> gameList = new ArrayList<ServerInfo>();
		gameList.add(gameInfo);
		for (RpcChannel gate : gateClients.keySet())
		{
			IGameMgrClient gateClient = gate.getClientObj();
			gateClient.recevieGameList(gameList);
		}
		
		System.out.println("regsterGame, gameInfo=" + gameInfo);
	}

	public void registerGate(RpcChannel channel, ServerInfo gateInfo) {
		infoToChannels.put(gateInfo, channel);
		gateClients.put(channel, gateInfo);
	}

	public void registerAdmin(RpcChannel channel) {
		IGameMgrClient registerClient = channel.getClientObj();
		if (adminClient != null) {
			registerClient.onRegisterAdmin(false);
			System.out.println("registerAdmin failed!");
			return;
		}
		
		registerClient.onRegisterAdmin(true);
		adminClient = registerClient;
		System.out.println("registerAdmin succeed!");
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

}
