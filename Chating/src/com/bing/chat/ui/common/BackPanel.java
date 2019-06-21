package com.bing.chat.ui.common;

import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.bing.common.ResourceLocation;

public class BackPanel extends JPanel {
	URL imgURL = ResourceLocation.getFileURL("image/back.jpg");

	// 重写的绘图函数，绘制平铺图片
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setOpaque(true);
		// 绘制平铺图片背景
		ImageIcon icon = new ImageIcon(imgURL);

		// 每一副图像的位置坐标
		int x = 0;
		int y = 0;

		// 平铺背景图片
		while (true) {
			// 绘制图片
			g.drawImage(icon.getImage(), x, y, this);

			// 如果绘制完毕，退出循环
			if (x > getSize().width && y > getSize().height)
				break;

			// 如果绘完一行，换行绘制
			if (x > getSize().width) {
				x = 0;
				y += icon.getIconHeight();
			}
			// 如果在当前行，得到下一个图片的坐标位置
			else
				x += icon.getIconWidth();
		}
	}
}
