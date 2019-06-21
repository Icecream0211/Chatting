package com.bing.server.copepro;

import java.nio.channels.SocketChannel;

import net.sf.json.JSONObject;

import com.bing.server.utility.ClientInstance;
import com.bing.server.utility.ClientPool;

public class LoginPro extends AProcedure {
	private JSONObject jsonobject;

	public LoginPro(SocketChannel client) {
		super(client);
	}

	public LoginPro(JSONObject obj, SocketChannel client) {
		this(client);
		this.jsonobject = obj;
	}

	@Override
	public void exec() {
		System.out.println("login procedure....");
		ClientInstance ins = new ClientInstance(jsonobject.getString("ip"),
				jsonobject.getInt("lis_port"));
		String returnString = "";
		ins.setSocketChannel(super.getSocketChannel());
		ins.setHostname(jsonobject.getString("hostname"));
		ins.setNickname(jsonobject.getString("nickname"));
		addClient(ins);
		System.out.println("add instance ....");
		returnString = "{status:1,action:1}";
		super.sendBack(returnString);
		System.out.println("send back ....");
		new UserAddNotificationPro(null,ins).start();
	}

	private void addClient(ClientInstance ins) {
		ClientPool.getInstance().addClient(ins);
	}
}
