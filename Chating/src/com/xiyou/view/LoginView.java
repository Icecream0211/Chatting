package com.xiyou.view;

import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import net.sf.json.JSONArray;

import com.bing.common.CommonConfig;
import com.bing.common.ResourceLocation;
import com.test.Test2;

@SuppressWarnings("serial")
public class LoginView extends JFrame {
	private JLabel headLabel;
	private JLabel nikeNameLabel;
	private JTabbedPane tabPanel;
	private JScrollPane panel1;
	private JPanel panel2;
	private JSeparator jSeparator1;

	public LoginView() {
		initComponents();
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
		headLabel.setIcon(new ImageIcon(ResourceLocation.getImageIcon("image/upload.jpg")
				.getImage().getScaledInstance(78, 49, Image.SCALE_DEFAULT)));
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				new java.awt.Color(255, 255, 255)));
		nikeNameLabel.setText("昵称" + "(" + 123456789 + ")");

		tabPanel = new JTabbedPane();
		JTree tree = new ContactTreeView(Test2.contactlists);
		panel1 = new JScrollPane(tree);
		panel2 = new JPanel();
		new JPanel();
		// 定义几个初始节点
		tabPanel.addTab("联系人", ResourceLocation.getImageIcon("image/contact.jpg"), panel1);
		// tabPanel.add("First", panel1);
		tabPanel.addTab("最近联系人", ResourceLocation.getImageIcon("image/recently.jpg"), panel2);
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
		javax.swing.ImageIcon icon3 = ResourceLocation.getImageIcon("image/logo.png");
		setIconImage(icon3.getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("登录...");
		setLocation(CommonConfig.SYS_SCREEN_WIDTH / 3,
				CommonConfig.SYS_SCREEN_HEIGHT / 3);
		// setUndecorated(true);
		// Create frame
		// Set icon
		pack();
	}

	public static void main(String args[]) {
		String classPath = LoginView.class.getClassLoader()
				.getResource("logo.jpg").getPath();
		System.out.println(classPath);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginView().setVisible(true);
			}
		});
	}

	private void initContactList() {
		String contactlists = "[{\"hostName\":\"\",\"ip\":\"192.168.6.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222},{\"hostName\":\"\",\"ip\":\"192.168.5.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222},{\"hostName\":\"\",\"ip\":\"192.168.4.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222}]";
		JSONArray arrs = JSONArray.fromObject(contactlists);
	}
}
