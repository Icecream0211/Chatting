package com.test;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.net.URL;
//import java.net.URLClassLoader;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import com.xiyou.view.MainView;

public class TestSysteTray {
	public static void main(String args[]) {
		TrayIcon trayIcon = null;
		if (SystemTray.isSupported()) { // 判断系统是否支持系统托盘
			SystemTray tray = SystemTray.getSystemTray(); // 获取系统托盘
			javax.swing.ImageIcon icon3 = new javax.swing.ImageIcon(
					MainView.class.getClassLoader().getResource("logo.jpg"));
			Image image = Toolkit.getDefaultToolkit().getImage("logo.jpg");// 载入图片
			ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 创建一个窗体
					JFrame frame = new JFrame();
					frame.setBounds(400, 400, 200, 200);
					JLabel label = new JLabel("welcome JDK1.6");
					frame.add(label);
					frame.setVisible(true);

				}
			};
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 创建托盘图标的右键弹出菜单，open与exit.
			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Open MainFrame");
			defaultItem.addActionListener(listener);
			MenuItem exitItem = new MenuItem("Exit System");
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			popup.add(defaultItem);
			popup.add(exitItem);
			trayIcon = new TrayIcon(icon3.getImage(), "My System Tray ", popup);// 创建托盘图标
			trayIcon.addActionListener(listener);// 双击托盘图标时打开窗体
			try {
				tray.add(trayIcon);// 将图标加入到系统托盘区
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
	}
}