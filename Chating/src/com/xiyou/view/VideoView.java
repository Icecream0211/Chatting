package com.xiyou.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.Executors;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Processor;
import javax.media.protocol.DataSource;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bing.common.ResourceLocation;
import com.bing.common.VideoChatingInstance;

public class VideoView extends JPanel {
	private ImageIcon videoReqIcon = ResourceLocation.getImageIcon("image/videoReq.jpg");
	private ImageIcon VideolocalIcon = ResourceLocation.getImageIcon("image/localVideo.jpg");
	private boolean isViewBigPlaying = false;
	private boolean isViewSmallPlaying = false;
	private JPanel viewBigPane;
	private JPanel viewSmallPane;
	private JPanel controlPane;

	private JButton closeButton;

	private boolean localPlay = false;
	private boolean remotePlay = false;

	private DataSource localData;
	private DataSource remoteData;

	private boolean isViewRun = true;
	private boolean isShow = true;
	//
	private Player localPlayer = null;
	private Player remotePlayer = null;
	//
	private Processor videotapeProcessor = null;
	private Player videotapePlayer = null;
	private DataSink videotapeFileWriter;
	private VideoChatingInstance videoIns;
	
	private JFrame showFrame;

	public VideoView(VideoChatingInstance videoChatingInstance) {
		this.videoIns = videoChatingInstance;
		this.setLayout(new BorderLayout());
		// 视图面板
		viewBigPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (!isViewBigPlaying) {
					g
							.drawImage(videoReqIcon.getImage(), 1, 1,
									videoReqIcon.getIconWidth(), videoReqIcon
											.getIconHeight(), null);

					g.drawRect(getSmallPlayRec().x - 1,
							getSmallPlayRec().y - 1,
							getSmallPlayRec().width + 1,
							getSmallPlayRec().height + 1);
				} else {

				}
			}
		};
		viewBigPane.setBackground(Color.black);
		this.add(viewBigPane, BorderLayout.CENTER);
		viewBigPane.setLayout(null);
		// ///////////////////////////////
		viewSmallPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (!isViewSmallPlaying) {
					g.drawImage(VideolocalIcon.getImage(), 0, 0, null);
				} 
			}
		};
		viewSmallPane.setBounds(getSmallPlayRec());
		viewBigPane.add(viewSmallPane);
		viewSmallPane.setLayout(null);

		// 控制面板组件
		closeButton = new JButton("挂断");
		controlPane = new JPanel();
		controlPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		controlPane.add(closeButton);
		this.add(controlPane, BorderLayout.SOUTH);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (localPlayer != null) {
					localPlayer.stop();
					localPlayer.deallocate();
					localPlayer.close();
				}
				if (remotePlayer != null) {
					remotePlayer.stop();
					remotePlayer.deallocate();
					remotePlayer.close();
				}
				if (videotapePlayer != null) {
					videotapePlayer.stop();
					videotapePlayer.deallocate();
					videotapePlayer.close();
				}
				if (videotapeProcessor != null) {
					videotapeProcessor.stop();
					videotapeProcessor.deallocate();
					videotapeProcessor.close();
				}
				if (videotapeFileWriter != null) {
					try {
						videotapeFileWriter.stop();
						videotapeFileWriter.close();
					} catch (IOException e1) {
					}
				}
			}

		});
		// this.setMinimumSize(new Dimension(videoReqIcon.getIconWidth()+2,
		// 241));
		// this.setPreferredSize(new Dimension(videoReqIcon.getIconWidth()+2,
		// 241));
		initFrame();
	}
	
	public void dispose(){
		closeViewUI();
		this.showFrame.setVisible(false);
		this.showFrame.dispose();
	}

	private void initFrame() {
		this.showFrame = new MyFrame() {
			@Override
			public void closeFrame() {
				videoIns.close();
			}
		};
		showFrame.add(this);
		showFrame.setLocationRelativeTo(null);
		//jf.setDefaultCloseOperation(3);
	}
	
	public void showVideoUI(){
		if(this.showFrame==null){
			this.initFrame();
		}
		this.showFrame.pack();
		this.showFrame.setVisible(true);
	}

	public Dimension getMinimumSize() {
		System.out
				.println("controlPane.getHeight():" + controlPane.getHeight());
		return new Dimension(videoReqIcon.getIconWidth() + 2, videoReqIcon
				.getIconHeight()
				+ controlPane.getHeight());
	}

	public Dimension getPreferredSize() {
		System.out
				.println("controlPane.getHeight():" + controlPane.getHeight());
		return new Dimension(videoReqIcon.getIconWidth() + 2, videoReqIcon
				.getIconHeight()
				+ controlPane.getPreferredSize().height);
	}

	public void localPlay(Component t) {
		if (t != null) {
			// 将可视容器加到窗体上
			t.setBounds(0, 0, VideolocalIcon.getIconWidth(), VideolocalIcon
					.getIconHeight());
			viewSmallPane.removeAll();
			viewSmallPane.add(t);
		}
		viewBigPane.validate();
		localPlay = true;
	}

	public void remotePlay(Component t) {
		if (t != null) {
			// 将可视容器加到窗体上
			t.setBounds(1, 1, videoReqIcon.getIconWidth(), videoReqIcon
					.getIconHeight());
			viewBigPane.add(t);
		}
		viewBigPane.validate();
		remotePlay = true;
	}

	private Rectangle getSmallPlayRec() {
		int bigShowWidth = videoReqIcon.getIconWidth();
		int bigShowHeight = videoReqIcon.getIconHeight();
		int smallShowWidth = VideolocalIcon.getIconWidth();
		int smallShowHeight = VideolocalIcon.getIconHeight();
		return new Rectangle(bigShowWidth - smallShowWidth - 2, bigShowHeight
				- smallShowHeight - 2, smallShowWidth, smallShowHeight);
	}

	public void closeViewUI() {
		isShow = false;
	}

	public boolean isViewRunning() {
		return isViewRun;
	}

	public boolean isShowing() {
		return isShow;
	}

	public void localReady() {
		localPlay = true;
	}

	public void remoteReady() {
		remotePlay = true;
	}

	public boolean isRemotePlay() {
		return remotePlay;
	}

	public void setRemotePlay(boolean remotePlay) {
		this.remotePlay = remotePlay;
	}

	public DataSource getRemoteData() {
		return remoteData;
	}

	public void setRemoteData(DataSource remoteData) {
		this.remoteData = remoteData;
	}

	public boolean isLocalPlay() {
		return localPlay;
	}

	public void setLocalPlay(boolean localPlay) {
		this.localPlay = localPlay;
	}

	public DataSource getLocalData() {
		return localData;
	}

	public void setLocalData(DataSource localData) {
		this.localData = localData;
	}

	public static void main(String[] args) {
		// Executors.newCachedThreadPool().execute(new PlayPane());
	}

	/*
	 * @Override public void run() { // PlayPane ss = new PlayPane(); JFrame jf
	 * = new JFrame("视频实例"); jf.add(this); jf.pack();
	 * jf.setLocationRelativeTo(null); jf.setDefaultCloseOperation(3);
	 * jf.setVisible(true); }
	 */
}
