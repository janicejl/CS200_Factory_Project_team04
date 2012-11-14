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
	BufferedImage image, diverter;
	boolean previousPositiion;
	boolean moving;
	int diverterX, diverterY;
	
	public GUIFeeder(Feeder f){
		feeder = f;
		diverterX = (int)feeder.getX() - 3;
		diverterY = (int)feeder.getY();
		moving = false;
		previousPositiion = feeder.getLane();
		try {
			image = ImageIO.read(new File("images/feeder.png"));
			diverter = ImageIO.read(new File("images/diverter.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g2){
		double x = feeder.getX();
		double y = feeder.getY();
		g2.drawImage(image, (int)x, (int)y, null);
		
		if (previousPositiion == feeder.getLane()){
			if (previousPositiion){
				g2.drawImage(diverter, diverterX, diverterY, null);
			}
			else if (!previousPositiion){
				g2.drawImage(diverter, diverterX, (int)feeder.getY() + 70, null);
			}
		}
		else{
			if (previousPositiion){
				if (diverterY != feeder.getY() + 70){
					diverterY += 2;
					g2.drawImage(diverter, diverterX, diverterY, null);
				}
				else{
					g2.drawImage(diverter, diverterX, diverterY, null);
					previousPositiion = feeder.getLane();
				}	
			}
			else {
				if (diverterY != feeder.getY()){
					diverterY -= 2;
					g2.drawImage(diverter, diverterX, diverterY, null);
				}
				else{
					g2.drawImage(diverter, diverterX, diverterY, null);
					previousPositiion = feeder.getLane();
				}
				
			}
		}
		
		
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
