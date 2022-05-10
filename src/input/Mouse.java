package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import math.Point;

public class Mouse implements MouseListener, MouseMotionListener{ 
	private int mouseX, mouseY;
	private int click;
	
	public Mouse() {
		this.mouseX = 0;
		this.mouseY = 0;
		this.click = 0;
	}

	public Point getPosition() {return new Point(mouseX, mouseY);}
	
	public int getClick() {return click;}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.click = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.click = 0;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
