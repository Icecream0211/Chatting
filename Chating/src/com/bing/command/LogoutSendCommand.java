package com.bing.command;

import java.io.IOException;

import com.bing.common.BackstageChannel;
import com.bing.common.CommonConfig;

public class LogoutSendCommand implements ICommand {
	public static int ACTION_CODE = 1;
	private String command;

	public LogoutSendCommand(String myip, int myport) {
		this.command = "{action:2,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + "}";
	}

	@Override
	public void exec() {
		System.out.println("logout command send....");
		try {
			BackstageChannel.getInstance().sendMsg(this.command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
