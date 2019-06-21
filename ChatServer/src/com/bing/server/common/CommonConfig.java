package com.bing.server.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommonConfig {
	public static String SERVER_HOST_IP;
	public static String SERVER_HOST_NAME;
	public static int SERVER_DEFAULT_PORT = 9999;
	public static int SERVER_PORT;

	static {
		try {
			SERVER_HOST_IP = InetAddress.getLocalHost().getHostAddress();
			SERVER_HOST_NAME = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}