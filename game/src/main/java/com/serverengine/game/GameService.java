package com.serverengine.game;

import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.MailBox;
import com.serverengine.base.ServerInfo;
import com.serverengine.entities.EntityMgr;
import com.serverengine.entities.ServerEntity;
import com.serverengine.log.Log;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;
import com.serverengine.serverinterface.IGameClient;
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
	
	public void handleServerMessage(MailBox caller, MailBox target,
			String methodName, Object... args) {
		ServerEntity entity = EntityMgr.getEntity(target.getEntityId());
		if (entity == null)
		{
			Log.error("实体不存在！entityId=" + target.getEntityId());
			return;
		}
		
		entity.onServerCall(methodName, args);
	}
	
	public void handleClientMessage(RpcChannel channel, String entityId, byte[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 客户端断开了连接
	 */
	public void handleClientDisconnected(String entityId) {
		ServerEntity entity = EntityMgr.getEntity(entityId);
		if (entity != null)
		{
			entity.onLostClient();
		}
	}

	public void connectServer(RpcChannel channel, ServerInfo gateInfo,
			CallbackObj callback) {
		BootstrapEntity bootstrapEntity = EntityMgr.createEntity("com.serverengine.game.BootstrapEntity", null);
		if (bootstrapEntity == null)
		{
			callback.done(false, "");
			return;
		}
		
		callback.done(true, bootstrapEntity.getEntityId());
	}
	
	/**
	 * 根据网关地址，获取逻辑服客户端
	 * 
	 * @param gateAddr
	 * @return
	 */
	public IGameClient getGameClient(ServerInfo gateAddr)
	{
		RpcChannel channel = allGates.get(gateAddr);
		if (channel == null)
		{
			Log.error("网关服连接已断开，addr=" + gateAddr);
			return null;
		}
		
		return channel.getClientObj();
	}  
	
	public static GameService getInstance() {
		return gameService;
	}
}
