package com.serverengine.gate;

import com.serverengine.base.MailBox;
import com.serverengine.base.ServerInfo;
import com.serverengine.log.Log;
import com.serverengine.serverinterface.IGameClient;
import com.serverengine.serverinterface.IGameService;

/**
 * 逻辑服客户端
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class GameClient implements IGameClient {

	public void forwardServerMessage(MailBox caller, MailBox target, String methodName, Object... args) {
		ServerInfo targetGame = target.getGame();
		IGameService gameService = GameMgrClient.getInstance().getGameService(targetGame);
		if (gameService == null) {
			Log.error("与逻辑服的连接已断开！addr=" + targetGame);
			return;
		}
		
		gameService.handleServerMessage(caller, target, methodName, args);
	}

	public void sendMessageToClient(String entityId, byte[] args) {
		// TODO Auto-generated method stub
		
	}

}
