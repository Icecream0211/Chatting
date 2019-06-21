package com.bing.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChatingServer {
	// 对方的socketchannel
	// 发送数据的socketchannel
	private Selector selector;
	private ServerSocketChannel chatServer;
	private ServerReadThread readThread;
	private boolean isstarted = false;

	public ChatingServer() {
		init();
	}

	public void stop() {
		if (isstarted) {
			this.readThread.stop();
			try {
				if (selector.isOpen()) {
					selector.close();
				}
				if (chatServer.isOpen()) {
					chatServer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				isstarted = false;
			}
		}
	}

	private void init() {
		if (!isstarted) {
			// 创建选择器
			try {
				selector = Selector.open();
				// 打开监听信道
				chatServer = ServerSocketChannel.open();
				// 与本地端口绑定
				chatServer.socket().bind(
						new InetSocketAddress(CommonConfig.CLIENT_TCP_PORT));
				// 设置为非阻塞模式
				chatServer.configureBlocking(false);
				// 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
				chatServer.register(selector, SelectionKey.OP_ACCEPT);
				// 创建一个处理协议的实现类,由它来具体操作
				// 反复循环,等待IO
				readThread = new ServerReadThread(selector);
				isstarted = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ServerReadThread implements Runnable {
		private Selector selector;
		private Future current;
		private boolean isrunned = false;;

		public ServerReadThread(Selector selector) {
			this.selector = selector;
			this.current = Executors.newCachedThreadPool().submit(this);
			this.isrunned = true;
		}

		@Override
		public void run() {
			try {
				while (selector.select() > 0) {
					// 等待某信道就绪(或超时)
					// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
					Iterator<SelectionKey> keyIter = selector.selectedKeys()
							.iterator();
					while (keyIter.hasNext()) {
						SelectionKey key = keyIter.next();
						keyIter.remove();
						try {
							if (key.isAcceptable()) {
								// 有客户端连接请求时
								SocketChannel clientChannel = ((ServerSocketChannel) key
										.channel()).accept();
								clientChannel.configureBlocking(false);
								clientChannel.register(key.selector(),
										SelectionKey.OP_READ,
										ByteBuffer.allocate(1024));
								key.interestOps(SelectionKey.OP_ACCEPT);
							}

							if (key.isReadable()) {
								CommandHall.handRead(key);
								key.cancel();
							}

							if (key.isValid() && key.isWritable()) {
								// 客户端可写时
								// protocol.handleWrite(key);
							}
						} catch (IOException ex) {
							// 出现IO异常（如客户端断开连接）时移除处理过的键
							continue;
						}
					}
				}
			} catch (Exception e) {
			}
		}

		public void stop() {
			try {
				if (isrunned) {
					this.current.cancel(true);
				}
				if (this.selector.isOpen()) {
					this.selector.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
