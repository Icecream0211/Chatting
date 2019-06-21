package com.bing.server.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

import com.bing.server.utility.ClientInstance;

public class NotifyThread implements Runnable {
	ClientInstance cls;
	String ms;

	public NotifyThread(String ms, ClientInstance cls) {
		this.cls = cls;
		this.ms = ms;
	}

	public void start() {
		Executors.newCachedThreadPool().execute(this);
	}

	@Override
	public void run() {
		try {
			ByteBuffer byff = ByteBuffer.wrap(this.ms.toString().getBytes(
					"UTF-16"));
			SocketChannel sc = this.cls.getSocketChannel();
			sc.write(byff);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}