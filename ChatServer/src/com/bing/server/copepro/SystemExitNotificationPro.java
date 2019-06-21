package com.bing.server.copepro;

import java.nio.channels.SocketChannel;
import java.util.List;

import com.bing.server.common.NotifyThread;
import com.bing.server.utility.ClientInstance;
import com.bing.server.utility.ClientPool;

public class SystemExitNotificationPro extends AProcedure {
	public SystemExitNotificationPro(SocketChannel client) {
		super(client);
	}

	@Override
	public void exec() {
		List<ClientInstance> users = (List<ClientInstance>) ClientPool
				.getInstance().getAllLists();

		System.out.println("Exit Server  Notification---->" + users.size());
		if (users.size() > 0) {
			StringBuilder sber = new StringBuilder();
			sber.append("{");
			sber.append("action:9");
			sber.append("}");
			for (ClientInstance cliins : users) {
				System.out.println("send to " + cliins.getIp()
						+ " an Server exit notification:" + sber.toString());
				new NotifyThread(sber.toString(), cliins).start();
			}
		}
	}
}
