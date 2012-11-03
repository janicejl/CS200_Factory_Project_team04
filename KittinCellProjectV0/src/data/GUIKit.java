package data;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

public class GUIKit {

	Kit kit;
	BufferedImage image;
	
	public GUIKit(Kit k) {
		kit = k;
		
		try {
			image = ImageIO.read(new File("src/data/graphics/kit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintKit(Graphics2D g) {
		double x = kit.getX();
		double y = kit.getY();
		
		g.drawImage(image, (int)x, (int)y, null);
		
		Vector<Part> parts = kit.parts;
		//Vector<BufferedImage> gParts = new Vector<BufferedImage>(); 
		//Setting the position of each individual part. 
		for (int i = 0, j = 0; i < parts.size(); i++) {
			parts.get(i).setX(x + ((i % 2) * 32));		//32 = size of testing image parts. 
			parts.get(i).setY(y + (j * 32));
			
			if (i % 2 == 1) {
				j++;
			}
			BufferedImage p;
			try {
				p = ImageIO.read(new File("src/data/graphics/" + parts.get(i).getID() + ".png"));
				g.drawImage(p, (int)parts.get(i).getX(), (int)parts.get(i).getY(), null);
			} catch (IOException e) {
				System.out.println(System.getProperty("user.dir"));
				e.printStackTrace();
			}
			
		}
		
	}
}
