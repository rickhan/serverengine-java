package com.serverengine.base;

/**
 * 服务器信息
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class ServerInfo {
	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public int hashCode()
	{
		return (host + port).hashCode();
	}
	
	@Override
	public String toString()
	{
		return host + ":" + port;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (o == null || o.getClass() != getClass())
		{
			return false;
		}
		
		ServerInfo other = (ServerInfo)o;
		if (other.host.equals(host) && other.port == port)
		{
			return true;
		}
		
		return false;
	}
}
