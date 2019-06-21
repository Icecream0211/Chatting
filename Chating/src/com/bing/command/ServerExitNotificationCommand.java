package com.bing.command;

import com.bing.chart.main.Main;

public class ServerExitNotificationCommand implements ICommand {
	@Override
	public void exec() {
		System.out.println("Server exit notification....");
		System.out.println("i am going to exit....");
		Main.getInstance().stop();
	}
}
