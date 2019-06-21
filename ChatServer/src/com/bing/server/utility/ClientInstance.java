package com.bing.server.utility;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ClientInstance {
	private String identify;
	private String ip;
	private int tcpport = -1;
	private int lis_port = -1;
	private String hostname;
	private String nickname = "No";
	private SocketChannel socketChannel;

	public ClientInstance(String ip, int port) {
		this.tcpport = port;
		this.lis_port = port;
		this.ip = ip;
		this.identify = ip + "_" + tcpport;
	}

	public ClientInstance() {
	}

	public int getLis_port() {
		return lis_port;
	}

	public void setLis_port(int lis_port) {
		this.lis_port = lis_port;
		this.tcpport = lis_port;
		madeIden();
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		madeIden();
	}

	public int getTcpport() {
		return tcpport;
	}

	public void setTcpport(int tcpport) {
		this.tcpport = tcpport;
		this.lis_port = tcpport;
		madeIden();
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	private void madeIden() {
		if (ip == null || ip.equals("") || tcpport == -1) {
			return;
		}
		this.identify = ip + "_" + tcpport;
	}

	public void close() {
		try {
			if (this.socketChannel.isOpen()) {
				this.socketChannel.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
