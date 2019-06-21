package com.test;

import java.util.Calendar;
import java.util.Random;

public class MyTest {
	public static void main(String[] args) {
		long times = Calendar.getInstance().getTimeInMillis();
		for (int i = 0; i < 100; i++) {
			System.out.println(new Random().nextInt(10));
		}
		long timeend = Calendar.getInstance().getTimeInMillis();
		System.out.println("short---->" + (timeend - times));
		System.out.println(Short.MAX_VALUE);
		System.out.println(Short.MIN_VALUE);
		System.out.println(Short.SIZE);
		System.out.println(Short.TYPE);
	}
}
