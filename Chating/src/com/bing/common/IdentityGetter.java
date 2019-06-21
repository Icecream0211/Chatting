package com.bing.common;

import com.bing.chat.api.IChatingInstance;

public class IdentityGetter {
	public static String getIdentity(IChatingInstance chatinstance) {
		String ip;
		StringBuilder identity = new StringBuilder();
		ip = chatinstance.getDestIp();
		String ip_str = ip.replace(".", "");
		identity.append(ip_str);
		identity.append("_");
		identity.append(chatinstance.getDestLisPort());
		identity.append(chatinstance.getDestConPort());
		identity.append(chatinstance.getMyPort());
		return identity.toString();
	}

	public static String getIdentity(String ipaddrss, int port) {
		String identity;
		String ip_str = ipaddrss.replace(".", "");
		identity = ip_str + port;
		return identity;
	}
	/*
	 * public static void main(String[] args) throws UnknownHostException {
	 * InetAddress[] ip; ip = Inet4Address
	 * .getAllByName(InetAddress.getLocalHost().getHostName()); for (InetAddress
	 * inet : ip) { if(inet instanceof Inet4Address){
	 * System.out.println(inet.getCanonicalHostName() + "-->" +
	 * inet.getHostAddress()); } } }
	 */
}
