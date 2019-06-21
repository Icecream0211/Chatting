package com.bing.common;

import java.awt.Component;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.bing.chat.api.ChatingInstanceAdapter;
import com.xiyou.view.TipWindow;
import com.xiyou.view.VideoView;

public class VideoChatingInstance extends ChatingInstanceAdapter {
	private boolean actived;
	private int[] destconports;
	// 通信通道
	private VideoChannel videoChannel;
	private boolean isANC = false;
	private boolean isRequester = false;

	private VideoView videoView;

	// UI
	public VideoChatingInstance(String destIp, int destLisPort,
			int destConPort, int chattype) {
		super(destIp, destLisPort, destConPort, chattype);
	}

	public VideoChatingInstance(String ip, int tcpport, int tcpport2) {
		this(ip, tcpport, tcpport2, 5);
	}

	public VideoChatingInstance(String ip, int tcpport, int[] conports) {
		this(ip, tcpport, conports[0]);
		destconports = conports;
	}

	/**
	 * @param reable
	 * @param socketChannel
	 * @throws IOException
	 */

	public void iniVideoComponents(SocketChannel socketchannel, int reable) {
		try {
			if (reable == -1 || reable == TipWindow.Accept) {
				this.videoView = new VideoView(this);
				this.videoView.showVideoUI();
			}
			this.videoChannel = new VideoChannel(this, socketchannel);
			if (socketchannel == null) {
				isRequester = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ANC(reable);
	}

	private void REJ() {
		String command = "{status:2,action:4}";
		this.sendMsg(command);
		close();
	}

	public void close() {
		if (this.videoView != null) {
			this.videoView.dispose();
		}
		this.videoChannel.close();
		ChatsPool.getInstance().removeMySelf(this);
	}

	public void sendMsg(String msg) {
		try {
			this.videoChannel.sendMsg(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ANC(int recable) {
		if (recable == -1 || recable == TipWindow.Accept) {
			if (!isRequester && !this.isANC) {
				String command = "{status:1,action:5,con_port_o:"
						+ this.getMyRTPPorts()[0] + ",con_port_t:"
						+ this.getMyRTPPorts()[1] + "}";
				this.sendMsg(command);
				this.isANC = true;
			}
		} else {
			this.REJ();
		}
	}

	public void renderMsg(String msg) {
	}

	public boolean isOpenVideoChat() {
		return this.videoChannel == null;
	}

	public VideoChannel getVideoChannel() {
		return videoChannel;
	}

	public void setVideoChannel(VideoChannel videoChannel) {
		this.videoChannel = videoChannel;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	@Override
	public int getDestConPort() {
		return super.getDest_con_port();
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
	public int getMyPort() {
		return 0;
	}

	public void setDestRTPports(int[] destconports) {
		this.destconports = destconports;
	}

	public int[] getDestRTPPorts() {
		return this.destconports;
	}

	public int[] getMyRTPPorts() {
		return this.videoChannel.getPorts();
	}

	public void initVideoReciver() {
		this.videoChannel.initReciver();
	}

	public void playLocalView(Component visualComponent) {
		if (visualComponent != null) {
			this.videoView.localPlay(visualComponent);
		}
	}

	public void playRemoteView(Component visualComponent) {
		if (visualComponent != null) {
			this.videoView.remotePlay(visualComponent);
		}
	}
}