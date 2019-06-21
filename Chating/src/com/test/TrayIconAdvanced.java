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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
public class TrayIconAdvanced extends JFrame {
    private TrayIcon ti;
    public static void main(String[] args) {
        TrayIconAdvanced frame = new TrayIconAdvanced();
        frame.setTitle("TrayIconAdvanced");
        frame.setVisible(true);
        frame.setSize(200, 65);
        frame.setLocation(300, 200);
    }
    public TrayIconAdvanced() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    setVisible(false);//隐藏窗口
                    minimizeToTray();//如果系统支持托盘图标,将窗口放入系统托盘区
                } else {
                    System.exit(0);//否则结束程序
                }
            }
        });
        JPanel root = new JPanel();
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);//结束程序
            }
        });
        root.add(exitButton);
        getContentPane().add(root);
        pack();
        initTi();
    }
    private void initTi() {
        Image image = Toolkit.getDefaultToolkit().getImage(
                      this.getClass().getResource("TrayIcon.png"));
        PopupMenu popupTi = new PopupMenu();//弹出菜单
        MenuItem showItem = new MenuItem("Show");//菜单项
        ActionListener showListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(true);//重新显示窗口
                SystemTray.getSystemTray().remove(ti);//从系统托盘中移出
            }
        };
        showItem.addActionListener(showListener);
        popupTi.add(showItem);
        ti = new TrayIcon(image, "TrayIcon", popupTi);//图标，标题，右键弹出菜单
        ti.addActionListener(showListener);//增加一个双击事件听众
       
    }
    public void minimizeToTray() {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(ti);//在系统托盘区中增加图标
        } catch (AWTException e) {
            System.err.println(e);
        }
    }
}