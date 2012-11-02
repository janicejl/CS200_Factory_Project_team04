package laneManager;

import data.Part;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GUINest {
	
	Nest nest;
	BufferedImage image;
	
	public GUINest(Nest n) {
		nest = n;
		try {
			image = ImageIO.read(new File("graphics/nest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g) {
		double x = nest.getX();
		double y = nest.getY();
		
		g.drawImage(image, (int)x, (int)y, null);
		
		ArrayList<Part> parts = nest.parts;
		
		for (int i = 0, j = 0; i < parts.size(); i++) {
			parts.get(i).setX(x + ((i % 4) * 32));		//32 = size of testing image parts. 
			parts.get(i).setY(y + (j * 32));
			
			if (i % 4 == 3) {
				j++;
			}
		}
		
		BufferedImage part;
		if (parts.size() > 0) {
			try {
				part = ImageIO.read(new File("graphics/" + parts.get(0).getID() + ".png"));
				
				for (int i = 0; i < parts.size(); i++) {
					g.drawImage(part, (int)parts.get(i).getX(), (int)parts.get(i).getY(), null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
	}
}
