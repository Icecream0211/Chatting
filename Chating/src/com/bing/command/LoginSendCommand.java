package com.bing.command;

import java.io.IOException;

import com.bing.common.BackstageChannel;
import com.bing.common.CommonConfig;

public class LoginSendCommand implements ICommand {
	public static int ACTION_CODE = 1;
	private String command;

	public LoginSendCommand(String myip, int myport) {
		this.command = "{action:1,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + ",hostname:\""
				+ CommonConfig.MY_HOSTNAME + "\",nickname:\""
				+ CommonConfig.MY_HOSTNAME + "\"}";
	}

	@Override
	public void exec() {
		System.out.println("login command send....");
		try {
			BackstageChannel.getInstance().sendMsg(this.command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
