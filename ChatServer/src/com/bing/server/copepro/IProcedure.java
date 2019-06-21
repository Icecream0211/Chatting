package com.bing.server.copepro;

import java.nio.channels.SocketChannel;

public interface IProcedure {
	public void exec();
	SocketChannel getSocketChannel();
}
