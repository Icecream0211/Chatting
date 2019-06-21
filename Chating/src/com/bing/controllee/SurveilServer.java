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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SurveilServer {
	private int animationPort = -1;
	private int animationCTLPort = -1;
	private ShotThreadImgSend animationThr;
	private ShotThreadCTL animationCTLThr;
	private boolean isstarted = false;

	public SurveilServer(int animationPort, int animationCTLPort) {
		this.animationPort = animationPort;
		this.animationCTLPort = animationCTLPort;
	}

	public void start() {
		if (!isstarted && this.animationPort != -1
				&& this.animationCTLPort != -1) {
			try {
				ServerSocket serSocket1 = new ServerSocket(this.animationPort);
				animationThr = new ShotThreadImgSend(serSocket1);
				ServerSocket serSocket2 = new ServerSocket(
						this.animationCTLPort);
				animationCTLThr = new ShotThreadCTL(serSocket2);
				animationThr.start();
				animationCTLThr.start();
				isstarted = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isStarted() {
		return isstarted;
	}

	public void stop() {
		if (this.animationThr.isStarted()) {
			this.animationThr.stop();
		}
		if (this.animationCTLThr.isStarted()) {
			this.animationCTLThr.stop();
		}
	}

	/**
	 * 多线程的连接：
	 * 
	 */
	class ShotThreadImgSend implements Runnable {
		private ServerSocket server;
		private boolean isstarted = false;
		private Future furtur;
		private ImageSending imsend;

		public ShotThreadImgSend(ServerSocket server) {
			this.server = server;
		}

		public void start() {
			if (!isstarted) {
				furtur = Executors.newCachedThreadPool().submit(this);
				isstarted = true;
			}
		}

		public void stop() {
			if (isstarted) {
				try {
					if (!server.isClosed()) {
						server.close();
					}
					furtur.cancel(true);
					if (imsend.isStarted()) {
						imsend.stop();
					}
					isstarted = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void run() {
			Socket connection = null;
			try {
				while (isstarted) {
					connection = server.accept();
					handSocket(connection, 0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public boolean isStarted() {
			return isstarted;
		}

		public void handSocket(Socket socket, int i) {
			imsend = new ImageSending(socket);
			imsend.start();
		}
	}

	class ShotThreadCTL implements Runnable {
		private ServerSocket server;
		private boolean isstarted = false;
		private Future furtur;
		private ControllorAction ctlAction;

		public ShotThreadCTL(ServerSocket server) {
			this.server = server;
		}

		public void start() {
			if (!isstarted) {
				furtur = Executors.newCachedThreadPool().submit(this);
				isstarted = true;
			}
		}

		public void stop() {
			if (isstarted) {
				try {
					if (ctlAction.isStarted()) {
						ctlAction.stop();
					}
					if (!server.isClosed()) {
						server.close();
					}
					furtur.cancel(false);
					isstarted = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public boolean isStarted() {
			return isstarted;
		}

		public void run() {
			Socket connection = null;
			try {
				while (isstarted) {
					connection = server.accept();
					handSocket(connection, 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void handSocket(Socket socket, int i) {
			ctlAction = new ControllorAction(socket);
			ctlAction.start();
		}
	}
}
