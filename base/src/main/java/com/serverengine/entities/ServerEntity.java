package com.serverengine.entities;

import java.util.UUID;

import com.serverengine.base.App;
import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.MailBox;
import com.serverengine.base.ServerInfo;
import com.serverengine.components.ComponentMgr;
import com.serverengine.serverinterface.IGameClient;

/**
 * 实体基类
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class ServerEntity {
	/**
	 * 实体id
	 */
	protected String entityId;
	
	/**
	 * 信箱
	 */
	protected MailBox mailBox;
	
	/**
	 * 组件管理器
	 */
	protected ComponentMgr componentMgr;
	
	public ServerEntity(String id)
	{
		if (id == null)
		{
			id = UUID.randomUUID().toString();
		}
		
		this.entityId = id;
		mailBox = new MailBox();
		mailBox.setEntityId(entityId);
		mailBox.setGame(AppStartupInfo.currentServerInfo());
		componentMgr = new ComponentMgr(this);
		EntityMgr.addEntity(this);
	}

	public void loadFromDB()
	{
		componentMgr.loadFromDB();
	}
	
	public void saveToDB()
	{
		componentMgr.saveToDB();
	}
	
	/**
	 * 与客户端断开了连接
	 */
	public void onLostClient()
	{
		mailBox.setGate(null); // 对应的网关信息，需要清空
	}
	
	public String getEntityId() {
		return entityId;
	}

	public MailBox getMailBox() {
		return mailBox;
	}

	public void setGateInfo(ServerInfo gate) {
		mailBox.setGate(gate);
	}
	
	public void destroy()
	{
		EntityMgr.removeEntity(entityId);
		componentMgr.release();
	}
	
	public void callServerMethod(MailBox target, String methodName, Object... args)
	{
		if (AppStartupInfo.isSameServer(target.getGame()))
		{
			ServerEntity targetEntity = EntityMgr.getEntity(target.getEntityId());
			if (targetEntity != null)
			{
				targetEntity.onServerCall(methodName, args);
			}
		}
		else 
		{
			IGameClient gameClient = App.getInstance().getGameClient(target.getGate());
			if (gameClient == null)
			{
				return;
			}
			
			gameClient.forwardServerMessage(mailBox, target, methodName, args);
		}
	}
	
	public void callClientMethod(String methodName, Object... args)
	{
		
	}
	
	/**
	 * 被服务端的其他entity调用
	 * 
	 * @param methodName
	 * @param args
	 */
	public void onServerCall(String methodName, Object... args)
	{
		componentMgr.onEntityMethod(methodName, args);
	}
	
	/**
	 * 被客户端调用
	 * 
	 * @param methodName
	 * @param args
	 */
	public void onClientCall(String methodName, Object... args)
	{
		// TODO: 在此处加上权限判断，有的函数是不允许客户端调用的
		componentMgr.onEntityMethod(methodName, args);
	}
}
