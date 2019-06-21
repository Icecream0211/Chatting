package com.bing.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.bing.controllee.SurveillanceInstance;

public class SurveilClient {
	private int port1;
	private int port2;
	private Socket imagesocket;
	private Socket controlsocket;
	private ImageReciving reciv;
	private boolean isstarted = false;
	private SurveillanceInstance surveilIns;

	public SurveilClient(int port1, int port2, SurveillanceInstance surveilIns) {
		this.port1 = port1;
		this.port2 = port2;
		this.surveilIns = surveilIns;
	}

	public void start() {
		if (!isstarted) {
			try {
				imagesocket = new Socket(surveilIns.getDestIp(), port1);
				controlsocket = new Socket(surveilIns.getDestIp(), port2);
				Thread.sleep(1000);
				reciv = new ImageReciving(imagesocket, controlsocket,surveilIns);
				reciv.start();
				isstarted = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isStarted(){
		return isstarted;
	}

	public void stop() {
		if (isstarted) {
			try {
				if (reciv.isStarted()) {
					reciv.stop();
				}
				if (!imagesocket.isClosed()) {
					imagesocket.close();
				}
				if (!controlsocket.isClosed()) {
					controlsocket.close();
				}
				isstarted=false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
