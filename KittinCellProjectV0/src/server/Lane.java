package server;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import data.Part;
import Feeder.Feeder;
import laneManager.Nest;

public class Lane implements ActionListener, Serializable{
    private Vector<Part> importList;  //Item Collection that is imported
	private Vector<Part> itemList;  //items moving down 
    private Vector<Part> queueList; 
    private Rectangle2D.Double backgroundRectangle;
    private int conveyerBeltSpeed;
    private int maxX;
    private int maxY;
    private int verticalSpacing;
    private int queueBorder = 120;
    private boolean queueFull; 
    private boolean openGate;
    private boolean isVibrating;
    private int vibrationCounter;
    private Feeder feeder;
    private boolean release = false;
    private int releaseCount = 0;
    private Nest nest;
    private boolean atQueue = false;
    private int gateCounter;
    
    public class Gate implements Serializable{
    	public double topNodeX, topNodeY, bottomNodeX, bottomNodeY;
    	public void setNodes(double bottomNodeX, double bottomNodeY, double topNodeX, double topNodeY) {
    		this.topNodeX = topNodeX;
    		this.topNodeY = topNodeY;
    		this.bottomNodeX = bottomNodeX;
    		this.bottomNodeY = bottomNodeY;
    	}
    	public void setNodes(double bottomNodeX, double bottomNodeY) {
    		this.bottomNodeX = bottomNodeX;
    		this.bottomNodeY = bottomNodeY;
    	}
		public double getTopNodeX() {
			return topNodeX;
		}
		public double getTopNodeY() {
			return topNodeY;
		}
		public double getBottomNodeX() {
			return bottomNodeX;
		}
		public double getBottomNodeY() {
			return bottomNodeY;
		}
		
		public void setDefaultPosition() {
			this.setNodes(105, verticalSpacing + 20, 105, verticalSpacing);
		}	
    }
    
    public Gate gate1;
    public Gate gate2;
    
    private Lane() {
    	this.gateCounter = 0;
    	this.maxX = 600;
		this.maxY = 30;
    	this.verticalSpacing = 0;
	    this.conveyerBeltSpeed = 1;
	    this.itemList = new Vector<Part> ();
	    this.importList = new Vector<Part> ();
	    this.queueList = new Vector<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		this.queueFull = false;
		this.openGate = false;
	 }
    
    public Lane(int width, int verticalSpacing, Nest n) {
    	this.nest = n;
		maxX = width;
		maxY = 30;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 1;
	    itemList = new Vector<Part> ();
	    importList = new Vector<Part> ();
	    queueList = new Vector<Part> ();
		backgroundRectangle = new Rectangle2D.Double(0, 0, maxX, maxY );
	    itemList = new Vector<Part> ();
	    importList = new Vector<Part> ();
	    queueList = new Vector<Part> ();
		backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		gate1 = new Gate();
		gate1.setDefaultPosition();
		gate2 = new Gate();
		gate2.setNodes(105, verticalSpacing + 30, 105, verticalSpacing + 50);
		queueFull = false;	
		isVibrating = false;
		this.vibrationCounter = 0;
	    for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
    }
    
    public Lane(int width, int verticalSpacing, Nest n, Feeder f) {
		maxX = width;
		maxY = 50;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 1;
	    itemList = new Vector<Part> ();
	    importList = new Vector<Part> ();
	    queueList = new Vector<Part> ();
		backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		gate1 = new Gate();
		gate1.setDefaultPosition();
		gate2 = new Gate();
		gate2.setNodes(105, verticalSpacing + 30, 105, verticalSpacing + 50);
		queueFull = false;			
		for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
		nest = n;
	    feeder = f;
    }
    
    public void actionPerformed( ActionEvent ae ) {	
	    if(itemList.size() > 0) {
	    	for(int i = 0; i < itemList.size(); i++) { 
		    	if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
		    		itemList.get(i).setX(itemList.get(i).getX() - conveyerBeltSpeed);
		    	}	    	
		    	else {
		    		itemList.get(i).setDestination(true);
		    		atQueue = true;
		    	}
	    	}
	    } 
	    
	    if(openGate == true && gateCounter < 37) {
	    	if(gateCounter < 18) { //opengate
	    		gate1.setNodes(105, verticalSpacing + 20 - gateCounter);
	    		gate2.setNodes(105,verticalSpacing + 30 + gateCounter);
	    		gateCounter++;
	    		//finishes at 100,vert + 31
	    	}	
	    	else if(gateCounter < 36 && gateCounter > 17) {
	    		gate1.setNodes(105, verticalSpacing + gateCounter - 17);
	    		gate2.setNodes(105,verticalSpacing + 65 - gateCounter);
	    		gateCounter++;
	    	}
	    		
	    	else if(gateCounter == 36) {
	    		gateCounter = 0;
	    		openGate = false;
	    	}	
	    }
	    
	    //Vibrates all parts in Lane along Y axis using Sine Function with Amplitude of 10 pixels.
	    if(isVibrating == true && vibrationCounter < 50) {
		    vibrationCounter++;
			if(vibrationCounter < 49) {    
		    	if(itemList.size() > 0) {
			    	for(int i = 0; i < itemList.size(); i++) { 
				    	if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
				    		itemList.get(i).setY(itemList.get(i).getY() - 10 * Math.sin(50*Math.PI*vibrationCounter));
				    	}
			    	}	
		    	}
			}	
			else {
				vibrationCounter = 0;
				isVibrating = false;	
			}
	    	
	    }
	    //update feeder
	    feeder.updateDiverter();
    }

	public void addPart(Part part) {
    	part.setX(maxX-80);
    	part.setY(maxY/2 + verticalSpacing - 10);
    	importList.add(part);
    }
    
	//f = whether or not it should release part to top lane or bottom lane. 
    public void releasePart() {
//		if(importList.size() != 0) {
//			Part temp = importList.remove(0);
			Part temp = feeder.removePart();
			temp.setX(maxX-80);
			temp.setY(maxY/2 + verticalSpacing - 10);
			itemList.add(temp);
			System.out.println("release!");
//		}
    }
	
    public void releaseQueue(){
    	if(itemList.size() != 0){
    		if(itemList.get(0).getDestination() == true){
    			nest.addPart(itemList.remove(0));
    			openGate = true;
    			atQueue = false;
    			System.out.println("Rawr!!!!");
    		}
    	}
    	return;
    }
    
    public Gate getGate1() {
    	return this.gate1;
    }
    
    public Gate getGate2() {
    	return this.gate2;
    }
    
    public Vector<Part> getItemList() {
    	return this.itemList;
    }
    
    public Vector<Part> getQueueList() {
    	return this.queueList;
    }
    
    public void setConveyerBeltSpeed(int speed) {
    	this.conveyerBeltSpeed = speed;
    }
      
    public int getConveyerBeltSpeed() {
    	return this.conveyerBeltSpeed;
    }
    
    public boolean isAtQueue() {
		return atQueue;
	}

	public void setAtQueue(boolean atQueue) {
		this.atQueue = atQueue;
	}

	public boolean isRelease() {
		return release;
	}

	public void setRelease(boolean release) {
		this.release = release;
	}
    
    public Feeder getFeeder() {
		return feeder;
	}

	public void setFeeder(Feeder feeder) {
		this.feeder = feeder;
	}

	public int getReleaseCount() {
		return releaseCount;
	}

	public void setReleaseCount(int releaseCount) {
		this.releaseCount = releaseCount;
	}
    
    
    public void vibrateLane() {
    	if(itemList.size() > 0) {
    		isVibrating = true;
    	}
    	
    }
    
}  
