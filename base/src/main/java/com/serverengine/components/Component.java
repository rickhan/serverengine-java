package com.serverengine.components;

import com.serverengine.entities.ServerEntity;

/**
 * 组件实体
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public abstract class Component {
	/**
	 * 组件的拥有者
	 */
	private final ServerEntity owner;
	
	public Component(ServerEntity owner)
	{
		this.owner = owner;
	}
	
	/**
	 * 初始化
	 * 
	 * @return
	 */
	public abstract boolean init();
	
	/**
	 * 开始工作
	 * 
	 * @return
	 */
	public abstract boolean start();
	
	/**
	 * 停止工作
	 * 
	 * @return
	 */
	public abstract boolean stop();
	
	/**
	 * 回收
	 */
	public abstract void release();
	
	/**
	 * 从数据库加载数据
	 */
	public abstract void loadFromDB();
	
	/**
	 * 保存数据到数据库
	 */
	public abstract void saveToDB();

	/**
	 * getOwner
	 * 
	 * @return
	 */
	public ServerEntity getOwner() {
		return owner;
	}
}
