package com.bing.chat.api;

public interface IChatingInstance {
	public void sendMsg(String msg);
	public String getDestIp();
	public int getDestLisPort();
	public int getDestConPort();
	public int getMyPort();
	public void close();
}
