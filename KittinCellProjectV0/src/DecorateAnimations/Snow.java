package DecorateAnimations;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Snow{
	BufferedImage snowflake;//image
	double x;//x pos
	double y; //y pos
	boolean goLeft;//if it is now going left
	public Snow() {
		try {
			snowflake = ImageIO.read(new File("images/snow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		double r=Math.random()*1200;
		x=r;
		y=0;
	}

	public synchronized Image getSnowflake() {
		return snowflake;
	}

	public synchronized void setSnowflake(BufferedImage snowflake) {
		this.snowflake = snowflake;
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

	public synchronized boolean isLeft() {
		return goLeft;
	}

	public synchronized void setLeft(boolean leftOrRight) {
		this.goLeft = leftOrRight;
	}



}

