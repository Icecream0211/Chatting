package com.xiyou.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.bing.chat.filesend.IFileThread;
import com.bing.chat.filesend.ProgressView;
import com.bing.common.ResourceLocation;

public class TipWindow extends JDialog implements Callable<Integer> {
	private static final long serialVersionUID = 8541659783234673950L;
	private static Dimension dim;
	private int x, y;
	private int width, height;
	private static Insets screenInsets;
	private int duration = 5;
	private int popupduration = 15;
	private int type = 1;// 1 普通信息 2 带有接受拒绝按钮 3 进度条(文件发送方) 4 进度条(接收方)带有打开文件按钮

	public static final int Cancel = 1;
	public static final int Accept = 2;
	public static final int Sure = 3;
	public static final int Close = 4;
	public static final int Runout = 5;

	private Map<String, String> feaMap = null;
	private Point oldP;// 上一次坐标,拖动窗口时用
	private ImageIcon img = null;// 图像组件
	private JLabel imgLabel = null; // 背景图片标签
	private JPanel headPan = null;
	private JPanel btnPan = null;
	private JLabel title = null;
	private JLabel close = null;// 关闭按钮
	private JTextArea feature = null;
	private JScrollPane jfeaPan = null;
	private JLabel releaseLabel = null;
	private JLabel sure = null;
	private JLabel accepted = null;
	private JLabel cancelb = null;
	private JLabel openFile = null;
	private JLabel openDirect = null;
	private int result = -1;
	private SimpleDateFormat sdf = null;
	{
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		feaMap = new HashMap<String, String>();
		feaMap.put("name", "中国信息大学固定资产管理系统");
		feaMap.put("release", sdf.format(new Date()));
		feaMap.put(
				"feature",
				"1.开发环境:windows\n2.开发语言:java\n3.开发工具:Eclipse3.2\n4.数据库类型:SQL Server2005\n5.开发人员:花新昌\n6.联系方式:15210477080");
	}

	public TipWindow(int width, int height) {
		this.width = width;
		this.height = height;
		initComponents();
	}

	public TipWindow(int width, int height, int duration) {
		this.duration = duration;
		this.width = width;
		this.height = height;
		initComponents();
	}

	public TipWindow(int width, int height, int duration, int tiptype) {
		this.type = tiptype;
		this.duration = duration;
		this.width = width;
		this.height = height;
		initComponents();
	}

	public void run() {
		Toolkit.getDefaultToolkit().beep();
		setAlwaysOnTop(true);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
		for (int i = 0; i <= height; i += 10) {
			try {
				this.setLocation(x, y - i);
				Thread.sleep(popupduration);
			} catch (InterruptedException ex) {
			}
		}
		if (this.duration != -1) {
			// 此处代码用来实现让消息提示框5秒后自动消失
			try {
				Thread.sleep(this.duration * 1000);
				close(Runout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getType() {
		return this.type;
	}

	private void initComponents() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
				this.getGraphicsConfiguration());
		x = (int) (dim.getWidth() - width - 3);
		y = (int) (dim.getHeight() - screenInsets.bottom - 3);
		this.setSize(width, height);
		this.setLocation(x, y);
		init();
		handle();
		// this.setBackground(Color.black);
	}

	public static int createMessageTipWindow(int w, int h, int d, String msg) {
		TipWindow tw = new TipWindow(w, h, d, 1);
		JTextPane txtPanel = new JTextPane();
		txtPanel.setText(msg);
		txtPanel.setEditable(false);
		txtPanel.setFont(new Font("Arial", Font.BOLD, 15));
		// label.set
		// feaPan.add(label);
		// tw.add(feaPan,BorderLayout.CENTER);
		tw.add(txtPanel, BorderLayout.CENTER);
		Executors.newCachedThreadPool().submit(tw);
		return Runout;
	}

	public static int createMessageTipWindow(String msg) {
		createMessageTipWindow(300, 220, 10, msg);
		return Runout;
	}

	public static int createConfirmTipWindow(String msg) {
		return createConfirmTipWindow(300, 220, -1, msg);
	}

	public static ProgressView createProgressTipWindow(IFileThread fileth,
			int totalsize, int type) {
		TipWindow tw = new TipWindow(300, 220, -1, type);
		ProgressView view = new ProgressView(fileth, tw);
		tw.add(view, BorderLayout.CENTER);
		Executors.newCachedThreadPool().submit(tw);
		return view;
	}

	public static int createConfirmTipWindow(int w, int h, int d, String msg) {
		TipWindow tw = new TipWindow(w, h, d, 2);
		JTextPane txtPanel = new JTextPane();
		txtPanel.setText(msg);
		txtPanel.setEditable(false);
		txtPanel.setFont(new Font("Arial", Font.BOLD, 15));
		// label.set
		// feaPan.add(label);
		// tw.add(feaPan,BorderLayout.CENTER);
		tw.add(txtPanel, BorderLayout.CENTER);
		Future<Integer> fur = Executors.newCachedThreadPool().submit(tw);
		int i = -1;
		try {
			i = fur.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return i;
		// return tw;
	}

	public void init() {
		// 新建300x220的消息提示框
		img = ResourceLocation.getImageIcon("image/logo.png");
		imgLabel = new JLabel(img);
		// 设置各个面板的布局以及面板中控件的边界
		headPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		btnPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		title = new JLabel("");
		close = new JLabel(" x");// 关闭按钮
		feature = new JTextArea(feaMap.get("feature"));
		jfeaPan = new JScrollPane(feature);
		releaseLabel = new JLabel("登录  " + feaMap.get("release"));

		// 将各个面板设置为透明，否则看不到背景图片
		((JPanel) getContentPane()).setOpaque(false);
		headPan.setOpaque(false);
		btnPan.setOpaque(false);

		// 设置JDialog的整个背景图片
		getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());

		// 设置提示框的边框,宽度和颜色
		getRootPane().setBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));

		title.setPreferredSize(new Dimension(260, 26));
		title.setVerticalTextPosition(JLabel.CENTER);
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setFont(new Font("宋体", Font.PLAIN, 12));
		title.setForeground(Color.black);

		close.setFont(new Font("Arial", Font.BOLD, 15));
		close.setPreferredSize(new Dimension(20, 20));
		close.setVerticalTextPosition(JLabel.CENTER);
		close.setHorizontalTextPosition(JLabel.CENTER);
		close.setCursor(new Cursor(12));
		close.setToolTipText("关闭");
		feature.setEditable(false);
		feature.setForeground(Color.red);
		feature.setFont(new Font("宋体", Font.PLAIN, 13));
		feature.setBackground(new Color(184, 230, 172));
		// 设置文本域自动换行
		feature.setLineWrap(true);

		jfeaPan.setPreferredSize(new Dimension(250, 80));
		jfeaPan.setBorder(null);
		jfeaPan.setBackground(Color.black);

		releaseLabel.setForeground(Color.DARK_GRAY);
		releaseLabel.setFont(new Font("宋体", Font.PLAIN, 12));

		// 为了隐藏文本域，加个空的JLabel将他挤到下面去
		JLabel jsp = new JLabel();
		jsp.setPreferredSize(new Dimension(300, 25));

		sure = new JLabel("确定");
		sure.setHorizontalAlignment(SwingConstants.CENTER);
		accepted = new JLabel("接受");
		accepted.setHorizontalAlignment(SwingConstants.CENTER);
		cancelb = new JLabel("取消");
		cancelb.setHorizontalAlignment(SwingConstants.CENTER);

		openDirect = new JLabel("打开目录");
		openDirect.setHorizontalAlignment(SwingConstants.CENTER);
		openFile = new JLabel("打开文件");
		openFile.setHorizontalAlignment(SwingConstants.CENTER);

		openDirect.setPreferredSize(new Dimension(110, 30));
		openDirect.setCursor(new Cursor(12));
		openFile.setPreferredSize(new Dimension(110, 30));
		openFile.setCursor(new Cursor(12));

		sure.setPreferredSize(new Dimension(110, 30));
		// 设置标签鼠标手形
		sure.setCursor(new Cursor(12));
		accepted.setPreferredSize(new Dimension(110, 30));
		accepted.setCursor(new Cursor(12));
		cancelb.setPreferredSize(new Dimension(110, 30));
		// 设置标签鼠标手形
		cancelb.setCursor(new Cursor(12));

		headPan.add(title);
		headPan.add(close);
		headPan.setBorder(BorderFactory.createLineBorder(Color.gray));
		// feaPan.add(jsp);
		// feaPan.add(jfeaPan);
		// feaPan.add(releaseLabel);
		if (this.type == 2) {
			btnPan.add(accepted);
			btnPan.add(cancelb);
		} else if (type == 1 || type == 3) {
			btnPan.add(sure);
		} else if (type == 4) {
			btnPan.add(sure);
		}
		add(headPan, BorderLayout.NORTH);
		add(btnPan, BorderLayout.SOUTH);
	}

	public void handle() {
		// 为更新按钮增加相应的事件
		if (type == 1 || type == 3 || type == 4) {
			sure.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					close(Sure);
				}

				public void mouseEntered(MouseEvent e) {
					sure.setBorder(BorderFactory.createLineBorder(Color.gray));
				}

				public void mouseExited(MouseEvent e) {
					sure.setBorder(null);
				}
			});
		} else if (type == 2) {
			accepted.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					close(Accept);
				}

				public void mouseEntered(MouseEvent e) {
					accepted.setBorder(BorderFactory
							.createLineBorder(Color.gray));
				}

				public void mouseExited(MouseEvent e) {
					accepted.setBorder(null);
				}
			});
			cancelb.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					close(Cancel);
				}

				public void mouseEntered(MouseEvent e) {
					cancelb.setBorder(BorderFactory
							.createLineBorder(Color.gray));
				}

				public void mouseExited(MouseEvent e) {
					cancelb.setBorder(null);
				}
			});
		}
		/*
		 * else if (type == 4) { openFile.addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent e) { }
		 * 
		 * public void mouseEntered(MouseEvent e) {
		 * openFile.setBorder(BorderFactory .createLineBorder(Color.gray)); }
		 * 
		 * public void mouseExited(MouseEvent e) { openFile.setBorder(null); }
		 * }); openDirect.addMouseListener(new MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent e) {
		 * System.out.println("openFile"); }
		 * 
		 * public void mouseEntered(MouseEvent e) {
		 * openDirect.setBorder(BorderFactory .createLineBorder(Color.gray)); }
		 * 
		 * public void mouseExited(MouseEvent e) { openDirect.setBorder(null); }
		 * }); }
		 */
		// 增加鼠标拖动事件
		title.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				Point newP = new Point(e.getXOnScreen(), e.getYOnScreen());
				int x = getX() + (newP.x - oldP.x);
				int y = getY() + (newP.y - oldP.y);
				setLocation(x, y);
				oldP = newP;

			}
		});
		// 鼠标按下时初始坐标,供拖动时计算用
		title.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				oldP = new Point(e.getXOnScreen(), e.getYOnScreen());
			}
		});

		// 右上角关闭按钮事件
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				close(Close);
			}

			public void mouseEntered(MouseEvent e) {
				close.setBorder(BorderFactory.createLineBorder(Color.gray));
			}

			public void mouseExited(MouseEvent e) {
				close.setBorder(null);
			}
		});
	}

	public void close(int close2) {
		if (this.result == -1) {
			copeResult(close2);
			this.result = close2;
		}
		x = this.getX();
		y = this.getY();
		int ybottom = (int) dim.getHeight() - screenInsets.bottom;
		for (int i = 0; i <= ybottom - y; i += 10) {
			try {
				setLocation(x, y + i);
				Thread.sleep(popupduration);
			} catch (InterruptedException ex) {
			}
		}
		dispose();
	}

	// overwrit
	public void copeResult(int close2) {
		System.out.println(close2);
	}

	public static void main(String args[]) {
		/*
		 * JProgressBar bar = TipWindow.createProgressTipWindow(4); try {
		 * Thread.sleep(5000); new BarThread(bar).start(); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */
	}

	@Override
	public Integer call() throws Exception {
		run();
		while (this.result == -1) {
			Thread.sleep(1000);
		}
		return this.result;
	}
	/**
	 * 废弃代码
	 */
/*	static class BarThread extends Thread {
		private static int DELAY = 500;
		JProgressBar progressBar;

		public BarThread(JProgressBar bar) {
			progressBar = bar;
		}

		public void run() {
			int minimum = progressBar.getMinimum();
			int maximum = progressBar.getMaximum();
			Runnable runner = new Runnable() {
				public void run() {
					int value = progressBar.getValue();
					progressBar.setValue(value + 1);
				}
			};
			for (int i = minimum; i < maximum; i++) {
				try {
					SwingUtilities.invokeAndWait(runner);
					// Our task for each step is to just sleep
					Thread.sleep(DELAY);
				} catch (InterruptedException ignoredException) {
				} catch (InvocationTargetException ignoredException) {
				}
			}
		}
	}*/
}