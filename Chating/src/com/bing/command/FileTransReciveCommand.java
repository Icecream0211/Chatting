package com.bing.command;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JFileChooser;

import com.bing.chat.filesend.FileTransInstance;
import com.xiyou.view.TipWindow;

import net.sf.json.JSONObject;

public class FileTransReciveCommand implements ICommand {
	private JSONObject jsobj;
	private SocketChannel socketChannel;

	public FileTransReciveCommand(SocketChannel clientChannel,
			JSONObject jsonObj) {
		this.jsobj = jsonObj;
		this.socketChannel = clientChannel;
	}

	public void exec() {
		String ip = jsobj.getString("ip");
		int lisport = jsobj.getInt("lis_port");
		String filename = jsobj.getString("filename");
		int filesize = jsobj.getInt("filesize");
		FileTransInstance fileins = new FileTransInstance(ip, lisport, lisport);

		int res = TipWindow.createConfirmTipWindow("A file named " + filename
				+ ",sized approximately " + filesize
				+ " reached,recive or not?");
		if (res == TipWindow.Accept) {
			File file;
			JFileChooser chooer = new JFileChooser();
			int chooen = chooer.showSaveDialog(null);
			if (chooen == JFileChooser.APPROVE_OPTION) {
				file = chooer.getSelectedFile();
				fileins.initComponents(file,filesize,socketChannel);
			} else {
				REJ();
			}
		} else {
			REJ();
		}
	}

	private void REJ() {
		String command = "{status:2,action:10}";
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
