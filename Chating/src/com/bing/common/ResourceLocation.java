package com.bing.common;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import com.bing.chat.api.IResourceLoader;

public class ResourceLocation {
	private static String identity = "";
	private static String defaultid = "Chating";
	private static String appPath;
	private static String runningPath;
	private static IResourceLoader resourceLoader;

	static {
		String abc = ResourceLocation.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		lockId(abc);
		int pos = abc.lastIndexOf(identity);
		System.out.println("abc--->" + abc);
		String pat;
		if (identity.toLowerCase().equals("chating")) {
			pat = abc.substring(0, pos + 7);
		} else {
			pat = abc.substring(0, pos + 4);
		}
		System.out.println("identify-->" + identity);
		System.out.println("pat-->" + pat);
		appPath = pat + "/";
		runningPath = abc;
		resourceLoader = new FileSystemResourceLoader();
		System.out.println("appPath--->" + appPath);
		System.out.println("runningPath--->" + runningPath);
	}

	private static void lockId(String abc) {
		int pos = abc.lastIndexOf("bin");
		String proname = "";
		if (pos != -1) {
			String part1;
			part1 = abc.substring(0, pos - 1);
			System.out.println("part1-->" + part1);
			int pos2 = part1.lastIndexOf("/");
			if (pos2 != -1) {
				proname = part1.substring(pos2 + 1);
				System.out.println("proname-->" + proname);
			}
		} else {
			int pos2 = abc.lastIndexOf("/");
			proname = abc.substring(pos2 + 1);
			System.out.println("proname-->" + proname);
		}
		if (proname != null && proname != "") {
			identity = proname;
		} else {
			identity = defaultid;
		}
	}

	public static void main(String[] args) {
		System.out.println("appPath-->" + appPath);
		System.out.println("runningPath-->" + runningPath);
	}

	public static Image getImage(String path) {
		// MainView.class.getClassLoader().getResource("image/logo.png");
		Image image = null;
		System.out.println("path-->" + path);
		String abslotpath = appPath + path;
		System.out.println("absolate path-->" + abslotpath);
		try {
			image = Toolkit.getDefaultToolkit().createImage(
					((File) resourceLoader.getResource(abslotpath)).toURI()
							.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static ImageIcon getImageIcon(String path) {
		ImageIcon abc = new ImageIcon(getImage(path));
		return abc;
	}

	public static File getFile(String path) {
		System.out.println("file path-->" + path);
		System.out.println("absolate file path-->" + appPath + path);
		return (File) resourceLoader.getResource(appPath + path);
	}

	public static URL getFileURL(String path) {
		System.out.println("file path-->" + path);
		System.out.println("absolate file path-->" + appPath + path);
		try {
			return ((File) resourceLoader.getResource(appPath + path)).toURI()
					.toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}