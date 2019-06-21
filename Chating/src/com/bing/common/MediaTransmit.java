package com.bing.common;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.Codec;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;
import javax.media.cdm.CaptureDeviceManager;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.SourceCloneable;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;

import com.bing.common.utility.RandomPort;

public class MediaTransmit {
	private String ipAddress;
	private int portBase = -1;
	private MediaLocator audioLocator = null, vedioLocator = null;
	private Processor audioProcessor = null;
	private Processor videoProcessor = null;
	private DataSource audioDataLocal = null, videoDataLocal = null;
	private DataSource audioDataOutput = null, videoDataOutput = null;
	private RTPManager rtpMgrs[];
	private DataSource mediaData = null;
	private List<SendStream> sendstreams;
	private DataSource dataLocalClone = null;
	private boolean audioable = false, videoable = false;
	private int destports[];
	private boolean isstart = false;
	private PlayProcess process;

	// private VideoView playFrame;
	private VideoChatingInstance videoIns;

	public MediaTransmit(String ipAddress, String pb) {
		this.ipAddress = ipAddress;
		Integer integer = Integer.valueOf(pb);
		if (integer != null) {
			this.portBase = integer.intValue();
		}
		this.init();
	}

	public MediaTransmit(String destIp, int[] ports) {
		this.ipAddress = destIp;
		this.destports = ports;
		this.init();
	}

	public MediaTransmit(String destIp, int[] ports,
			VideoChatingInstance videoins) {
		this.ipAddress = destIp;
		this.destports = ports;
		this.videoIns = videoins;
		this.init();
	}

	protected void init() {
		if (!isstart) {
			// /////////////////////////////////////////////
			// playFrame = new VideoView();
			// JFrame jf = new JFrame("视频实例");
			// jf.add(playFrame);
			// jf.pack();
			// jf.setLocationRelativeTo(null);
			// jf.setDefaultCloseOperation(3);
			Runtime.getRuntime().addShutdownHook(new Thread(new hokeRun()));
			// jf.setVisible(true);
			// ////////////////////////////////////////////
			Vector<CaptureDeviceInfo> video = CaptureDeviceManager
					.getDeviceList(new VideoFormat(null));
			Vector<CaptureDeviceInfo> audio = CaptureDeviceManager
					.getDeviceList(new AudioFormat(AudioFormat.LINEAR, 44100,
							16, 2));
			// MediaLocator mediaLocator = new
			// MediaLocator("file:/C:/纯音乐 - 忧伤还是快乐.mp3");
			if (audio != null && audio.size() > 0) {
				audioLocator = ((CaptureDeviceInfo) audio.get(0)).getLocator();
				if ((audioProcessor = createProcessor(audioLocator)) != null) {
					audioDataLocal = mediaData;
					audioDataOutput = audioProcessor.getDataOutput();
					this.audioable = true;
				}
			} else {
				System.out.println("******错误：没有检测到您的音频采集设备！！！");
			}
			// /////////////////////////////////////////////////////////
			if (video != null && video.size() > 0) {
				vedioLocator = ((CaptureDeviceInfo) video.get(0)).getLocator();
				if ((videoProcessor = createProcessor(vedioLocator)) != null) {
					videoDataLocal = mediaData;
					videoDataOutput = videoProcessor.getDataOutput();
					this.videoable = true;
				}
			} else {
				System.out.println("******错误：没有检测到您的视频设备！！！");
			}
			// /////////////////////////////////////////////////////////
			final DataSource[] dataSources = new DataSource[2];
			/*
			 * if (audioable && videoable) { dataSources[0] = audioDataLocal;
			 * dataSources[1] = videoDataLocal; }
			 */
			// ////////////////////////////////////////////////远程传输
			if (audioable && videoable) {
				dataSources[0] = videoDataOutput;
				dataSources[1] = audioDataOutput;
			}
			DataSource dsoutput;
			try {
				if (audioable && videoable) {
					dsoutput = Manager.createMergingDataSource(dataSources);
				} else if (audioable) {
					dsoutput = audioDataOutput;
				} else if (videoable) {
					dsoutput = videoDataOutput;
				} else {
					throw new Exception("now audio or video device available");
				}
				createTransmitter(dsoutput);
			} catch (IncompatibleSourceException e) {
				e.printStackTrace();
				return;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			if (audioProcessor!=null) {
				audioProcessor.start();
			}
			if (videoProcessor!=null) {
				videoProcessor.start();
				try {
					process = new PlayProcess(dataLocalClone, this.videoIns,
							"local");
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			isstart = true;
		}
	}

	public boolean isStarted() {
		return isstart;
	}

	private Processor createProcessor(MediaLocator locator) {
		Processor processor = null;
		if (locator == null)
			return null;
		// 通过设备定位器得到数据源，
		try {
			mediaData = Manager.createDataSource(locator);
			// 创建可克隆数据源
			mediaData = Manager.createCloneableDataSource(mediaData);
			// 克隆数据源，用于传输到远程
			dataLocalClone = ((SourceCloneable) mediaData).createClone();
			// dataLocalClone2 = ((SourceCloneable) mediaData).createClone();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		try {
			// processor =
			// javax.media.Manager.createProcessor(dataLocalClone);//必须使用cloneable
			// data，否则clones没有数据来源
			processor = javax.media.Manager.createProcessor(mediaData);
		} catch (NoProcessorException npe) {
			npe.printStackTrace();
			return null;
		} catch (IOException ioe) {
			return null;
		}
		boolean result = waitForState(processor, Processor.Configured);
		if (result == false)
			return null;

		TrackControl[] tracks = processor.getTrackControls();
		if (tracks == null || tracks.length < 1)
			return null;
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		processor.setContentDescriptor(cd);
		Format supportedFormats[];
		Format chosen;
		boolean atLeastOneTrack = false;
		for (int i = 0; i < tracks.length; i++) {
			if (tracks[i].isEnabled()) {
				supportedFormats = tracks[i].getSupportedFormats();
				if (supportedFormats.length > 0) {
					if (supportedFormats[0] instanceof VideoFormat) {
						chosen = checkForVideoSizes(tracks[i].getFormat(),
								supportedFormats[0]);
					} else
						chosen = supportedFormats[0];
					tracks[i].setFormat(chosen);
					System.err
							.println("Track " + i + " is set to transmit as:");
					System.err.println("  " + chosen);
					atLeastOneTrack = true;
				} else
					tracks[i].setEnabled(false);
			} else
				tracks[i].setEnabled(false);
		}

		if (!atLeastOneTrack)
			return null;
		result = waitForState(processor, Controller.Realized);
		if (result == false)
			return null;
		setJPEGQuality(processor, 0.5f);
		return processor;
	}

	private String createTransmitter(DataSource dataOutput) {
		PushBufferDataSource pbds = (PushBufferDataSource) dataOutput;
		PushBufferStream pbss[] = pbds.getStreams();
		System.out.println("pbss.length:" + pbss.length);
		rtpMgrs = new RTPManager[pbss.length];
		SendStream sendStream;
		if (sendstreams == null) {
			sendstreams = new LinkedList<SendStream>();
		}
		int port;
		// SourceDescription srcDesList[];

		for (int i = 0; i < pbss.length; i++) {
			try {
				rtpMgrs[i] = RTPManager.newInstance();
				if (destports == null || destports.length == 0) {
					port = portBase + 2 * i;
				} else {
					port = destports[i];
				}
				SessionAddress localAddr = new SessionAddress(
						InetAddress.getLocalHost(),
						RandomPort.getPort(CommonConfig.PORT_START - 40 + i
								* 50));// 9998
				SessionAddress destAddr = new SessionAddress(
						InetAddress.getByName(this.ipAddress), port);// 9994
																		// 9996
				/*
				 * SessionAddress destAddr = new SessionAddress(InetAddress
				 * .getByName(ipAddress), port);
				 */
				rtpMgrs[i].initialize(localAddr);
				rtpMgrs[i].addTarget(destAddr);
				// rtpMgrs[i].initialize(new
				// RTPSocketAdapter(InetAddress.getByName("192.168.6.68"),
				// port));
				System.out.println("Created RTP session: " + this.ipAddress
						+ " " + port);
				sendStream = rtpMgrs[i].createSendStream(dataOutput, i);
				sendStream.start();
				sendstreams.add(sendStream);
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		}

		return null;
	}

	Format checkForVideoSizes(Format original, Format supported) {

		int width, height;
		Dimension size = ((VideoFormat) original).getSize();
		Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
		Format h263Fmt = new Format(VideoFormat.H263_RTP);

		if (supported.matches(jpegFmt)) {
			width = (size.width % 8 == 0 ? size.width
					: (int) (size.width / 8) * 8);
			height = (size.height % 8 == 0 ? size.height
					: (int) (size.height / 8) * 8);
		} else if (supported.matches(h263Fmt)) {
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
			return supported;
		}
		return (new VideoFormat(null, new Dimension(width, height),
				Format.NOT_SPECIFIED, null, Format.NOT_SPECIFIED))
				.intersects(supported);
	}

	void setJPEGQuality(Player p, float val) {

		Control cs[] = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
		for (int i = 0; i < cs.length; i++) {

			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
				Object owner = ((Owned) cs[i]).getOwner();
				if (owner instanceof Codec) {
					Format fmts[] = ((Codec) owner)
							.getSupportedOutputFormats(null);
					for (int j = 0; j < fmts.length; j++) {
						if (fmts[j].matches(jpegFmt)) {
							qc = (QualityControl) cs[i];
							qc.setQuality(val);
							System.err.println("- Setting quality to " + val
									+ " on " + qc);
							break;
						}
					}
				}
				if (qc != null)
					break;
			}
		}
	}

	private Integer stateLock = new Integer(0);
	private boolean failed = false;

	Integer getStateLock() {
		return stateLock;
	}

	void setFailed() {
		failed = true;
	}

	private synchronized boolean waitForState(Processor p, int state) {
		p.addControllerListener(new StateListener());
		failed = false;
		if (state == Processor.Configured) {
			p.configure();
		} else if (state == Processor.Realized) {
			p.realize();
		}
		while (p.getState() < state && !failed) {
			synchronized (getStateLock()) {
				try {
					getStateLock().wait();
				} catch (InterruptedException ie) {
					return false;
				}
			}
		}

		if (failed)
			return false;
		else
			return true;
	}

	class StateListener implements ControllerListener {

		public void controllerUpdate(ControllerEvent ce) {
			if (ce instanceof ControllerClosedEvent)
				setFailed();
			if (ce instanceof ControllerEvent) {
				synchronized (getStateLock()) {
					getStateLock().notifyAll();
				}
			}
			if (ce instanceof RealizeCompleteEvent) {
			}
		}
	}

	class hokeRun implements Runnable {

		@Override
		public void run() {
			int size = MediaTransmit.this.rtpMgrs.length;
			for (int i = 0; i < size; i++) {
				MediaTransmit.this.rtpMgrs[i].dispose();
			}
		}

	}

	public static void main(String[] args) {
		String[] strs = { "192.168.6.68", "9994" };
		int[] ports = { 9994, 9996 };
		new MediaTransmit(strs[0], ports);
	}

	public void close() {
		if (isstart) {
			if (videoProcessor != null) {
				videoProcessor.addControllerListener(new ProcessListener());
				videoProcessor.stop();
				videoProcessor.close();
				//videoProcessor.deallocate();
				videoProcessor = null;
			}
			if (audioProcessor != null) {
				audioProcessor.addControllerListener(new ProcessListener());
				audioProcessor.stop();
				//audioProcessor.deallocate();
				audioProcessor.close();
				audioProcessor = null;
			}
			if (this.process!=null&&this.process.isStarted()) {
				this.process.close();
			}
			try {
				for (SendStream ss : sendstreams) {
					ss.stop();
					ss.close();
					// TODO Auto-generated catch block
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// close the RTP session.
			for (RTPManager rtpMgr : rtpMgrs) {
				if (rtpMgr != null) {
					rtpMgr.removeTargets("Closing session from AVReceive3");
					rtpMgr.dispose();
					rtpMgr = null;
				}
			}
			if (this.audioDataLocal != null) {
				this.audioDataLocal.disconnect();
			}
			if (this.videoDataLocal != null) {
				this.videoDataLocal.disconnect();
			}
			if (this.audioDataOutput != null) {
				this.audioDataOutput.disconnect();
			}
			if (this.videoDataOutput != null) {
				this.videoDataOutput.disconnect();
			}
			if (this.dataLocalClone != null) {
				this.dataLocalClone.disconnect();
			}
			if (mediaData != null) {
				mediaData.disconnect();
			}
			isstart = false;
		}
	}

	class ProcessListener implements ControllerListener {
		@Override
		public void controllerUpdate(ControllerEvent ce) {
			Processor pro = (Processor) ce.getSource();
			if (ce instanceof StopEvent) {
				System.out.println("processListener...stopEvent");
				pro.removeControllerListener(this);
				pro.deallocate();
				pro.close();
			}
		}
	}
}
