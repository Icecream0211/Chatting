package com.bing.chat.voice.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.media.Time;
import javax.media.protocol.DataSource;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;

import org.omg.SendingContext.RunTime;

import com.bing.chat.ui.common.BackPanel;
import com.bing.common.ResourceLocation;
import com.bing.common.VoiceChannel;
import com.bing.common.VoiceChatingInstance;
import com.bing.common.VoiceProcess;
import com.xiyou.view.MyFrame;

public class VoiceView extends BackPanel {
	private ImageIcon play = ResourceLocation
			.getImageIcon("/image/microphone0.png");
	private ImageIcon play_enter = ResourceLocation
			.getImageIcon("/image/microphone1.png");
	private ImageIcon stop = ResourceLocation
			.getImageIcon("/image/microphoneoff0.png");
	private ImageIcon stop_enter = ResourceLocation
			.getImageIcon("/image/microphoneoff1.png");
	private ImageIcon sound = ResourceLocation
			.getImageIcon("/image/sound0.jpg");
	private ImageIcon sound_enter = ResourceLocation
			.getImageIcon("/image/sound1.jpg");
	private ImageIcon soundoff = ResourceLocation
			.getImageIcon("/image/soundoff0.jpg");
	private ImageIcon soundoff_enter = ResourceLocation
			.getImageIcon("/image/soundoff1.jpg");

	private JLabel mediatime;
	private JLabel playbutton;
	private JLabel soundbutton;
	private JSlider soundvolume;
	private boolean isplay = false;
	private boolean issoundoff = false;

	private JFrame showFrame;
	private Future upMediaTimeFu;
	private VoiceChatingInstance voiceIns;

	private DataSource data;
	private VoiceProcess voiceProcess;

	public VoiceView() {
		init();
	}

	public VoiceView(DataSource source, VoiceProcess voicePro,
			VoiceChatingInstance voiceIns) {
		this.data = source;
		this.voiceIns = voiceIns;
		this.voiceProcess = voicePro;
		init();
	}

	public void init() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		mediatime = new JLabel("MediaTime");

		playbutton = new JLabel("");
		playbutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (!isplay) {
					playbutton.setIcon(play_enter);
				} else {
					playbutton.setIcon(stop_enter);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isplay) {
					playbutton.setIcon(play);
				} else {
					playbutton.setIcon(stop);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!isplay) {
					playbutton.setIcon(stop);
					voiceProcess.playoff();
				} else {
					playbutton.setIcon(play);
					voiceProcess.playon();
				}
				isplay = !isplay;
			}
		});
		playbutton.setIcon(play);
		soundbutton = new JLabel("");
		soundbutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!issoundoff) {
					soundbutton.setIcon(sound_enter);
				} else {
					soundbutton.setIcon(soundoff_enter);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!issoundoff) {
					soundbutton.setIcon(sound);
				} else {
					soundbutton.setIcon(soundoff);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!issoundoff) {
					soundbutton.setIcon(soundoff);
					voiceProcess.soundOff();
				} else {
					soundbutton.setIcon(sound);
					voiceProcess.soundOn();
				}
				issoundoff = !issoundoff;
			}
		});
		soundbutton.setIcon(sound);
		soundvolume = new SoundAdapter();
		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JSlider) {
					System.out.println("刻度: "
							+ ((JSlider) e.getSource()).getValue());
					voiceProcess.setSoundVolume(((JSlider) e.getSource())
							.getValue());
				}
			}
		};
		soundvolume.addChangeListener(listener);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addGap(52)
								.addComponent(playbutton)
								.addGap(18)
								.addComponent(soundvolume,
										GroupLayout.DEFAULT_SIZE, 203,
										Short.MAX_VALUE).addGap(18)
								.addComponent(soundbutton).addGap(95))
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addGap(102)
								.addComponent(mediatime,
										GroupLayout.PREFERRED_SIZE, 205,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(143, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addGap(21)
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addComponent(soundbutton)
										.addComponent(playbutton)
										.addComponent(soundvolume,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mediatime).addGap(8)));
		setLayout(groupLayout);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new VoiceView());
		frame.pack();
		frame.setVisible(true);
		/*
		 * FineResizeFrame frame = new FineResizeFrame(); frame.addShowPanel(new
		 * VoiceView()); frame.setSize(new Dimension(354,150)); //frame.pack();
		 * frame.setVisible(true);
		 */
	}

	public void showUp() {
		if (this.showFrame == null) {
			this.showFrame = new MyFrame() {
				@Override
				public void closeFrame() {
					voiceIns.close();
					// voiceProcess.close();
				}
			};
			showFrame.getContentPane().add(this);
			showFrame.pack();
			showFrame.setVisible(true);
			showFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			upMediaTimeFu = Executors.newCachedThreadPool().submit(
					new UpdateMediaTime());
		}
	}

	private void setTime(String txt) {
		this.mediatime.setText(txt);
	}

	class UpdateMediaTime implements Runnable {
		boolean abc = true;
		int minue = 0;
		int seconds = 0;

		@Override
		public void run() {
			while (abc) {
				try {
					Thread.sleep(1000);
					int a = (int) voiceProcess.getMediaTime().getSeconds();
					if (a >= 60) {
						minue = a / 60;
					}
					setTime(minue + ":" + a % 60);
				} catch (InterruptedException e) {
					// e.printStackTrace();
					abc = false;
					break;
				}
			}
		}
	}

	public void dispose() {
		if (this.showFrame != null && this.showFrame.isShowing()) {
			this.showFrame.dispose();
		}
		if (upMediaTimeFu != null) {
			upMediaTimeFu.cancel(true);
		}
	}
}
