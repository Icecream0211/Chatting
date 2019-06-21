package com.bing.server.common;

import com.bing.server.utility.ClientInstance;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class ClientInstanceProcesser implements JsonValueProcessor {
	private Object process(Object value) {
		StringBuilder jsonsb = new StringBuilder();
		ClientInstance ins = (ClientInstance) value;
		jsonsb.append("{");
		jsonsb.append("'ip':'" + ins.getIp() + "',");
		jsonsb.append("'lis_port':" + ins.getTcpport() + ",");
		jsonsb.append("'nickname':'" + ins.getNickname()+ "',");
		jsonsb.append("'hostname':'" + ins.getHostname() + "'}");
		return jsonsb.toString();

	}

	public Object processArrayValue(Object value, JsonConfig config) {
		System.out.println("ddddd");
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig config) {
		System.out.println(key);
		return process(value);
	}
}
