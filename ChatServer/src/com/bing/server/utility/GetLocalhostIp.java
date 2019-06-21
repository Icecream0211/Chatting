package com.bing.server.utility;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class GetLocalhostIp {
	public static String myip;
	static {
		try {
			myip = getLocalIP();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getLocalIP() throws Exception {
		String ip = "127.0.0.1";
		HashMap<String, String> ipMap = new HashMap<String, String>();
		Enumeration e1 = (Enumeration) NetworkInterface.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			Enumeration e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue; // omit IPv6 address
				// System.out.print(ia.getHostAddress());
				if (ia.getHostAddress() != null && ia.getHostAddress() != "") {
					ipMap.put(ni.getName(), ia.getHostAddress());
				}
				if (e2.hasMoreElements()) {
					System.out.print(", ");
				}
			}
			System.out.print("\n");
		}
		Iterator<String> iter = ipMap.keySet().iterator();
		for (; iter.hasNext();) {
			String obj = iter.next();
			System.out.println("key->" + obj + ",value-->" + ipMap.get(obj));
			if (!obj.equals("lo")) {
				ip = ipMap.get(obj);
			}
		}
		return ip;
	}

	public static void main(String[] args) {
		try {
			GetLocalhostIp.getLocalIP();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
