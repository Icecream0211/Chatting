package com.bing.chat.ui.mainview;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import net.sf.json.JSONObject;

import com.bing.chart.main.Main;
import com.bing.chat.ui.common.BackPanel;
import com.bing.chat.ui.common.TopPanel;
import com.bing.chat.ui.common.TopPanelListenerAdapter;
import com.bing.chat.ui.tree.NewTreePanel;
import com.bing.chat.ui.tree.TreeUpdateThreadN;
import com.bing.common.ResourceLocation;
import com.test.Test2;

public class MainResizeFrame extends JFrame {
	private boolean isTopLeft;// 是否处于左上角调整窗口状态
	private boolean isTop;// 是否处于上边界调整窗口状态
	private boolean isTopRight;// 是否处于右上角调整窗口状态
	private boolean isRight;// 是否处于右边界调整窗口状态
	private boolean isBottomRight;// 是否处于右下角调整窗口状态
	private boolean isBottom;// 是否处于下边界调整窗口状态
	private boolean isBottomLeft;// 是否处于左下角调整窗口状态
	private boolean isLeft;// 是否处于左边界调整窗口状态
	private final static int RESIZE_WIDTH = 5;// 判定是否为调整窗口状态的范围与边界距离
	private final static int MIN_WIDTH = 20;// 窗口最小宽度
	private final static int MIN_HEIGHT = 20;// 窗口最小高度
	//
	private String title;
	private final TopPanel topPanel;
	private final BackPanel showpanel;
	private final NewTreePanel newTreePanel;
	private String nickname;
	private JLabel mynickname;
	private String hostname;
	private String ip;
	private int lisport;

	public MainResizeFrame(String title, String hostname, String ip, int lisport) {
		this(title, hostname + "(" + ip + ":" + lisport + ")");
		this.hostname = hostname;
		this.ip = ip;
		this.lisport = lisport;
	}

	public MainResizeFrame(String title, String nickname) {
		this.title = title;
		this.nickname = nickname;
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().setBounds(new Rectangle(1, 1, 1, 1));
		addMouseMotionListener(new ResizeAdapter(this));
		topPanel = new TopPanel(this.title, this);
		topPanel.setPreferredSize(new Dimension(512, 29));
		topPanel.setMinimumSize(new Dimension(309, 29));
		topPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		topPanel.addTopButtonListener(topPanel.closeButton,
				new TopPanelListenerAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						Main.getInstance().stop();
					}
				});
		showpanel = new BackPanel();
		showpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		showpanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout
				.createParallelGroup(Alignment.TRAILING)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addComponent(topPanel,
										GroupLayout.PREFERRED_SIZE, 279,
										Short.MAX_VALUE).addGap(0))
				.addGroup(
						Alignment.LEADING,
						groupLayout
								.createSequentialGroup()
								.addGap(10)
								.addComponent(showpanel,
										GroupLayout.DEFAULT_SIZE, 259,
										Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addComponent(topPanel, GroupLayout.PREFERRED_SIZE, 37,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(showpanel, GroupLayout.DEFAULT_SIZE, 457,
								Short.MAX_VALUE)));

		newTreePanel = new NewTreePanel("[]");
		JPanel panel = new JPanel();
		GroupLayout gl_showpanel = new GroupLayout(showpanel);
		gl_showpanel.setHorizontalGroup(gl_showpanel
				.createParallelGroup(Alignment.TRAILING)
				.addComponent(newTreePanel, Alignment.LEADING,
						GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
				.addComponent(panel, Alignment.LEADING,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));
		gl_showpanel.setVerticalGroup(gl_showpanel.createParallelGroup(
				Alignment.TRAILING).addGroup(
				gl_showpanel
						.createSequentialGroup()
						.addGap(4)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 72,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(newTreePanel, GroupLayout.DEFAULT_SIZE,
								375, Short.MAX_VALUE).addContainerGap()));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(ResourceLocation.getImageIcon("image/upload.jpg"));

		mynickname = new JLabel(this.nickname);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addGap(8)
						.addComponent(lblNewLabel)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(mynickname, GroupLayout.PREFERRED_SIZE,
								173, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGap(26)
																.addComponent(
																		mynickname))
												.addComponent(
														lblNewLabel,
														GroupLayout.PREFERRED_SIZE,
														72,
														GroupLayout.PREFERRED_SIZE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		showpanel.setLayout(gl_showpanel);
		getContentPane().setLayout(groupLayout);
		topPanel.addTopButtonListener(topPanel.closeButton,
				new TopPanelListenerAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						abc(e);
					}
				});
		setUndecorated(true);
	}

	public void addShowPanel(Component c) {
		this.showpanel.add(c);
	}

	public void abc(MouseEvent ee) {
		this.dispose();
		this.setVisible(false);
		System.exit(0);
	}

	private class ResizeAdapter extends MouseAdapter {
		private Component c;

		public ResizeAdapter(Component c) {
			this.c = c;
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			int x = event.getX();
			int y = event.getY();
			int width = c.getWidth();
			int height = c.getHeight();
			int cursorType = Cursor.DEFAULT_CURSOR;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
			// 先将所有调整窗口状态重置
			isTopLeft = isTop = isTopRight = isRight = isBottomRight = isBottom = isBottomLeft = isLeft = false;
			if (y <= RESIZE_WIDTH) {
				if (x <= RESIZE_WIDTH) {// 左上角调整窗口状态
					isTopLeft = true;
					cursorType = Cursor.NW_RESIZE_CURSOR;
				} else if (x >= width - RESIZE_WIDTH) {// 右上角调整窗口状态
					isTopRight = true;
					cursorType = Cursor.NE_RESIZE_CURSOR;
				} else {// 上边界调整窗口状态
					isTop = true;
					cursorType = Cursor.N_RESIZE_CURSOR;
				}
			} else if (y >= height - RESIZE_WIDTH) {
				if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
					isBottomLeft = true;
					cursorType = Cursor.SW_RESIZE_CURSOR;
				} else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
					isBottomRight = true;
					cursorType = Cursor.SE_RESIZE_CURSOR;
				} else {// 下边界调整窗口状态
					isBottom = true;
					cursorType = Cursor.S_RESIZE_CURSOR;
				}
			} else if (x <= RESIZE_WIDTH) {// 左边界调整窗口状态
				isLeft = true;
				cursorType = Cursor.W_RESIZE_CURSOR;
			} else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
				isRight = true;
				cursorType = Cursor.E_RESIZE_CURSOR;
			}
			// 最后改变鼠标光标
			c.setCursor(new Cursor(cursorType));
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			int x = event.getX();
			int y = event.getY();
			int width = c.getWidth();
			int height = c.getHeight();
			// 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
			int nextX = c.getX();
			int nextY = c.getY();
			int nextWidth = width;
			int nextHeight = height;
			if (isTopLeft || isLeft || isBottomLeft) {// 所有左边调整窗口状态
				nextX += x;
				nextWidth -= x;
			}
			if (isTopLeft || isTop || isTopRight) {// 所有上边调整窗口状态
				nextY += y;
				nextHeight -= y;
			}
			if (isTopRight || isRight || isBottomRight) {// 所有右边调整窗口状态
				nextWidth = x;
			}
			if (isBottomLeft || isBottom || isBottomRight) {// 所有下边调整窗口状态
				nextHeight = y;
			}
			if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
				nextWidth = MIN_WIDTH;
				if (isTopLeft || isLeft || isBottomLeft) {// 如果是从左边缩小的窗口，x坐标也要调整
					nextX = c.getX() + width - nextWidth;
				}
			}
			if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
				nextHeight = MIN_HEIGHT;
				if (isTopLeft || isTop || isTopRight) {// 如果是从上边缩小的窗口，y坐标也要调整
					nextY = c.getY() + height - nextHeight;
				}
			}
			// 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
			setBounds(nextX, nextY, nextWidth, nextHeight);
		}
	}

	public static void main(String[] args) {
		// 一个简单的演示小例子
		MainResizeFrame frame = new MainResizeFrame("QQ2222",
				"192.168.4.4:9999");
		/*
		 * frame.setSize(283, 500); Dimension screenSize =
		 * Toolkit.getDefaultToolkit().getScreenSize();
		 * frame.setLocation((screenSize.width - frame.getWidth()) / 2,
		 * (screenSize.height - frame.getHeight()) / 2);
		 * frame.addMouseListener(new MouseAdapter() { public void
		 * mouseClicked(MouseEvent event) { if (event.getClickCount() > 1) {
		 * System.exit(0); } } }); // frame.setSize(500, 500);
		 * frame.updateUserList(Test2.contactlists2); // frame.addShowPanel(new
		 * TxtChatView()); frame.setVisible(true);
		 */
		frame.showup();
		frame.updateUserList(Test2.contactlists2);
	}

	public void updateUserList(String contactLists) {
		SwingUtilities.invokeLater(new TreeUpdateThreadN(contactLists,
				newTreePanel));
	}

	public void showup() {
		this.setSize(283, 550);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.getInstance().stop();
			}
		});
		setVisible(true);
	}

	public void close() {
		this.dispose();
	}

	public void addNode(JSONObject jsonobj) {
		this.newTreePanel.addNode(jsonobj);
	}

	public void removeNode(JSONObject jsonObject) {
		this.newTreePanel.removeNode(jsonObject);
	}
}