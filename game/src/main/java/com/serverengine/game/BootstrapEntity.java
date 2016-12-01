package com.serverengine.game;

import com.serverengine.annoations.CLIENT;
import com.serverengine.annoations.RPC;
import com.serverengine.components.Component;
import com.serverengine.entities.ServerEntity;
import com.serverengine.rpc.CallbackObj;

/**
 * 客户端建立连接后创建的第一个entity
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class BootstrapEntity extends ServerEntity {

	class LoginComponent extends Component {

		public LoginComponent(ServerEntity owner) {
			super(owner);
		}

		@Override
		public boolean init() {
			return true;
		}

		@Override
		public boolean start() {
			return true;
		}

		@Override
		public boolean stop() {
			return true;
		}

		@Override
		public void release() {
			// NOTHING TO DO
		}

		@Override
		public void loadFromDB() {
			// NOTHING TO DO
		}

		@Override
		public void saveToDB() {
			// NOTHING TO DO
		}
		
		/**
		 * 登录、帐号验证
		 * 
		 * @param account
		 * @param password
		 * @param callback
		 */
		@RPC
		public void login(String account, String password, CallbackObj callback)
		{
			// TODO: add login logic here
		}
		
		/**
		 * 客户端函数
		 * 
		 * @param msg
		 */
		@CLIENT
		public void receiveTextMsg(String msg)
		{
			// 不要在此处添加任何代码
		}
		
	}

	public BootstrapEntity(String id) {
		super(id);
		
		componentMgr.addComponent(LoginComponent.class);
	}

}
