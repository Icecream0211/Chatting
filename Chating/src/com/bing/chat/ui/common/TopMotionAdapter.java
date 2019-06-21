package com.bing.chat.ui.common;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class TopMotionAdapter extends MouseAdapter implements
		MouseMotionListener {
	private TopPanel top;
	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;

	private Component c;
	@Override
	public void mouseEntered(MouseEvent e) {
		top.setCursor(new Cursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		top.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		tmp = new Point(e.getX(), e.getY());
		isDragged = true;
	}

	public TopMotionAdapter(TopPanel top) {
		this.top = top;
		c = top.getFather();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (isDragged) {
			loc = new Point(c.getLocation().x + e.getX() - tmp.x,
					c.getLocation().y + e.getY() - tmp.y);
			 c.setLocation(loc);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		/*if (isDragged) {
			loc = new Point(c.getLocation().x + e.getX() - tmp.x,
					c.getLocation().y + e.getY() - tmp.y);
			c.setLocation(loc);
		}*/
	}
}
