package com.bing.chart.main;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

import com.bing.common.BackstageChannel;
import com.bing.common.ChatingServer;
import com.bing.common.ChatsPool;
import com.bing.common.MainInstance;

public class Main {
	private ChatingServer server;
	private static Main instance=null;
	private Main() {
		this.init();
		//Runtime.getRuntime().addShutdownHook(new Thread(new ExitHook()));
	}
	public static Main getInstance(){
		ReentrantLock look = new ReentrantLock();
		look.lock();
		if(instance==null){
			instance = new Main();
		}
		look.unlock();
		return instance;
	}

	private void init() {
		server = new ChatingServer();// 启动后台监听服务器
		BackstageChannel.getInstance();// 启动后台连接服务器进程
		MainInstance.getInstance();
	}

	public void stop() {
		MainInstance.getInstance().stop();
		ChatsPool.getInstance().stop();
		server.stop();
		BackstageChannel.getInstance().stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		File fil = new File("/");
		System.out.println(fil.getAbsolutePath());
		Main.getInstance();
	}

	class ExitHook implements Runnable {
		@Override
		public void run() {
			stop();
			//当服务器退出，可以做一些事情
		}
	}
}
