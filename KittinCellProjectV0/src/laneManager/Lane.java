package laneManager;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import data.Part;

public class Lane implements ActionListener {
    private ArrayList<Part> importList;  //Item Collection that is imported
	private ArrayList<Part> itemList;  //items moving down 
    private ArrayList<Part> queueList; 
    private ArrayList<Part> nestList;  //utilize to dump
    private Rectangle2D.Double backgroundRectangle;
    private int counter;
    private int conveyerBeltSpeed;
    private int releaseSpeed;
    private int maxX;
    private int maxY;
    private int verticalSpacing;
    private int queueBorder = 120;
    private Timer timer; 
    private boolean nestFull;
    private boolean queueFull; //Unimplemented, we need to determine a limit.
    private LaneGraphics laneGraphics;
    private BufferedImage beltImage;  
    private Lane() {
    	this.maxX = 600;
		this.maxY = 100;
    	this.counter = 0;
    	this.verticalSpacing = 0;
	    this.conveyerBeltSpeed = 1;
	    this.releaseSpeed = 10; //wait 50 cycles to release next part
	    this.itemList = new ArrayList<Part> ();
	    this.importList = new ArrayList<Part> ();
	    this.queueList = new ArrayList<Part> ();
	    this.nestList = new ArrayList<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		//this.setSize(maxX, maxY);
		this.timer = new Timer(10,this);
		this.timer.start();
		this.nestFull = false;
		this.queueFull = false;
		//create an import list with 11 parts to test
	    /*this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));
	    this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 20));*/
    }
    
    public Lane(LaneGraphics lg, int width, int verticalSpacing) {
    	this.laneGraphics = lg;
		this.maxX = width;
		this.maxY = 100;
    	this.counter = 0;
    	this.verticalSpacing = verticalSpacing;
	    this.conveyerBeltSpeed = 1;
	    this.releaseSpeed = 50; //wait 50 cycles to release next part
	    this.itemList = new ArrayList<Part> ();
	    this.importList = new ArrayList<Part> ();
	    this.queueList = new ArrayList<Part> ();
	    this.nestList = new ArrayList<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		this.timer = new Timer(10,this);
		this.timer.start();
		this.nestFull = false;
		this.queueFull = false;		
		//create an import list with 3 parts to test
	    this.importList.add(new Part("Part1"));
	    this.importList.add(new Part("Part2"));
	    this.importList.add(new Part("Part3"));
	    for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    	
	    }
	    //this.importList.add(new Ellipse2D.Double(maxX, maxY/2 + verticalSpacing, 20, 15));
    }
    
    public void actionPerformed( ActionEvent ae ) {
    	/*if(counter % releaseSpeed == 0) {  //places parts on the conveyer belt
    		if(importList.size() != 0) {
    			itemList.add(importList.remove(0));
    		}	//Create a button releases
    	}*/
    	
    	
    	counter++;
    	    	
	    if(itemList.size() > 0) {
	    	for(int i = 0; i < itemList.size(); i++) { 
		    	if(itemList.get(i).getX() > 120) { //Moves parts down the line
		    		itemList.get(i).setX(itemList.get(i).getX()-conveyerBeltSpeed);
		    		//itemList.get(i).setY(maxY/2 + verticalSpacing);
		    	}	    	
		    	else {
		    		
		    	//	switch(i) {
		    	//	case 0:
		    			//itemList.get(i).setFrame( itemList.get(i).getX() + queueBorder*i, maxY/2 + verticalSpacing, itemList.get(i).getWidth(), itemList.get(i).getHeight());  //Move 1 x-pixel and 1 y-pixel every 50 milliseconds
				//    	break;
		    	
		    		
		    		
		    		}
		    		
		    		//itemList.get(i).setFrame(queueBorder ,maxY/2, itemList.get(i).getWidth(), itemList.get(i).getHeight());  
		    		//queueBorder+=20;
		    		
		    		
		    		
		    	//}
		    	
		    	/*else { 
		    		switch(i) { //Load into 4X2 matrix
			    	case 0: 
			    		itemList.get(0).setFrame(10 ,5 + verticalSpacing, itemList.get(0).getWidth(), itemList.get(0).getHeight());  
			    		break;
			    	case 1: 
			    		itemList.get(1).setFrame(10 ,26 + verticalSpacing, itemList.get(1).getWidth(), itemList.get(1).getHeight());  
			    		break;
			    	case 2: 
			    		itemList.get(2).setFrame(10 ,47 + verticalSpacing, itemList.get(2).getWidth(), itemList.get(2).getHeight());  
			    		break;
			    	case 3: 
			    		itemList.get(3).setFrame(10 ,68 + verticalSpacing, itemList.get(3).getWidth(), itemList.get(3).getHeight());  
			    		break;
			    	case 4: 
			    		itemList.get(4).setFrame(50 ,5 + verticalSpacing, itemList.get(i).getWidth(), itemList.get(i).getHeight());  
			    		break;
			    	case 5: 
			    		itemList.get(5).setFrame(50 ,26 + verticalSpacing, itemList.get(i).getWidth(), itemList.get(i).getHeight());  
			    		break;	
			    	case 6: 
			    		itemList.get(6).setFrame(50 ,47 + verticalSpacing, itemList.get(i).getWidth(), itemList.get(i).getHeight());  
			    		break;
			    	case 7: 
			    		itemList.get(7).setFrame(50 ,68 + verticalSpacing, itemList.get(i).getWidth(), itemList.get(i).getHeight());
			    		if(nestFull == false) 
			    			nestFull = true;
			    		break;
			    	case 8: 
			    		queueList.add(itemList.remove(8));
			    		break;
			    	}
		    	}*/
		    }
	    }
	    
	    //////
	    
	    
	    if(queueList.size() > 0) {
		    for(int i = 0; i < queueList.size(); i++) {
		    	//queueList.get(i).setFrame(120 + 30*i ,maxY/2 + verticalSpacing, queueList.get(i).getWidth(), queueList.get(i).getHeight());
		    }
	    }
		laneGraphics.repaint();
    } //Queue first, dont nest yet!
    
    public void paint() {
    	
    }

    public ArrayList<Part> getItemList() {
    	return this.itemList;
    }
    
    public ArrayList<Part> getQueueList() {
    	return this.queueList;
    }
    
    public void setConveyerBeltSpeed(int speed) {
    	this.conveyerBeltSpeed = speed;
    }
    
    public void setReleaseSpeed(int speed) {
    	this.releaseSpeed = speed;
    }
  
    //public void setImportList(ArrayList<Ellipse2D.Double> importList) {
    	//this.importList = importList;
    //}
    
    public int getConveyerBeltSpeed() {
    	return this.conveyerBeltSpeed;
    }
    
    public void releasePart() {
    	if(importList.size() != 0) {
			itemList.add(importList.remove(0));
		}
    	System.out.println("release!");
    }
    
    
    public void addPart(Part part) {
    	importList.add(part);
    }
    
}  
