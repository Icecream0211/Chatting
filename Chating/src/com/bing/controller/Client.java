package com.bing.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/* 
 客户端的实现：主要实现的功能是从服务器得到服务器桌面的图片，并进行显示。 
 */

public class Client {
	public static String ip = "192.168.6.72";

	public static void main(String[] args) {
		Socket imagesocket;
		Socket controlsocket;
		ImageReciving reciv;
		try {
			imagesocket = new Socket(ip, 12000);
			controlsocket = new Socket(ip, 12000-1);
			Thread.sleep(1000);
			reciv = new ImageReciving(imagesocket, controlsocket,null);
			reciv.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
