package com.bing.server.copepro;

import java.nio.channels.SocketChannel;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.bing.server.common.NotifyThread;
import com.bing.server.utility.ClientInstance;
import com.bing.server.utility.ClientPool;

public class UserExitNotificationPro extends AProcedure {
	private ClientInstance ins;

	public UserExitNotificationPro(SocketChannel client) {
		super(client);
	}

	public UserExitNotificationPro(SocketChannel client, ClientInstance ins) {
		this(client);
		this.ins = ins;
	}

	@Override
	public void exec() {
		List<ClientInstance> users = (List<ClientInstance>) ClientPool
				.getInstance().getAllExceptMeLists(ins);
		System.out.println("Exit Notification---->" + users.size());
		if (users.size() > 0) {
			JsonConfig config = new JsonConfig();
			config.setExcludes(new String[] { "identify", "socketChannel",
					"tcpport" });
			StringBuilder sber = new StringBuilder();
			sber.append("{");
			sber.append("action:6,user:");
			sber.append(JSONObject.fromObject(ins, config).toString());
			sber.append("}");
			/*
			 * String notifyStr = "{action:7,ip:\"" + ins.getIp() +
			 * "\",lis_port:" + ins.getTcpport() + ",hostname:\"" +
			 * ins.getHostName() + "\",nickname:\"" + ins.getNickName() + "\"}";
			 */
				for (ClientInstance cliins : users) {
					System.out.println("send to " + cliins.getIp()
							+ " an user exit notification:" + sber.toString());
					new NotifyThread(sber.toString(), cliins).start();
				}
		}
	}
}
