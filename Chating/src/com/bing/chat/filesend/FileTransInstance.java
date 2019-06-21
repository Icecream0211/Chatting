package com.bing.chat.filesend;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;

import javax.swing.JFileChooser;

import com.bing.chat.api.ChatingInstanceAdapter;

public class FileTransInstance extends ChatingInstanceAdapter {

	private FileTransInstance(String dest_ip, int dest_lis_port,
			int dest_con_port, int chattype) {
		super(dest_ip, dest_lis_port, dest_con_port, chattype);
	}

	public FileTransInstance(String dest_ip, int dest_lis_port,
			int dest_con_port) {
		this(dest_ip, dest_lis_port, dest_con_port, 10);
	}

	private FileTransChannel channel;
	private File fileseled;
	private int fileSize = -1;// B
	private boolean isRequester = false;
	private boolean isANC = false;

	@Override
	public String getDestIp() {
		return this.getDest_ip();
	}

	@Override
	public int getDestLisPort() {
		return this.getDest_lis_port();
	}

	@Override
	public int getDestConPort() {
		return this.getDest_con_port();
	}

	@Override
	public int getMyPort() {
		return 0;
	}

	@Override
	public void close() {
		this.channel.close();
	}

	public void initComponents(File sel, int filesize,
			SocketChannel socketChannel) {
		this.fileseled = sel;
		this.fileSize = filesize;
		try {
			this.channel = new FileTransChannel(this, socketChannel);
			if (socketChannel == null) {
				isRequester = true;
			}
			this.ANC();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void ANC() {
		if (!isRequester && !this.isANC) {
			String command = "{status:1,action:10,file_port:"
					+ this.channel.getFileLisPort() + "}";
			this.sendMsg(command);
			this.isANC = true;
		}
	}

	public int getFileTotalSize() {
		return this.fileSize;
	}

	@Override
	public void sendMsg(String msg) {
		try {
			this.channel.sendMsg(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFileSelected() {
		return this.fileseled;
	}

	public String getFilePath() {
		if (this.fileseled != null) {
			return this.fileseled.getAbsolutePath();
		} else
			return null;
	}
}
