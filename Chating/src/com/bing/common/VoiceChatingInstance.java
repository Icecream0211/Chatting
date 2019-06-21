package com.bing.common;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.bing.chat.api.ChatingInstanceAdapter;
import com.bing.chat.voice.ui.VoiceView;

public class VoiceChatingInstance extends ChatingInstanceAdapter {
	public VoiceChatingInstance(String destIp, int destLisPort, int destConPort) {
		super(destIp, destLisPort, destConPort, 4);
	}

	private boolean actived;
	private VoiceView viceView;
	// 通信通道
	private VoiceChannel viceChannel;
	// UI
	private boolean isRequester = false;
	private boolean isANC = false;

	private void iniComponents() {
	}

	public void iniViceComponents(SocketChannel socketchannel) {
		try {
			this.viceChannel = new VoiceChannel(this, socketchannel);
			if (socketchannel == null) {
				isRequester = true;
			}
			this.ANC();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ANC() {
		if (!isRequester && !this.isANC) {
			String command = "{status:1,action:4,con_port:"
					+ this.getMyRTPPort() + "}";
			this.sendMsg(command);
			this.isANC = true;
		}
	}

	/**
	 * @param socketChannel
	 *            如果为null，则是主动发起txtchat，否则则是接受txtchat请求
	 * @throws IOException
	 */

	public void sendMsg(String msg) {
		this.viceChannel.sendMsg(msg);
	}

	public boolean isOpenVoiceChat() {
		return this.viceChannel == null;
	}

	public VoiceChannel getViceChannel() {
		return viceChannel;
	}

	public void setViceChannel(VoiceChannel viceChannel) {
		this.viceChannel = viceChannel;
	}

	public VoiceView getViceView() {
		return viceView;
	}

	public void setViceView(VoiceView viceView) {
		this.viceView = viceView;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	@Override
	public int getDestConPort() {
		return this.getDest_con_port();
	}

	@Override
	public String getDestIp() {
		return this.getDest_ip();
	}

	@Override
	public int getDestLisPort() {
		return this.getDest_lis_port();
	}

	@Override
	public int getMyPort() {
		return 0;
	}

	public int getMyRTPPort() {
		return this.viceChannel.getMyRTPPort();
	}

	public void initVoiceSenderandReciver() {
		this.viceChannel.initVoiceSenderandReciver(getDestConPort() + 4);//
	}

	@Override
	public void close() {
		if (this.viceView != null) {
			// close the vioceview;
		}
		if (this.viceChannel != null) {
			this.viceChannel.close();
		}
		ChatsPool.getInstance().removeMySelf(this);
	}
}
