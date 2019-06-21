package com.bing.chat.filesend;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xiyou.view.TipWindow;

public class ProgressView extends JPanel {
	private final JProgressBar progressBar;
	private final JLabel txtMsg;
	private JLabel openFile;
	private JLabel openDirectory;
	private IFileThread filethr;
	private File file;
	private TipWindow tw;

	public ProgressView(IFileThread filethr, TipWindow tw) {
		txtMsg = new JLabel("File transmitted");
		progressBar = new JProgressBar();
		this.filethr = filethr;
		this.tw = tw;
		this.init();
	}

	private void init() {
		openFile = new JLabel("Open File");
		openDirectory = new JLabel("Open Directory");
		openFile.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				openFile.setForeground(Color.red);
			}

			public void mouseExited(MouseEvent e) {
				openFile.setForeground(Color.black);
			}

			public void mouseReleased(MouseEvent e) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						desktop.open(filethr.getFile());
					}
				} catch (Exception ef) {
					JOptionPane.showMessageDialog(null, "文件格式未知，无法打开！！！");
				}
			}
		});

		openFile.setVisible(false);
		openDirectory.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				openDirectory.setForeground(Color.red);
			}

			public void mouseExited(MouseEvent e) {
				openDirectory.setForeground(Color.black);
			}
			public void mouseReleased(MouseEvent e) {
				try {
					File file = filethr.getFile();
					File director = new File(file.getAbsolutePath().substring(
							0, file.getAbsolutePath().lastIndexOf("\\")));
					if (Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						desktop.open(director);
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}

			}
		});

		openDirectory.setVisible(false);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(27)
										.addComponent(openFile)
										.addPreferredGap(
												ComponentPlacement.RELATED,
												219, Short.MAX_VALUE)
										.addComponent(openDirectory).addGap(66))
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(txtMsg,
												GroupLayout.DEFAULT_SIZE, 430,
												Short.MAX_VALUE)
										.addContainerGap())
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(progressBar,
												GroupLayout.DEFAULT_SIZE, 430,
												Short.MAX_VALUE)
										.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING)
				.addGroup(
						groupLayout
								.createSequentialGroup()
								.addGap(22)
								.addComponent(progressBar,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtMsg, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(14)
								.addGroup(
										groupLayout
												.createParallelGroup(
														Alignment.BASELINE)
												.addComponent(openFile)
												.addComponent(openDirectory))
								.addGap(171)));
		progressBar.setIndeterminate(true);
		progressBar.setBorderPainted(false);
		this.progressBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				progressBar.setIndeterminate(false);
				String result = MessageFormat.format(
						"{0,number,percent} completed.",
						progressBar.getPercentComplete());
				txtMsg.setText(result);
				if ((int) progressBar.getPercentComplete() == 1) {
					if (tw.getType() == 4) {
						openFile.setVisible(true);
						openDirectory.setVisible(true);
						txtMsg.setText(filethr.getFile().getName()
								+ " recived finished!");
					} else if (tw.getType() == 3) {
						txtMsg.setText(filethr.getFile().getName()
								+ " send finished!");
					}
				}
			}
		});
		setLayout(groupLayout);
	}

	public JProgressBar getProgressBar() {
		return this.progressBar;
	}

	public void setIFielThread(IFileThread filthr) {
		if (this.filethr == null) {
			this.filethr = filthr;
		}
	}
}
