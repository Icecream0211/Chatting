package com.bing.server.utility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import net.sf.json.JSONObject;

import com.bing.server.copepro.LoginPro;
import com.bing.server.copepro.LogoutPro;
import com.bing.server.copepro.UserListPro;

public class ServerHander {
	private static int bufferSize = 1024;

	public static void handleAccept(SelectionKey key) throws IOException {
		SocketChannel clientChannel = ((ServerSocketChannel) key.channel())
				.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ,
				ByteBuffer.allocate(bufferSize));
	}

	public static void handleRead(SelectionKey key) throws IOException {
		// 获得与客户端通信的信道
		SocketChannel clientChannel = (SocketChannel) key.channel();

		// 得到并清空缓冲区
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		if (buffer == null) {
			buffer = ByteBuffer.allocate(bufferSize);
		}
		buffer.clear();
		// 读取信息获得读取的字节数
		long bytesRead = clientChannel.read(buffer);
		if (bytesRead == -1) {
			// 没有读取到内容的情况
			clientChannel.close();
		} else {
			// 将缓冲区准备为数据传出状态
			buffer.flip();
			// 将字节转化为为UTF-16的字符串
			String receivedString = Charset.forName("UTF-16").newDecoder()
					.decode(buffer).toString();
			/*System.out.println("接收到来自"
					+ clientChannel.socket().getRemoteSocketAddress() + "的信息:"
					+ receivedString);*/
			try {
				JSONObject cliobj = JSONObject.fromObject(receivedString);
				handRequest(clientChannel, cliobj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	private static void handRequest(SocketChannel socketChannel,
			JSONObject jsonobject) {
		ClientInstance ins = new ClientInstance();
		switch (jsonobject.getInt("action")) {
		case 1: // 1 login
			new LoginPro(jsonobject, socketChannel).start();
			break;
		case 2:// 2 logout
			new LogoutPro(jsonobject, socketChannel).start();
			break;
		case 8:// 8 Userlist
			new UserListPro(jsonobject, socketChannel).start();
			break;
		}
	}
}
