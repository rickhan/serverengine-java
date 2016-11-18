package com.serverengine.base;

import com.serverengine.rpc.RpcServer;

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

	public static App getInstance()
	{
		return instance;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param args
	 */
	public App(String[] args) throws Exception {
		mainGroup = new NioEventLoopGroup(Runtime.getRuntime()
				.availableProcessors());
		
		startArgs = args;
		instance = this;
		
		if (AppStartupInfo.initArgs(args) == false)
		{
			System.exit(1);
		}
		
		if (init() == false)
		{
			System.exit(1);
		}
	}
	
	public NioEventLoopGroup getMainGroup()
	{
		return mainGroup;
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
}
