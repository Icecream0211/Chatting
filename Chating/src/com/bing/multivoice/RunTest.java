package com.bing.multivoice;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RunTest {
	public static void main(String[] args) {
		try {
			InetAddress group = InetAddress.getByName("228.8.8.8");
			ChartReceive recive = new ChartReceive(group);
			recive.start();
			ChartSend send = new ChartSend(group);
			send.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
