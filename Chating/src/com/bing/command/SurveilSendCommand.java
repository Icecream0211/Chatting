package com.bing.command;

import com.bing.chat.filesend.FileSelector;
import com.bing.chat.filesend.FileTransInstance;
import com.bing.common.ChatsPool;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;
import com.bing.controllee.SurveillanceInstance;

public class SurveilSendCommand implements ICommand {
	private String command;
	private ContactNode conNode;

	public SurveilSendCommand(ContactNode coNode) {
		this.conNode = coNode;
	}

	@Override
	public void exec() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("action:11,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT);
		sb.append("}");
		this.command = sb.toString();
		System.out.println("send file command:-->" + this.command);
		SurveillanceInstance surveilins = new SurveillanceInstance(
				conNode.getIp(), conNode.getTcpport(), conNode.getTcpport());
		surveilins.initComponent(null);
		surveilins.sendMsg(this.command);
		ChatsPool.getInstance()
				.addChatIns(surveilins.getIdentify(), surveilins);
	}
}
