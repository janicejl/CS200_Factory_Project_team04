package data;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Random;

import javax.imageio.*;

import sun.java2d.pipe.BufferedTextPipe;

public class GUIPart {

	Part part;
	BufferedImage image;
	static BufferedImage nullImage;
	Random rand = new Random();

	public GUIPart (Part p) {
		part = p;
		try {
			image = ImageIO.read(new File(part.getImagePath()));
			nullImage = ImageIO.read(new File("images/null.png"));
			//image = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
			//System.out.println("Picture found.");
		} catch (IOException e) {
			System.out.println("Picture not found.");
			e.printStackTrace();
		}
	}

	public void paintPart(Graphics2D g) {
		double x = part.getX();
		double y = part.getY();
		g.setColor(Color.WHITE);
		
		if(part.getPartDropped()){ // paints nullImage if part was dropped
			g.drawImage(nullImage, (int)x, (int)y, null);
		}
		else {
			g.drawImage(image, (int)x, (int)y, null);
		}		
	}
	
	public void paintVibratingPart(Graphics2D g, int v) {
		double x = part.getX();
		double y = part.getY();
		g.setColor(Color.WHITE);
		
		if(part.getPartDropped()){ // paints nullImage if part was dropped
			g.drawImage(nullImage, (int)x, (int)y, null);
		}
		else {
			g.drawImage(image, (int)x, (int)y + (rand.nextInt(v*2)-v), null);
		}		 
	}
	
	
	
	public BufferedImage getImage() {
		if(part.getPartDropped()){
			return nullImage;
		}
		else {
			return image;
		}
	}
}