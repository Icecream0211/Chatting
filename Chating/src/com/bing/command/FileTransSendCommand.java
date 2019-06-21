package com.bing.command;

import com.bing.chat.filesend.FileSelector;
import com.bing.chat.filesend.FileTransInstance;
import com.bing.common.ChatsPool;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;

public class FileTransSendCommand implements ICommand {
	public static final int ACTION_CODE = 10;
	private String command;
	private ContactNode node;

	public FileTransSendCommand(ContactNode conNode) {
		this.node = conNode;
	}

	@Override
	public void exec() {
		FileSelector filesel = new FileSelector();
		if (filesel.select()) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("action:10,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
					+ CommonConfig.CLIENT_TCP_PORT);
			sb.append(",filename:\"" + filesel.getSelectedFile().getName()
					+ "\"");
			sb.append(",filesize:" + filesel.getFileSize() + "}");
			this.command = sb.toString();
			System.out.println("send file command:-->" + this.command);
			FileTransInstance fileins = new FileTransInstance(node.getIp(),
					node.getTcpport(), node.getTcpport());
			fileins.initComponents(filesel.getSelectedFile(), (int) filesel
					.getSelectedFile().length(), null);
			fileins.sendMsg(this.command);
			ChatsPool.getInstance().addChatIns(fileins.getIdentify(), fileins);
		}
	}
}
