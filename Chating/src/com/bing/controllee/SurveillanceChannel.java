package com.bing.controllee;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.bing.common.CommonConfig;
import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;
import com.bing.controller.SurveilClient;
import com.xiyou.view.TipWindow;

import net.sf.json.JSONObject;

public class SurveillanceChannel {
	private int myport;
	private SocketChannel socketChannel;
	private boolean isRequester;
	private Selector selector;
	private ReadThread clientReader;
	private int animationport = -1;
	private int animationCTLport = -1;
	private SurveillanceInstance surveilIns;
	private SurveilServer server;
	private SurveilClient client;

	public SurveillanceChannel(SurveillanceInstance surveIns,
			SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		this.surveilIns = surveIns;
		initialize();
	}

	private void initialize() throws IOException {
		// 打开监听信道并设置为非阻塞模式
		if (this.socketChannel == null) {
			this.isRequester = true;
			socketChannel = SocketChannel.open(new InetSocketAddress(surveilIns
					.getDestIp(), surveilIns.getDestLisPort()));
		} else {
			initImgSendServer();
		}
		this.myport = socketChannel.socket().getLocalPort();
		socketChannel.configureBlocking(false);
		// 打开并注册选择器到信道
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 启动读取线程
		clientReader = new ReadThread(selector);
	}

	private void initImgSendServer() {
		server = new SurveilServer(this.getAnimationPort(),
				this.getAnimationCtlPort());
		server.start();
	}

	private void initImgRecieveServer(int port1, int port2) {
		client = new SurveilClient(port1, port2, this.surveilIns);
		client.start();
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
						for (SelectionKey key : selector.selectedKeys()) {
							// 如果该SelectionKey对应的Channel中有可读的数据
							if (key.isReadable()) {
								SocketChannel clientChannel = (SocketChannel) key
										.channel();
								// 得到并清空缓冲区
								ByteBuffer buffer = (ByteBuffer) key
										.attachment();
								if (buffer == null) {
									buffer = ByteBuffer.allocate(1024);
								}
								buffer.clear();
								// 读取信息获得读取的字节数
								try {
									long bytesRead = clientChannel.read(buffer);
									if (bytesRead == -1) {
										// 没有读取到内容的情况
										// fileIns.close();
										return;
									} else {
										// 将缓冲区准备为数据传出状态
										buffer.flip();
										// 将字节转化为为UTF-16的字符串
										String receivedString = Charset
												.forName("UTF-16").newDecoder()
												.decode(buffer).toString();
										System.out.println(receivedString);
										// 准备发送的文本
										// 设置为下一次读取或是写入做准备
										JSONObject jsonObj = JSONObject
												.fromObject(receivedString);
										int actioncode = jsonObj
												.getInt("action");
										int status = jsonObj.getInt("status");
										if (actioncode == 11 && status == 1) {
											int port1 = jsonObj
													.getInt("con_port_o");
											int port2 = jsonObj
													.getInt("con_port_t");
											initImgRecieveServer(port1, port2);
										} else if (status == 2
												&& actioncode == 11) {
											surveilIns.close();
											TipWindow
													.createMessageTipWindow(surveilIns
															.getDestIp()
															+ " reject to recive remote controlled");
										} else if (status == 3
												&& actioncode == 11) {
											surveilIns.close();
											TipWindow
													.createMessageTipWindow(surveilIns
															.getDestIp()
															+ "  remote controlled stopped");
										}
									}
								} catch (IOException e) {
									surveilIns.close();
									e.printStackTrace();
								} catch (Exception e) {
									surveilIns.close();
									e.printStackTrace();
								}
								key.interestOps(SelectionKey.OP_READ);
							}
							// 删除正在处理的SelectionKey
							selector.selectedKeys().remove(key);
						}
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void sendMsg(String msg) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes("UTF-16"));
		if (socketChannel != null && socketChannel.isConnected()
				&& socketChannel.isOpen()) {
			socketChannel.write(writeBuffer);
		}
	}

	public void close() {
		try {
			if (server!=null&&server.isStarted()) {
				server.stop();
			}
			if(client!=null&&client.isStarted()){
				client.stop();
			}
			if (this.socketChannel != null&&socketChannel.isOpen()) {
				this.sendMsg("{action:11,status:3}");
				this.socketChannel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getAnimationPort() {
		if (this.animationport == -1) {
			try {
				this.animationport = RandomPort.getPort(CommonConfig.PORT_START
						+ (new Random()).nextInt(10));
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
		}
		return this.animationport;
	}

	public int getAnimationCtlPort() {
		if (this.animationCTLport == -1) {
			try {
				this.animationCTLport = RandomPort
						.getPort(CommonConfig.PORT_START
								+ (new Random()).nextInt(10));
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
		}
		return this.animationCTLport;
	}
}
