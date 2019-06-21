package com.bing.server.copepro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public abstract class AProcedure implements IProcedure, Runnable {
	private SocketChannel clientChannel;

	public AProcedure(SocketChannel client) {
		this.clientChannel = client;
	}

	public void start() {
		Executors.newCachedThreadPool().execute(this);
	}

	@Override
	public abstract void exec();

	@Override
	public void run() {
		exec();
	}
	
	public SocketChannel getSocketChannel(){
		return this.clientChannel;
	}

	public void sendBack(String msg) {
		try {
			this.clientChannel.write(ByteBuffer.wrap(msg.getBytes("UTF-16")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
