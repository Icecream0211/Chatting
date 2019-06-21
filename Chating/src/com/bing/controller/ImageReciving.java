package com.bing.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.bing.controllee.SurveillanceInstance;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class ImageReciving implements Runnable {
	private Socket socket;
	private Socket controlsocket;
	private JPEGImageDecoder decoder;
	private ControlledScreenPanel screenShow;
	private boolean isstarted = false;
	private Future reciveFuture;
	private MainControlSocket msocket;
	private SurveillanceInstance surveilIns;

	public ImageReciving(Socket soc, Socket csocket,
			SurveillanceInstance surveilIns) {
		this.socket = soc;
		this.surveilIns = surveilIns;
		if (csocket != null) {
			this.controlsocket = csocket;
			msocket = new MainControlSocket(this.controlsocket);
		}
		screenShow = new ControlledScreenPanel(msocket, this.surveilIns);
	}

	public void start() {
		if (!isstarted) {
			init();
			isstarted = true;
			this.reciveFuture = Executors.newCachedThreadPool().submit(this);
		}
	}

	public void stop() {
		try {
			this.reciveFuture.cancel(true);
			if (!this.socket.isClosed()) {
				this.socket.close();
			}
			this.isstarted = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		try {
			decoder = JPEGCodec.createJPEGDecoder(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		BufferedImage image = null;
		try {
			while (isstarted) {
				System.out.println("recive......");
				try {
					image = decoder.decodeAsBufferedImage();
				} catch (ImageFormatException e) {
				}
				if (image != null) {
					/* 显示图像 */
					/*
					 * ImageTools .writeTtt(image, "D:/sss/" +
					 * Calendar.getInstance().getTimeInMillis() + ".jpg");
					 */
					screenShow.setBufferedImage(image);
				}
			}
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			stop();
		}
	}

	public boolean isStarted() {
		return isstarted;
	}
}
