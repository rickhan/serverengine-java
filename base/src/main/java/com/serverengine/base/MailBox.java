package com.serverengine.base;

/**
 * 信箱
 * 
 * @author rick <rick.han@yahoo.com>
 *
 */
public class MailBox {
	/**
	 * 实体id
	 */
	private String entityId;
	
	/**
	 * gate addr
	 */
	private ServerInfo gate;
	
	/**
	 * game addr
	 */
	private ServerInfo game;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public ServerInfo getGate() {
		return gate;
	}

	public void setGate(ServerInfo gate) {
		this.gate = gate;
	}

	public ServerInfo getGame() {
		return game;
	}

	public void setGame(ServerInfo game) {
		this.game = game;
	}
}
