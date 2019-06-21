package com.bing.chat.filesend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.swing.JProgressBar;

import com.xiyou.view.TipWindow;

public class FileSendThread implements Runnable, IFileThread {
	private boolean isstarted = false;
	private String filePath;
	private Socket sendClient;
	private int recport;
	private String destip;
	private long sendedSize = 0;
	private FileTransInstance fileins;
	private final JProgressBar probar;

	public FileSendThread(FileTransInstance fileins, JProgressBar jProgressBar,
			int destport) {
		this.fileins = fileins;
		this.recport = destport;
		probar = jProgressBar;
		if (this.fileins.getFilePath() != null
				&& this.fileins.getFilePath() != "") {
			this.filePath = this.fileins.getFilePath();
		} else {
			throw new RuntimeException("File Path should not be null!");
		}
		this.destip = this.fileins.getDestIp();
	}

	public void start() {
		if (!isstarted) {
			this.probar.setMaximum(this.fileins.getFileTotalSize());
			this.probar.setMinimum(0);
			Executors.newCachedThreadPool().execute(this);
			this.isstarted = true;
		}
	}

	public long getSendedSize() {
		return sendedSize;
	}

	public void stop() {
		if (isstarted) {
			try {
				if (!sendClient.isClosed()) {
					sendClient.close();
				}
				isstarted = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.fileins.close();
	}

	@Override
	public void run() {
		try {
			sendClient = new Socket(this.destip, recport);
			FileInputStream fin = new FileInputStream(filePath);
			BufferedInputStream bin = new BufferedInputStream(fin);
			BufferedOutputStream bout = new BufferedOutputStream(
					sendClient.getOutputStream());
			int i = 0;
			System.out.println("probar.max===->" + this.probar.getMaximum());
			System.out.println("probar.min===->" + this.probar.getMinimum());
			while (true) {
				i = bin.read();
				if (i == -1) {
					System.out.println("-------------------1");
					bout.write(-1);
					System.out.println("sendedSize===->" + sendedSize);
					break;
				}
				bout.write(i);
				sendedSize++;
				this.probar.setValue((int) sendedSize);
			}
			System.out.println("sendedSize== final  =->" + sendedSize);
			bin.close();
			bout.flush();
			bout.close();
			sendClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				sendClient.close();
			} catch (Exception ef) {
				ef.printStackTrace();
			}
		}
	}

	@Override
	public File getFile() {
		return this.fileins.getFileSelected();
	}
}
