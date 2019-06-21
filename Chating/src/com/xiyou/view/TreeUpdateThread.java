package com.xiyou.view;

public class TreeUpdateThread implements Runnable {
	private String abc;
	private ContactTreeView tree;
	public TreeUpdateThread(String abc,ContactTreeView tree){
		this.abc = abc;
		this.tree = tree;
	}
	@Override
	public void run() {
		tree.setContactlist(abc);
		tree.updateShow();
	}
}
