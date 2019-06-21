package com.bing.command;

import net.sf.json.JSONObject;

import com.bing.common.MainInstance;
import com.xiyou.view.TipWindow;

public class UserExitNotificationCommand implements ICommand {
	public static int ACTION_CODE = 7;
	private JSONObject obj;

	public UserExitNotificationCommand(JSONObject obj) {
		this.obj = obj;
	}

	@Override
	public void exec() {
		System.out.println("get user exit notification....");
		JSONObject userobj = obj.getJSONObject("user");
		MainInstance.getInstance().removeUser(obj.getJSONObject("user"));
		TipWindow.createMessageTipWindow(userobj.getString("ip") + " exited");
	}
}
