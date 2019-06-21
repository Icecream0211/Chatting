package org.demo;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChatClient extends JFrame {
	JPanel p1, p2, p3, p4;
	JLabel label = new JLabel("对方ＩＰ：");

	// 初始时，在接收图像窗口显示一幅静态图片
	public ChatClient() {
		super("视频传输");
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
		// p3.add(label2, "Center");
		add(label, "North");
		add(p1, "Center");
		setVisible(true);
	}

	public static void main(String[] args) {
		new ChatClient();
	}
}
