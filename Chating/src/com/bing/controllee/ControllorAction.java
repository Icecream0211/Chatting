package com.bing.controllee;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//（三）被控端接收，响应键盘鼠标事件
public class ControllorAction implements Runnable {
	private Socket socket;
	private ObjectInputStream in;
	private Robot action;
	private boolean isstarted;
	private Future furtur;

	public ControllorAction(Socket soc) {
		this.socket = soc;
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if (!isstarted) {
			furtur = Executors.newCachedThreadPool().submit(this);
			isstarted = true;
		}
	}

	public void run() {
		System.out.println("controllaction runn....");
		try {
			action = new Robot();
			while (isstarted) {
				Object obj = in.readObject(); // 获得鼠标键盘事件
				if (obj != null) {
					handleEvent((InputEvent) obj); // 处理鼠标键盘事件
				}
			}
		} catch (IOException e) {
			stop();
			if (e instanceof EOFException) {

			} else
				e.printStackTrace();
		} catch (AWTException e) {
			stop();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			stop();
			e.printStackTrace();
		}
	}

	private void handleEvent(InputEvent event) {
		MouseEvent mevent = null; // 鼠标事件
		MouseWheelEvent mwevent = null;// 鼠标滚动事件
		KeyEvent kevent = null; // 键盘事件
		int mousebuttonmask = -100; // 鼠标按键

		switch (event.getID()) {
		case MouseEvent.MOUSE_MOVED: // 鼠标移动
			mevent = (MouseEvent) event;
			action.mouseMove(mevent.getX(), mevent.getY());
			break;

		case MouseEvent.MOUSE_PRESSED: // 鼠标键按下
			mevent = (MouseEvent) event;
			action.mouseMove(mevent.getX(), mevent.getY());
			mousebuttonmask = getMouseClick(mevent.getButton());
			if (mousebuttonmask != -100)
				action.mousePress(mousebuttonmask);
			break;

		case MouseEvent.MOUSE_RELEASED: // 鼠标键松开
			mevent = (MouseEvent) event;
			action.mouseMove(mevent.getX(), mevent.getY());
			mousebuttonmask = getMouseClick(mevent.getButton());// 取得鼠标按键
			if (mousebuttonmask != -100)
				action.mouseRelease(mousebuttonmask);
			break;

		case MouseEvent.MOUSE_WHEEL: // 鼠标滚动
			mwevent = (MouseWheelEvent) event;
			action.mouseWheel(mwevent.getWheelRotation());
			break;

		case MouseEvent.MOUSE_DRAGGED: // 鼠标拖拽
			mevent = (MouseEvent) event;
			action.mouseMove(mevent.getX(), mevent.getY());
			break;

		case KeyEvent.KEY_PRESSED: // 按键
			kevent = (KeyEvent) event;
			action.keyPress(kevent.getKeyCode());
			break;

		case KeyEvent.KEY_RELEASED: // 松键
			kevent = (KeyEvent) event;
			action.keyRelease(kevent.getKeyCode());
			break;

		default:
			break;
		}
	}

	private int getMouseClick(int button) { // 取得鼠标按键
		if (button == MouseEvent.BUTTON1) // 左键 ,中间键为BUTTON2
			return InputEvent.BUTTON1_MASK;
		if (button == MouseEvent.BUTTON3) // 右键
			return InputEvent.BUTTON3_MASK;
		return -100;
	}

	public boolean isStarted() {
		return isstarted;
	}

	public void stop() {
		if (isstarted) {
			furtur.cancel(true);
			isstarted = false;
		}
	}
}
