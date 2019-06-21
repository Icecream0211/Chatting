package com.test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.xiyou.view.ContactTreeView;

public class Test2 {
	public static String contactlists = "[{\"hostname\":\"bing\",\"ip\":\"192.168.6.226\",\"nickname\":\"\",\"lis_port\":333},{\"hostname\":\"Bing\",\"ip\":\"192.168.5.226\",\"nickname\":\"\",\"lis_port\":333},{\"hostname\":\"\",\"ip\":\"192.168.4.226\",\"nickname\":\"\",\"lis_port\":333 }]";
	public static String contactlists2 = "[{\"hostname\":\"\",\"ip\":\"192.168.6.226\",\"nickname\":\"\",\"lis_port\":44444},{\"hostname\":\"Binf\",\"ip\":\"192.168.5.226\",\"nickname\":\"\",\"lis_port\":4444},{\"hostname\":\"\",\"ip\":\"192.168.4.226\",\"nickname\":\"\",\"lis_port\":4444 }]";

	public static void main(String[] args) throws Exception {
		JSONArray arrs = JSONArray.fromObject(contactlists);
		System.out.println(arrs.size());
		JSONObject jo;
		for (int i = 0; i < arrs.size(); i++) {
			jo = (JSONObject) arrs.get(i);
			System.out.println(jo.getString("ip"));
		}
		// new Test2().init(arrs);

		JFrame jf = new JFrame("根据节点类型定义图标");
		JTree tree = new ContactTreeView(contactlists);
		// 定义几个初始节点
		jf.add(new JScrollPane(tree));
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	public void init(JSONArray arrs) throws Exception {
	}
}

// 定义一个NodeData类，用于封装节点数据
