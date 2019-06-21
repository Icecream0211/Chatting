package com.bing.common;

import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONObject;

import com.bing.command.LoginSendCommand;
import com.bing.command.LogoutSendCommand;
import com.bing.command.UserListSendCommand;
import com.xiyou.view.MainView;

public class MainInstance {
	private MainView mainView;// old ui
	// private MainResizeFrame mainView;//new ui
	private boolean issigninSuccess = false;
	private static MainInstance instance = null;

	private MainInstance() {
		this.init();
	}

	private void init() {
		this.mainView = new MainView(CommonConfig.MY_HOSTNAME,
				CommonConfig.MY_IP, CommonConfig.CLIENT_TCP_PORT);// old ui
		/*
		 * this.mainView = new
		 * MainResizeFrame("LocalNetChat",CommonConfig.MY_HOSTNAME,
		 * CommonConfig.MY_IP, CommonConfig.CLIENT_TCP_PORT);
		 */// new ui
		this.mainView = new MainView(CommonConfig.MY_HOSTNAME,
				CommonConfig.MY_IP, CommonConfig.CLIENT_TCP_PORT);
		boolean abc = Login();
		if (abc) {
			this.mainView.showup();
			System.out.println("sigin success!");
			CommonConfig.islogined = true;
			UserListSendCommand userlistreq = new UserListSendCommand();
			userlistreq.exec();
		} else {
			// show up the bubble window:log failure;
		}
	}

	private boolean Login() {
		LoginSendCommand loginsend = new LoginSendCommand(CommonConfig.MY_IP,
				CommonConfig.CLIENT_TCP_PORT);
		loginsend.exec();
		try {
			int i = 0;
			while (!CommonConfig.islogined) {
				System.out.println(i + "...");
				Thread.currentThread().sleep(1000);
				i++;
				/*
				 * if (i >= 5) { break; }
				 */
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return CommonConfig.islogined;
	}

	public void setLoginFlag(boolean flag) {
		this.issigninSuccess = flag;
	}

	public void updateUserList(String lists) {
		this.mainView.updateUserList(lists);
	}

	public void addUser(JSONObject jsonobj) {
		this.mainView.addNode(jsonobj);
	}

	public MainView getMainView(){
		return mainView;
	}
	public static MainInstance getInstance() {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		if (instance == null) {
			instance = new MainInstance();
		}
		lock.unlock();
		return instance;
	}

	public void  stop() {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		if (CommonConfig.islogined) {
			LogoutSendCommand logout = new LogoutSendCommand(
					CommonConfig.MY_IP, CommonConfig.CLIENT_TCP_PORT);
			logout.exec();
		}
		CommonConfig.islogined=false;
		lock.unlock();
	}

	public void removeUser(JSONObject jsonObject) {
		this.mainView.removeNode(jsonObject);
		ChatsPool.getInstance().removeAllOppose(jsonObject);
	}
}
