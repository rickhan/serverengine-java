package com.serverengine.entities;

import java.util.UUID;

import com.serverengine.base.AppStartupInfo;
import com.serverengine.base.MailBox;
import com.serverengine.base.ServerInfo;
import com.serverengine.components.ComponentMgr;

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
}
