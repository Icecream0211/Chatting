package com.bing.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TxtChannel {
	// 信道选择器
	private Selector selector;
	// 与服务器通信的信道
	SocketChannel socketChannel;
	// 要连接的服务器Ip地址
	private String hostIp;
	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;
	private ReadThread clientReader;
	private boolean isANS = false;
	private int myport;
	private boolean isRequester = false;

	// 所属的聊天实体
	private TxtChatingInstance chatInstance;

	/**
	 * 构造函数
	 * 
	 * @param chatinstance
	 * @throws IOException
	 */
	public TxtChannel(TxtChatingInstance chatinstance,
			SocketChannel socketChannel) throws IOException {
		this.chatInstance = chatinstance;
		this.hostIp = chatInstance.getDestIp();
		this.hostListenningPort = chatInstance.getDestLisPort();
		this.socketChannel = socketChannel;
		initialize();
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		// 打开监听信道并设置为非阻塞模式
		if (this.socketChannel == null) {
			this.isRequester = true;
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
					hostListenningPort));
		}
		this.myport = socketChannel.socket().getLocalPort();
		socketChannel.configureBlocking(false);
		// 打开并注册选择器到信道
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 启动读取线程
		clientReader = new ReadThread(selector);
	}

	/**
	 * 发送字符串到服务器
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-16"));
		if (socketChannel.isOpen()) {
			socketChannel.write(writeBuffer);
		}
	}

	public void close() {
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

		public ReadThread(Selector selector) {
			this.selector = selector;
			current = Executors.newCachedThreadPool().submit(this);
			running = true;
		}

		public void stop() {
			try {
				running = false;
				this.selector.close();
				current.cancel(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (running) {
					if (selector.select() > 0) {
						// 遍历每个有可用IO操作Channel对应的SelectionKey
						for (SelectionKey sk : selector.selectedKeys()) {
							// 如果该SelectionKey对应的Channel中有可读的数据
							if (sk.isReadable()) {
								// 使用NIO读取Channel中的数据
								SocketChannel clientChannel = (SocketChannel) sk
										.channel();
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								buffer.clear();
								long bytesRead = clientChannel.read(buffer);
								if (bytesRead == -1) {
									// 没有读取到内容的情况
									clientChannel.close();
								} else {
									clientChannel.read(buffer);
									buffer.flip();
									String receivedString = Charset.forName(
											"UTF-16").newDecoder().decode(
											buffer).toString();
									System.out.println("接收到来自服务器"
											+ clientChannel.socket()
													.getRemoteSocketAddress()
											+ "的信息:" + receivedString);
									getMessage(receivedString);
									sk.interestOps(SelectionKey.OP_READ);
								}
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
		this.chatInstance.renderMsg(msg);
	}

	public void answerReq() {
		if (!isANS) {
			// this.sendMsg("{status:1,port:"+this.+"}" );
		}
	}

	public boolean isRequester() {
		return isRequester;
	}

	public void setRequester(boolean isRequester) {
		this.isRequester = isRequester;
	}

	public int getMyport() {
		return this.myport;
	}
}
