package com.bing.common;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.bing.chat.api.ChatingInstanceAdapter;
import com.xiyou.view.TxtChatView;
public class TxtChatingInstance extends ChatingInstanceAdapter {
	private boolean actived;
	private boolean isRequester = false;
	private int myport = -1;
	public static int chatType = 3;// 和action_code 一样
	// 通信通道
	private TxtChannel txtChannel;
	// UI
	private TxtChatView txtView;//old ui
	//private TxtChatView txtView;//new ui
	public TxtChannel getTxtChannel() {
		return txtChannel;
	}

	public TxtChatingInstance(String dest_ip, int dest_lis_port,
			int dest_con_port) {
		super(dest_ip, dest_lis_port, dest_con_port,3);
	}

	public TxtChatingInstance(String dest_ip, int dest_lis_port,
			int dest_con_port, SocketChannel clientChannel) {
		super(dest_ip, dest_lis_port, dest_con_port,3);
	}

	/**
	 * @param socketChannel
	 *            如果为null，则是主动发起txtchat，否则则是接受txtchat请求
	 * @throws IOException
	 */

	public void iniTxtComponents(SocketChannel socketChannel) {
		try {
			this.txtView = new TxtChatView(this);
			this.txtView.setLocationRelativeTo(null);
			this.txtChannel = new TxtChannel(this, socketChannel);
			this.setMyport(this.txtChannel.getMyport());
			initRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initRequest() {
		if (this.txtChannel.isRequester()) {
			// new TxtChatSendCommand(this).exec();
		}
	}

	public void sendMsg(String msg) {
		try {
			this.txtChannel.sendMsg(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void renderMsg(String msg) {
		if (!this.txtView.isShowing()) {
			this.showViewUp();
		}
		this.txtView.updateShowTextPane(msg);
	}

	public boolean isOpenTxtChat() {
		return this.txtChannel == null;
	}

	public void setTxtChannel(TxtChannel txtChannel) {
		this.txtChannel = txtChannel;
	}
	public TxtChatView getTxtView() {
		return txtView;
	}

	public void setTxtView(TxtChatView txtView) {
		this.txtView = txtView;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public int getMyport() {
		return myport;
	}

	public void setMyport(int myport) {
		this.myport = myport;
		makeIdentify();
	}

	public String getIdentify() {
		return super.getIdentify();
	}

	@Override
	public String getDestIp() {
		return super.getDest_ip();
	}

	@Override
	public int getDestLisPort() {
		return super.getDest_lis_port();
	}

	@Override
	public int getDestConPort() {
		return super.getDest_con_port();
	}

	@Override
	public int getMyPort() {
		return this.getMyport();
	}

	public void showViewUp() {
		if (null != this.txtView) {
			//this.txtView.showup();
			this.txtView.setVisible(true);
		}
	}

	public void anew() {
		try {
			if (null == this.txtView) {
				this.txtView = new TxtChatView(this);
				this.txtView.setVisible(true);
			}
			if (null == this.txtChannel) {
				this.txtChannel = new TxtChannel(this, null);
				this.setMyport(this.txtChannel.getMyport());
			}
			initRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		if(this.txtView!=null){
			this.txtView.dispose();
		}
		if(this.txtChannel!=null){
			this.txtChannel.close();
		}
	}
}
