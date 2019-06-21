package com.xiyou.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.bing.command.FileTransSendCommand;
import com.bing.command.SurveilSendCommand;
import com.bing.common.CommonConfig;
import com.bing.common.ContactNode;
import com.bing.common.MainInstance;
import com.bing.common.ResourceLocation;
import com.bing.common.TxtChatingInstance;

public class TxtChatView extends MyFrame {
	private static final long serialVersionUID = 1L;
	private JButton surveilButton;
	private JButton filesendButton;
	private JButton sendButton;
	private JComboBox fontStyleSelect;
	private JComboBox fontSizeSelect;
	private JComboBox foregroundSelect;
	private JLabel headLabel;
	private JLabel friendMarkLabel;
	private JLabel fontStyleLabel;
	private JLabel fontSizeLabel;
	private JLabel foregroundLabel;
	private JPanel topPane;
	private JPanel toolPane;
	private JScrollPane showScrollPane;
	private JScrollPane sendScrollPane;
	private JSeparator jSeparator1;
	private JTextPane showTextPane;
	private JTextPane sendTextPane;
	private SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
	private TxtChatingInstance chatinstance;
	int begin = 0;
	// 当前登录用户的昵称
	private String yourname = "你的昵称";
	private String nikename;

	// 聊天对象的信息
	public TxtChatView(TxtChatingInstance chatingInstance) {
		this.chatinstance = chatingInstance;
		initComponents();
	}

	public TxtChatView() {
		// TODO Auto-generated constructor stub
		initComponents();
	}

	private void initComponents() {
		// 界面显示风格
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		topPane = new JPanel();
		headLabel = new JLabel();
		friendMarkLabel = new JLabel();
		showScrollPane = new JScrollPane();
		showTextPane = new JTextPane();
		jSeparator1 = new JSeparator();
		toolPane = new JPanel();
		fontStyleLabel = new JLabel();
		fontStyleSelect = new JComboBox();
		fontSizeLabel = new JLabel();
		fontSizeSelect = new JComboBox();
		foregroundLabel = new JLabel();
		foregroundSelect = new JComboBox();
		surveilButton = new JButton();
		filesendButton = new JButton();
		sendScrollPane = new JScrollPane();
		sendTextPane = new JTextPane();
		sendButton = new JButton();

		showTextPane.setEditable(false);
		// 默认字体颜色
		StyleConstants.setForeground(simpleAttributeSet, Color.red);
		// 默认字体大小
		StyleConstants.setFontSize(simpleAttributeSet, 13);
		sendTextPane.setCharacterAttributes(simpleAttributeSet, false);
		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// setDefaultCloseOperation(operation)
		setTitle(this.nikename);
		headLabel.setIcon(new ImageIcon(ResourceLocation.getImageIcon("image/upload.jpg")
				.getImage().getScaledInstance(78, 49, Image.SCALE_DEFAULT)));
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				new java.awt.Color(255, 255, 255)));
		friendMarkLabel.setText("对方昵称:" + "(" + chatinstance.getDestIp() + ":"
				+ chatinstance.getDestLisPort() + ")");
		GroupLayout jPanel1Layout = new GroupLayout(topPane);
		topPane.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout
						.createSequentialGroup()
						.addComponent(headLabel, GroupLayout.PREFERRED_SIZE,
								54, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(friendMarkLabel,
								GroupLayout.PREFERRED_SIZE, 210,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(129, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(friendMarkLabel, GroupLayout.DEFAULT_SIZE, 49,
						Short.MAX_VALUE)
				.addComponent(headLabel, GroupLayout.DEFAULT_SIZE, 49,
						Short.MAX_VALUE));

		showScrollPane.setViewportView(showTextPane);

		jSeparator1.setForeground(new java.awt.Color(204, 204, 0));

		toolPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				new java.awt.Color(204, 204, 0)));

		fontStyleLabel.setText("\u5b57\u4f53:");

		fontStyleSelect.setModel(new DefaultComboBoxModel(new String[] { "宋体",
				"黑体", "Dialog", "Gulim" }));
		fontStyleSelect.setBorder(null);
		// 字体设置
		fontStyleSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox jcb = (JComboBox) e.getSource();

					String fam = (String) jcb.getSelectedItem();
					System.out.println(fam);
					StyleConstants.setFontFamily(simpleAttributeSet, fam);

					sendTextPane.setCharacterAttributes(simpleAttributeSet,
							false);
					sendTextPane.getStyledDocument().setCharacterAttributes(0,
							sendTextPane.getText().length(),
							simpleAttributeSet, false);
				} else {

				}
			}
		});

		fontSizeLabel.setText("\u5b57\u53f7:");

		fontSizeSelect.setModel(new DefaultComboBoxModel(new String[] { "13",
				"14", "15", "16", "17", "18", "18", "20", "21", "22" }));
		fontSizeSelect.setBorder(null);

		// 字号处理
		fontSizeSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox jcb = (JComboBox) e.getSource();

					System.out.println((String) (jcb.getSelectedItem()));
					System.out.println(jcb.getSelectedIndex());
					StyleConstants
							.setForeground(simpleAttributeSet, Color.blue);

					int size = Integer.valueOf((String) jcb.getSelectedItem());
					StyleConstants.setFontSize(simpleAttributeSet, size);

					sendTextPane.setCharacterAttributes(simpleAttributeSet,
							false);
					sendTextPane.getStyledDocument().setCharacterAttributes(0,
							sendTextPane.getText().length(),
							simpleAttributeSet, false);
				} else {

				}

			}
		});

		foregroundLabel.setText("\u989c\u8272:");

		foregroundSelect.setModel(new DefaultComboBoxModel(new String[] { "红色",
				"蓝色", "绿色", "黄色", "黑色" }));
		foregroundSelect.setBorder(null);

		// 字体颜色处理
		foregroundSelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JComboBox jcb = (JComboBox) e.getSource();
					System.out.println((String) (jcb.getSelectedItem()));
					System.out.println(jcb.getSelectedIndex());
					String colorName = (String) (jcb.getSelectedItem());

					if (colorName.equals("黑色")) {
						StyleConstants.setForeground(simpleAttributeSet,
								Color.black);
					} else if (colorName.equals("蓝色")) {
						StyleConstants.setForeground(simpleAttributeSet,
								Color.blue);
					} else if (colorName.equals("绿色")) {
						StyleConstants.setForeground(simpleAttributeSet,
								Color.green);
					} else if (colorName.equals("黄色")) {
						StyleConstants.setForeground(simpleAttributeSet,
								Color.yellow);
					} else if (colorName.equals("红色")) {
						StyleConstants.setForeground(simpleAttributeSet,
								Color.red);
					}

					sendTextPane.setCharacterAttributes(simpleAttributeSet,
							false);
					sendTextPane.getStyledDocument().setCharacterAttributes(0,
							sendTextPane.getText().length(),
							simpleAttributeSet, false);
				} else {

				}

			}
		});

		// 为了保证插入图片后,发送格式不变
		sendTextPane.setText("<p></p>");
		sendTextPane.setText("");
		surveilButton.setText("\u89c6\u9891\u76d1\u63a7");
		filesendButton.setText("\u4f20\u9001\u6587\u4ef6");

		GroupLayout jPanel2Layout = new GroupLayout(toolPane);
		toolPane.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addComponent(fontStyleLabel)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(fontStyleSelect,
												GroupLayout.PREFERRED_SIZE, 49,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(fontSizeLabel)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(fontSizeSelect,
												GroupLayout.PREFERRED_SIZE, 48,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(foregroundLabel,
												GroupLayout.PREFERRED_SIZE, 27,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(foregroundSelect,
												GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(surveilButton,
												GroupLayout.DEFAULT_SIZE, 50,
												Short.MAX_VALUE)
										.addComponent(filesendButton,
												GroupLayout.DEFAULT_SIZE, 50,
												Short.MAX_VALUE)
										.addGap(101, 101, 101)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(fontStyleLabel, GroupLayout.DEFAULT_SIZE,
								29, Short.MAX_VALUE)
						.addComponent(fontStyleSelect,
								GroupLayout.PREFERRED_SIZE, 26,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(fontSizeLabel, GroupLayout.DEFAULT_SIZE,
								29, Short.MAX_VALUE)
						.addComponent(fontSizeSelect,
								GroupLayout.PREFERRED_SIZE, 26,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(foregroundLabel,
								GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
						.addComponent(foregroundSelect,
								GroupLayout.PREFERRED_SIZE, 26,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(surveilButton,
								GroupLayout.PREFERRED_SIZE, 28,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(filesendButton,
								GroupLayout.PREFERRED_SIZE, 28,
								GroupLayout.PREFERRED_SIZE)));

		sendScrollPane.setViewportView(sendTextPane);
		sendButton.setText("发送");
		surveilButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TxtChatView.this.sendSurveillanceRequest(e);
			}
		});

		filesendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TxtChatView.this.sendFileProcess(e);
			}
		});

		// 点发送的处理
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendClicked(e);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addComponent(sendScrollPane,
										GroupLayout.DEFAULT_SIZE, 327,
										Short.MAX_VALUE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)

								.addComponent(sendButton,
										GroupLayout.PREFERRED_SIZE, 78,
										GroupLayout.PREFERRED_SIZE))
				.addComponent(jSeparator1, GroupLayout.DEFAULT_SIZE, 411,
						Short.MAX_VALUE)
				.addComponent(topPane, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(toolPane, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(showScrollPane, GroupLayout.DEFAULT_SIZE, 411,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(topPane,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(9, 9, 9)
								.addComponent(jSeparator1,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(showScrollPane,
										GroupLayout.PREFERRED_SIZE, 225,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(toolPane,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(
														sendScrollPane,
														GroupLayout.DEFAULT_SIZE,
														102, Short.MAX_VALUE)
												.addComponent(
														sendButton,
														GroupLayout.DEFAULT_SIZE,
														102, Short.MAX_VALUE))));
		pack();
	}

	protected void sendSurveillanceRequest(ActionEvent e) {
		System.out.println("Surveillance Request sended...");
		ContactNode node = new ContactNode(this.chatinstance.getDestIp(),
				this.chatinstance.getDestLisPort(), 1);
		new SurveilSendCommand(node).exec();
	}

	protected void sendFileProcess(ActionEvent e) {
		System.out.println("FileSend pressed");
		ContactNode node = new ContactNode(this.chatinstance.getDestIp(),
				this.chatinstance.getDestLisPort(), 1);
		new FileTransSendCommand(node).exec();
	}

	/*
	 * 发送按钮监听
	 */
	private void sendClicked(ActionEvent e) {
		Map<Integer, Icon> icons = getAllIcons(sendTextPane.getStyledDocument()
				.getRootElements());
		String content = sendTextPane.getText();
		if (content.trim().equals("")) {
			return;
		}
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		String remark = yourname + " " + hour + ":" + minute + ":" + second
				+ "\n";
		this.chatinstance.sendMsg(content);
		updateShowTextPane(remark, content, icons, simpleAttributeSet);
		sendTextPane.setText("");
	}

	public void updateShowTextPane(String content) {
		Map<Integer, Icon> icons = getAllIcons(sendTextPane.getStyledDocument()
				.getRootElements());
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);

		String remark = "Mate" + " " + hour + ":" + minute + ":" + second
				+ "\n";
		updateShowTextPane(remark, content, icons, simpleAttributeSet);
	}

	public void updateShowTextPane(String remark, String content) {
		Map<Integer, Icon> icons = getAllIcons(sendTextPane.getStyledDocument()
				.getRootElements());
		Calendar c = Calendar.getInstance();
		updateShowTextPane(remark, content, icons, simpleAttributeSet);
	}

	/**
	 * 更新显示pane
	 * 
	 * @param remark
	 * @param content
	 * @param icons
	 * @param simpleAttributeSet
	 */
	public void updateShowTextPane(String remark, String content,
			Map<Integer, Icon> icons, SimpleAttributeSet simpleAttributeSet) {

		StringBuffer b = new StringBuffer();
		b.append(content);
		int begin = showTextPane.getDocument().getEndPosition().getOffset() - 1;

		try {
			SimpleAttributeSet s1 = new SimpleAttributeSet();
			StyleConstants.setForeground(s1, Color.blue);
			showTextPane.getStyledDocument().insertString(begin, remark, s1);
			begin = showTextPane.getStyledDocument().getEndPosition()
					.getOffset() - 1;
			showTextPane.getStyledDocument().insertString(begin, b.toString(),
					simpleAttributeSet);

			for (int site : icons.keySet()) {
				System.out.println("插入位置:" + site + begin);
				showTextPane.getDocument().remove(site + begin, 1);
				showTextPane.setCaretPosition(site + begin);
				showTextPane.insertIcon(icons.get(site));
			}

			try {
				showTextPane.getStyledDocument()
						.insertString(
								showTextPane.getDocument().getEndPosition()
										.getOffset() - 1, "\n",
								simpleAttributeSet);
				showTextPane.setCaretPosition(showTextPane.getDocument()
						.getEndPosition().getOffset() - 1);
			} catch (BadLocationException e2) {
				e2.printStackTrace();
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 获得JTextPane中的所有图片的所在位置和图片对象
	 * 
	 * @param roots
	 * @return
	 */
	public Map<Integer, Icon> getAllIcons(Element[] roots) {
		Map<Integer, Icon> icons = new HashMap<Integer, Icon>();

		for (int a = 0; a < roots.length; a++) {
			for (int c = 0; c < roots[a].getElementCount(); c++) {
				Element element = roots[a].getElement(c);
				Icon icon = StyleConstants.getIcon(element.getAttributes());

				if (icon != null) {
					icons.put(element.getStartOffset(), icon);
				} else
					icons.putAll(getAllIcons(new Element[] { element }));
			}
		}

		return icons;
	}

	public static void main(String args[]) {
		// java.awt.EventQueue.invokeLater(new Runnable() {
		// public void run() {
		new TxtChatView().setVisible(true);
		// }
		// });
	}

	public JTextPane getShowTextPane() {
		return showTextPane;
	}

	public void setShowTextPane(JTextPane showTextPane) {
		this.showTextPane = showTextPane;
	}

	public void showup() {
		//this.pack();
		/*this.setLocation(CommonConfig.SYS_SCREEN_WIDTH / 3,
				CommonConfig.SYS_SCREEN_HEIGHT / 3);*/
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void closeFrame() {
	}
}