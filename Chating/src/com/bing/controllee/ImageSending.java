package com.bing.controllee;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageSending implements Runnable {
	public static final int IMAGE_GETTIME = 500;
	private Socket socket;
	private JPEGImageEncoder encoder;
	private ShotImage shotImage;
	private boolean isstarted = false;
	private Future sendFuture;

	public ImageSending(Socket soc) {
		this.socket = soc;
	}

	public void start() {
		if (!isstarted) {
			try {
				encoder = JPEGCodec
						.createJPEGEncoder(this.socket.getOutputStream());
				shotImage = new ShotImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isstarted = true;
			this.sendFuture = Executors.newCachedThreadPool().submit(this);
		}
	}

	private void init() {
	}

	public void stop() {
		try {
			this.sendFuture.cancel(true);
			if (!this.socket.isClosed()) {
				this.socket.close();
			}
			this.isstarted = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isStarted() {
		return isstarted;
	}

	@Override
	public void run() {
		System.out.println("sendimage run....");
		BufferedImage image;
		try {
			while (isstarted) {
				System.out.println("sending.....");
				image = shotImage.snapShot(); // 获取屏幕图像
				ImageIO.write(image, "jpeg", this.socket.getOutputStream());
				//encoder.encode(image);// 发送图像给主控端
				this.socket.getOutputStream().flush();
				Thread.sleep(IMAGE_GETTIME); // 图像采集时间间隔
			}
		} catch (ImageFormatException e) {
			stop();
			e.printStackTrace();
		} catch (IOException e) {
			stop();
			e.printStackTrace();
		} catch (InterruptedException e) {
			stop();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
