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
	public static boolean addEntity(ServerEntity entity) {
		if (entities.containsKey(entity.getEntityId())) {
			Log.error("存在相同id的实体 id=" + entity.getEntityId() + " type="
					+ entity.getClass());
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
	public static void removeEntity(String id) {
		entities.remove(id);
	}

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	public static ServerEntity getEntity(String id) {
		return entities.get(id);
	}
	
	/**
	 * 创建实体
	 * 
	 * @param className
	 * @param entityId
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ServerEntity> T createEntity(String className, String entityId) {
		try {
			Class<? extends ServerEntity> entity = (Class<? extends ServerEntity>) Class
					.forName(className);
			ServerEntity serverEntity = entity.getDeclaredConstructor(String.class).newInstance(entityId);
			serverEntity.loadFromDB();
			return (T)serverEntity;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
