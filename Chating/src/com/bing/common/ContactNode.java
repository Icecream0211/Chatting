package com.bing.common;

public class ContactNode {
	private String displayname;
	private String ip;
	private int tcpport;

	private String hostname;
	private String nickname;
	private int nodetype;//0.父级node 1.子级node
	public String getDisplayname() {
		return displayname;
	}
	
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public int getNodetype() {
		return nodetype;
	}

	public void setNodetype(int nodetype) {
		this.nodetype = nodetype;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTcpport() {
		return tcpport;
	}

	public void setTcpport(int tcpport) {
		this.tcpport = tcpport;
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

	public ContactNode(String ip, int tcpport, int type) {
		this.tcpport = tcpport;
		this.ip = ip;
		this.nodetype = type;
		this.displayname = ip;
	}
	
	public ContactNode(int type,String displayname){
		this.nodetype = type;
		this.displayname = displayname;
	}

	public String toString() {
		return displayname;
	}
}
