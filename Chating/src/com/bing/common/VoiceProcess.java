package com.bing.common;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DeallocateEvent;
import javax.media.GainChangeEvent;
import javax.media.GainChangeListener;
import javax.media.Manager;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;
import javax.media.Time;
import javax.media.protocol.DataSource;

import com.bing.chat.voice.ui.VoiceView;

public class VoiceProcess implements ControllerListener, GainChangeListener {
	private Player player;
	private DataSource data;
	private VoiceReceiver reciver;
	private VoiceView voiceView;
	private VoiceSender sender;
	private boolean isStarted = false;
	// private VoiceChannel channel;
	private VoiceChatingInstance voiceIns;

	public VoiceProcess(DataSource source, VoiceReceiver reci) {
		this.reciver = reci;
		this.data = source;
		this.voiceView = new VoiceView(data, this, null);
		// frame = new JFrame();
		// Executors.newCachedThreadPool().execute(this);
		Tstart();
	}

	public VoiceProcess(DataSource source, VoiceReceiver reci,
			VoiceSender sender) {
		this.reciver = reci;
		this.data = source;
		this.sender = sender;
		this.voiceView = new VoiceView(data, this, null);
		// frame = new JFrame();
		// Executors.newCachedThreadPool().execute(this);
		Tstart();
	}

	public VoiceProcess(DataSource source, VoiceReceiver reci,
			VoiceChatingInstance channel) {
		this.reciver = reci;
		this.data = source;
		this.voiceIns = channel;
		this.voiceView = new VoiceView(data, this, voiceIns);
		start();
	}

	private void start() {
		System.out.println("Voice PRocess started.......");
		// Executors.newCachedThreadPool().execute(this);
		Tstart();
	}

	public void setSender(VoiceSender sender) {
		this.sender = sender;
		start();
	}

	@Override
	public void gainChange(GainChangeEvent ev) {
		System.out.println("gain change");
		System.out.println(player.getGainControl().getLevel());
	}

	@Override
	public void controllerUpdate(ControllerEvent e) {
		if (e instanceof RealizeCompleteEvent) {
			System.out.println("New RealizedCompleteEvent /////");
			this.player.getGainControl().addGainChangeListener(this);
			this.voiceView.showUp();
		}
		player.prefetch();
		if (e instanceof PrefetchCompleteEvent) {
			player.start();
		}
		if (e instanceof StopEvent) {
			System.out.println("New StopEvent /////");
			player.close();
			player.deallocate();
		}
		if (e instanceof DeallocateEvent) {
			System.out.println("New DeallocateEvent /////");
		}
	}

	public void stopPlayer() {
		player.stop();
		player.close();
	}

	/*
	 * @Override public void run() {
	 * 
	 * frame = new JFrame(); frame.pack(); frame.setVisible(true);
	 * 
	 * }
	 */

	private void Tstart() {
		if (!this.isStarted) {
			try {
				player = Manager.createPlayer(data);
				player.addControllerListener(this);
				player.realize();
				// player.start();
				this.isStarted = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void soundOn() {
		this.player.getGainControl().setMute(false);
	}

	public void soundOff() {
		System.out.println("soundoff");
		this.player.getGainControl().setMute(true);
		// TODO Auto-generated method stub
	}

	public void playoff() {
		System.out.println("playoff");
		// TODO Auto-generated method stub
		// this.player.stop();
		this.sender.suspend();

	}

	public void playon() {
		System.out.println("playon");
		// this.player.start();
		this.sender.resume();
	}

	public Time getMediaTime() {
		return this.player.getMediaTime();
	}

	public void setSoundVolume(int value) {
		System.out.println("set valu->" + value);
		this.player.getGainControl().setLevel((float) (value / 100.0));
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void close() {
		//
		/*
		 * if(sender.isStarted()){ this.sender.stop(); }
		 * if(this.reciver.isStarted()){ this.reciver.stop(); }
		 */
		if (player != null) {
			// player.close();
			player.stop();
		}
		this.data.disconnect();
		if (voiceView != null) {
			voiceView.dispose();
		}
	}
}
