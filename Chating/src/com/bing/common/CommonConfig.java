package com.bing.common;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
import java.util.Properties;

import com.bing.chart.exception.ConfigureException;

public class CommonConfig {
	public static String UI_STILE = "DEFAULT";
	public static final int SYS_SCREEN_WIDTH;
	public static final int SYS_SCREEN_HEIGHT;
	public static String SERVER_IP;
	public static int SERVER_PORT;
	public static String MY_IP;
	public static String MY_HOSTNAME;
	public static int CLIENT_TCP_PORT;
	public static int CLIENT_UDP_PORT;
	public static int BUFFERSIZE = 2048;
	public static boolean islogined = false;
	// 临时配置
	public static int TMP_RTPPORT_1 = 6000;
	public static int TMP_RTPPORT_2 = 6004;
	public static int TMP_RTPPORT_3 = 6008;
	public static int TMP_RTPPORT_4 = 6012;

	public static int PORT_START;
	public static String CONFIG_PATH = "";

	static {
		// 设置屏幕默认尺寸
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		if (null != screen) {
			SYS_SCREEN_WIDTH = screen.width;
			SYS_SCREEN_HEIGHT = screen.height;
		} else {
			SYS_SCREEN_WIDTH = 0;
			SYS_SCREEN_HEIGHT = 0;
		}
		// 设置配置文件之中的所标出来的参数
		try {
			initConfig();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfigureException e) {
			e.printStackTrace();
		}
	}

	private static void initConfig() throws IOException, ConfigureException {
		try {
			Properties pro = new Properties();
			CONFIG_PATH = System.getProperty("user.dir")
					+ "/Chat.properties";
			System.out.println(CONFIG_PATH);
			pro.load(new FileInputStream(CONFIG_PATH));
			SERVER_IP = pro.getProperty("serverip");
			SERVER_PORT = Integer.valueOf(pro.getProperty("serverport"));
			CLIENT_TCP_PORT = Integer.valueOf(pro.getProperty("tcpport"));
			PORT_START = Integer.valueOf(pro.getProperty("portgetstart"));
			MY_IP = InetAddress.getLocalHost().getHostAddress();
			MY_HOSTNAME = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigureException();
		}
	}
}