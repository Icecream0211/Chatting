package com.bing.controller;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.bing.controllee.SurveillanceInstance;

//（三）图像显示，鼠标，键盘监听
/*-图像显示-*/
public class ControlledScreenPanel extends JPanel implements
		MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
	private BufferedImage image;
	private MainControlSocket control; // 发送鼠标，键盘事件到被控端
	private JFrame showFrame;
	private Dimension size;
	private boolean isShowed = false;
	private SurveillanceInstance surveilins;

	public void showUP() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		showFrame = new JFrame();
		JScrollPane scpan = new JScrollPane(this,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		showFrame.add(scpan);
		showFrame.setVisible(true);
		showFrame.setSize(size.width, size.height - 100);
		showFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(surveilins!=null){
					surveilins.close();
				}
			}
		});
	}

	public ControlledScreenPanel(MainControlSocket mainSocket,
			SurveillanceInstance surveilIns) {
		if (mainSocket != null) {
			this.control = mainSocket;
		}
		this.surveilins = surveilIns;
	}

	public void setBufferedImage(BufferedImage bi) { // 更新图像
		image = bi;
		// Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		// image = ImageTools.resizeBufferedImage(bi, d.width, d.height, true);
		// Dimension d = new Dimension(image.getWidth(this),
		// image.getHeight(this));
		// setPreferredSize(d);
		this.size = new Dimension(image.getWidth(this), image.getHeight(this));
		this.setPreferredSize(this.size);
		// revalidate();
		repaint();
		if (this.showFrame == null) {
			showUP();
		}
	}

	public void paintComponent(Graphics g) { // 绘制图像
		Dimension d = getSize();
		if (image != null) {
			/*
			 * BufferedImage img = ImageTools.resizeBufferedImage(image,
			 * d.width, d.height, true);
			 */
			g.drawImage(image, 0, 0, d.width, d.height, null);
		}
	}

	/*-鼠标，键盘监听-*/
	public void mouseDragged(MouseEvent e) {
		control.sendControlledAction(e);
	}

	public void mouseMoved(MouseEvent e) {
		control.sendControlledAction(e);
	}

	// --------------------------------------------------------------------------
	public void mouseClicked(MouseEvent e) {
		requestFocus();// 单击获得焦点
	}

	public void mousePressed(MouseEvent e) { // 鼠标按下
		control.sendControlledAction(e);
	}

	public void mouseReleased(MouseEvent e) { // 鼠标释放
		control.sendControlledAction(e);
	}

	public void mouseEntered(MouseEvent e) {
		control.sendControlledAction(e);
	}

	public void mouseExited(MouseEvent e) {
		control.sendControlledAction(e);
	}

	// --------------------------------------------------------------------------

	public void mouseWheelMoved(MouseWheelEvent e) { // 滑轮滚动
		control.sendControlledAction(e);
	}

	// --------------------------------------------------------------------------

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) { // 键盘按下
		control.sendControlledAction(e);
	}

	public void keyReleased(KeyEvent e) { // 键盘释放
		control.sendControlledAction(e);
	}
}
