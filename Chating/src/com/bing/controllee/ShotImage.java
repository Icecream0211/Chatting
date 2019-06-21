package com.bing.controllee;

/************************************************************************ 
 *************************************************************************/
/* 
 图片采集的代码 
 */

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ShotImage {
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private String imageFormat = "jpg";
	private int Num = 0;
	private BufferedImage screenshot;
	private Robot robot;

	public ShotImage() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public synchronized BufferedImage snapShot() {
		try {
			screenshot = robot.createScreenCapture(new Rectangle(0, 0,
					(int) dimension.getWidth(), (int) dimension.getHeight()));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return screenshot;
	}
}