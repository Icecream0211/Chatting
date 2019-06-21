package com.xiyou.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.bing.common.ResourceLocation;

public abstract class MyFrame extends JFrame {
	private Image logo = ResourceLocation.getImage("image/logo.png");
	private String title="Local Chat";
	public MyFrame(){
		this.init();
	}
	public void init(){
		this.setIconImage(logo);
		this.setTitle(this.title);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
			
		});
	}
	public abstract void closeFrame();
}
