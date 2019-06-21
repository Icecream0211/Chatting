package com.bing.chatserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;
import com.bing.server.common.CommonConfig;
import com.bing.server.utility.ServerHander;

public class DaemonServer implements Runnable {
	private Future serverFuture = null;
	private boolean isrunned = false;
	private ServerSocketChannel serverChannel;
	private Thread currentThread;
	private Selector selector;
	private int serverPort=CommonConfig.SERVER_DEFAULT_PORT;
	private InetSocketAddress socketAddress;

	public void start() {
		this.serverFuture = Executors.newSingleThreadExecutor().submit(this);
		this.isrunned = true;
		StartShowMessage();
	}

	public DaemonServer() {
		Runtime.getRuntime().addShutdownHook(new Thread(new ExitHookThread()));
	}

	@Override
	public void run() {
		if (this.currentThread == null) {
			currentThread = Thread.currentThread();
		}
		try {
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			System.out.println(serverChannel.socket().getInetAddress());
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(
					new InetSocketAddress(CommonConfig.SERVER_DEFAULT_PORT));
			// serverChannel.register(selector, serverChannel.validOps());
			this.serverPort = CommonConfig.SERVER_DEFAULT_PORT;
			CommonConfig.SERVER_PORT = CommonConfig.SERVER_DEFAULT_PORT;
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e1) {
			try {
				this.serverPort = RandomPort.getPort();
				CommonConfig.SERVER_PORT = this.serverPort;
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
				System.exit(0);
			}
			e1.printStackTrace();
		}
		while (!currentThread.isInterrupted()) {
			SelectionKey key = null;
			Socket socket = null;
			try {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					if (key.isAcceptable()) {
						ServerHander.handleAccept(key);
					}
					if (key.isReadable()) {
						ServerHander.handleRead(key);
					}

					if (key.isValid() && key.isWritable()) {
						// socket = (Socket) key.attachment();
						// key.channel().register(selector,
						// SelectionKey.OP_READ,
						// socket);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new DaemonServer().start();
	}

	private void StartShowMessage() {
		System.out.println("Server is starting at port " + this.serverPort
				+ "....");
	}

	public void stop() {
		try {
			if (this.selector.isOpen()) {
				this.selector.close();
			}
			if (this.serverChannel.isOpen()) {
				this.serverChannel.close();
			}
			this.currentThread.interrupt();
			this.isrunned = false;
			this.serverFuture.cancel(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ExitHookThread implements Runnable {
		@Override
		public void run() {
			stop();
		}
	}
}
