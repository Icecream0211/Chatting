package com.bing.chart.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.demo.TxtChat;

import com.bing.common.CommonConfig;
import com.bing.common.ResourceLocation;

public class MainFrame extends JFrame {
	JPanel p1, p2, p3, p4;
	JLabel label = new JLabel("对方ＩＰ：");
	// 初始时，在接收图像窗口显示一幅静态图片
	JLabel label2 = new JLabel(ResourceLocation.getImageIcon("image/load.gif"));

	// 定义二个线程，用于接收数据和发送数据
	public MainFrame() {
		try {
			/**
			 * 代码 如上，第一句是调用默认的JAVA SWING风格的截面，也就是跨平台的 风格
			 * 第二句是调用本地系统的显示风格，如果你的操作系统是WINDOWS，那么出现的就是WINDOWS风格 第三句是MOTIF风格
			 */
			// UIManager.setLookAndFeel(UIManager
			// .getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager
			// .setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			setLayout(new BorderLayout());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setBounds(CommonConfig.SYS_SCREEN_WIDTH / 3,
				CommonConfig.SYS_SCREEN_HEIGHT / 3 - 100, 600, 500);
		setSize(600, 500);

		setBounds(150, 100, 500, 500);
		p1 = new JPanel(new GridLayout(1, 2));
		p2 = new JPanel(new GridLayout(2, 1));
		p3 = new JPanel(new BorderLayout());
		p4 = new JPanel(new BorderLayout());
		// 加载文本数据传输类
		p1.add(new TxtChat());
		p1.add(p2);
		p2.add(p3);
		p2.add(p4);
		p3.add("North", new JLabel("Java视频图像传输"));
		p3.add(label2, "Center");
		add(label, "North");
		add(p1, "Center");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
