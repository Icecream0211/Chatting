package com.xiyou.view;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.EventQueue;
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
import java.io.File;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bing.chart.main.Main;
import com.bing.common.CommonConfig;
import com.bing.common.ResourceLocation;
import com.test.Test2;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	private String hostname;
	private String ip;
	private int lisport;
	private JLabel headLabel;
	private JLabel nikeNameLabel;
	private JTabbedPane tabPanel;
	private JScrollPane panel1;
	private JPanel panel2;
	private JSeparator jSeparator1;
	private ContactTreeView contractTree;
	private TrayIcon tricon;
	private boolean istrayed = false;

	public MainView(String hostname, String ip, int lisport) {
		this.hostname = hostname;
		this.ip = ip;
		this.lisport = lisport;
		initComponents();
	}

	public void showup() {
		this.setVisible(true);
		/*
		 * java.awt.EventQueue.invokeLater(new Runnable() { public void run() {
		 * new MainView().setVisible(true); } });
		 */
	}

	private void initComponents() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 第一部分的panel---------start--------------
		headLabel = new JLabel();
		nikeNameLabel = new JLabel();
		jSeparator1 = new JSeparator();
		jSeparator1.setForeground(new java.awt.Color(204, 204, 0));

		headLabel.setIcon(new ImageIcon(ResourceLocation
				.getImageIcon("image/upload.jpg").getImage()
				.getScaledInstance(78, 49, Image.SCALE_DEFAULT)));
		// headLabel.setIcon(new ImageIcon(new ImageIcon("image\\upload.jpg")
		// .getImage().getScaledInstance(78, 49, Image.SCALE_DEFAULT))); //old
		// use

		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				new java.awt.Color(255, 255, 255)));
		nikeNameLabel.setText(this.hostname + "(" + this.ip + ":"
				+ this.lisport + ")");

		tabPanel = new JTabbedPane();
		contractTree = new ContactTreeView();
		contractTree.setContactlist("[]");
		contractTree.updateShow();
		panel1 = new JScrollPane(contractTree);
		panel2 = new JPanel();
		new JPanel();
		// 定义几个初始节点
		tabPanel.addTab("联系人",
				ResourceLocation.getImageIcon("image/contact.jpg"), panel1);
		// tabPanel.add("First", panel1);
		tabPanel.addTab("最近联系人",
				ResourceLocation.getImageIcon("image/recently.jpg"), panel2);
		// tabPanel.add("Second", );
		// tabPanel.add("Third", panel3);
		tabPanel.setTabPlacement(JTabbedPane.TOP);
		Container c = getContentPane();
		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);
		// 自动设定组件、组之间的间隙
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup line1 = layout.createSequentialGroup();
		line1.addComponent(headLabel);
		line1.addComponent(nikeNameLabel);

		GroupLayout.ParallelGroup para1 = layout
				.createParallelGroup(GroupLayout.Alignment.LEADING);
		para1.addGroup(line1);
		// para1.addComponent(jSeparator1);
		para1.addComponent(tabPanel);

		layout.setHorizontalGroup(para1);
		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(headLabel)
								.addComponent(nikeNameLabel))
				.addComponent(tabPanel));
		// .addComponent(jSeparator1)
		setSize(250, 517);
		// setSize(new Dimension(300, 600));
		ImageIcon icon3 =ResourceLocation.getImageIcon("image/logo.png");
		setIconImage(icon3.getImage());
		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Local Net Chat");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Main.getInstance().stop();
				winClose();
			}
		});
		setLocation(CommonConfig.SYS_SCREEN_WIDTH / 3,
				CommonConfig.SYS_SCREEN_HEIGHT / 3);
		// setUndecorated(true);
		// Create frame
		// Set icon
		pack();
	}

	private void winClose() {
		if (!istrayed) {
			Object[] options = { "是的，退出程序", "不，隐藏到任务栏通知区域", "点错了" };
			// 构造对话框
			int n = JOptionPane.showOptionDialog(this, "您确定要推出？", "Question",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
			if (n == JOptionPane.YES_OPTION) {
				System.out.println("yesoption");
				exitSystem();
			} else if (n == JOptionPane.NO_OPTION) {
				System.out.println("no option");
				makeTray();
			} else if (n == JOptionPane.CANCEL_OPTION) {
				System.out.println("cancel option");
				this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			} else {
			}
		}
	}

	public void exitSystem() {
		Main.getInstance().stop();
		// System.exit(0);
	}

	private void makeTray() {
		// setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		// this.addWindowListener(new WindowAdapter() {
		// public void windowClosing(WindowEvent e) {
		if (SystemTray.isSupported()) {
			setVisible(false);// 隐藏窗口
			initTi();
			minimizeToTray();// 如果系统支持托盘图标,将窗口放入系统托盘区
		} else {
			// System.exit(0);// 否则结束程序
		}
		// }
		// });
	}

	private void initTi() {
		if (!istrayed) {
			Image image = ResourceLocation.getImage("image/logo.png");
			// ImageIcon icon = new
			// ImageIcon(this.getClass().getResource("image/logo.png"));
			/*
			 * ImageIcon icon1 = new
			 * ImageIcon(MainView.class.getClass().getResource(
			 * "image/logo.png"));
			 */
			ImageIcon icon2 = ResourceLocation.getImageIcon("image/logo.png");
			image = icon2.getImage();
			PopupMenu popupTi = new PopupMenu();// 弹出菜单
			MenuItem showItem = new MenuItem("Show");// 菜单项
			MenuItem exitItem = new MenuItem("exit");// 菜单项
			ActionListener showListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(true);// 重新显示窗口
					// SystemTray.getSystemTray().remove(tricon);// 从系统托盘中移出
				}
			};
			exitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					exitSystem();
				}
			});
			showItem.addActionListener(showListener);
			popupTi.add(showItem);
			popupTi.add(exitItem);
			tricon = new TrayIcon(image, "TrayIcon", popupTi);// 图标，标题，右键弹出菜单
			tricon.addActionListener(showListener);// 增加一个双击事件听众
		}
	}

	public void minimizeToTray() {
		SystemTray tray = SystemTray.getSystemTray();
		try {
			if (!istrayed) {
				tray.add(tricon);// 在系统托盘区中增加图标
				istrayed = true;
			}
		} catch (AWTException e) {
			System.err.println(e);
		}
	}

	public static void main(String args[]) {
		MainView mainView;
		String classPath = MainView.class.getClassLoader()
				.getResource("image/logo.png").getPath();
		System.out.println(classPath);
		mainView = new MainView(CommonConfig.MY_HOSTNAME, CommonConfig.MY_IP,
				CommonConfig.CLIENT_TCP_PORT);
		EventQueue que = new EventQueue();
		mainView.showup();
		mainView.updateUserList(Test2.contactlists2);
		Scanner canner = new Scanner(System.in);
		canner.next();

	}

	public void addNode(JSONObject obj) {
		this.contractTree.addNode(obj);
	}

	public void removeNode(JSONObject jsonObject) {
		this.contractTree.removeNode(jsonObject);
	}

	public void updateUserList(String contactLists) {
		SwingUtilities.invokeLater(new TreeUpdateThread(contactLists,
				contractTree));
	}
}
