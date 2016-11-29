package com.serverengine.entities;

import java.util.concurrent.ConcurrentHashMap;

import com.serverengine.log.Log;

/**
 * 实体管理器
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class EntityMgr {
	/**
	 * 所有的实体
	 */
	private static ConcurrentHashMap<String, ServerEntity> entities = new ConcurrentHashMap<String, ServerEntity>();

	/**
	 * 添加
	 * 
	 * @param entity
	 * @return
	 */
	public static boolean addEntity(ServerEntity entity)
	{
		if (entities.containsKey(entity.getEntityId()))
		{
			Log.error("存在相同id的实体 id=" + entity.getEntityId() + " type=" + entity.getClass());
			return false;
		}
		
		entities.put(entity.getEntityId(), entity);
		return true;
	}
	
	/**
	 * 移除
	 * 
	 * @param id
	 */
	public static void removeEntity(String id)
	{
		entities.remove(id);
	}
}
