package com.test;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Test4 {

	public static void main(String[] args) throws Exception {
		Test4 t = new Test4();
		t.getLocalIP();
	}

	public void getLocalIP() throws Exception {
		Enumeration e1 = (Enumeration) NetworkInterface.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			System.out.print(ni.getName());
			System.out.print(": ");
			Enumeration e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue; // omit IPv6 address
				System.out.print(ia.getHostAddress());
				if (e2.hasMoreElements()) {
					System.out.print(", ");
				}
			}
			System.out.print("\n");
		}
	}
}