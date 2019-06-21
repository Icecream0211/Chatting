package com.bing.common;

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

import net.sf.json.JSONObject;

import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;
import com.xiyou.view.TipWindow;

public class VideoChannel {
	private VideoChatingInstance videoins;
	private MediaReceive reciver;
	private MediaTransmit sender;
	private int myport;
	private int ports[];
	private SocketChannel socketChannel;
	private boolean isRequester;
	private Selector selector;
	private ReadThread clientReader;
	private boolean isStarted = false;

	public VideoChannel(VideoChatingInstance chatins,
			SocketChannel socketChannel) throws IOException {
		this.videoins = chatins;
		this.socketChannel = socketChannel;
		initialize();
	}

	public void initReciver() {
		if (this.reciver == null) {
			initPorts();
			this.reciver = new MediaReceive(ports, this.videoins);
		}
	}

	public void initSender(int[] ports) {
		if (this.sender == null) {
			this.sender = new MediaTransmit(this.videoins.getDestIp(), ports,
					this.videoins);
		}
	}

	private void initialize() throws IOException {
		if (!isStarted) {
			// 打开监听信道并设置为非阻塞模式
			if (this.socketChannel == null) {
				this.isRequester = true;
				socketChannel = SocketChannel.open(new InetSocketAddress(
						videoins.getDestIp(), videoins.getDestLisPort()));
				this.initReciver();
			} else {
				this.initReciver();
				this.initSender(this.videoins.getDestRTPPorts());
			}
			this.myport = socketChannel.socket().getLocalPort();
			socketChannel.configureBlocking(false);
			// 打开并注册选择器到信道
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
			// 启动读取线程
			clientReader = new ReadThread(selector);
			isStarted = true;
		}
	}

	private void initPorts() {
		if (ports == null) {
			ports = new int[2];
			try {
				ports[0] = RandomPort.getRTPPort(CommonConfig.PORT_START - 40
						+ (new Random()).nextInt(10) * 30);
				ports[1] = RandomPort.getRTPPort(CommonConfig.PORT_START - 40
						+ (new Random()).nextInt(10) * 40);
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void close() {
		if (isStarted) {
			try {
				if (this.sender!=null&&this.sender.isStarted())
					this.sender.close();
				if (this.sender!=null&&this.reciver.isStarted())
					this.reciver.close();
				if (this.socketChannel != null && this.socketChannel.isOpen()
						&& this.socketChannel.isConnected()) {
					this.sendMsg("{action:5,status:3}");
					// this.socketChannel.finishConnect();
					this.socketChannel.close();
				}
				if (this.clientReader.running) {
					this.clientReader.stop();
				}
				isStarted = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int[] getPorts() {
		return ports;
	}

	public void setPorts(int[] ports) {
		this.ports = ports;
	}

	public void sendMsg(String msg) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes("UTF-16"));
		socketChannel.write(writeBuffer);
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
										// clientChannel.close();
										videoins.close();
										return;
									} else {
										// 将缓冲区准备为数据传出状态
										buffer.flip();
										// 将字节转化为为UTF-16的字符串
										String receivedString = Charset
												.forName("UTF-16").newDecoder()
												.decode(buffer).toString();
										System.out
												.println("video channel socket revive-->"
														+ receivedString);
										// 准备发送的文本
										// 设置为下一次读取或是写入做准备
										JSONObject jsonObj = JSONObject
												.fromObject(receivedString);
										int actioncode = jsonObj
												.getInt("action");
										System.out.println("recive:--->"
												+ jsonObj.toString());
										int status = jsonObj.getInt("status");
										if (status == 1 && actioncode == 5) {
											int[] abc = new int[2];
											abc[0] = jsonObj
													.getInt("con_port_o");
											abc[1] = jsonObj
													.getInt("con_port_t");
											VideoChannel.this.videoins
													.setDestRTPports(abc);
											initSender(abc);
										} else if (status == 2
												&& actioncode == 5) {
											VideoChannel.this.videoins.close();
											TipWindow
													.createMessageTipWindow(videoins
															.getDestIp()
															+ " refused to recived it");
										} else if (status == 3
												&& actioncode == 5) {
											System.out
													.println("Video chat close");
											VideoChannel.this.videoins.close();
											TipWindow
													.createMessageTipWindow("Video Chat closed");
										}
									}
								} catch (IOException e) {
									e.printStackTrace();
								} catch (Exception e) {
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
}
