package org.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bing.chart.view.SendMessageButton;

public class TxtChat extends JPanel implements ActionListener, Runnable {
	JPanel p1, p2, p3, p4;
	JLabel jpic;
	JTextArea txt1, txt2;
	JButton btn1, btn2;
	JScrollPane scroll, scroll2;
	Thread thread;
	boolean boo1 = false, boo2 = false;

	public TxtChat() {
		setLayout(new BorderLayout());
		txt1 = new JTextArea();
		txt2 = new JTextArea(4, 40);
		p1 = new JPanel(new GridLayout(2, 1));
		p2 = new JPanel();
		p3 = new JPanel(new BorderLayout());
		// jpic = new JLabel(new ImageIcon("image//uploadd.jpg"));
		jpic = new JLabel();
		jpic.setSize(20, 100);
		btn1 = new JButton("取消");
		// btn2 = new JButton("发送信息");
		btn2 = new SendMessageButton();
		scroll = new JScrollPane(txt1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2 = new JScrollPane(txt2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p1.add(scroll);
		p1.add(p3);
		p2.add(btn1);
		p2.add(btn2);
		p3.add(jpic, "North");
		p3.add(scroll2, "Center");
		add(p1, "Center");
		add(p2, "South");
		setVisible(true);
		Font f = new Font("", Font.PLAIN, 18);
		txt1.setFont(f);
		txt1.setForeground(Color.red);
		txt2.setFont(f);
		txt2.setForeground(Color.blue);
		btn1.setBackground(Color.cyan);
		btn2.setBackground(Color.yellow);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		thread = new Thread(this);
		thread.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn2) {
			byte buffer[] = txt2.getText().trim().getBytes();
			try {
				InetAddress address = InetAddress.getByName("localhost");
				DatagramPacket data_pack = new DatagramPacket(buffer,
						buffer.length, address, 888);
				DatagramSocket mail_data = new DatagramSocket();
				txt1.append("我说：" + txt2.getText() + '\n');
				mail_data.send(data_pack);
				txt2.setText("");
			} catch (Exception e1) {
				System.out.println("聊天信息发送失败！");
			}
		}
	}

	public void run() {
		DatagramPacket pack = null;
		DatagramSocket mail_data = null;
		byte data[] = new byte[8192];
		try {
			pack = new DatagramPacket(data, data.length);
			mail_data = new DatagramSocket(666);
		} catch (Exception e) {
		}
		while (true) {
			if (Thread.currentThread() == thread) {
				if (mail_data == null) {
					break;
				} else {
					try {
						mail_data.receive(pack);
						int length = pack.getLength();
						String message = new String(pack.getData(), 0, length);
						txt1.append("某某说：" + message + '\n');
					} catch (Exception e) {
						System.out.println("接收数据失败！");
					}
				}
			}
		}// while end
	} // run() end
}