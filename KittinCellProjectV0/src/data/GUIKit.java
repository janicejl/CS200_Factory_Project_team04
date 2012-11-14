package data;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.*;

public class GUIKit {

	Kit kit;
	BufferedImage image;
	
	public GUIKit(Kit k) {
		kit = k;
		
		try {
			image = ImageIO.read(new File("images/crate.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintKit(Graphics2D g) {
		kit.update();
		
		double x = kit.getX();
		double y = kit.getY();
		
		g.drawImage(image, (int)x, (int)y, null);
		
		List<Part> parts = kit.getPartsList();
		//Setting the position of each individual part. 
		for (int i = 0, j = 0; i < parts.size(); i++) {
			parts.get(i).setX(x + ((i % 2) * 25));		//25 = size of testing image parts. 
			parts.get(i).setY(y + (j * 25));
			
			if (i % 2 == 1) {
				j++;
			}
			BufferedImage p;
			try {
				p = ImageIO.read(new File("images/kt" + parts.get(i).getID() + ".png"));
				if(kit.isGrabbed()){
					g.drawImage(p, -300, -300, null);
				}
				else if(!kit.isGrabbed()){
					g.drawImage(p, (int)parts.get(i).getX(), (int)parts.get(i).getY(), null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
