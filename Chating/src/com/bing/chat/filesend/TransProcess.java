package com.bing.chat.filesend;

import java.awt.Toolkit;

import javax.swing.SwingWorker;

public class TransProcess extends SwingWorker<Void, Void> {
	private IFileThread fileThread;
	private int totalSize;
	private int processval;
	public TransProcess(IFileThread fileth, int totalsize) {
		this.fileThread = fileth;
		this.totalSize = totalsize;
	}

	@Override
	protected Void doInBackground() throws Exception {
		while (fileThread.getSendedSize() < totalSize) {
			processval = (int) (fileThread.getSendedSize() / (float) (totalSize / 100));
			this.setProgress(processval);
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void done() {
		this.setProgress(100);
		Toolkit.getDefaultToolkit().beep();
	}
}
