package com.bing.multivoice;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * 
 * 把接收到的信息传到麦克，即播放
 */

public class ChartReceive extends Thread {
	// 格式
	private AudioFormat format = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 44100.0f,
			false);
	// 管道
	private SourceDataLine line;
	private byte[] data;

	private MulticastSocket mulSocket;
	private InetAddress multiGroup;

	public ChartReceive(InetAddress group) {
		multiGroup = group;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("receive threading start");
		int length = (int) (format.getFrameSize() * format.getFrameRate() / 2.0f);
		try {
			line.open(format);
			line.start();
			// DatagramSocket socket = new DatagramSocket(ChartSend.PORT);
			mulSocket = new MulticastSocket(ChartSend.PORT);
			mulSocket.joinGroup(multiGroup);
			while (true) {
				// 数组的创建载什么时候，是否影响数据信息？
				data = new byte[length];
				DatagramPacket dp = new DatagramPacket(data, data.length);
				mulSocket.receive(dp);
				System.out.println("dp come from->"
						+ dp.getAddress().getHostAddress());

				boolean ismyself = dp.getAddress().getHostAddress()
						.equals(InetAddress.getLocalHost().getHostAddress());
				System.out.println("local packet?->" + ismyself);
				if (ismyself) {
					continue;
				} else
					line.write(data, 0, data.length);
				/*
				 * System.out.println("receive success " + new String(data,
				 * "UTF-8"));
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
