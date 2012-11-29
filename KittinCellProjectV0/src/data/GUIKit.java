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
	
	protected Vector<BufferedImage> partImages;			//array of all the parts image
	protected Vector<String> partImagesPath;			//array of all the image path
	
	public GUIKit(Kit k) {
		kit = k;
		
		partImages = new Vector<BufferedImage>();
		partImagesPath = new Vector<String>();
		
		try {
			image = ImageIO.read(new File("images/crate.png"));
			
			for (int i = 0; i < 9; i++)
			{
				partImages.add(ImageIO.read(new File("images/kt"+ i + ".png")));
				partImagesPath.add("images/kt" + i + ".png");
				i++;
			}
			
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
		
		g.drawImage(image, (int)x, (int)y, null);
		
		//Setting the position of each individual part relative to the top left corner of the kit. 
		for (int i = 0, j = 0; i < kit.getPartsList().size(); i++) {
			kit.getPartsList().get(i).setX(x + ((i % 2) * 25));		//25 = size of testing image parts. 
			kit.getPartsList().get(i).setY(y + (j * 25));
			
			if (i % 2 == 1) {
				j++;
			}
			
		}
		
		for (int i = 0; i < kit.getPartsList().size(); i++) {
			g.drawImage(partImages.get(partImagesPath.indexOf(kit.getPartsList().get(i).getImagePath())), (int)kit.getPartsList().get(i).getX(), (int)kit.getPartsList().get(i).getY(), null);
		}

		
	}
}
