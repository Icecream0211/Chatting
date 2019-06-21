package com.bing.chat.ui.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.bing.common.ContactNode;
import com.bing.common.ResourceLocation;

public class ContactRenderer extends DefaultTreeCellRenderer {
	// 初始化5个图标
	/*
	 * ImageIcon rootIcon = new ImageIcon("icon/root.gif"); ImageIcon
	 * databaseIcon = new ImageIcon("icon/database.gif"); ImageIcon tableIcon =
	 * new ImageIcon("icon/table.gif"); ImageIcon columnIcon = new
	 * ImageIcon("icon/column.gif"); ImageIcon indexIcon = new
	 * ImageIcon("icon/index.gif");
	 */
	ImageIcon rooticon = ResourceLocation.getImageIcon("image/computer.png");

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// 执行父类默认的节点绘制操作
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		ContactNode data = (ContactNode) node.getUserObject();
		// 根据数据节点里的nodeType数据决定节点图标
		ImageIcon icon=null;
		switch (data.getNodetype()) {
		case 0:
			icon = rooticon;
			break;
		case 1:
			break;
		} // 改变图标
		if(icon!=null){
			this.setIcon(icon);
		}
		return this;
	}
}