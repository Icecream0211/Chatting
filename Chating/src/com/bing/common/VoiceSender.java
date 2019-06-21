package com.bing.common;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Future;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
import javax.media.Control;
import javax.media.Format;
import javax.media.Manager;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Processor;
import javax.media.control.FormatControl;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;

import jmapps.util.StateHelper;

import com.bing.common.ecxcption.AllPortBeenOccupied;
import com.bing.common.utility.RandomPort;

public class VoiceSender {
	private RTPManager mgr;
	private Processor p;
	private int destport = -1;
	private SessionAddress local;
	private SessionAddress target;
	private String destip;
	private Future senderFuture;
	private CaptureDeviceInfo info;
	private SendStream voiceSenderStream;
	private boolean isStarted = false;
	private VoiceChatingInstance voiceIns;

	public static void main(String[] args) {
		try {// 设置本地地址和远端地址都为本机地址，自己传给自己，试验用
			SessionAddress local = new SessionAddress(
					InetAddress.getLocalHost(), 566);
			SessionAddress target = new SessionAddress(
					InetAddress.getLocalHost(), 50000);
			// new VoiceSender(local,target);
			new VoiceSender("192.168.6.68", 50000,null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public VoiceSender(SessionAddress local, SessionAddress target) {
		this.target = target;
		this.local = local;
		// senderFuture = Executors.newCachedThreadPool().submit(this);
		start();
	}

	public VoiceSender(String ip, int destport, VoiceChatingInstance voiceIns) {
		this.destport = destport;
		this.destip = ip;
		this.voiceIns = voiceIns;
		// senderFuture = Executors.newCachedThreadPool().submit(this);
		start();
	}

	public VoiceSender(RTPManager mgr) {
		this.mgr = mgr;
		start();
	}

	public void start() {
		// CaptureDeviceInfo info = (CaptureDeviceInfo)
		// CaptureDeviceManager.getDeviceList(new VideoFormat(null)).get(0);
		Vector<CaptureDeviceInfo> audio = CaptureDeviceManager
				.getDeviceList(new AudioFormat(AudioFormat.LINEAR, 44100, 16, 2));
		if (audio != null && audio.size() > 0) {
			System.out.println("xxxx0");
			try {
				info = audio.get(0);
				p = Manager.createProcessor(info.getLocator());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoProcessorException e) {
				e.printStackTrace();
			}
			StateHelper sh = new StateHelper(p);
			// doSomeVideoProcess(p,sh);
			doSomeAudioProcess(p, sh);// 用这一句替换上一句可改成音频传输
			mgr = RTPManager.newInstance();
			try {
				if (this.destport != -1) {
					local = new SessionAddress(InetAddress.getLocalHost(),
							RandomPort.getPort(CommonConfig.PORT_START - 40
									+ (new Random()).nextInt(10) * 50));
					target = new SessionAddress(
							InetAddress.getByName(this.destip), this.destport);
				}
				mgr.initialize(local);
				mgr.addTarget(target);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidSessionAddressException e) {
				e.printStackTrace();
			} catch (AllPortBeenOccupied e) {
				e.printStackTrace();
			}
			DataSource ds = p.getDataOutput();
			PushBufferDataSource pbds = (PushBufferDataSource) ds;
			PushBufferStream pbss[] = pbds.getStreams();
			System.out.println("xxxx01");
			System.out.println(pbss.length);
			for (int i = 0; i < pbss.length; i++) {
				try {
					voiceSenderStream = mgr.createSendStream(ds, i);
					voiceSenderStream.start();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UnsupportedFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			p.start();
			/*
			 * try { Thread.sleep(60000);//传送一分钟后关闭 } catch
			 * (InterruptedException e) { e.printStackTrace(); }
			 */
			isStarted = true;
		} else {
			System.out.println("No Audio Device available right now!");
			this.voiceIns.sendMsg("{status:4,action:4}");
		}
	}

	public void disconnect() {
		try {
			if (p != null) {
				p.stop();
				p.close();
				p.deallocate();
			}
			if (voiceSenderStream != null) {
				voiceSenderStream.stop();
				voiceSenderStream.close();
			}
			if (mgr != null) {
				mgr.removeTargets("client disconnnected");
				mgr.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		disconnect();
		// this.senderFuture.cancel(true);
		isStarted = false;
	}

	public boolean isStarted() {
		return isStarted;
	}

	private static void doSomeVideoProcess(Processor p, StateHelper sh) {
		sh.configure(5000);
		p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
		setVideoTrackFormat(p.getTrackControls());
		sh.realize(5000);
		setJPEGQuality(p, 0.5f);
	}

	private static void doSomeAudioProcess(Processor p, StateHelper sh) {
		sh.configure(5000);
		p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
		setAudioTrackFormat(p.getTrackControls());
		sh.realize(5000);
	}

	private static Format checkForVideoSizes(Format original, Format supported) {
		int width, height;
		Dimension size = ((VideoFormat) original).getSize();
		Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
		Format h263Fmt = new Format(VideoFormat.H263_RTP);
		if (supported.matches(jpegFmt)) {
			// For JPEG, make sure width and height are divisible by 8.
			width = (size.width % 8 == 0 ? size.width
					: (int) (size.width / 8) * 8);
			height = (size.height % 8 == 0 ? size.height
					: (int) (size.height / 8) * 8);
		} else if (supported.matches(h263Fmt)) {
			// For H.263, we only support some specific sizes.
			if (size.width < 128) {
				width = 128;
				height = 96;
			} else if (size.width < 176) {
				width = 176;
				height = 144;
			} else {
				width = 352;
				height = 288;
			}
		} else {
			// We don't know this particular format. We'll just
			// leave it alone then.
			return supported;
		}
		return (new VideoFormat(null, new Dimension(width, height),
				Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED))
				.intersects(supported);
	}

	private static void setAudioTrackFormat(TrackControl[] trackControls) {
		boolean ok = false;
		for (TrackControl t : trackControls) {
			if (!ok && t instanceof FormatControl) {
				if (((FormatControl) t).setFormat(new AudioFormat(
						AudioFormat.ULAW_RTP, 8000, 8, 1)) == null)
					t.setEnabled(false);
				else
					ok = true;
			} else {
				t.setEnabled(false);
			}
		}
	}

	private static boolean setVideoTrackFormat(TrackControl[] tracks) {
		if (tracks == null || tracks.length < 1)
			return false;
		boolean atLeastOneTrack = false;
		for (TrackControl t : tracks) {
			if (t.isEnabled()) {
				Format[] supported = t.getSupportedFormats();
				Format chosen = null;
				if (supported.length > 0) {
					if (supported[0] instanceof VideoFormat)
						chosen = checkForVideoSizes(t.getFormat(), supported[0]);
					else
						chosen = supported[0];
					t.setFormat(chosen);
					atLeastOneTrack = true;
				} else {
					t.setEnabled(false);
				}
			} else {
				t.setEnabled(false);
			}
		}
		return atLeastOneTrack;
	}

	private static void setJPEGQuality(Processor p, float val) {
		Control cs[] = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
		// Loop through the controls to find the Quality control for
		// the JPEG encoder.
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
				Object owner = ((Owned) cs[i]).getOwner();
				// Check to see if the owner is a Codec.
				// Then check for the output format.
				if (owner instanceof Codec) {
					Format fmts[] = ((Codec) owner)
							.getSupportedOutputFormats(null);
					for (int j = 0; j < fmts.length; j++) {
						if (fmts[j].matches(jpegFmt)) {
							qc = (QualityControl) cs[i];
							qc.setQuality(val);
							break;
						}
					}
				}
				if (qc != null)
					break;
			}
		}
	}

	public void suspend() {
		this.p.stop();
	}

	public void resume() {
		this.p.start();
		// TODO Auto-generated method stub
	}

	/*
	 * @Override public void run() { start(); }
	 */
}
