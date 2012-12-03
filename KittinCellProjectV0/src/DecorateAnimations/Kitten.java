package DecorateAnimations;

import java.awt.Dimension;

import javax.swing.JLabel;

public class Kitten {
	JLabel image;
	Dimension size;
	boolean hide,appeared,finished;
	
	public Kitten(JLabel l) {
		image = l;
		size = l.getPreferredSize();
		hide=true;
		appeared=false;
		finished=false;
	}
	
	
	public synchronized JLabel getImage() {
		return image;
	}
	public synchronized void setImage(JLabel image) {
		this.image = image;
	}
	public synchronized Dimension getSize() {
		return size;
	}
	public synchronized void setSize(Dimension size) {
		this.size = size;
	}


	public synchronized boolean isHide() {
		return hide;
	}


	public synchronized void setHide(boolean hide) {
		this.hide = hide;
	}


	public synchronized boolean isAppeared() {
		return appeared;
	}


	public synchronized void setAppeared(boolean appeared) {
		this.appeared = appeared;
	}


	public synchronized boolean isFinished() {
		return finished;
	}


	public synchronized void setFinished(boolean finished) {
		this.finished = finished;
	}


	

}
