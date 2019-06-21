package com.bing.voiceTest;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.GainChangeEvent;
import javax.media.GainChangeListener;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
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
import javax.swing.JFrame;

public class VoiceReceiver extends JFrame implements ReceiveStreamListener,
		ControllerListener, GainChangeListener,Runnable {
	private Player player;
	private static RTPManager mgr;
	private SessionAddress localAddr;
	private SessionAddress targetAddr;

	public static void main(String[] args) {
		try {
			SessionAddress local = new SessionAddress(InetAddress.getLocalHost(),50000);
			SessionAddress target = new SessionAddress(InetAddress.getLocalHost(),60000);		
			//new VoiceSender(local,target);
			mgr = RTPManager.newInstance();
			mgr.initialize(local);
			mgr.addTarget(target);
			new VoiceReceiver(mgr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (InvalidSessionAddressException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public VoiceReceiver(RTPManager mgr) {
/*		this.localAddr = local;
		this.targetAddr = target;*/
		this.mgr = mgr;
		Executors.newCachedThreadPool().execute(this);
	}

	public void start() {
		//mgr = RTPManager.newInstance();
		mgr.addReceiveStreamListener(this);
		/*try {
			mgr.initialize(localAddr);
			mgr.addTarget(targetAddr);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidSessionAddressException e) {
			e.printStackTrace();
		}*/
		BufferControl bc = (BufferControl) mgr
				.getControl("javax.media.control.BufferControl");
		if (bc != null)
			bc.setBufferLength(350);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
		setBounds(100, 100, 200, 50);
		setMinimumSize(new Dimension(200, 50));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setUndecorated(true);
		setVisible(true);
	}

	@Override
	public void update(ReceiveStreamEvent e) {
		if (e instanceof NewReceiveStreamEvent) {
			ReceiveStream rs = ((NewReceiveStreamEvent) e).getReceiveStream();
			DataSource ds = rs.getDataSource();
			try {
				// player = Manager.createPlayer(ds);
				player = Manager.createRealizedPlayer(ds);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoPlayerException e1) {
				e1.printStackTrace();
			} catch (CannotRealizeException e23) {
				e23.printStackTrace();
			}
			player.realize();
			player.addControllerListener(this);
			player.getGainControl().addGainChangeListener(this);
			player.start();
			// this.initDrag();
		} else if (e instanceof ByeEvent) {
			disconnect();
		}
	}

	/*
	 * Point loc = null; Point tmp = null; boolean isDragged = false;
	 */
	/*
	 * private void initDrag() { this.addMouseListener(new
	 * java.awt.event.MouseAdapter() { public void
	 * mouseReleased(java.awt.event.MouseEvent e) { isDragged = false;
	 * setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); }
	 * 
	 * public void mousePressed(java.awt.event.MouseEvent e) { tmp = new
	 * Point(e.getX(), e.getY()); isDragged = true; setCursor(new
	 * Cursor(Cursor.MOVE_CURSOR)); } }); this.addMouseMotionListener(new
	 * java.awt.event.MouseMotionAdapter() { public void
	 * mouseDragged(java.awt.event.MouseEvent e) { if (isDragged) { loc = new
	 * Point(getLocation().x + e.getX() - tmp.x, getLocation().y + e.getY() -
	 * tmp.y); setLocation(loc); } }
	 * 
	 * }); }
	 */
	public void disconnect() {
		if (player != null) {
			player.stop();
			player.close();
		}
		if (mgr != null) {
			mgr.removeTargets("closing session");
			mgr.dispose();
			mgr = null;
		}
	}

	@Override
	public void controllerUpdate(ControllerEvent e) {
		/*
		 * Control[] controls = player.getControls(); for (Control trol :
		 * controls) { if (trol.getControlComponent() != null) {
		 * add(trol.getControlComponent()); } }
		 */
		System.out.println("controllllll");
		if (e instanceof RealizeCompleteEvent) {
			if (player.getVisualComponent() != null)
				add(player.getVisualComponent());
			if (player.getControlPanelComponent() != null) {
				player.getControlPanelComponent().setMinimumSize(
						new Dimension(200, 40));
				add(player.getControlPanelComponent(), BorderLayout.SOUTH);
			}
			/*
			 * if (player.getControl("CachingControl")!= null)
			 * add(player.getControl("CachingControl"));
			 */
			this.doLayout();
			pack();
		}
	}

	@Override
	public void gainChange(GainChangeEvent ev) {
		System.out.println("gain change");
		System.out.println(player.getGainControl().getLevel());
	}

	@Override
	public void run() {
		start();
	}
}
