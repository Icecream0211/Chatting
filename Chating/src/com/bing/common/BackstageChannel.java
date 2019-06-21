package com.bing.common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class BackstageChannel {

	private static BackstageChannel instance = null;
	// 信道选择器
	private Selector selector;
	// 与服务器通信的信道
	SocketChannel socketChannel;
	// 要连接的服务器Ip地址
	private String hostIp;
	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;
	private ReadThread clientReader;
	
	private boolean isstarted=false;

	// 所属的聊天实体
	/**
	 * 构造函数
	 * 
	 * @param chatinstance
	 * @throws IOException
	 */
	private BackstageChannel(String serip, int serport) throws IOException {
		this.hostIp = serip;
		this.hostListenningPort = serport;
		initialize();
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		// 打开监听信道并设置为非阻塞模式
		socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
				hostListenningPort));
		socketChannel.configureBlocking(false);
		// 打开并注册选择器到信道
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 启动读取线程
		clientReader = new ReadThread(selector);
		clientReader.start();
	}

	/**
	 * 发送字符串到服务器
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-16"));
		socketChannel.write(writeBuffer);
	}

	public void stop() {
		this.clientReader.stop();
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ReadThread implements Runnable {
		private Selector selector;
		private Future current;
		
		private boolean running = false;
		private Thread currentThread;

		public ReadThread(Selector selector) {
			this.selector = selector;
		}
		
		public void start(){
			running = true;
			current = Executors.newCachedThreadPool().submit(this);
		}

		public void stop() {
			try {
				running = false;
				this.selector.close();
				//current.cancel(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			if (this.currentThread == null) {
				currentThread = Thread.currentThread();
			}
			try {
				while (!currentThread.isInterrupted()) {
					if (selector.select() > 0) {
						// 遍历每个有可用IO操作Channel对应的SelectionKey
						for (SelectionKey sk : selector.selectedKeys()) {
							// 如果该SelectionKey对应的Channel中有可读的数据
							if (sk.isReadable()) {
								CommandHall.handRead(sk);
							}
							// 删除正在处理的SelectionKey
							selector.selectedKeys().remove(sk);
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void getMessage(String msg) {
	}

	public static BackstageChannel getInstance() {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		if (instance == null) {
			try {
				instance = new BackstageChannel(CommonConfig.SERVER_IP,
						CommonConfig.SERVER_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		lock.unlock();
		return instance;
	}
}
