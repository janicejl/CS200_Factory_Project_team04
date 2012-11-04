package laneManager;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import data.Part;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class LaneGraphics extends JPanel /*implements ActionListener*/ {
	private ArrayList<Lane> lanes = new ArrayList<Lane> ();
	private int maxX;
	private int maxY;
	private Rectangle2D.Double backgroundRectangle;
	private int counter;
	private boolean emptyConveyorOn;
	private double emptyConveyorMove;
	private BufferedImage conveyorImage; 
	
	//private Timer timer; 
    
    public LaneGraphics() {
    	counter = 0;

    	lanes.add(new Lane(this,600,-10)); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(this,600,60)); 
    	lanes.add(new Lane(this,600,130)); 
    	lanes.add(new Lane(this,600,200));
    	lanes.add(new Lane(this,600,270)); 
    	lanes.add(new Lane(this,600,340));
    	lanes.add(new Lane(this,600,410)); 
    	lanes.add(new Lane(this,600,480));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 700;
    	this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		
    	emptyConveyorOn = true; 
    	emptyConveyorMove = 0;
		
		try {
            conveyorImage = ImageIO.read(new File("images/conveyerLong.png"));
        } catch (IOException e) {
        	System.out.println("Image load issue");
        }
		
		
    }
    
    public void paintComponent(Graphics g) {

    	Graphics2D g2 = (Graphics2D)g;
		counter++;
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill( backgroundRectangle );
		//g2.setColor( Color.RED );
		
		///for (int i = 0; i < 5; i++) {
		//	g2.drawImage(beltImage, i*20, 50, null);
		//}
		 if(emptyConveyorOn){
	            emptyConveyorMove += 0.23; //magic ratio
	            if(emptyConveyorMove > 20.0){
	                emptyConveyorMove = 0;
	            }
	        }

	        for(int i = -1; i < 40; i++){ // main conveyor images
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,20,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,90,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,160,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,230,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,300,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,370,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,440,null); // empty conveyor   
	        	g2.drawImage(conveyorImage, i * 20 - (int)emptyConveyorMove,510,null); // empty conveyor   
	        	
	        
	        }  

	        
	        
	        
	        
	        
	        g2.setColor(Color.BLUE);		
		
		for (int i = 0; i < lanes.size(); i++) {
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++) {
				g2.fill(new Ellipse2D.Double(lanes.get(i).getItemList().get(j).getX(),lanes.get(i).getItemList().get(j).getY(),20,20));
						
						
						//lanes.get(i).getItemList().get(j));
			}
			for (int k = 0; k < lanes.get(i).getQueueList().size(); k++) {
				//g2.fill(lanes.get(i).getQueueList().get(k));
			}
		}
    }
    
    public void setVibration() { //Unimplimented
    	System.out.println("Unimplemented");
    }
  

    
    public void releaseItem(int lane) {
    	lanes.get(lane).releasePart();
    }
    
    public void addPartToLane(int lane, Part part) {
    	lanes.get(lane).addPart(part);
    }
    
}  
