package laneManager;

import data.Part;
import data.GUIPart;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import javax.imageio.ImageIO;

public class GUINest {
	
	Nest nest;						//A reference to Nest from server
	BufferedImage image;			//Image to paint for animation. 
	CopyOnWriteArrayList<Part> parts;			//Array of the parts inside the nest 
	ArrayList<GUIPart> gParts;		//Array of the graphic parts class to paint the parts. 
	
	public GUINest(Nest n) {
		nest = n;
		parts = new CopyOnWriteArrayList<Part>();
		gParts = new ArrayList<GUIPart>();
		try {
			image = ImageIO.read(new File("images/nest.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g, int managerNum) {
		double x = nest.getX();
		
		if (managerNum == 0) { 				//For the Kit Assembly Manager
			//Move it
			x = x + 320;
		} else if (managerNum == 1) { 		//For the Lane Manager  
			// No need to change x, already set to zero. 
		}
		double y = nest.getY();
		
		g.drawImage(image, (int)x, (int)y, null);
		
		parts = nest.getParts();
		
		gParts.clear();
		for (int i = 0, j = 0; i < parts.size(); i++) {
			if (i < 8) {
				parts.get(i).setX(x + ((i % 4) * 25));		//25 = size of image parts. 
				parts.get(i).setY(y + (j * 25));
				
				if (i % 4 == 3) {
					j++;
				}
			} // else no need to set position since it is already set when it is added. 
			
			gParts.add(new GUIPart(parts.get(i)));
		}
		
		for (int i = 0; i < gParts.size(); i++) {
			gParts.get(i).paintPart(g);
		}
		/*BufferedImage part;
		if (parts.size() > 0) {
			try {
				part = ImageIO.read(new File("images/" + parts.get(0).getID() + ".png"));
				
				for (int i = 0; i < parts.size(); i++) {
					g.drawImage(part, (int)parts.get(i).getX(), (int)parts.get(i).getY(), null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
				
	}
	
	public void setNest(Nest nest) {
		this.nest = nest;
	}
}
