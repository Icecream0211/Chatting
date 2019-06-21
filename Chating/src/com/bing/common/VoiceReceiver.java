package com.bing.common;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReentrantLock;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.GainChangeEvent;
import javax.media.GainChangeListener;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;

import net.sf.fmj.media.rtp.RTPSocketAdapter;

public class VoiceReceiver implements ReceiveStreamListener,
		ControllerListener, GainChangeListener {
	private Player player;
	private RTPManager mgr;
	private SessionAddress localAddr;
	private SessionAddress targetAddr;
	private int lisport = -1;
	private VoiceProcess process;
	private VoiceSender sender;
	private VoiceChannel channel;
	private RTPSocketAdapter rtpSocket;
	private boolean isStarted = false;
	private VoiceChatingInstance voiceIns;

	public static void main(String[] args) {
		try {
			SessionAddress local = new SessionAddress(
					InetAddress.getLocalHost(), 60000);
			SessionAddress target = new SessionAddress(
					InetAddress.getLocalHost(), 50000);
			// new VoiceReceiver(local, target).start();
			VoiceSender sender = new VoiceSender("192.168.6.68", 50000,null);
			new VoiceReceiver(50000, sender);
			// Thread.sleep(3000);
			/*
			 * new Thread(new Runnable() {
			 * 
			 * @Override public void run() { new VoiceReceiver(50000); }
			 * }).start(); new Thread(new Runnable() {
			 * 
			 * @Override public void run() { new VoiceSender("192.168.6.68",
			 * 50000); } }).start();
			 */
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public VoiceReceiver(RTPManager mgr) {
		/*
		 * this.localAddr = local; this.targetAddr = target;
		 */
		this.mgr = mgr;
		start();
	}

	public VoiceReceiver(int lisport, VoiceChatingInstance voiceIns) {
		this.lisport = lisport;
		// reciveFuture = Executors.newCachedThreadPool().submit(this);
		// this.channel = voiceIns;
		this.voiceIns = voiceIns;
		start();
	}

	public VoiceReceiver(int lisport, VoiceSender sender) {
		this.lisport = lisport;
		// reciveFuture = Executors.newCachedThreadPool().submit(this);
		this.sender = sender;
		start();
	}

	public void start() {
		mgr = RTPManager.newInstance();
		mgr.addReceiveStreamListener(this);
		try {
			if (this.lisport != -1) {
				rtpSocket = new RTPSocketAdapter(InetAddress.getLocalHost(),
						this.lisport);
				System.out.println("recive lisport--->" + this.lisport);
				mgr.initialize(rtpSocket);
			} else {
				mgr.initialize(localAddr);
				mgr.addTarget(targetAddr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidSessionAddressException e) {
			e.printStackTrace();
		}
		BufferControl bc = (BufferControl) mgr
				.getControl("javax.media.control.BufferControl");
		if (bc != null)
			bc.setBufferLength(350);
		/*
		 * addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { disconnect(); }
		 * }); setBounds(100, 100, 200, 50); setMinimumSize(new Dimension(200,
		 * 50)); setDefaultCloseOperation(EXIT_ON_CLOSE); //
		 * setUndecorated(true); setVisible(true);
		 */
		isStarted = true;
	}

	public void setSender(VoiceSender sender) {
		if (this.process != null && sender != null) {
			this.sender = sender;
			this.process.setSender(sender);
		}
	}

	@Override
	public synchronized void update(ReceiveStreamEvent e) {
		if (e instanceof NewReceiveStreamEvent) {
			System.out.println("New ReciveStreamEvent......");
			ReceiveStream rs = ((NewReceiveStreamEvent) e).getReceiveStream();
			DataSource ds = rs.getDataSource();
			// try {
			// player = Manager.createPlayer(ds);
			process = new VoiceProcess(ds, this, voiceIns);
			// setSender(channel.getSender());

			// player = Manager.createRealizedPlayer(ds);
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// } catch (NoPlayerException e1) {
			// e1.printStackTrace();
			// }
			// player.realize();
			// player.addControllerListener(this);
			// player.getGainControl().addGainChangeListener(this);
			// this.initDrag();
		} else if (e instanceof ByeEvent) {
			System.out.println("bye event recived");
			disconnect();
			// this.process.stopPlayer();
		}
	}

	public void disconnect() {
		ReentrantLock lok = new ReentrantLock();
		lok.lock();
		if (this.process != null && this.process.isStarted()) {
			this.process.close();
		}
		if (mgr != null) {
			mgr.removeReceiveStreamListener(this);
			mgr.removeTargets("closing session");
			mgr.dispose();
			mgr = null;
		}
		rtpSocket.close();
		lok.unlock();
	}

	@Override
	public void controllerUpdate(ControllerEvent e) {
		if (e instanceof RealizeCompleteEvent) {
			player.getGainControl().addGainChangeListener(this);
			if (player.getVisualComponent() != null)
				// add(player.getVisualComponent());
				if (player.getControlPanelComponent() != null) {
					player.getControlPanelComponent().setMinimumSize(
							new Dimension(200, 40));
					// add(player.getControlPanelComponent(),
					// BorderLayout.SOUTH);
				}
			/*
			 * if (player.getControl("CachingControl")!= null)
			 * add(player.getControl("CachingControl"));
			 */
			// this.doLayout();
			// pack();
			// this.setVisible(true);
		}
		if (e instanceof PrefetchCompleteEvent) {
			player.start();
		}
	}

	@Override
	public void gainChange(GainChangeEvent ev) {
		System.out.println("gain change");
		System.out.println(player.getGainControl().getLevel());
	}

	/*
	 * @Override public void run() { start(); }
	 */

	public void stop() {
		disconnect();
		isStarted = false;
	}

	public boolean isStarted() {
		return isStarted;
	}
}
