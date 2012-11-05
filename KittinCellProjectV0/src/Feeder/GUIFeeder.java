package Feeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

import data.GUIPart;
import data.Part;

public class GUIFeeder {
	
	Feeder feeder;
	BufferedImage image;
	Rectangle feederStatus;
	
	public GUIFeeder(Feeder f){
		feeder = f;
		feederStatus = new Rectangle((int)feeder.getX() + 4, 
					(int)feeder.getY() + 130,
					75, 10);
		try {
			image = ImageIO.read(new File("images/feeder.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g2){
		double x = feeder.getX();
		double y = feeder.getY();
		int statusWidth;
		g2.drawImage(image, (int)x, (int)y, null);
		g2.setColor(Color.ORANGE);
		if (feeder.getPartAmount() > 0){
			statusWidth = (int)feeder.getPartAmount()*3;
		}
		else {
			statusWidth = 0;
		}
		feederStatus.setSize(statusWidth, 10);
		g2.fill(feederStatus);
		
		Vector<GUIPart> parts = new Vector<GUIPart>();
		for (int i = 0, j = 0; i < feeder.getPartAmount(); i++) {
			feeder.getParts().get(i).setX(feeder.getX() + ((i % 4) * 25));		//25 = size of testing image parts. 
			feeder.getParts().get(i).setY(feeder.getY()+ (j * 25));
			
			if (i % 4 == 3) {
				j++;
			}
			
			parts.add(new GUIPart(feeder.getParts().get(i)));
			parts.get(i).paintPart(g2);
		}
		
		
	}

	public synchronized Feeder getFeeder() {
		return feeder;
	}

	public synchronized void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	
	
}
