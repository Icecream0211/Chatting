package com.test;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bing.common.ContactNode;
import com.bing.common.ContactRenderer;

public class Test1 {
	public static void main(String[] args) throws Exception {
		/*
		 * String contactlists =
		 * "[{\"hostName\":\"\",\"ip\":\"192.168.6.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222},{\"hostName\":\"\",\"ip\":\"192.168.5.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222},{\"hostName\":\"\",\"ip\":\"192.168.4.226\",\"nickName\":\"\",\"tcpport\":333,\"udpport\":222}]"
		 * ; JSONArray arrs = JSONArray.fromObject(contactlists);
		 * System.out.println(arrs.size()); JSONObject jo; for (int i = 0; i <
		 * arrs.size(); i++) { jo = (JSONObject) arrs.get(i);
		 * System.out.println(jo.getString("ip")); } new Test1().init(arrs);
		 */
		for (int i = 0; i < 5; i++){
			System.out.println(Math.round(10));
			System.out.println((new Random()).nextInt(10));
		}
	}

	public void init(JSONArray arrs) throws Exception {

		JFrame jf = new JFrame("根据节点类型定义图标");
		final JTree tree;
		// 定义几个初始节点

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				new ContactNode("0.0.0.0", 0, 0));
		for (int i = 0; i < arrs.size(); i++) {
			JSONObject jobj = arrs.getJSONObject(i);
			ContactNode node = new ContactNode(jobj.getString("ip"),
					jobj.getInt("tcpport"), 0);
			node.setHostname(jobj.getString("hostName"));
			node.setNickname(jobj.getString("nickName"));

			DefaultMutableTreeNode mtb1 = new DefaultMutableTreeNode(node);
			DefaultMutableTreeNode hostname = new DefaultMutableTreeNode(
					new ContactNode(1, "HostName:" + node.getHostname()));
			DefaultMutableTreeNode nikename = new DefaultMutableTreeNode(
					new ContactNode(1, "NickName:" + node.getNickname()));
			DefaultMutableTreeNode tcpport = new DefaultMutableTreeNode(
					new ContactNode(1, "TCP:" + node.getTcpport()));
			root.add(mtb1);
			mtb1.add(hostname);
			mtb1.add(nikename);
			mtb1.add(tcpport);
		}

		// 定义salaryDb的两个子节点
		/*
		 * DefaultMutableTreeNode employee = new DefaultMutableTreeNode( new
		 * NodeData(DBObjectType.TABLE, "员工表")); DefaultMutableTreeNode attend =
		 * new DefaultMutableTreeNode( new NodeData(DBObjectType.TABLE, "考勤表"));
		 * // 定义customerDb的一个子节点 DefaultMutableTreeNode contact = new
		 * DefaultMutableTreeNode( new NodeData(DBObjectType.TABLE, "联系方式表"));
		 */
		// 定义employee的三个子节点

		// 通过add方法建立树节点之间的父子关系
		// root.add(salaryDb);
		// root.add(customerDb);
		// salaryDb.add(employee);
		// salaryDb.add(attend);
		// customerDb.add(contact);
		// employee.add(id);
		// employee.add(name);
		// employee.add(gender);
		// 以根节点创建树
		tree = new JTree(root);
		JTree trees = new JTree();

		// 设置该JTree使用自定义的节点绘制器
		tree.setCellRenderer(new ContactRenderer());

		// 设置是否显示根节点的“展开/折叠”图标,默认是false
		tree.setShowsRootHandles(true);
		// 设置节点是否可见,默认是true
		tree.setRootVisible(true);
		// 设置使用Windows风格外观
		UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// 更新JTree的UI外观
		SwingUtilities.updateComponentTreeUI(tree);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				ContactNode nodeInfo = (ContactNode) node.getUserObject();
				System.out.println(node.getPath());
				System.out.println(nodeInfo.getDisplayname());
				System.out.println(nodeInfo.getHostname());
				System.out.println(nodeInfo.getIp());
				System.out.println(nodeInfo.getTcpport());
			}
		});
		jf.add(new JScrollPane(tree));
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}

// 定义一个NodeData类，用于封装节点数据
