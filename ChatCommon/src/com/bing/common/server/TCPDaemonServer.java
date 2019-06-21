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
		// ����ѡ����
		selector = Selector.open();
		// �򿪼����ŵ�
		ServerSocketChannel listenerChannel = ServerSocketChannel.open();
		// �뱾�ض˿ڰ�
		listenerChannel.socket().bind(new InetSocketAddress(tcpport));
		// ����Ϊ������ģʽ
		listenerChannel.configureBlocking(false);
		// ��ѡ�����󶨵������ŵ�,ֻ�з������ŵ��ſ���ע��ѡ����.����ע�������ָ�����ŵ����Խ���Accept����
		listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
		// ����һ������Э���ʵ����,�������������
		// TCPProtocol protocol = new TCPProtocolImpl(BufferSize);
		// ����ѭ��,�ȴ�IO
		while (true) {
			// �ȴ�ĳ�ŵ�����(��ʱ)
			if (selector.select(TimeOut) == 0) {
				continue;
			}

			// ȡ�õ�����.selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				try {
					if (key.isAcceptable()) {
						handAccept(key);
						// �пͻ�����������ʱ
						// protocol.handleAccept(key);
					}

					if (key.isReadable()) {
						handRead(key);
						// �ӿͻ��˶�ȡ����
						// protocol.handleRead(key);
						// clientChannel = (SocketChannel) key.channel();
					}

					if (key.isValid() && key.isWritable()) {
						handWrite(key);
						// �ͻ��˿�дʱ
						// protocol.handleWrite(key);
					}
				} catch (Exception ex) {
					// ����IO�쳣����ͻ��˶Ͽ����ӣ�ʱ�Ƴ�������ļ�
					keyIter.remove();
					continue;
				}
				// �Ƴ�������ļ�
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
