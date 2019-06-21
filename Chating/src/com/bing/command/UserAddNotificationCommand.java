package com.bing.command;

import java.io.IOException;

import net.sf.json.JSONObject;

import com.bing.common.BackstageChannel;
import com.bing.common.CommonConfig;
import com.bing.common.MainInstance;
import com.xiyou.view.TipWindow;

public class UserAddNotificationCommand implements ICommand {
	public static int ACTION_CODE = 7;
	private JSONObject obj;

	public UserAddNotificationCommand(JSONObject obj) {
		this.obj = obj;
	}

	@Override
	public void exec() {
		System.out.println("get add user notification....");
		JSONObject userobj = obj.getJSONObject("user");
		MainInstance.getInstance().addUser(userobj);
		TipWindow.createMessageTipWindow(userobj.getString("ip") + " login");
	}
}
