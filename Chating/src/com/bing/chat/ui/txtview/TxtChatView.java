package com.bing.chat.ui.txtview;

import java.awt.event.MouseEvent;

import com.bing.chat.ui.common.ResizeFrame;
import com.bing.common.TxtChatingInstance;

public class TxtChatView extends ResizeFrame {
	private TxtChatViewPanel txtpanel;
	private TxtChatingInstance txtins;
	public TxtChatView(TxtChatingInstance txtChatingInstance){
		this.txtins = txtChatingInstance;
		if(txtpanel==null)
			txtpanel = new TxtChatViewPanel(this);
		this.addShowPanel(this.txtpanel);
	}
	public static void main(String[] args) {
		TxtChatView txtview = new TxtChatView(null);
		txtview.showup();
		txtview.setIsshowed(true);
	}
	public void updateShowTextPane(String msg) {
		this.txtpanel.updateShowTextPane(msg);
	}
	public void sendMsg(String content) {
		this.txtins.sendMsg(content);
	}
	@Override
	public void CloseButtonClickAction(MouseEvent ee) {
		this.dispose();
	}
}
