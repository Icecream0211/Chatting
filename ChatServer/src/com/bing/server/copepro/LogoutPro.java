package com.bing.server.copepro;

import java.nio.channels.SocketChannel;

import net.sf.json.JSONObject;

import com.bing.server.utility.ClientInstance;
import com.bing.server.utility.ClientPool;

public class LogoutPro extends AProcedure {
	private JSONObject jsonobject;

	public LogoutPro(SocketChannel client) {
		super(client);
	}

	public LogoutPro(JSONObject obj, SocketChannel client) {
		this(client);
		this.jsonobject = obj;
	}

	@Override
	public void exec() {
		ClientInstance ins = new ClientInstance(jsonobject.getString("ip"),
				jsonobject.getInt("lis_port"));
		System.out.println("remove before "
				+ ClientPool.getInstance().getAllLists().size());
		removeClient(ins);
		System.out.println("remove after "
				+ ClientPool.getInstance().getAllLists().size());
		new UserExitNotificationPro(null, ins).start();
	}

	private void removeClient(ClientInstance ins) {
		ClientPool.getInstance().removeClient(ins);
	}
}
