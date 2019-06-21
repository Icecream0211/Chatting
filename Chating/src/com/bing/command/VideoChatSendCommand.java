package com.bing.command;

import com.bing.common.ChatsPool;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;
import com.bing.common.TxtChatingInstance;
import com.bing.common.VideoChatingInstance;
import com.bing.common.VoiceChatingInstance;

public class VideoChatSendCommand implements ICommand {
	private ContactNode node;
	private String command;

	public VideoChatSendCommand(ContactNode conNode) {
		this.node = conNode;
	}

	@Override
	public void exec() {
		VideoChatingInstance videoIns;
		videoIns = new VideoChatingInstance(node.getIp(), node.getTcpport(),
				node.getTcpport());
		videoIns.iniVideoComponents(null, -1);
		ChatsPool.getInstance().addChatIns(videoIns.getIdentify(), videoIns);
		System.out.println("Voice chat request send....");
		this.command = "{action:5,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + ",con_port_o:"
				+ videoIns.getMyRTPPorts()[0] + ",con_port_t:"
				+ videoIns.getMyRTPPorts()[1] + "}";
		System.out.println("video send command:" + this.command);
		videoIns.sendMsg(this.command);
	}
}
