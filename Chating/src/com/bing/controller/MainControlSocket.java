package com.bing.controller;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*-发送鼠标，键盘事件到被控端-*/
public class MainControlSocket {
	private Socket socket = null;
	private ObjectOutputStream out = null; // 事件对象发送封装
	private int lisport = -1;
	private String destip;

	public MainControlSocket(Socket soc) {
		this.socket = soc;
		this.lisport = this.socket.getPort();
		this.destip = this.socket.getInetAddress().getHostAddress();
		try {
			out = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MainControlSocket(int port) {
		this.lisport = port;
	}

	public void sendControlledAction(InputEvent event) {
		/*
		 * MouseEvent event2 = new MouseEvent(me.getSource(), me.getID(),
		 * me.getWhen(), me.getModifiers(), x, y, xAbs, yAbs,
		 * me.getClickCount(), me.isPopupTrigger(), me.getButton());
		 */

		try {
			System.out.println("sendControlled Action");
			if (this.socket.isClosed()) {
				System.out.println("re-conneced ip:" + this.destip + ";port:"
						+ lisport);
				this.socket = new Socket(this.destip, this.lisport);
				out = new ObjectOutputStream(this.socket.getOutputStream());
			}
			out.writeObject(event);
			// out.write(null);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!socket.isClosed()) {
					out.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}