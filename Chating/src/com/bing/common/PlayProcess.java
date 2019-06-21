package com.bing.common;

import java.util.concurrent.Executors;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;
import javax.media.protocol.DataSource;

public class PlayProcess implements ControllerListener {

	private DataSource data;
	private Player player;
	private String destination;
	private String type;
	private VideoChatingInstance mainPlaypanel;
	private boolean isstarted = false;

	public PlayProcess(DataSource data, String destination, String type) {
		this.data = data;
		this.destination = destination;
		this.type = type;
		// Executors.newCachedThreadPool().execute(this);
		start();
	}

	public PlayProcess(DataSource data2, VideoChatingInstance videoIns,
			String type2) {
		this.data = data2;
		this.type = type2;
		this.mainPlaypanel = videoIns;
		start();
		// Executors.newCachedThreadPool().execute(this);
	}

	public void setMainpanel(VideoChatingInstance mainpanel) {
		this.mainPlaypanel = mainpanel;
	}

	public void start() {
		if (!isstarted) {
			try {
				player = Manager.createPlayer(data);
				player.addControllerListener(this);
				player.realize();
				// player.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isstarted = true;
		}
	}

	public boolean isStarted(){
		return isstarted;
	}

	/*
	 * @Override public void run() { start(); }
	 */
	public void stopPlayer() {
		player.close();
	}
	
	public void close(){
		if(player!=null){
			player.stop();
		}
		this.data.disconnect();
	}

	public void controllerUpdate(ControllerEvent e) {
		if (e instanceof RealizeCompleteEvent) {
			if (player.getVisualComponent() != null) {
				if (type.equals("local")) {
					System.out.println("type===local");
					mainPlaypanel.playLocalView(player.getVisualComponent());
					/*
					 * UIMap.viewUIMap.get(destination).addLocalPlayWindow(
					 * player.getVisualComponent());
					 */
				} else {
					System.out.println("type===remote");
					mainPlaypanel.playRemoteView(player.getVisualComponent());
					/*
					 * UIMap.viewUIMap.get(destination).addRemotePlayWindow(
					 * player.getVisualComponent());
					 */
				}
			}
			player.prefetch();
		}
		if (e instanceof PrefetchCompleteEvent) {
			player.start();
			if (type.equals("local")) {
				// UIMap.viewUIMap.get(destination).localReady();
			} else {
				// UIMap.viewUIMap.get(destination).remoteReady();
			}
		}
		if(e instanceof StopEvent){
			player.deallocate();
			player.close();
		}
	}
}
