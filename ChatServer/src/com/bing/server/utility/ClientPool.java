package com.bing.server.utility;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import com.bing.server.copepro.SystemExitNotificationPro;
import com.bing.server.utility.ClientInstance;

public class ClientPool {
	private static ClientPool instance = null;
	private static ConcurrentMap<String, ClientInstance> pools;

	private ClientPool() {
		if (this.pools == null)
			this.pools = new ConcurrentHashMap<String, ClientInstance>();
	}

	public static ClientPool getInstance() {
		if (instance == null) {
			ReentrantLock lock = new ReentrantLock();
			lock.lock();
			instance = new ClientPool();
			lock.unlock();
		}
		return instance;
	}

	public void addClient(ClientInstance client) {
		System.out.println(this.pools.keySet());
		this.pools.put(client.getIdentify(), client);
	}

	public ClientInstance getClient(String ip) {
		return (ClientInstance) this.pools.get(ip);
	}

	public boolean removeClient(ClientInstance ins) {
		if (this.pools.containsKey(ins.getIdentify())) {
			this.pools.remove(ins.getIdentify());
			return true;
		}
		return true;
	}

	public Collection<ClientInstance> getAllLists() {
		return this.pools.values();
	}

	public List<ClientInstance> getAllExceptMeLists(ClientInstance requer) {
		Collection<ClientInstance> users = this.getAllLists();
		List<ClientInstance> pureUsers = new LinkedList<ClientInstance>();
		for (ClientInstance clis : users) {
			if (clis.getIdentify().equals(requer.getIdentify())) {
				continue;
			}
			pureUsers.add(clis);
		}
		return pureUsers;
	}

	public void stop() {
		new SystemExitNotificationPro(null).exec();
	}
}
