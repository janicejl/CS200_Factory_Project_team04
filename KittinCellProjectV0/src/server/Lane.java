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
import java.util.*;

public class Lane implements ActionListener, Serializable{
    private Vector<Part> importList;  //Item Collection that is imported
	private Vector<Part> itemList;  //items moving down 
    private Vector<Part> queueList; 
    private Rectangle2D.Double backgroundRectangle;
    private int conveyerBeltSpeed;			//Speed of the conveyor. 
    private int maxX;				//Lane horizontal size
    private int maxY;				//Lane vertical size
    private int verticalSpacing;	//vertical spacing between the lanes. 
    private int queueBorder = 120;
    private boolean queueFull; 		//Boolean state for whether the lane is full
    private boolean openGate;		//Boolean state for whether the gate should open to let a part into the nest
    private boolean isVibrating;	//Boolean state for whether the lanes should vibrarte. 
    private Feeder feeder;			//Feeder attached to the lane
    private boolean release = false;
    private int releaseCount = 0;
    private Nest nest;				//reference to the nest attached to the lane
    private boolean atQueue = false;		//Boolean for queuing 
    private int gateCounter;				//gate number
    private int vibrationAmplitude; 		//For vibration speed
    private int jamProbability;				//Probability of a lane being jammed for non-normative. 
    private int jamIndex;					//Index of the part that gets stuck
    private int jammedBorder;				//the coordinate position of where the lane jams
    private boolean jamOn = false;			//Boolean state for whether or not the lane can jam for non-normative. 
    private boolean isJammed;				//Boolean state for whether or not the lane is jammed. 
    private Random random;
    private boolean jamMessage = false;			//Boolean state for whether or not to send the jammed message
    private boolean unjamMessage = false;		//Boolean state for whether or not to send the unjammed message
    private boolean purging = false;			//Boolean state for purging. 
    
    //Class for the gates.
    public class Gate implements Serializable{
    	public double topNodeX, topNodeY, bottomNodeX, bottomNodeY;		//Coordinates of half the gate position. 
    	//Setting the position of the gate
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
		//gate position for the first lane
		public void setDefaultPosition() {
			this.setNodes(105, verticalSpacing + 20, 105, verticalSpacing);
		}	
    }	//Close the gate class
    
    //Together makes one gate
    public Gate gate1;		//top half of the gate
    public Gate gate2;		//bottom half of the gate
    
    //Constructor for lane
    private Lane() {
    	this.gateCounter = 0;
    	this.maxX = 600;
		this.maxY = 30;
    	this.verticalSpacing = 0;
	    this.conveyerBeltSpeed = 10;
	    this.itemList = new Vector<Part> ();
	    this.importList = new Vector<Part> ();
	    this.queueList = new Vector<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		this.queueFull = false;
		this.openGate = false;
	 }
  //Constructor for lane
    public Lane(int width, int verticalSpacing, Nest n) {
    	System.out.println("Loading constructor 1");
    	this.nest = n;
		maxX = width;
		maxY = 30;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 10;
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
	    for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
	    vibrationAmplitude = 0;
	    jamProbability = 100; //Initialized to 10
	    isJammed = false;
	    random = new Random();
    }
  //Constructor for lane
    public Lane(int width, int verticalSpacing, Nest n, Feeder f) {
    	System.out.println("Loading constructor 2");
    	maxX = width;
		maxY = 50;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 10;
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
	    vibrationAmplitude = 0;
	    jamProbability = 1; //one out of ten parts will jam 
	    System.out.println("Jam probability established to 3");
	    isJammed = false;
	    random = new Random();
//	    vibrateLane(1);
    }
    
    public void actionPerformed( ActionEvent ae ) {	
	    if(itemList.size() > 0) {
	    	//set jammed border randomly;
	    	//must be between queueborder + jamIndex*20 and less than max (random)
	    	if(isJammed) {
	    		for(int i = 0; i < jamIndex; i++) {
	    			if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
	    				itemList.get(i).setX(itemList.get(i).getX() - conveyerBeltSpeed);
	    			}
	    			else {
	    				if(purging == true){
	    					releaseQueue();
	    				}
	    				else{
	    					itemList.get(i).setDestination(true);
	    					atQueue = true;
	    				}
	    			}
	    		}
	    		for(int i = jamIndex; i < itemList.size(); i++) {
	    			if(itemList.get(i).getX() > jammedBorder + i*20) { //Moves parts down the line
	    				itemList.get(i).setX(itemList.get(i).getX() - conveyerBeltSpeed);
	    			}
	    			else {
	    				jamMessage = true;
	    			}
	    		}
	    	}
	    	
	    	else {
	    		for(int i = 0; i < itemList.size(); i++) { 
	    			if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
	    				itemList.get(i).setX(itemList.get(i).getX() - conveyerBeltSpeed);
	    			}
	    			else {
	    				if(purging == true){
	    					releaseQueue();
	    				}
	    				else{
	    					itemList.get(i).setDestination(true);
		    				atQueue = true;
	    				}
	    				
	    			}
	    		}	
		    }
	    }
	     //Opening and closing the gate appropriately. 
	    if(openGate == true && gateCounter < 37) {
	    	if(gateCounter < 18) { //opengate
	    		gate1.setNodes(105, verticalSpacing + 20 - gateCounter);
	    		gate2.setNodes(105,verticalSpacing + 30 + gateCounter);
	    		gateCounter++;
	    		//finishes at 100,vert + 31
	    	}	
	    	else if(itemList.size() > 0  && nest.getSize() < 8) {
	    		gate1.setNodes(105, verticalSpacing + 19 - gateCounter);
	    		gate2.setNodes(105,verticalSpacing + 29 + gateCounter);
	    		
	    		
	    	}
	    	else if(gateCounter < 36 && gateCounter > 17) { //close gate
	    		gate1.setNodes(105, verticalSpacing + gateCounter - 17);
	    		gate2.setNodes(105,verticalSpacing + 65 - gateCounter);
	    		gateCounter++;
	    	}
	    		
	    	else if(gateCounter == 36) {
	    		gateCounter = 0;
	    		openGate = false;
	    	}	
	    }
	    
	    if(isVibrating == true) { 
	    	for(int i = 0; i < itemList.size(); i++) { 
		    	if(itemList.get(i).getX() > queueBorder + i*20) { //Moves parts down the line
		    		itemList.get(i).setY(itemList.get(i).getY());
		    	}
	    	}
	    	if(isJammed == true){
	    		if(Math.random()*1000 > 995){
	    			setJamOn(false);
	    		}
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
	
	public boolean isVibrating(){
		return isVibrating;
	}
	
	public int getVibrationAmplitude(){
		return vibrationAmplitude;
	}
    
	//f = whether or not it should release part to top lane or bottom lane. 
    public void releasePart() {
			Part temp = feeder.removePart();
			temp.setX(maxX-80);
			temp.setY(maxY/2 + verticalSpacing - 10);
			itemList.add(temp);
			System.out.println("release!");
		
			if(random.nextInt(jamProbability) == 0 && isJammed == false && jamOn == true) {
				jamIndex = itemList.size() - 1;
				isJammed = true;	
				
				jammedBorder = 200 + random.nextInt(100);
				//jammedBorder = random.nextInt(120) (DO ME)
			}
			
			//release queue--
			//check if its zero
			
			
			
    }
	
    public void releaseQueue(){
    	if(itemList.size() != 0){
    		if(itemList.get(0).getDestination() == true){
    			if(purging == true){
    				itemList.remove(0);
    				if(itemList.size() == 0){
    					purging = false;
    				}
    			}
    			else{
    				nest.addPart(itemList.remove(0));
    			}
    			openGate = true;
    			atQueue = false;
    			System.out.println("Rawr!!!!");
    			if(jamIndex>0) jamIndex--;
    		}
    	}
    	return;
    }
    
    public void purge(){
    	purging = true;
    	if(itemList.size() == 0){
    		purging = false;
    	}
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
    
    public void vibrateLane(int amplitude) {
		isVibrating = true;
		vibrationAmplitude = amplitude;
    }
    
    public void setJamProbability(int probability) {
    	if(probability <= 100 && probability >= 0)
    		this.jamProbability = probability;
    	else System.out.println("Insert probability value between 0-100");
    }
    
    public int getJamProbability() {
    	return this.jamProbability;
    }

    public boolean isLaneJammed() {
    	return isJammed;
    }
    
    public boolean getJamMessage() {
    	return jamMessage;
    }

	public void setJamMessage(boolean jamMessage) {
		this.jamMessage = jamMessage;
	}

	public boolean isJamOn() {
		return jamOn;
	}

	public void setJamOn(boolean jamOn) {
		this.jamOn = jamOn;
		if(jamOn == false){
			isJammed = false;
			jamMessage = false;
			unjamMessage = true;
		}		
	}

	public boolean isUnjamMessage() {
		return unjamMessage;
	}

	public void setUnjamMessage(boolean unjamMessage) {
		this.unjamMessage = unjamMessage;
	}

	public boolean isJammed() {
		return isJammed;
	}

	public void setJammed(boolean isJammed) {
		this.isJammed = isJammed;
	}

	public boolean isPurging() {
		return purging;
	}

	public void setPurging(boolean purging) {
		this.purging = purging;
	}
    
}  
