package com.bing.controllee;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.bing.chat.api.ChatingInstanceAdapter;
import com.bing.chat.filesend.FileTransChannel;

public class SurveillanceInstance extends ChatingInstanceAdapter {

	public SurveillanceInstance(String dest_ip, int dest_lis_port,
			int dest_con_port, int chattype) {
		super(dest_ip, dest_lis_port, dest_con_port, chattype);
	}

	public SurveillanceInstance(String ip, int tcpport, int tcpport2) {
		this(ip, tcpport, tcpport2, 11);
	}

	private SurveillanceChannel channel;
	private boolean isRequester = false;
	private boolean isANC = false;

	public void initComponent(SocketChannel socketChannel) {
		try {
			this.channel = new SurveillanceChannel(this, socketChannel);
			if (socketChannel == null) {
				isRequester = true;
			}
			this.ANC();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ANC() {
		if (!isRequester && !this.isANC) {
			String command = "{status:1,action:11,con_port_o:"
					+ this.channel.getAnimationPort() + ",con_port_t:"
					+ this.channel.getAnimationCtlPort() + "}";
			this.sendMsg(command);
			this.isANC = true;
		}
	}

	@Override
	public String getDestIp() {
		return this.getDest_ip();
	}

	@Override
	public int getDestLisPort() {
		return this.getDest_lis_port();
	}

	@Override
	public int getDestConPort() {
		return this.getDest_con_port();
	}

	@Override
	public int getMyPort() {
		return 0;
	}

	@Override
	public void close() {
		this.channel.close();
	}

	@Override
	public void sendMsg(String msg) {
		try {
			this.channel.sendMsg(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
