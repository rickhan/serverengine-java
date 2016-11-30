package com.serverengine.base;

import com.serverengine.log.Log;
import com.serverengine.rpc.RpcClient;
import com.serverengine.rpc.RpcServer;
import com.serverengine.serverinterface.IGameClient;
import com.serverengine.serverinterface.IGameMgrService;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 应用程序基类
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public abstract class App {

	/**
	 * 实例
	 */
	private static App instance = null;

	/**
	 * 主线程池
	 */
	protected NioEventLoopGroup mainGroup;

	/**
	 * 启动参数
	 */
	protected String[] startArgs;

	/**
	 * rpc服务器，每个进程都可能有一个运行实例
	 */
	protected RpcServer mainServer;

	/**
	 * 管理服管户端，只有管理服此实例为空
	 */
	protected RpcClient gamemanagerClient;

	public static App getInstance() {
		return instance;
	}

	/**
	 * 构造函数
	 * 
	 * @param args
	 */
	public App(String[] args) throws Exception {
		mainGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		startArgs = args;
		instance = this;

		if (AppStartupInfo.initArgs(args) == false) {
			Log.error("初始化失败");
			System.exit(1);
		}

		Log.init();

		if (init() == false) {
			System.exit(1);
		}
	}

	public NioEventLoopGroup getMainGroup() {
		return mainGroup;
	}

	/**
	 * 获取管理服客户端
	 * 
	 * @return
	 */
	public IGameMgrService getGameMgrService() {
		return gamemanagerClient.getClient();
	}

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public abstract boolean init();

	/**
	 * 停止
	 * 
	 * @return
	 */
	public abstract boolean stop();
	
	/**
	 * 获取逻辑服客户端
	 * 
	 * @param addr
	 * @return
	 */
	public IGameClient getGameClient(ServerInfo addr)
	{
		assert(false);
		return null;
	}
}
