package server;
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

import laneManager.LaneGraphics;
import data.Part;

public class Lane implements ActionListener {
    private ArrayList<Part> importList;  //Item Collection that is imported
	private ArrayList<Part> itemList;  //items moving down 
    private ArrayList<Part> queueList; 
    private Rectangle2D.Double backgroundRectangle;
    private int conveyerBeltSpeed;
    private int maxX;
    private int maxY;
    private int verticalSpacing;
    private int queueBorder = 120;
    private Timer timer; 
    private boolean nestFull;
    private boolean queueFull; //Unimplemented, we need to determine a limit.
    
    private Lane() {
    	this.maxX = 600;
		this.maxY = 100;
    	this.verticalSpacing = 0;
	    this.conveyerBeltSpeed = 1;
	    this.itemList = new ArrayList<Part> ();
	    this.importList = new ArrayList<Part> ();
	    this.queueList = new ArrayList<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		this.timer = new Timer(10,this);
		this.timer.start();
		this.nestFull = false;
		this.queueFull = false;
	 }
    
    public Lane(int width, int verticalSpacing) {
    	maxX = width;
		maxY = 100;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 1;
	    itemList = new ArrayList<Part> ();
	    importList = new ArrayList<Part> ();
	    queueList = new ArrayList<Part> ();
		backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		timer = new Timer(10,this);
		timer.start();
		nestFull = false;
		queueFull = false;		
		importList.add(new Part("Part1"));
		importList.add(new Part("Part2"));
	    importList.add(new Part("Part3"));
	    for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
    }
    
    public void actionPerformed( ActionEvent ae ) {	
	    if(itemList.size() > 0) {
	    	for(int i = 0; i < itemList.size(); i++) { 
		    	if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
		    		itemList.get(i).setX(itemList.get(i).getX() - conveyerBeltSpeed);
		    	}	    	
		    	else {
		    	///NEST ALGORITHM HERE!////
		    	}
	    	}
	    } //Queue first, dont nest yet!
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
