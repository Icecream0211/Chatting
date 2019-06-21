package com.bing.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import net.sf.json.JSONObject;

import com.bing.chart.main.Main;
import com.bing.command.FileTransReciveCommand;
import com.bing.command.LoginRespondCommand;
import com.bing.command.ServerExitNotificationCommand;
import com.bing.command.SurveilReciveCommand;
import com.bing.command.TxtChatReceiveCommand;
import com.bing.command.UserAddNotificationCommand;
import com.bing.command.UserExitNotificationCommand;
import com.bing.command.UserListRespondCommand;
import com.bing.command.VideoChatReciveCommand;
import com.bing.command.VoiceChatReciveCommand;

public class CommandHall {
	public static void handRead(SelectionKey key) {
		SocketChannel clientChannel = (SocketChannel) key.channel();
		// 得到并清空缓冲区
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		if (buffer == null) {
			buffer = ByteBuffer.allocate(1024);
		}
		buffer.clear();
		// 读取信息获得读取的字节数
		try {
			long bytesRead = clientChannel.read(buffer);
			if (bytesRead == -1) {
				// 没有读取到内容的情况
				clientChannel.close();
				key.cancel();
				return;
			} else {
				// 将缓冲区准备为数据传出状态
				buffer.flip();
				// 将字节转化为为UTF-16的字符串
				String receivedString = Charset.forName("UTF-16").newDecoder()
						.decode(buffer).toString();
				System.out.println(receivedString);
				// 准备发送的文本
				// 设置为下一次读取或是写入做准备
				JSONObject jsonObj = JSONObject.fromObject(receivedString);
				int actioncode = jsonObj.getInt("action");
				switch (actioncode) {
				case 1:
					new LoginRespondCommand(jsonObj).exec();
					break;
				case 3:
					createTxtCommand(clientChannel, jsonObj);
					break;
				case 4:
					createVoiceCommand(clientChannel, jsonObj);
					break;
				case 5:
					createVideoCommand(clientChannel, jsonObj);
					break;
				case 6:
					new UserExitNotificationCommand(jsonObj).exec();
					break;
				case 7:
					new UserAddNotificationCommand(jsonObj).exec();
					break;
				case 8:
					new UserListRespondCommand(jsonObj).exec();
					break;
				case 9:
					new ServerExitNotificationCommand().exec();
					break;
				case 10:
					new FileTransReciveCommand(clientChannel, jsonObj).exec();
					break;
				case 11:
					new SurveilReciveCommand(clientChannel, jsonObj).exec();
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Main.getInstance().stop();
		} catch (Exception e) {
			e.printStackTrace();
			Main.getInstance().stop();
		}
	}

	private static void createTxtCommand(SocketChannel socketChannel,
			JSONObject jsonObj) {
		TxtChatReceiveCommand txtcommand = new TxtChatReceiveCommand(
				socketChannel, jsonObj);
		txtcommand.exec();
	}

	private static void createVoiceCommand(SocketChannel socketChannel,
			JSONObject jsonObj) {
		VoiceChatReciveCommand newvoice = new VoiceChatReciveCommand(
				socketChannel, jsonObj);
		newvoice.exec();

	}

	private static void createVideoCommand(SocketChannel socketChannel,
			JSONObject jsonObj) {
		VideoChatReciveCommand newvideo = new VideoChatReciveCommand(
				socketChannel, jsonObj);
		newvideo.exec();
	}

}
