package laneManager;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import server.Lane;

import data.Part;
import Feeder.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import java.util.Vector;
import java.util.Random;
import java.util.TreeMap;

public class LaneGraphics extends JPanel /*implements ActionListener*/ {
	private Vector<Lane> lanes;
	private int maxX;
	private int maxY;
	private Rectangle2D.Double backgroundRectangle;
	private Vector<Boolean> emptyConveyorOnList;
	private Vector<Double> emptyConveyorMoveList;
	private BufferedImage conveyorImage; 
	private Vector<GUIFeeder> gFeeders;
	
    public LaneGraphics() {
    	lanes = new Vector<Lane>();
    	lanes.add(new Lane(600,-10)); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,60)); 
    	lanes.add(new Lane(600,130)); 
    	lanes.add(new Lane(600,200));
    	lanes.add(new Lane(600,270)); 
    	lanes.add(new Lane(600,340));
    	lanes.add(new Lane(600,410)); 
    	lanes.add(new Lane(600,480));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 700;
    	
    	this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		
    	emptyConveyorOnList  = new Vector<Boolean>(); 
    	emptyConveyorMoveList = new Vector<Double> ();
    	gFeeders = new Vector<GUIFeeder> ();
    	
    	for(int i = 0; i < 8; i++) {
    		emptyConveyorOnList.add(true);
    		emptyConveyorMoveList.add(0.0);	
    	}
    	
    	for (int i = 0; i < 4; i++) {
    		gFeeders.add(new GUIFeeder(new Feeder(525,20 + i*140)));
    	}
		
		try {
            conveyorImage = ImageIO.read(new File("images/conveyerLong.png"));
        } catch (IOException e) {
        	System.out.println("Image load issue");
        }	
    }
    
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill( backgroundRectangle );
		for (int i = 0; i < 8; i++) {
			 if(emptyConveyorOnList.get(i)){
		            emptyConveyorMoveList.set(i,emptyConveyorMoveList.get(i) + 0.23 * lanes.get(i).getConveyerBeltSpeed()); //magic ratio
		            if(emptyConveyorMoveList.get(i) > 20.0){
		                emptyConveyorMoveList.set(i, 0.0);
		            }
		        }
		}
			
        for(int i = -1; i < 40; i++){ // main conveyor images
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(0).intValue(),20,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(1).intValue(),90,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(2).intValue(),160,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(3).intValue(),230,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(4).intValue(),300,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(5).intValue(),370,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(6).intValue(),440,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(7).intValue(),510,null); // empty conveyor   
        }  
        
        g2.setColor(Color.BLUE);		
		for (int i = 0; i < lanes.size(); i++) {
			if(i < 4) //only four nests
				gFeeders.get(i).paintNest(g2);
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++) 
				g2.fill(new Ellipse2D.Double(lanes.get(i).getItemList().get(j).getX(),lanes.get(i).getItemList().get(j).getY(),20,20));
		}
    }
    
    public void setVibration() { //Unimplimented
    	System.out.println("Unimplemented");
    }
      
    public void releaseItem(int lane) {
    	lanes.get(lane).releasePart();
    }
    
    public synchronized void setLanes(Vector<Lane> _lanes) {
		this.lanes = _lanes;
	}
    
	public synchronized Vector<Lane> getLanes() {
		return lanes;
	}

	public void addPartToLane(int lane, Part part) {
    	lanes.get(lane).addPart(part);
    }
    
}
