package com.bing.command;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JFileChooser;

import net.sf.json.JSONObject;

import com.bing.chat.filesend.FileTransInstance;
import com.bing.controllee.SurveillanceInstance;
import com.xiyou.view.TipWindow;

public class SurveilReciveCommand implements ICommand {
	private JSONObject jsobj;
	private SocketChannel socketChannel;

	public SurveilReciveCommand(SocketChannel clientChannel, JSONObject jsonObj) {
		this.jsobj = jsonObj;
		this.socketChannel = clientChannel;
	}

	public void exec() {
		String ip = jsobj.getString("ip");
		int lisport = jsobj.getInt("lis_port");
		SurveillanceInstance surveilIns = new SurveillanceInstance(ip, lisport,
				lisport);
		int res = TipWindow.createConfirmTipWindow(ip
				+ " request a surveillance,recieve or not?");
		if (res == TipWindow.Accept) {
			surveilIns.initComponent(socketChannel);
		} else {
			REJ();
		}
	}

	private void REJ() {
		String command = "{status:2,action:11}";
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
