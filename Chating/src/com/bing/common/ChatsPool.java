package com.bing.common;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONObject;

import com.bing.chat.api.ChatingInstanceAdapter;
import com.bing.chat.api.IChatingInstance;

public class ChatsPool {
	private static ChatsPool instance = null;
	private static ConcurrentMap<String, IChatingInstance> pool;
	private ChatsPool() {
		if (this.pool == null) {
			pool = new ConcurrentHashMap<String, IChatingInstance>();
		}
	}

	public static ChatsPool getInstance() {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		if (instance == null) {
			instance = new ChatsPool();
		}
		lock.unlock();
		return instance;
	}

	public void addChatIns(String identy, IChatingInstance ins) {
		this.pool.put(identy, ins);
	}

	public IChatingInstance getChatIns(String identy) {
		if (this.pool.containsKey(identy))
			return (IChatingInstance) this.pool.get(identy);
		else
			return null;
	}

	public void stop() {
			Iterator<String> iter = this.pool.keySet().iterator();
			for (; iter.hasNext();) {
				IChatingInstance ins = this.pool.get(iter.next());
				ins.close();
		}
	}

	public void removeAllOppose(JSONObject jsonObject) {
		Iterator<String> iter = this.pool.keySet().iterator();
		for (; iter.hasNext();) {
			String key = iter.next();
			if (key.indexOf(jsonObject.getString("ip")) > -1
					&& key.indexOf(jsonObject.getInt("lis_port")) > -1) {
				IChatingInstance ins = this.pool.get(key);
				ins.close();
			}
		}
	}

	public void removeMySelf(ChatingInstanceAdapter videoChatingInstance) {
		if (this.pool.containsValue(videoChatingInstance)) {
			this.pool.remove(videoChatingInstance.getIdentify());
		}
	}
}
