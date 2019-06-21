package com.bing.chat.filesend;

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

import javax.swing.JProgressBar;

import net.sf.json.JSONObject;

import com.bing.common.CommonConfig;
import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;
import com.xiyou.view.TipWindow;

public class FileTransChannel {
	private int myport;
	private SocketChannel socketChannel;
	private boolean isRequester;
	private Selector selector;
	private ReadThread clientReader;
	private int filelisport = -1;
	private FileReciveThread fileRecive;
	private FileSendThread filesend;
	private FileTransInstance fileIns;

	public FileTransChannel(FileTransInstance fileIns,
			SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		this.fileIns = fileIns;
		initialize();
	}

	private void initialize() throws IOException {
		// 打开监听信道并设置为非阻塞模式
		if (this.socketChannel == null) {
			this.isRequester = true;
			socketChannel = SocketChannel.open(new InetSocketAddress(fileIns
					.getDestIp(), fileIns.getDestLisPort()));
		} else {
			this.initFileRecive();
		}
		this.myport = socketChannel.socket().getLocalPort();
		socketChannel.configureBlocking(false);
		// 打开并注册选择器到信道
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 启动读取线程
		clientReader = new ReadThread(selector);
	}

	public void initFileRecive() {
		if (filelisport == -1) {
			this.initFileLisPort();
		}
		ProgressView view = TipWindow.createProgressTipWindow(fileRecive,
				(int) this.fileIns.getFileSelected().length(), 4);
		fileRecive = new FileReciveThread(this.fileIns, view.getProgressBar(),
				filelisport);
		view.setIFielThread(fileRecive);
		fileRecive.start();
	}

	/*
	 * private JProgressBar returnBar(IFileThread filthr,int length,int type){
	 * 
	 * }
	 */

	private void initFileLisPort() {
		if (filelisport == -1) {
			try {
				this.filelisport = RandomPort.getPort(CommonConfig.PORT_START
						+ (new Random()).nextInt(10) * 50);
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
		}
	}

	public int getFileLisPort() {
		if (this.filelisport == -1) {
			this.initFileLisPort();
		}
		return this.filelisport;
	}

	public void initFileSend(int port) {
		ProgressView view = TipWindow.createProgressTipWindow(filesend,
				(int) this.fileIns.getFileSelected().length(), 3);
		filesend = new FileSendThread(this.fileIns, view.getProgressBar(), port);
		view.setIFielThread(filesend);
		filesend.start();
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
										fileIns.close();
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
										if (actioncode == 10 && status == 1) {
											int fileport = jsonObj
													.getInt("file_port");
											initFileSend(fileport);
										} else if (status == 2
												&& actioncode == 10) {
											TipWindow
													.createMessageTipWindow(fileIns
															.getDestIp()
															+ " refused to recived it");
											fileIns.close();
										} else if (status == 3
												&& actioncode == 10) {
											fileIns.close();
											TipWindow
													.createMessageTipWindow("File recived closed");
										}
									}
								} catch (IOException e) {
									// voiceIns.close();
									fileIns.close();
									e.printStackTrace();
								} catch (Exception e) {
									// voiceIns.close();
									fileIns.close();
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
			if (this.socketChannel != null) {
				if (!isRequester) {
					this.sendMsg("{action:10,status:3}");
				}
				this.socketChannel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
