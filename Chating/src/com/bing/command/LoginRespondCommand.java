package com.bing.command;

import net.sf.json.JSONObject;

import com.bing.common.CommonConfig;
import com.bing.common.MainInstance;

public class LoginRespondCommand implements ICommand {
	public static int ACTION_CODE = 1;
	private int status;

	public LoginRespondCommand(JSONObject jsonObj) {
		this.status = jsonObj.getInt("status");
	}

	@Override
	public void exec() {
		System.out.println("login response back....");
		System.out.println("status:" + this.status);
		/*
		 * MainInstance.getInstance() .setLoginFlag(this.status == 1 ? true :
		 * false);
		 */
		CommonConfig.islogined = this.status == 1 ? true : false;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
