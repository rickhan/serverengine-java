package com.serverengine.components;

import java.util.HashMap;

import com.serverengine.annoations.RPC;
import com.serverengine.entities.ServerEntity;
import com.serverengine.log.Log;
import com.serverengine.rpc.MethodMgr;

/**
 * 组件管理器
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class ComponentMgr {
	/**
	 * 组件列表
	 */
	private HashMap<Class<?>, Component> components;
	
	/**
	 * 拥有者
	 */
	private ServerEntity owner;
	
	/**
	 * 函数管理
	 */
	private MethodMgr methodMgr;
	
	public ComponentMgr(ServerEntity owner)
	{
		components = new HashMap<Class<?>, Component>();
		this.owner = owner;
		methodMgr = new MethodMgr();
	}

	/**
	 * 添加组件
	 * 
	 * @param componentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T addComponent(Class<? extends Component> componentType)
	{
		if (components.containsKey(componentType))
		{
			return null;
		}
		
		try {
			Component component = (Component)componentType.getDeclaredConstructor(ServerEntity.class).newInstance(owner);
			if (component == null) 
			{
				return null;
			}
			
			if (component.init() == false)
			{
				Log.error(componentType + ".init()==false");
				component.release();
				return null;
			}
			
			if (component.start() == false)
			{
				Log.error(componentType + ".start()==false");
				component.release();
				return null;
			}
			
			methodMgr.extractMethodsWithAnnotation(component, RPC.class);
			components.put(componentType, component);
			return (T) component;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<?> componentType)
	{
		return (T) components.get(componentType);
	}
	
	public void onEntityMethod(String methodName, Object... args)
	{
		methodMgr.handleEvent(methodName, args);
	}
	
	public ServerEntity getOwner() {
		return owner;
	}

	public void setOwner(ServerEntity owner) {
		this.owner = owner;
	}
	
	/**
	 * 停止所有
	 */
	public void stopAll()
	{
		for (Component component : components.values())
		{
			component.stop();
		}
	}
	
	/**
	 * release
	 */
	public void release()
	{
		stopAll();
		components.clear();
		this.owner = null;
		methodMgr.release();
		methodMgr = null;
	}
	
	public void loadFromDB()
	{
		for (Component component : components.values())
		{
			component.loadFromDB();
		}
	}
	
	public void saveToDB()
	{
		for (Component component : components.values())
		{
			component.saveToDB();
		}
	}
}
