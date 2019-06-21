package com.bing.command;

import java.io.IOException;

import com.bing.common.BackstageChannel;
import com.bing.common.CommonConfig;

public class UserListSendCommand implements ICommand {
	public static int ACTION_CODE = 3;
	private String command;

	public UserListSendCommand() {
		this.command = "{action:8,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + "}";
	}

	@Override
	public void exec() {
		System.out.println("send userlist requ...");
		try {
			BackstageChannel.getInstance().sendMsg(this.command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
