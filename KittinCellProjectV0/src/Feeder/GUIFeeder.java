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
import javax.swing.ImageIcon;

import data.GUIPart;
import data.Part;

public class GUIFeeder {
	
	Feeder feeder;
	BufferedImage image, diverter, partbox, part;
	
	public GUIFeeder(Feeder f){
		feeder = f;
		try {
			//load images
			image = ImageIO.read(new File("images/feeder.png"));
			diverter = ImageIO.read(new File("images/diverter.png"));
			partbox = ImageIO.read(new File("images/crate.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintNest(Graphics2D g2){
		double x = feeder.getX();
		double y = feeder.getY();
		g2.drawImage(image, (int)x, (int)y, null);
		
		//Check to see if the dierter was in the same
		//position as last frame.  If so, paint it there.
		if (feeder.isPreviousPosition() == feeder.getLane()){
			if (feeder.isPreviousPosition()){
				g2.drawImage(diverter, feeder.getDiverterX(), feeder.getDiverterY(), null);
			}
			else if (!feeder.isPreviousPosition()){
				g2.drawImage(diverter, feeder.getDiverterX(), (int)feeder.getY() + 70, null);
			}
		}
		//Diverter has switched, so update position
		else{
			//Check if diverter was previously in the top lane
			//draw accordingly
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
		
		//paint the parts
		ArrayList<GUIPart> parts = new ArrayList<GUIPart>();
		for (int i = 0; i < feeder.getPartAmount(); i++) {	
			parts.add(new GUIPart(feeder.getParts().get(i)));
			parts.get(i).paintPart(g2);
		}
	}
	
	//Paint the crate
	public void paintCrate(Graphics2D g2){
		if(feeder.getHasCrate()){
			g2.drawImage(partbox, (int)feeder.getX() + 130, (int)feeder.getY() + 12, null);
			try{
				part = ImageIO.read(new File(feeder.getInfo().getImagePath()));
				g2.drawImage(part, (int)feeder.getX() + 143, (int)feeder.getY() + 47, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized Feeder getFeeder() {
		return feeder;
	}

	public synchronized void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}
}
