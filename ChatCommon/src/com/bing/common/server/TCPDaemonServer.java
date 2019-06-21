package com.bing.common.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class TCPDaemonServer implements Runnable {
	private int tcpport = 5555;
	private int udpport = 5556;
	private Future serverFuture;
	private boolean isRuned = false;
	private Selector selector;
	private final int TimeOut = 3000;

	public TCPDaemonServer() {
	}

	public TCPDaemonServer(int tcpport) {
		this.tcpport = tcpport;
		this.udpport = udpport;
	}

	private void initServer() throws IOException {
		// 创建选择器
		selector = Selector.open();
		// 打开监听信道
		ServerSocketChannel listenerChannel = ServerSocketChannel.open();
		// 与本地端口绑定
		listenerChannel.socket().bind(new InetSocketAddress(tcpport));
		// 设置为非阻塞模式
		listenerChannel.configureBlocking(false);
		// 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
		listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
		// 创建一个处理协议的实现类,由它来具体操作
		// TCPProtocol protocol = new TCPProtocolImpl(BufferSize);
		// 反复循环,等待IO
		while (true) {
			// 等待某信道就绪(或超时)
			if (selector.select(TimeOut) == 0) {
				continue;
			}

			// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				try {
					if (key.isAcceptable()) {
						handAccept(key);
						// 有客户端连接请求时
						// protocol.handleAccept(key);
					}

					if (key.isReadable()) {
						handRead(key);
						// 从客户端读取数据
						// protocol.handleRead(key);
						// clientChannel = (SocketChannel) key.channel();
					}

					if (key.isValid() && key.isWritable()) {
						handWrite(key);
						// 客户端可写时
						// protocol.handleWrite(key);
					}
				} catch (Exception ex) {
					// 出现IO异常（如客户端断开连接）时移除处理过的键
					keyIter.remove();
					continue;
				}
				// 移除处理过的键
				keyIter.remove();
			}
		}

	}

	public void start() {
		if (isRuned) {
			return;
		}
		serverFuture = Executors.newCachedThreadPool().submit(this);
		this.isRuned = true;
	}

	@Override
	public void run() {
		try {
			this.initServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (this.isRuned) {
			serverFuture.cancel(true);
		}
	}
	public abstract void handWrite(SelectionKey key) throws IOException;
	public abstract void handRead(SelectionKey key) throws IOException;
	public abstract void handAccept(SelectionKey key) throws IOException;
}
