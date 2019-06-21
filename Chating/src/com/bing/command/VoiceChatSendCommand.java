package com.bing.command;

import com.bing.common.ChatsPool;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;
import com.bing.common.TxtChatingInstance;
import com.bing.common.VoiceChatingInstance;

public class VoiceChatSendCommand implements ICommand {
	private ContactNode node;
	private String command;

	public VoiceChatSendCommand(ContactNode conNode) {
		this.node = conNode;
	}

	@Override
	public void exec() {
		VoiceChatingInstance voiceIns;
		voiceIns = new VoiceChatingInstance(node.getIp(), node.getTcpport(),
				node.getTcpport());
		voiceIns.iniViceComponents(null);
		ChatsPool.getInstance().addChatIns(voiceIns.getIdentify(), voiceIns);
		System.out.println("Voice chat request send....");
		this.command = "{action:4,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + ",con_port:"
				+ voiceIns.getMyRTPPort() + "}";
		System.out.println("Voice send command:" + this.command);
		voiceIns.sendMsg(this.command);
	}
}
