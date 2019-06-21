package com.bing.controllee;

/*********************************************************************** 
 *************************************************************************/
/* 
 服务器端的实现： 
 主要实现采集服务器的桌面，并多线程实现给客户端的发送 
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private final static int PORT = 12000;

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(PORT);
			ServerSocket server2 = new ServerSocket(PORT - 1);
			new ShotThread(server).start();
			new ShotThread2(server2).start();
			System.out.println("init over");
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	} // end main
}

/**
 * 多线程的连接：
 * 
 */
class ShotThread extends Thread {
	ServerSocket server;
	int i = 0;

	public ShotThread(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		Socket connection = null;
		try {
			while (true) {
				connection = server.accept();
				handSocket(connection, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handSocket(Socket socket, int i) {
		System.out.println("shotthread handsocket...imagesending will starting");
		new ImageSending(socket).start();
	}
}

class ShotThread2 extends Thread {
	ServerSocket server;
	int i = 0;

	public ShotThread2(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		Socket connection = null;
		try {
			while (true) {
				connection = server.accept();
				handSocket(connection, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void handSocket(Socket socket, int i) {
		System.out.println("shotthread2 handsocket...controlloraction will starting");
		new ControllorAction(socket).start();
	}
}