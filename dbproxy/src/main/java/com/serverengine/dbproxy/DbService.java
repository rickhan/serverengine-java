package com.serverengine.dbproxy;

import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.base.ServerInfo;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;
import com.serverengine.serverinterface.IDbService;

/**
 * 数据服
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class DbService implements IDbService{

	private static DbService instance;
	
	/**
	 * 逻辑服列表
	 */
	private ConcurrentHashMap<ServerInfo, RpcChannel> games;
	
	public DbService() {
		games = new ConcurrentHashMap<ServerInfo, RpcChannel>();
		instance = this;
	}
	
	public void registerGame(RpcChannel channel, ServerInfo gameInfo, CallbackObj callback) {
		games.put(gameInfo, channel);
		callback.done(true);
	}

	public void getData(RpcChannel channel, String key, CallbackObj callback) {
		// TODO Auto-generated method stub
		
	}

	public void saveData(RpcChannel channel, String key, byte[] datas) {
		// TODO Auto-generated method stub
		
	}

}
