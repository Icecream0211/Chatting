package com.bing.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStringUtil {
	private static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String getNow(String format) {
		if (format != null)
			df.applyPattern(format);
		return df.format(Calendar.getInstance().getTime());
	}

	public static void main(String[] args) {
		System.out.println(TimeStringUtil.getNow(null));
	}
}
