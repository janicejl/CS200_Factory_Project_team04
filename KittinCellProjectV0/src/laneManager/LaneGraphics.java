package laneManager;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class LaneGraphics extends JFrame /*implements ActionListener*/ {
	private ArrayList<Lane> lanes = new ArrayList<Lane> ();
	private int maxX;
	private int maxY;
	private Rectangle2D.Double backgroundRectangle;
	private int counter;
	//private Timer timer; 
    
    public LaneGraphics() {
    	counter = 0;
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
    	Graphics2D g2 = (Graphics2D)g;
		counter++;
		g2.fill( backgroundRectangle );
		g2.setColor( Color.RED );
		
		for (int i = 0; i < lanes.size(); i++) {
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++) {
				g2.fill(lanes.get(i).getItemList().get(j));
			}
			for (int k = 0; k < lanes.get(i).getQueueList().size(); k++) {
				g2.fill(lanes.get(i).getQueueList().get(k));
			}
			
		}
    }
    
    public void setVibration() { //Unimplimented
    	System.out.println("Unimplemented");
    }
  
}  
