package com.serverengine.rpc;

/**
 * 通信消息结构体
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class Message {

	/**
	 * 消息长度
	 */
	private int messageLength;
	
	/**
	 * 消息内容
	 */
	private byte[] messageData;

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public void setMessageData(byte[] messageData) {
		this.messageData = messageData;
	}
	
	/**
	 * 创建一个消息
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static Message bulidMessage(byte[] args)
	{
		Message msg = new Message();
		msg.setMessageData(args);
		msg.setMessageLength(args.length + Integer.SIZE);
		return msg;
	}
}
