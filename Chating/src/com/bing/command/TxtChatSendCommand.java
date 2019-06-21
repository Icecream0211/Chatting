package com.bing.command;

import com.bing.common.ChatsPool;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;
import com.bing.common.TxtChatingInstance;

public class TxtChatSendCommand implements ICommand {
	public static int ACTION_CODE = 1;
	private String command;
	private TxtChatingInstance txtIns;
	private String destip;
	private int destlisport;

	public TxtChatSendCommand(TxtChatingInstance txtIns) {
		this.command = "{action:3,ip:\"" + CommonConfig.MY_IP + "\",lis_port:"
				+ CommonConfig.CLIENT_TCP_PORT + ",con_port:"
				+ txtIns.getMyport() + "}";
		this.txtIns = txtIns;
	}

	public TxtChatSendCommand(ContactNode conNode) {
		this.destip = conNode.getIp();
		this.destlisport = conNode.getTcpport();
	}

	@Override
	public void exec() {
		TxtChatingInstance txtIns;
		txtIns = (TxtChatingInstance) ChatsPool.getInstance().getChatIns(
				"3_" + this.destip + "_" + this.destlisport);
		if (null == txtIns) {
			txtIns = new TxtChatingInstance(this.destip, this.destlisport,
					this.destlisport);
			txtIns.iniTxtComponents(null);
			ChatsPool.getInstance().addChatIns(txtIns.getIdentify(), txtIns);
			System.out.println("Txt chat request send....");
			this.command = "{action:3,ip:\"" + CommonConfig.MY_IP
					+ "\",lis_port:" + CommonConfig.CLIENT_TCP_PORT
					+ ",con_port:" + txtIns.getMyport() + "}";
			txtIns.sendMsg(this.command);
		} else {
			txtIns.anew();
		}
		txtIns.showViewUp();
	}
}
