package com.bing.chat.ui.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class JTreeMouseAdapter extends JTree implements MouseListener,ActionListener {
	public JTreeMouseAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(Hashtable<?, ?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(Object[] value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(TreeModel newModel) {
		super(newModel);
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(TreeNode root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public JTreeMouseAdapter(Vector<?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
