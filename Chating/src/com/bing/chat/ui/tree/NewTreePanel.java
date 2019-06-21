package com.bing.chat.ui.tree;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.json.JSONObject;

import com.bing.common.ContactNode;
import com.bing.common.ResourceLocation;

public class NewTreePanel extends JPanel {
	private  String contactlist="[]";
	private boolean abc = false;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode(
			new ContactNode(0, "列表"));
	private JPopupMenu popMenu;

	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane;
	private JPanel panel;
	private ContactTreeView tree;

	public NewTreePanel(String lists) {
		this();
		this.contactlist = lists;
	}

	public NewTreePanel() {

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setSize(new Dimension(255, 455));
		tabbedPane.setPreferredSize(new Dimension(255, 455));
		tabbedPane.setAutoscrolls(true);

		scrollPane = new JScrollPane();
		tabbedPane.addTab("", ResourceLocation.getImageIcon("image/record1.jpg"), scrollPane,
				"contractlist");

		panel = new JPanel();
		panel.setAutoscrolls(true);
		scrollPane.setViewportView(panel);

		//tree = new JTree(root);
		tree = new ContactTreeView();
		tree.setContactlist(this.contactlist);
		tree.updateShow();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(tree, GroupLayout.DEFAULT_SIZE,
				221, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.LEADING).addComponent(tree, GroupLayout.DEFAULT_SIZE,
				330, Short.MAX_VALUE));
		panel.setLayout(gl_panel);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE));
		setLayout(groupLayout);
	}


	public String getContactlist() {
		return contactlist;
	}
	public void setContactlist(String contactlist) {
		this.contactlist = contactlist;
	}
	public void removeNode(JSONObject jsonObject) {
		this.tree.removeNode(jsonObject);
	}

	public void addNode(JSONObject obj) {
		this.tree.addNode(obj);
	}
	public void updateShow() {
		this.tree.setContactlist(this.contactlist);
		this.tree.updateShow();
	}
}
