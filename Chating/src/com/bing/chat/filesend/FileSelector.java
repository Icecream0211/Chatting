package com.bing.chat.filesend;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FileChooserUI;

public class FileSelector {
	private String fileName;
	private long size;
	private File selectedFile;
	private int type = 1; // 1 选择文件 2 选择保存

	public FileSelector() {
	}

	public FileSelector(int type) {
		this.type = type;
	}

	public boolean select() {
		JFileChooser chooer = new JFileChooser();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		int option;
		if (this.type == 1) {
			option = chooer.showOpenDialog(null);
			if (option == JFileChooser.APPROVE_OPTION) {
				selectedFile = chooer.getSelectedFile();
				this.fileName = chooer.getSelectedFile().getName();
				this.size = selectedFile.length();
				System.out.println(selectedFile.getAbsolutePath());
				System.out.println(selectedFile.getName());
				System.out.println(selectedFile.length());
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		FileSelector sel = new FileSelector(1);
		/*JFileChooser chooer = new JFileChooser();
		int option = chooer.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			System.out.println(chooer.getSelectedFile().getName());
			System.out.println(chooer.getSelectedFile().length());
		}*/
		 sel.select();
	}

	public int getFileSize() {
		return (int) (this.getSelectedFile().length());
	}

	public File getSelectedFile() {
		return this.selectedFile;
	}
}
