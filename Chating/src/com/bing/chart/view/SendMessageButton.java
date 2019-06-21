package com.bing.chart.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SendMessageButton extends JButton implements ActionListener {
	public SendMessageButton() {
		super();
		setText("SendMessage");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}
}
