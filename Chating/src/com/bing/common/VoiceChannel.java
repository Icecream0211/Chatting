package com.bing.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SessionAddress;

import net.sf.json.JSONObject;

import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;
import com.xiyou.view.TipWindow;

public class VoiceChannel {
	private VoiceReceiver reciver;
	private VoiceSender sender;
	private VoiceChatingInstance voiceIns;
	private int myport;
	private int myRTPport = -1;
	private SocketChannel socketChannel;
	private boolean isRequester;
	private Selector selector;
	private ReadThread clientReader;

	public VoiceChannel(VoiceChatingInstance voiceins) {
		this.voiceIns = voiceins;
		this.iniMyRTPport();
	}

	public VoiceChannel(VoiceChatingInstance voiceinstance,
			SocketChannel socketChannel) throws IOException {
		this(voiceinstance);
		this.socketChannel = socketChannel;
		initialize();
	}

	public void initVoiceReciver() {
		if (this.reciver == null) {
			this.reciver = new VoiceReceiver(this.getMyRTPPort(), this.voiceIns);
		}
	}

	public void initVoiceSender(int destport) {
		if (this.sender == null) {
			this.sender = new VoiceSender(voiceIns.getDestIp(), destport,this.voiceIns);
			this.reciver.setSender(this.sender);
		}
	}

	public void initVoiceSenderandReciver(int destconport) {
		initVoiceReciver();
		initVoiceSender(destconport);
	}

	private void initialize() throws IOException {
		// 打开监听信道并设置为非阻塞模式
		if (this.socketChannel == null) {
			this.isRequester = true;
			socketChannel = SocketChannel.open(new InetSocketAddress(voiceIns
					.getDestIp(), voiceIns.getDestLisPort()));
			this.initVoiceReciver();
		} else {
			this.initVoiceReciver();
			this.initVoiceSender(this.voiceIns.getDestConPort());
		}
		this.myport = socketChannel.socket().getLocalPort();
		socketChannel.configureBlocking(false);
		// 打开并注册选择器到信道
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 启动读取线程
		clientReader = new ReadThread(selector);
	}

	private void iniMyRTPport() {
		try {
			this.myRTPport = RandomPort.getRTPPort(CommonConfig.PORT_START
					+ (new Random()).nextInt(10) * 50);
		} catch (AllPortBeenOccupied e) {
			e.printStackTrace();
		}
	}

	public int getMyRTPPort() {
		if (-1 == this.myRTPport) {
			try {
				this.myRTPport = RandomPort.getRTPPort(CommonConfig.PORT_START
						- 40 + (new Random()).nextInt(10) * 20);
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
		}
		return this.myRTPport;
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
										voiceIns.close();
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
										if (actioncode == 4 && status == 1) {
											initVoiceSender(jsonObj
													.getInt("con_port"));
										} else if (status == 2
												&& actioncode == 4) {
											voiceIns.close();
											TipWindow
													.createMessageTipWindow(voiceIns
															.getDestIp()
															+ " refused to recived it");
										} else if (status == 3
												&& actioncode == 4) {
											voiceIns.close();
											TipWindow
													.createMessageTipWindow("Voice Chat closed");
										} else if (status == 4
												&& actioncode == 4) {
											TipWindow
													.createMessageTipWindow(voiceIns
															.getDestIp()
															+ " have no audio device!");
										}
									}
								} catch (IOException e) {
									voiceIns.close();
									e.printStackTrace();
								} catch (Exception e) {
									voiceIns.close();
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

	public VoiceReceiver getReciver() {
		return reciver;
	}

	public VoiceSender getSender() {
		return sender;
	}

	public void sendMsg(String msg) {
		ByteBuffer writeBuffer;
		try {
			writeBuffer = ByteBuffer.wrap(msg.getBytes("UTF-16"));
			if (socketChannel != null && socketChannel.isConnected()
					&& socketChannel.isOpen()) {
				socketChannel.write(writeBuffer);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (this.sender != null) {
				this.sender.stop();
			}
			if (this.reciver != null) {
				this.reciver.stop();
			}
			if (this.socketChannel != null && this.socketChannel.isOpen()
					&& this.socketChannel.isConnected()) {
				this.sendMsg("{action:4,status:3}");
				this.socketChannel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}