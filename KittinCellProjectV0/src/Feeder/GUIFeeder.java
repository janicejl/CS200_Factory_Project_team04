package Feeder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import data.GUIPart;
import data.Part;

public class GUIFeeder {
	
	Feeder feeder;
	BufferedImage image, diverter;
	
	public GUIFeeder(Feeder f){
		feeder = f;
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
		
		if (feeder.isPreviousPosition() == feeder.getLane()){
			if (feeder.isPreviousPosition()){
				g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
			}
			else if (!feeder.isPreviousPosition()){
				g2.drawImage(diverter, feeder.getDiverterX(), (int)feeder.getY() + 70, null);
			}
		}
		else{
			if (feeder.isPreviousPosition()){
				if (feeder.getDiverterY() != feeder.getY() + 70){
					g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
				}
				else{
					g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
				}	
			}
			else {
				if (feeder.getDiverterY() != feeder.getY()){
					g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
				}
				else{
					g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
				}
			}
		}
		
		
		ArrayList<GUIPart> parts = new ArrayList<GUIPart>();
		for (int i = 0; i < feeder.getPartAmount(); i++) {	
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
