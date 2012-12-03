package DecorateAnimations;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Kitten {
	BufferedImage image;
	Dimension size;
	boolean hide,appeared,finished;
	double x;
	double y;
	
	public Kitten(String s) {
		size = new Dimension();
		try {
			image = ImageIO.read(new File(s));
			size.setSize(image.getWidth(), image.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public synchronized BufferedImage getImage() {
		return image;
	}
	public synchronized void setImage(BufferedImage i) {
		this.image = i;
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


	public synchronized double getX() {
		return x;
	}


	public synchronized void setX(double x) {
		this.x = x;
	}


	public synchronized double getY() {
		return y;
	}


	public synchronized void setY(double y) {
		this.y = y;
	}

    public synchronized void setXY(double a,double b){
    	x = a;
    	y = b;
    }
	

}
