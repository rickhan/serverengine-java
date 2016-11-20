package com.serverengine.serverinterface;

import com.serverengine.base.ServerInfo;
import com.serverengine.rpc.CallbackObj;
import com.serverengine.rpc.RpcChannel;

/**
 * 数据服接口
 * 
 * @author rick
 *
 */
public interface IDbService {
	/**
	 * 注册逻辑服
	 * 
	 * @param channel
	 * @param gameInfo
	 * @param callback
	 */
	void registerGame(RpcChannel channel, ServerInfo gameInfo, CallbackObj callback);
	
	/**
	 * 获取数据
	 * 
	 * @param channel
	 * @param key
	 * @param callback
	 */
	void getData(RpcChannel channel, String key, CallbackObj callback);
	
	/**
	 * 保存数据
	 * 
	 * @param channel
	 * @param key
	 * @param datas
	 */
	void saveData(RpcChannel channel, String key, byte[] datas);
}
