package com.bing.command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.bing.common.ChatsPool;
import com.bing.common.VoiceChatingInstance;
import com.xiyou.view.TipWindow;

import net.sf.json.JSONObject;

public class VoiceChatReciveCommand implements ICommand {
	public static final int ACTION_CODE = 4;// 0x03
	private JSONObject jsonobj;
	private SocketChannel socketChannel;

	public VoiceChatReciveCommand(SocketChannel socketChannel,
			JSONObject jsonObj) {
		this.socketChannel = socketChannel;
		this.jsonobj = jsonObj;
	}

	@Override
	public void exec() {
		VoiceChatingInstance voiceins;
			voiceins = new VoiceChatingInstance(jsonobj.getString("ip"),
					jsonobj.getInt("lis_port"), jsonobj.getInt("con_port"));
		int reable = TipWindow
				.createConfirmTipWindow("A voice chat recived from"
						+ jsonobj.getString("ip") + ",recive or not?");
		if (reable == TipWindow.Accept) {
			voiceins.iniViceComponents(socketChannel);
			voiceins.initVoiceSenderandReciver();
			ChatsPool.getInstance()
					.addChatIns(voiceins.getIdentify(), voiceins);
		} else {
			REJ();
		}
	}

	private void REJ() {
		String command = "{status:2,action:4}";
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
