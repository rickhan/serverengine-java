package com.serverengine.game;

import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;
import com.serverengine.serverinterface.IGameService;

/**
 * 逻辑服函数
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class GameService implements IGameService {
	/**
	 * 单例
	 */
	private static GameService gameService;
	
	/**
	 * 已注册的网关
	 */
	private ConcurrentHashMap<ServerInfo, RpcChannel> allGates;
	
	/**
	 * 连接对应的信息
	 */
	private ConcurrentHashMap<RpcChannel, ServerInfo> channelToServerInfos;
	
	public GameService() {
		gameService = this;
		allGates = new ConcurrentHashMap<ServerInfo, RpcChannel>();
		channelToServerInfos = new ConcurrentHashMap<RpcChannel, ServerInfo>();
	}

	public void registerGate(RpcChannel channel, ServerInfo gateInfo, CallbackObj callback) {
		allGates.put(gateInfo, channel);
		channelToServerInfos.put(channel, gateInfo);
		callback.done(channel, true);
		
		Log.info("新网关注册， gateInfo=" + gateInfo);
	}

	public void handleClientMessage(RpcChannel channel, String entityId, byte[] args) {
		// TODO Auto-generated method stub

	}

	public void handleClientDisconnected(String entityId) {
		// TODO Auto-generated method stub

	}

	public void handleServerMessage(ServerInfo from, ServerInfo to, String entityId, byte[] args) {
		// TODO Auto-generated method stub

	}

	public static GameService getInstance() {
		return gameService;
	}
}
