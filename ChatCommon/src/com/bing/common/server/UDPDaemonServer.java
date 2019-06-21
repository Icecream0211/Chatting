package com.bing.common.server;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public abstract class UDPDaemonServer implements Runnable {
	private int udpport = 5556;
	private Future udpFuture;
	private boolean isRunned = false;

	public UDPDaemonServer() {
	}

	public UDPDaemonServer(int port) {
		this.udpport = port;
	}

	public void start() {
		if (isRunned) {
			return;
		}
		udpFuture = Executors.newCachedThreadPool().submit(this);
	}

	public void stop() {
		if (!isRunned) {
			return;
		}
		udpFuture.cancel(true);
	}

	public void run() {
		Selector selector = null;
		try {
			DatagramChannel channel = DatagramChannel.open();
			DatagramSocket socket = channel.socket();
			channel.configureBlocking(false);
			socket.bind(new InetSocketAddress(this.udpport));
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ
					| SelectionKey.OP_CONNECT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
		while (true) {
			try {
				int eventsCount = selector.select();
				if (eventsCount > 0) {
					Set selectedKeys = selector.selectedKeys();
					Iterator iterator = selectedKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey sk = (SelectionKey) iterator.next();
						iterator.remove();
						if (sk.isReadable()) {
							handWrite(sk);
						}
						if (sk.isValid() && sk.isWritable()) {
							handWrite(sk);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public abstract void handWrite(SelectionKey key) throws IOException;
	public abstract void handRead(SelectionKey key) throws IOException;
}
