package data;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.*;

public class GUIKit {

	Kit kit;					//A reference to the kit from the server. 
	BufferedImage image;		//image to paint for the graphics. 
    BufferedImage kitTop = null;
	protected Vector<GUIPart> gParts; // vector of GUIParts in kit
	
	public GUIKit(Kit k) {
		kit = k;
		
		gParts = new Vector<GUIPart>();
		
		try {
			image = ImageIO.read(new File("images/kit.png"));
			kitTop = ImageIO.read(new File("images/kitTop.png"));	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setKit(Kit k){
		kit = k;
	}
	
	public void paintKit(Graphics2D g) {
		kit.update();
		
		double x = kit.getX();
		double y = kit.getY();
		
		if(kit.getDone()){
			g.drawImage(kitTop, (int)x, (int)y, null);
		}
		else {		
			g.drawImage(image, (int)x, (int)y, null);
			
			//Setting the position of each individual part relative to the top left corner of the kit. 
			for (int i = 0, j = 0; i < kit.getPartsList().size(); i++) {
				kit.getPartsList().get(i).setX(x + ((i % 2) * 25));		//25 = size of testing image parts. 
				kit.getPartsList().get(i).setY(y + (j * 25));
				
				if (i % 2 == 1) {
					j++;
				}
				
				if(i >= gParts.size()){
					gParts.add(new GUIPart(kit.getPartsList().get(i)));
				}
				else {
					gParts.set(i, new GUIPart(kit.getPartsList().get(i)));
				}
			}
			
			for (int i = 0; i < kit.getPartsList().size(); i++) {
				g.drawImage(gParts.get(i).getImage(),(int)kit.getPartsList().get(i).getX(), (int)kit.getPartsList().get(i).getY(), null);
			}		
		}
	}
}
