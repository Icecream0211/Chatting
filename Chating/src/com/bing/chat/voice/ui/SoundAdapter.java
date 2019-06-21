package com.bing.chat.voice.ui;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * 
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SoundAdapter extends JSlider {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SoundAdapter() {
        // 设定布局器
        // 设定监听器
    /*    ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSlider) {
                    System.out.println("刻度: "
                            + ((JSlider) e.getSource()).getValue());
                }
            }
        };
        addChangeListener(listener);*/
        // 设定JSlider1
        // 注入自定义ui
        setUI(new MySliderUI(this));
        // 主刻度
        setMajorTickSpacing(10);
        // 次刻度
         setMinorTickSpacing(5);
        // 设定为显示
         setPaintTicks(true);
        setPaintLabels(true);
        // 监听slider1
        // 设定JSlider2
        //使用盒式容器
        //设定窗体大小
        //setPreferredSize(new Dimension(240, 100));
    }

    public static void main(String[] args) {
                createUI();
    }

    public static void createUI() {
        JFrame frame = new JFrame("音量刻度设置");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new SoundAdapter());
        frame.setResizable(false);
        frame.pack();
        //居中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
