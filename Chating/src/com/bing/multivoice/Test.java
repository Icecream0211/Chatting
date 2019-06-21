package com.bing.multivoice;

import java.io.*;
import java.net.*;

public class Test implements Runnable {
	static MulticastSocket ms = null; // 组播类
	static InetAddress group = null; // ip组
	static DatagramPacket sendPacket = null;
	static DatagramPacket recPacket = null;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 配置信息
		group = InetAddress.getByName("228.8.8.8");
		ms = new MulticastSocket(6789);
		ms.joinGroup(group);
		byte[] b = new byte[100];
		String str = "";
		sendPacket = new DatagramPacket(str.getBytes(), str.length(), group,
				6789);
		recPacket = new DatagramPacket(b, b.length);
		new Thread(new Test()).start();
		// 发送消息
		while (true) {
			str = br.readLine();
			sendPacket.setData(str.getBytes());
			sendPacket.setLength(str.length());
			ms.send(sendPacket);
			System.out.println("发送sendPacket后："
					+ new String(sendPacket.getData()));
		}
	}

	@Override
	public void run() {
		// 接收消息
		while (true) {
			try {
				byte[] b = new byte[100];
				recPacket.setData(b);
				recPacket.setLength(b.length);
				ms.receive(recPacket);
				System.out.println("接收recPacket后："
						+ new String(recPacket.getData()));
				// 打印IP:消息
				System.out.println(recPacket.getAddress().toString()
						+ ":"
						+ new String(recPacket.getData()).substring(0,
								recPacket.getLength()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}