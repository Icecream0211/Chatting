package com.bing.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageTools {
	/**
	 * 调整bufferedimage大小
	 * 
	 * @param source
	 *            BufferedImage 原始image
	 * @param targetW
	 *            int 目标宽
	 * @param targetH
	 *            int 目标高
	 * @param flag
	 *            boolean 是否同比例调整
	 * @return BufferedImage 返回新image
	 */
	public static BufferedImage resizeBufferedImage(BufferedImage source,
			int targetW, int targetH, boolean flag) {
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		if (flag && sx > sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else if (flag && sx <= sy) {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(targetW, targetH, type);
		}
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	public static BufferedImage readImage(String fileName) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(fileName));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return bi;
	}

	public static boolean writeImage(RenderedImage im, String formatName,
			String fileName) {
		boolean result = false;
		try {
			result = ImageIO.write(im, formatName, new File(fileName));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return result;
	}

	public static boolean writeJPEGImage(RenderedImage im, String fileName) {
		return writeImage(im, "JPEG ", fileName);
	}

	public static boolean writeGIFImage(RenderedImage im, String fileName) {
		return writeImage(im, "GIF ", fileName);
	}

	public static boolean writePNGImage(RenderedImage im, String fileName) {
		return writeImage(im, "PNG ", fileName);
	}

	public static boolean writeBMPImage(RenderedImage im, String fileName) {
		return writeImage(im, "BMP ", fileName);
	}

	public static void main(String[] args) {
		BufferedImage bi = ImageTools.readImage("test.jpg ");
		System.out.println(ImageTools.writeJPEGImage(bi, "tj.jpg "));
	}
	
	
	public static boolean writeTtt(BufferedImage img,String filename){
		File file = new File(filename);
        OutputStream os;
		try {
			os = new   FileOutputStream(file);
			JPEGImageEncoder   encoder   =   JPEGCodec.createJPEGEncoder(os); 
			encoder.encode(img);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (ImageFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
	}
}
