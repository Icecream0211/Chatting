package com.bing.common;

import java.net.InetAddress;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPControl;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewParticipantEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.RemotePayloadChangeEvent;
import javax.media.rtp.event.SessionEvent;
import javax.media.rtp.event.StreamMappedEvent;

import net.sf.fmj.media.rtp.RTPSocketAdapter;

/**
 * AVReceive3 to receive RTP transmission using the RTPConnector.
 */
public class MediaReceive implements ReceiveStreamListener, SessionListener,
		ControllerListener {
	private String sessions[] = null;
	private RTPManager mgrs[] = null;

	private int[] ports = {};
	boolean dataReceived = false;
	private Object dataSync = new Object();
	private boolean isstart = false;
	private VideoChatingInstance videoIns;
	private PlayProcess playProcess;

	public MediaReceive(String sessions[]) {
		this.sessions = sessions;
	}

	public MediaReceive(int[] ports, VideoChatingInstance videoins) {
		this.ports = ports;
		this.videoIns = videoins;
		this.initialize();
	}

	public boolean isStarted() {
		return isstart;
	}

	protected void initialize() {
		if (!isstart) {
			// playFrame = new VideoView();
			// JFrame jf = new JFrame("视频实例");
			// jf.add(playFrame);
			// jf.pack();
			// jf.setLocationRelativeTo(null);
			// jf.setDefaultCloseOperation(3);
			// jf.setVisible(true);
			try {
				// 每一个session对应一个RTPManager
				mgrs = new RTPManager[ports.length];
				// 创建播放窗口的向量vector
				SessionLabel session = null;
				// Open the RTP sessions.
				// 针对每一个会话对象进行ip、port和ttl的解析
				for (int i = 0; i < ports.length; i++) {
					// Parse the session addresses.
					// 进行会话对象的解析，得到ip、port和ttl
					try {
						session = new SessionLabel(InetAddress.getLocalHost()
								.getHostAddress() + "/" + ports[i]);
					} catch (IllegalArgumentException e) {
						System.err
								.println("Failed to parse the session address given: "
										+ sessions[i]);
						// return false;
					}
					System.err.println("  - Open RTP session for: addr: "
							+ session.addr + " port: " + session.port
							+ " ttl: " + session.ttl);
					// 这对本条会话对象创建RTPManager
					mgrs[i] = (RTPManager) RTPManager.newInstance();
					mgrs[i].addSessionListener(this);
					mgrs[i].addReceiveStreamListener(this);

					// Initialize the RTPManager with the RTPSocketAdapter
					// 将本机ip和端口号加入RTP会话管理
					System.out.println("session.addr:" + session.addr);
					mgrs[i].initialize(new RTPSocketAdapter(InetAddress
							.getByName(session.addr), session.port, session.ttl));
					BufferControl bc = (BufferControl) mgrs[i]
							.getControl("javax.media.control.BufferControl");
					if (bc != null)
						bc.setBufferLength(350);
				}
				isstart = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Close the players and the session managers.
	 */
	protected void close() {
		if (isstart) {
			if (this.playProcess != null && this.playProcess.isStarted()) {
				this.playProcess.close();
			}
			// close the RTP session.
			for (int i = 0; i < mgrs.length; i++) {
				if (mgrs[i] != null) {
					mgrs[i].removeSessionListener(this);
					mgrs[i].removeReceiveStreamListener(this);
					mgrs[i].removeTargets("Closing session from AVReceive3");
					mgrs[i].dispose();
					mgrs[i] = null;
				}
			}
			isstart = false;
		}
	}

	/**
	 * SessionListener.
	 */
	@SuppressWarnings("deprecation")
	public synchronized void update(SessionEvent evt) {

		if (evt instanceof NewParticipantEvent) {
			Participant p = ((NewParticipantEvent) evt).getParticipant();
			System.err.println("  - A new participant had just joined: " + p);
		}
	}

	/**
	 * ReceiveStreamListener
	 */
	public synchronized void update(ReceiveStreamEvent evt) {

		RTPManager mgr = (RTPManager) evt.getSource();
		Participant participant = evt.getParticipant(); // could be null.
		ReceiveStream stream = evt.getReceiveStream(); // could be null.

		if (evt instanceof RemotePayloadChangeEvent) {

			System.err.println("  - Received an RTP PayloadChangeEvent.");
			System.err.println("Sorry, cannot handle payload change.");
			// System.exit(0);

		}
		if (evt instanceof NewReceiveStreamEvent) {
			System.out.println("evt instanceof NewReceiveStreamEvent");
			try {
				stream = ((NewReceiveStreamEvent) evt).getReceiveStream();
				final DataSource data = stream.getDataSource();

				// Find out the formats.
				RTPControl ctl = (RTPControl) data
						.getControl("javax.media.rtp.RTPControl");
				if (ctl != null) {
					System.err.println("  - Recevied new RTP stream: "
							+ ctl.getFormat());
				} else
					System.err.println("  - Recevied new RTP stream");

				if (participant == null)
					System.err
							.println("      The sender of this stream had yet to be identified.");
				else {
					System.err.println("      The stream comes from: "
							+ participant.getCNAME());
				}
				playProcess = new PlayProcess(data, this.videoIns, "remote");
				// create a player by passing datasource to the Media Manager
				// new Thread() {
				// public void run() {
				// playFrame.remotePlay(data);
				// }
				// }.start();
				// Player p = javax.media.Manager.createPlayer(data);
				// if (p == null)
				// return;
				//
				// p.addControllerListener(this);
				// p.realize();
				// PlayerWindow pw = new PlayerWindow(p, stream);
				// playerWindows.addElement(pw);
				// Notify intialize() that a new stream had arrived.
				synchronized (dataSync) {
					dataReceived = true;
					dataSync.notifyAll();
				}

			} catch (Exception e) {
				System.err.println("NewReceiveStreamEvent exception "
						+ e.getMessage());
				return;
			}

		}
		if (evt instanceof StreamMappedEvent) {
			System.out.println("evt instanceof StreamMappedEvent");
			stream = ((StreamMappedEvent) evt).getReceiveStream();
			if (stream != null && stream.getDataSource() != null) {
				DataSource ds = stream.getDataSource();
				// Find out the formats.
				RTPControl ctl = (RTPControl) ds
						.getControl("javax.media.rtp.RTPControl");
				System.err.println("  - The previously unidentified stream ");
				if (ctl != null)
					System.err.println("      " + ctl.getFormat());
				System.err.println("      had now been identified as sent by: "
						+ participant.getCNAME());
				System.out.println("ds == null?" + (ds == null));
				//if (ds != null) {
					playProcess = new PlayProcess(ds, this.videoIns, "remote");
				//}
			}
		}

		if (evt instanceof ByeEvent) {
			System.err.println("  - Got \"bye\" from: "
					+ participant.getCNAME());
			close();
		}
	}

	/**
	 * ControllerListener for the Players.
	 */
	public synchronized void controllerUpdate(ControllerEvent ce) {
		Player p = (Player) ce.getSourceController();
		if (p == null)
			return;
	}

	/**
	 * A utility class to parse the session addresses.
	 */
	class SessionLabel {
		public String addr = null;
		public int port;
		public int ttl = 1;

		SessionLabel(String session) throws IllegalArgumentException {

			int off;
			String portStr = null, ttlStr = null;

			if (session != null && session.length() > 0) {
				while (session.length() > 1 && session.charAt(0) == '/') {
					session = session.substring(1);
				}
				off = session.indexOf('/');
				if (off == -1) {
					if (!session.equals(""))
						addr = session;
				} else {
					addr = session.substring(0, off);
					session = session.substring(off + 1);
					off = session.indexOf('/');
					if (off == -1) {
						if (!session.equals(""))
							portStr = session;
					} else {
						portStr = session.substring(0, off);
						session = session.substring(off + 1);
						off = session.indexOf('/');
						if (off == -1) {
							if (!session.equals(""))
								ttlStr = session;
						} else {
							ttlStr = session.substring(0, off);
						}
					}
				}
			}

			if (addr == null)
				throw new IllegalArgumentException();

			if (portStr != null) {
				try {
					Integer integer = Integer.valueOf(portStr);
					if (integer != null)
						port = integer.intValue();
				} catch (Throwable t) {
					throw new IllegalArgumentException();
				}
			} else
				throw new IllegalArgumentException();

			if (ttlStr != null) {
				try {
					Integer integer = Integer.valueOf(ttlStr);
					if (integer != null)
						ttl = integer.intValue();
				} catch (Throwable t) {
					throw new IllegalArgumentException();
				}
			}
		}
	}

	public static void main(String argv[]) {
		String[] strs = { "192.168.6.68/9994", "192.168.6.68/9996" };
		int[] ports = { 9904, 9906 };
		// MediaReceive avReceive = new MediaReceive(strs);
		MediaReceive avReceive = new MediaReceive(ports, null);
		avReceive.initialize();
	}
}
