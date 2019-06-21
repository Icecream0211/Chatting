package com.bing.chat.ui.common;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.bing.common.ResourceLocation;

public class TopPanel extends JPanel {
	private String title;
	public final JLabel closeButton;
	public final JLabel maxButton;
	public final JLabel minbutton;
	public final JLabel titlelabel;
	private ImageIcon close = ResourceLocation.getImageIcon("image/guanbi.jpg");
	private ImageIcon close_fake = ResourceLocation
			.getImageIcon("image/guanbi1.jpg");
	private ImageIcon max = ResourceLocation.getImageIcon("image/zuidahua.jpg");
	private ImageIcon max_fake = ResourceLocation
			.getImageIcon("image/zuidahua1.jpg");
	private ImageIcon min = ResourceLocation
			.getImageIcon("image/zuixiaohua.jpg");
	private ImageIcon min_fake = ResourceLocation
			.getImageIcon("image/zuixiaohua1.jpg");
	private ImageIcon image = ResourceLocation.getImageIcon("image/back.jpg");

	private JFrame fauther;

	public TopPanel(String title, JFrame fauther) {
		this.fauther = fauther;
		this.title = title;
		setDragable();
		// this.addMouseMotionListener(new TopMotionAdapter(this));
		this.addMouseListener(new TopMotionAdapter(this));
		minbutton = new JLabel("");
		minbutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				minbutton.setIcon(min_fake);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				minbutton.setIcon(min);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
		minbutton.setIcon(min);
		maxButton = new JLabel("");
		maxButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				maxButton.setIcon(max_fake);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				maxButton.setIcon(max);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		});
		maxButton.setIcon(max);

		closeButton = new JLabel("");
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(close_fake);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(close);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

		});
		closeButton.setIcon(close);
		titlelabel = new JLabel(this.title);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addComponent(titlelabel, GroupLayout.DEFAULT_SIZE,
								221, Short.MAX_VALUE).addGap(6)
						.addComponent(minbutton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(maxButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(closeButton)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																titlelabel,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																20,
																Short.MAX_VALUE)
														.addGroup(
																Alignment.LEADING,
																groupLayout
																		.createParallelGroup(
																				Alignment.TRAILING,
																				false)
																		.addComponent(
																				closeButton,
																				Alignment.LEADING)
																		.addComponent(
																				maxButton,
																				Alignment.LEADING,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				minbutton,
																				Alignment.LEADING,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)))
										.addGap(270)));
		setLayout(groupLayout);
	}

	URL imgURL = ResourceLocation.getFileURL("image/back.jpg");

	// 重写的绘图函数，绘制平铺图片
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setOpaque(true);
		// 绘制平铺图片背景
		ImageIcon icon = new ImageIcon(imgURL);

		// 每一副图像的位置坐标
		int x = 0;
		int y = 0;

		// 平铺背景图片
		while (true) {
			// 绘制图片
			g.drawImage(icon.getImage(), x, y, this);

			// 如果绘制完毕，退出循环
			if (x > getSize().width && y > getSize().height)
				break;

			// 如果绘完一行，换行绘制
			if (x > getSize().width) {
				x = 0;
				y += icon.getIconHeight();
			}
			// 如果在当前行，得到下一个图片的坐标位置
			else
				x += icon.getIconWidth();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("tst");
		frame.getContentPane().add(new TopPanel("showmu", frame));
		frame.setDefaultCloseOperation(3);
		frame.setUndecorated(true);
		frame.pack();
		frame.setVisible(true);
	}

	public void addTopButtonListener(JLabel abc,
			TopPanelListenerAdapter topadapter) {
		abc.addMouseListener(topadapter);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Component getFather() {
		return this.fauther;
	}

	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;

	private void setDragable() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				isDragged = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());
				isDragged = true;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent e) {
				if (isDragged) {
					loc = new Point(fauther.getLocation().x + e.getX() - tmp.x,
							fauther.getLocation().y + e.getY() - tmp.y);
					fauther.setLocation(loc);
				}
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="10,10"
