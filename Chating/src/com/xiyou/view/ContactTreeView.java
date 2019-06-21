package com.xiyou.view;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bing.chat.ui.tree.ContactRenderer;
import com.bing.command.TxtChatSendCommand;
import com.bing.command.VideoChatSendCommand;
import com.bing.command.VoiceChatSendCommand;
import com.bing.common.ContactNode;

public class ContactTreeView extends JTreeMouseAdapter {
	private static String contactlist;
	private boolean abc = false;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode(
			new ContactNode(0, "列表"));
	private JPopupMenu popMenu;

	public ContactTreeView(String lists) {
		super(root);// Jtree(root)
		this.contactlist = lists;
		this.init();
	}

	public ContactTreeView() {
		super(root);// Jtree(root)
	}

	public void updateShow() {
		this.init();
	}

	private void init() {
		root.removeAllChildren();
		initPopMenu();
		JSONArray arrs = JSONArray.fromObject(contactlist);
		for (int i = 0; i < arrs.size(); i++) {
			JSONObject jobj = arrs.getJSONObject(i);
			DefaultMutableTreeNode mtb1 = makeNode(jobj);
			/*
			 * ContactNode node = new ContactNode(jobj.getString("ip"),
			 * jobj.getInt("lisport"), 0);
			 * node.setHostname(jobj.getString("hostname"));
			 * node.setNickname(jobj.getString("nickname"));
			 * 
			 * DefaultMutableTreeNode mtb1 = new DefaultMutableTreeNode(node);
			 * DefaultMutableTreeNode hostname = new DefaultMutableTreeNode( new
			 * ContactNode(1, "HostName:" + node.getHostname()));
			 * DefaultMutableTreeNode nikename = new DefaultMutableTreeNode( new
			 * ContactNode(1, "NickName:" + node.getNickname()));
			 * DefaultMutableTreeNode tcpport = new DefaultMutableTreeNode( new
			 * ContactNode(1, "TCP:" + node.getTcpport())); mtb1.add(hostname);
			 * mtb1.add(nikename); mtb1.add(tcpport);
			 */
			root.add(mtb1);
		}
		setCellRenderer(new ContactRenderer());
		setShowsRootHandles(true);
		setRootVisible(true);
		this.addMouseListener(this);
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		// 更新JTree的UI外观
		if (!abc) {
			SwingUtilities.updateComponentTreeUI(this);
			getSelectionModel().setSelectionMode(
					TreeSelectionModel.SINGLE_TREE_SELECTION);
			setRootVisible(false);
			// addTreeSelectionListener(this);
		}
	}

	private DefaultMutableTreeNode makeNode(JSONObject jobj) {
		ContactNode node = new ContactNode(jobj.getString("ip"), jobj
				.getInt("lis_port"), 0);
		node.setHostname(jobj.getString("hostname"));
		node.setNickname(jobj.getString("nickname"));

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
		return mtb1;
	}

	public String getContactlist() {
		return contactlist;
	}

	public void addNode(JSONObject obj) {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode newNode = makeNode(obj);
		model.insertNodeInto(newNode, root, root.getChildCount() - 1);
		TreeNode[] nodes = model.getPathToRoot(newNode);
		TreePath path = new TreePath(nodes);
		this.scrollPathToVisible(path);
		//this.updateUI();
	}

	public void setContactlist(String contactlist) {
		this.contactlist = contactlist;
	}

	/*
	 * @Override public void valueChanged(TreeSelectionEvent e) {
	 * DefaultMutableTreeNode node = (DefaultMutableTreeNode) this
	 * .getLastSelectedPathComponent(); ContactNode nodeInfo = (ContactNode)
	 * node.getUserObject(); System.out.println(node.getPath());
	 * System.out.println(nodeInfo.getDisplayname());
	 * System.out.println(nodeInfo.getHostname());
	 * System.out.println(nodeInfo.getIp());
	 * System.out.println(nodeInfo.getTcpport()); }
	 */

	public void initPopMenu() {
		if (popMenu == null) {
			popMenu = new JPopupMenu();
			JMenuItem addItem = new JMenuItem("Txt Chat");
			addItem.addActionListener(this);
			JMenuItem delItem = new JMenuItem("Vice Chat");
			delItem.addActionListener(this);
			JMenuItem editItem = new JMenuItem("Video Chat");
			editItem.addActionListener(this);
			popMenu.add(addItem);
			popMenu.add(delItem);
			popMenu.add(editItem);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.isMetaDown()) {// 检测鼠标右键单击
			TreePath path = this.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
			if (path == null || path.getPathCount() >= 3) {
				return;
			}
			setSelectionPath(path);
			popMenu.show(this, e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		/*
		 * TreePath path = this.getPathForLocation(e.getX(), e.getY()); //
		 * 关键是这个方法的使用 if (path == null) { return; } setSelectionPath(path);
		 * 
		 * if (e.getButton() == 3) { popMenu.show(this, e.getX(), e.getY()); }
		 */
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this
				.getLastSelectedPathComponent();
		ContactNode conNode = (ContactNode) node.getUserObject();
		if (e.getActionCommand().equals("Txt Chat")) {
			System.out.println("start a txt chat session");
			new TxtChatSendCommand(conNode).exec();
		}
		if (e.getActionCommand().equals("Vice Chat")) {
			System.out.println("start a vice chat session");
			new VoiceChatSendCommand(conNode).exec();

		}
		if (e.getActionCommand().equals("Video Chat")) {
			System.out.println("start a video chat session");
			new VideoChatSendCommand(conNode).exec();
		}

	}

	public void removeNode(JSONObject jsonObject) {
		Enumeration<DefaultMutableTreeNode> nodes = root.children();
		while (nodes.hasMoreElements()) {
			DefaultMutableTreeNode nex = nodes.nextElement();
			ContactNode connode = (ContactNode) nex.getUserObject();
			if (connode.getIp().equals(jsonObject.getString("ip"))
					&& connode.getTcpport() == jsonObject.getInt("lis_port")) {
				((DefaultTreeModel) this.getModel()).removeNodeFromParent(nex);
			}
		}
	}
}
