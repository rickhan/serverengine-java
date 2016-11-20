package com.serverengine.base;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.serverengine.log.Log;

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

	/**
	 * 管理服信息
	 */
	private static ServerInfo gameManagerInfo;

	/**
	 * 所有的服务进程配置信息
	 */
	private static HashMap<String, ServerInfo> allServerInfos;

	static {
		allServerInfos = new HashMap<String, ServerInfo>();
	}

	/**
	 * 初始化启动信息
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean initArgs(String[] args) throws Exception {
		if (args == null || args.length < 2) {
			throw new Exception("参数太少，至少需要2个参数");
		}

		serverName = args[1];

		SAXReader reader = new SAXReader();
		Document document = reader.read(args[0]);
		Element root = document.getRootElement();
		List<Element> elements = root.elements();
		for (Element element : elements) {
			String name = element.getName();
			if (name.equals("GameManager")) {
				gameManagerInfo = new ServerInfo();
				gameManagerInfo.setHost(element.attributeValue("host"));
				gameManagerInfo.setPort(Integer.parseInt(element.attributeValue("port")));
			} else if (name.equals("Games")) {
				List<Element> games = element.elements();
				for (Element game : games) {
					String gameName = game.attributeValue("name");
					if (allServerInfos.containsKey(gameName)) {
						Log.error("存在重复的配置，逻辑服配置段=" + gameName);
						continue;
					}

					ServerInfo info = new ServerInfo();
					info.setHost(game.attributeValue("host"));
					info.setPort(Integer.parseInt(game.attributeValue("port")));
					allServerInfos.put(gameName, info);
				}
			} else if (name.equals("Gates")) {
				List<Element> gates = element.elements();
				for (Element gate : gates) {
					String gateName = gate.attributeValue("name");
					if (allServerInfos.containsKey(gateName)) {
						Log.error("存在重复的配置，网关服配置段=" + gateName);
						continue;
					}

					ServerInfo info = new ServerInfo();
					info.setHost(gate.attributeValue("host"));
					info.setPort(Integer.parseInt(gate.attributeValue("port")));
					allServerInfos.put(gateName, info);
				}
			} else if (name.equals("Dbs")) {
				List<Element> dbs = element.elements();
				for (Element db : dbs) {
					String dbName = db.attributeValue("name");
					if (allServerInfos.containsKey(db)) {
						Log.error("存在重复的配置，数据服配置段=" + dbName);
						continue;
					}

					ServerInfo info = new ServerInfo();
					info.setHost(db.attributeValue("host"));
					info.setPort(Integer.parseInt(db.attributeValue("port")));
					allServerInfos.put(dbName, info);
				}
			}
		}

		if (serverName.equals("gamemamanger")) {
			currentServerInfo = gameManagerInfo;
		} else {
			currentServerInfo = allServerInfos.get(serverName);
		}
		return true;
	}

	/**
	 * 当前的服务器信息
	 * 
	 * @return
	 */
	public static ServerInfo currentServerInfo() {
		return currentServerInfo;
	}

	/**
	 * 当前进程启动名
	 * 
	 * @return
	 */
	public static String currentServerName() {
		return serverName;
	}
	
	/**
	 * 管理服信息
	 * 
	 * @return
	 */
	public static ServerInfo gameManagerInfo()
	{
		return gameManagerInfo;
	}
}
