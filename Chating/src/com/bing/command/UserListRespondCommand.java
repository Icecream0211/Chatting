package com.bing.command;

import com.bing.common.MainInstance;

import net.sf.json.JSONObject;

public class UserListRespondCommand implements ICommand {
	private static int ACTION_CODE = 8;
	private int status;
	private JSONObject json;

	public UserListRespondCommand(JSONObject jsonobj) {
		this.json = jsonobj;
	}

	@Override
	public void exec() {
		System.out.println("get user list");
		MainInstance.getInstance().updateUserList(
				json.getJSONArray("list").toString());
	}
}
