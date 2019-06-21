package com.bing.chat.filesend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;

import com.xiyou.view.TipWindow;

public class FileReciveThread implements Runnable, IFileThread {
	private boolean isstarted = false;
	private int port;
	private ServerSocket ss;
	private Socket recvSocket;
	private File file;
	private long recivedSize = 0;
	private FileTransInstance fileIns;
	private final JProgressBar probar;

	public FileReciveThread(FileTransInstance fileins,
			JProgressBar jProgressBar, int lisport) {
		this.fileIns = fileins;
		this.port = lisport;
		this.probar = jProgressBar;
		this.file = this.fileIns.getFileSelected();
	}

	public void start() {
		if (!isstarted) {
			System.out.println("file recive thread start.method........");
			this.probar.setMaximum(this.fileIns.getFileTotalSize());
			this.probar.setMinimum(0);
			Executors.newCachedThreadPool().execute(this);
			isstarted = true;
		}
	}

	public boolean isStarted() {
		return this.isstarted;
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(port);
			recvSocket = ss.accept();
			System.out.println("file recive thread start.........");
			FileOutputStream fout = new FileOutputStream(file);
			BufferedOutputStream bout = new BufferedOutputStream(fout);
			BufferedInputStream bin = new BufferedInputStream(
					recvSocket.getInputStream());
			int i = 0;
			System.out.println("probar.max===->" + this.probar.getMaximum());
			System.out.println("probar.min===->" + this.probar.getMinimum());
			while (true) {
				i = bin.read();
				if (i == -1) {
					System.out.println("-------------------1");
					break;
				}
				bout.write(i);
				recivedSize++;
				probar.setValue((int) recivedSize);
			}
			System.out.println("recivedSize===->" + recivedSize);
			bin.close();
			bout.flush();
			bout.close();
			recvSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
	}

	public void close() {
		if (isstarted) {
			try {
				if (!recvSocket.isClosed()) {
					recvSocket.close();
				}
				if (recvSocket.isClosed()) {
					this.fileIns.close();
				}
				if (!ss.isClosed()) {
					ss.close();
				}
				isstarted = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public long getSendedSize() {
		return this.recivedSize;
	}

	@Override
	public File getFile() {
		return file;
	}
}
