package com.test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceLocationTest {
	public static void main(String[] args) {
		//fileSytemType();
		// classPathType();
		System.out.println(System.getProperty("user.dir"));
		try {
			System.out.println(ResourceLocationTest.class.getResource("").toURI().getPath());
			System.out.println(ResourceLocationTest.class.getResource("/").toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void fileSytemType() {
		File file1 = new File("d:/java/test.txt");
		File file2 = new File("/test.txt");
		File file3 = new File("test.txt");
		
		System.out.println("d:/java/test.txt --> " + file1.getAbsolutePath());
		System.out.println("/test.txt --> " + file2.getAbsolutePath());
		System.out.println("test.txt --> " + file3.getAbsolutePath());
	}

	public static void classPathType() {
		URL url1 = ResourceLocationTest.class.getResource("test.txt");
		URL url2 = ResourceLocationTest.class.getResource("/test.txt");

		System.out.println("/test.txt --> " + url1);
		System.out.println("test.txt --> " + url2);
	}
}