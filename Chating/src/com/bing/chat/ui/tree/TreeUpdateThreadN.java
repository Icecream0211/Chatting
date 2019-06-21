package com.bing.chat.ui.tree;

import com.bing.chat.ui.tree.NewTreePanel;

public class TreeUpdateThreadN implements Runnable {
	private String abc;
	private NewTreePanel tree;
	public TreeUpdateThreadN(String abc,NewTreePanel tree){
		this.abc = abc;
		this.tree = tree;
	}
	@Override
	public void run() {
		tree.setContactlist(abc);
		tree.updateShow();
	}
}
