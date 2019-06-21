package com.bing.common.utility;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.bing.common.ecxcption.AllPortBeenOccupied;

public class RandomPort {
	private static int minnum = 5555;
	private static int maxnum = 60000;

	private static List ports = new LinkedList();
	
	public static void initminnum(int param) {
		minnum = param;
	}

	public static int getPort() throws AllPortBeenOccupied {
		int current = minnum;
		int loops = current;
		int port = 0;
		while (port == 0) {
			current = loops;
			loops = current + 10;
			if (loops <= maxnum) {
				port = runs(current, loops);
			} else {
				throw new AllPortBeenOccupied();
			}
		}
		return port;
	}
	public static int getPort(int minnumstart) throws AllPortBeenOccupied {
		minnum = minnumstart;
		int current = minnum;
		int loops = current;
		int port = 0;
		while (port == 0) {
			current = loops;
			loops = current + 10;
			if (loops <= maxnum) {
				port = runs(current, loops);
			} else {
				throw new AllPortBeenOccupied();
			}
		}
		return port;
	}

	public static int getRTPPort() throws AllPortBeenOccupied {
		int current = minnum;
		int loops = current;
		int port = 0;
		while (port == 0) {
			current = loops;
			loops = current + 10;
			if (loops <= maxnum) {
				port = rtpruns(current, loops);
			} else {
				throw new AllPortBeenOccupied();
			}
		}
		return port;
	}

	public static int getRTPPort(int minstartport) throws AllPortBeenOccupied {
		minnum = minstartport;
		int current = minnum;
		int loops = current;
		int port = 0;
		while (port == 0) {
			current = loops;
			loops = current + 10;
			if (loops <= maxnum) {
				port = rtpruns(current, loops);
			} else {
				throw new AllPortBeenOccupied();
			}
		}
		return port;
	}

	private static int runs(int start, int loops) {
		int returnport = 0;
		for (int i = start; i < loops; i++) {
			try {
				ServerSocket sers = new ServerSocket(i);
				sers.close();
				if(ports.contains(i)){
					continue;
				}else{
					ports.add(i);
					returnport = i;
					break;
				}
			} catch (IOException e) {
			}
		}
		return returnport;
	}

	private static int rtpruns(int start, int loops) {
		int returnport = 0;
		for (int i = start; i < loops; i++) {
			if (i % 2 == 0) {
				try {
					ServerSocket sers = new ServerSocket(i);
					ServerSocket sers2 = new ServerSocket(i+1);
					sers.close();
					sers2.close();
					if(ports.contains(i)){
						continue;
					}else{
						ports.add(i);
						returnport = i;
						break;
					}
				} catch (IOException e) {
				}
			}
		}
		return returnport;
	}
	
	
	public static void removePorts(int port){
		ports.remove(ports);
	}

	public static void main(String[] args) {
		try {
			// for(int i=0;i<=6;i++){

			int port = RandomPort.getRTPPort();
			System.out.println(port);

			RandomPort.initminnum(8000);

			System.out.println(RandomPort.getRTPPort());
			RandomPort.initminnum(9000);
			int abc = RandomPort.getRTPPort(6000);
			//System.out.println(RandomPort.getRTPPort());
			try {
				DatagramSocket ds = new DatagramSocket(abc);
				ServerSocket socket  = new ServerSocket(abc);
				socket.close();
				System.out.println(RandomPort.getRTPPort(6000));
				System.out.println(RandomPort.getRTPPort(6000));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// }
		} catch (AllPortBeenOccupied e) {
			e.printStackTrace();
		}
	}
}
