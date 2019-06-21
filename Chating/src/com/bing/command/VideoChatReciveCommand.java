package com.bing.command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.bing.common.ChatsPool;
import com.bing.common.VideoChatingInstance;
import com.bing.common.VoiceChatingInstance;
import com.xiyou.view.TipWindow;

import net.sf.json.JSONObject;

public class VideoChatReciveCommand implements ICommand {
	public static final int ACTION_CODE = 4;// 0x03
	private JSONObject jsonobj;
	private SocketChannel socketChannel;

	public VideoChatReciveCommand(SocketChannel socketChannel,
			JSONObject jsonObj) {
		this.socketChannel = socketChannel;
		this.jsonobj = jsonObj;
	}

	@Override
	public void exec() {
		int[] ports = new int[2];
		String ips = jsonobj.getString("ip");
		ports[0] = jsonobj.getInt("con_port_o");
		ports[1] = jsonobj.getInt("con_port_t");
		int lisport = jsonobj.getInt("lis_port");
		VideoChatingInstance videoins;
		videoins = new VideoChatingInstance(ips, lisport, ports);
		int reable = TipWindow
				.createConfirmTipWindow("A voice chat recived from"
						+ jsonobj.getString("ip") + ",recive or not?");
		if (reable == TipWindow.Accept) {
			videoins.iniVideoComponents(socketChannel, reable);
			ChatsPool.getInstance()
					.addChatIns(videoins.getIdentify(), videoins);
		} else {
			REJ();
		}
	}

	private void REJ() {
		String command = "{status:2,action:5}";
		ByteBuffer writeBuffer;
		try {
			writeBuffer = ByteBuffer.wrap(command.getBytes("UTF-16"));
			socketChannel.write(writeBuffer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
