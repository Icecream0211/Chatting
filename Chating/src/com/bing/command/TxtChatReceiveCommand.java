package com.bing.command;

import java.nio.channels.SocketChannel;

import net.sf.json.JSONObject;

import com.bing.common.ChatsPool;
import com.bing.common.TxtChatingInstance;

public class TxtChatReceiveCommand implements ICommand {
	// new TxtChat request Cammand
	public static final int ACTION_CODE = 3;// 0x03
	private String dest_ip;
	private int dest_lis_port;
	private int dest_con_port;
	private SocketChannel socketChannel;
	private JSONObject obj;

	public TxtChatReceiveCommand(SocketChannel socketChannel, JSONObject jsonObj) {
		this.socketChannel = socketChannel;
		this.setDest_ip(jsonObj.getString("ip"));
		this.setDest_lis_port(jsonObj.getInt("lis_port"));
		this.setDest_con_port(socketChannel.socket().getPort());
		this.obj = jsonObj;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void exec() {
		TxtChatingInstance chatIns;
		chatIns = (TxtChatingInstance) ChatsPool.getInstance().getChatIns(
				"3_" + this.dest_ip + "_" + this.dest_lis_port);
		if (null == chatIns) {
			chatIns = new TxtChatingInstance(this.dest_ip, dest_lis_port,
					dest_con_port);
			chatIns.iniTxtComponents(socketChannel);
			ChatsPool.getInstance().addChatIns(chatIns.getIdentify(), chatIns);
		}
	}

	public String getDest_ip() {
		return dest_ip;
	}

	public void setDest_ip(String dest_ip) {
		this.dest_ip = dest_ip;
	}

	public int getDest_lis_port() {
		return dest_lis_port;
	}

	public void setDest_lis_port(int dest_lis_port) {
		this.dest_lis_port = dest_lis_port;
	}

	public int getDest_con_port() {
		return dest_con_port;
	}

	public void setDest_con_port(int dest_con_port) {
		this.dest_con_port = dest_con_port;
	}
}
