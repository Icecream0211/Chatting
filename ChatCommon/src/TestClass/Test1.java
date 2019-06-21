package TestClass;

import java.nio.channels.SelectionKey;

import com.bing.common.server.TCPDaemonServer;

public class Test1 extends TCPDaemonServer {

	@Override
	public void handWrite(SelectionKey key) {
		System.out.println("1");
	}

	@Override
	public void handRead(SelectionKey key) {
		System.out.println("2");
	}

	@Override
	public void handAccept(SelectionKey key) {
		System.out.println("3");
	}

	public static void main(String args[]) {
		new Test1().start();
	}
}
