package laneManager;
<<<<<<< HEAD
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
=======

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import java.awt.event.*;
>>>>>>> 95604875d141e759978497033539fe2099587533
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

<<<<<<< HEAD
public class LaneGraphics extends JPanel /*implements ActionListener*/ {
=======
public class LaneGraphics extends JFrame /*implements ActionListener*/ {
>>>>>>> 95604875d141e759978497033539fe2099587533
	private ArrayList<Lane> lanes = new ArrayList<Lane> ();
	private int maxX;
	private int maxY;
	private Rectangle2D.Double backgroundRectangle;
	private int counter;
<<<<<<< HEAD
	private BufferedImage beltImage; 
	
	   
=======
>>>>>>> 95604875d141e759978497033539fe2099587533
	//private Timer timer; 
    
    public LaneGraphics() {
    	counter = 0;
<<<<<<< HEAD
    	lanes.add(new Lane(this,600,0)); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(this,600,80)); 
    	lanes.add(new Lane(this,600,160)); 
    	lanes.add(new Lane(this,600,220));
    	lanes.add(new Lane(this,600,290)); 
    	lanes.add(new Lane(this,600,360));
    	lanes.add(new Lane(this,600,430)); 
    	lanes.add(new Lane(this,600,500));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 700;
    	this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		
		try {
			beltImage = ImageIO.read(new File("images/LaneGraphic.png"));
		} catch(IOException e) {System.out.println("Didn't read in image");}
		System.out.println("Read");
		
    }
    
  // public static void main(String[] args ) {
	//	LaneGraphics b = new LaneGraphics();
	//}
    
    public void paint(Graphics g) { //put belt here!
=======
    	lanes.add(new Lane(this,600,0)); //MUST SPACE EACH LANE BY 200 PIXELS OR ELSE!
    	lanes.add(new Lane(this,500,200)); 
    	lanes.add(new Lane(this,400,400)); 
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 600;
    	this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );	
    }
    
   public static void main(String[] args ) {
		LaneGraphics b = new LaneGraphics();
	}
    
    public void paint(Graphics g) {
>>>>>>> 95604875d141e759978497033539fe2099587533
    	Graphics2D g2 = (Graphics2D)g;
		counter++;
		g2.fill( backgroundRectangle );
		g2.setColor( Color.RED );
		
<<<<<<< HEAD
		///for (int i = 0; i < 5; i++) {
		//	g2.drawImage(beltImage, i*20, 50, null);
		//}
		
		
		
		
		for (int i = 0; i < lanes.size(); i++) {
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++) {
				g2.fill(new Ellipse2D.Double(lanes.get(i).getItemList().get(j).getX(),lanes.get(i).getItemList().get(j).getY(),20,20));
						
						
						//lanes.get(i).getItemList().get(j));
			}
			for (int k = 0; k < lanes.get(i).getQueueList().size(); k++) {
				//g2.fill(lanes.get(i).getQueueList().get(k));
			}

=======
		for (int i = 0; i < lanes.size(); i++) {
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++) {
				g2.fill(lanes.get(i).getItemList().get(j));
			}
			for (int k = 0; k < lanes.get(i).getQueueList().size(); k++) {
				g2.fill(lanes.get(i).getQueueList().get(k));
			}
			
>>>>>>> 95604875d141e759978497033539fe2099587533
		}
    }
    
    public void setVibration() { //Unimplimented
    	System.out.println("Unimplemented");
    }
  
<<<<<<< HEAD
    
    public void releaseItem(int lane) {
    	lanes.get(lane).releasePart();
    }
    
=======
>>>>>>> 95604875d141e759978497033539fe2099587533
}  
