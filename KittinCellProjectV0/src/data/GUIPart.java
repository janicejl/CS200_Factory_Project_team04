package data;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class GUIPart {

	Part part;
	BufferedImage image;

	public GUIPart (Part p) {
		part = p;
		try {
			image = ImageIO.read(new File("images/kt" + p.getID() + ".png"));
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
		
		g.drawImage(image, (int)x, (int)y, null);
	}
}