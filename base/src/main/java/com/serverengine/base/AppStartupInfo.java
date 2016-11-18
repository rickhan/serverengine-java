package com.serverengine.base;

/**
 * 进程启动信息
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class AppStartupInfo {
	/**
	 * 本进程的配置信息
	 */
	private static ServerInfo currentServerInfo;

	/**
	 * 进程启动名
	 */
	private static String serverName = "";

	public static boolean initArgs(String[] args) throws Exception {
		if (args == null || args.length < 2) {
			throw new Exception("参数太少，至少需要2个参数");
		}

		serverName = args[1];
		return true;
	}

	public static ServerInfo currentServerInfo() {
		return currentServerInfo;
	}

	public static String thisServerName() {
		return serverName;
	}
}
