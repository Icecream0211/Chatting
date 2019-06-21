package com.bing.server.copepro;

import java.nio.channels.SocketChannel;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.bing.server.common.ClientInstanceProcesser;
import com.bing.server.utility.ClientInstance;
import com.bing.server.utility.ClientPool;

public class UserListPro extends AProcedure {
	private JSONObject jsonobject;

	public UserListPro(SocketChannel client) {
		super(client);
	}

	public UserListPro(JSONObject obj, SocketChannel client) {
		this(client);
		this.jsonobject = obj;
	}

	@Override
	public void exec() {
		System.out.println("userlist procedure....");
		ClientInstance ins = new ClientInstance(jsonobject.getString("ip"),
				jsonobject.getInt("lis_port"));
		String returnString = "";
		List<ClientInstance> userlist = ClientPool.getInstance()
				.getAllExceptMeLists(ins);
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(ClientInstance.class,
				new ClientInstanceProcesser());
		JSONArray arr = JSONArray.fromObject(userlist, config);
		returnString = "{status:1,action:8,list:" + arr.toString() + "}";
		System.out.println("returnStr--->" + returnString);
		super.sendBack(returnString);
	}
}
