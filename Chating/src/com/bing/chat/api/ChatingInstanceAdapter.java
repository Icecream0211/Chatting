package com.bing.chat.api;

public abstract class ChatingInstanceAdapter implements IChatingInstance {
	private String identify;
	private String dest_ip;
	private int dest_lis_port = -1;
	private int dest_con_port = -1;
	private int chartype = -1;

	public ChatingInstanceAdapter(String dest_ip, int dest_lis_port,
			int dest_con_port, int chattype) {
		this.setDest_ip(dest_ip);
		this.setDest_lis_port(dest_lis_port);
		this.setDest_con_port(dest_con_port);
		this.setChartype(chattype);
	}

	public int getChartype() {
		return chartype;
	}

	public void setChartype(int chartype) {
		this.chartype = chartype;
		makeIdentify();
	}

	protected String getDest_ip() {
		return dest_ip;
	}

	protected void setDest_ip(String dest_ip) {
		this.dest_ip = dest_ip;
		makeIdentify();
	}

	protected int getDest_lis_port() {
		return dest_lis_port;
	}

	protected void setDest_lis_port(int dest_lis_port) {
		this.dest_lis_port = dest_lis_port;
		makeIdentify();
	}

	protected int getDest_con_port() {
		return dest_con_port;
	}

	protected void setDest_con_port(int dest_con_port) {
		this.dest_con_port = dest_con_port;
		makeIdentify();
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	@Override
	public abstract void sendMsg(String msg);
	public void makeIdentify() {
		if (this.dest_ip.equals("") || this.dest_ip == null
				|| this.dest_lis_port == -1 ||this.chartype==-1)
			return;
		this.identify = this.chartype + "_" + this.dest_ip + "_"
				+ this.dest_lis_port;
	}
}
