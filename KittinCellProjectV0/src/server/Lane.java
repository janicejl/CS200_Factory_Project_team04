package server;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import data.Part;
import Feeder.Feeder;
import laneManager.Nest;

public class Lane implements ActionListener, Serializable{
    private ArrayList<Part> importList;  //Item Collection that is imported
	private ArrayList<Part> itemList;  //items moving down 
    private ArrayList<Part> queueList; 
    private Rectangle2D.Double backgroundRectangle;
    private int conveyerBeltSpeed;
    private int maxX;
    private int maxY;
    private int verticalSpacing;
    private int queueBorder = 120;
    private boolean queueFull; 
    private boolean openGate;
    private Feeder feeder;
    private Nest n;
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
    	
    	
    }
    
    public Gate gate;

    private Lane() {
    	this.gateCounter = 0;
    	this.maxX = 600;
		this.maxY = 30;
    	this.verticalSpacing = 0;
	    this.conveyerBeltSpeed = 1;
	    this.itemList = new ArrayList<Part> ();
	    this.importList = new ArrayList<Part> ();
	    this.queueList = new ArrayList<Part> ();
		this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		this.queueFull = false;
		this.openGate = false;
	 }
    
    public Lane(int width, int verticalSpacing, Nest n) {
    	this.n = n;
		maxX = width;
		maxY = 30;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 1;
	    itemList = new ArrayList<Part> ();
	    importList = new ArrayList<Part> ();
	    queueList = new ArrayList<Part> ();
		backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		//nestFull = false;
		gate = new Gate();
		gate.setNodes(80, verticalSpacing + 20, 100, verticalSpacing + 5);
		queueFull = false;		
		/*importList.add(new Part("1"));
		importList.add(new Part("2"));
	    importList.add(new Part("3"));*/
	    for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
    }
    
    
    public Lane(int width, int verticalSpacing, Feeder f) {

		maxX = width;
		maxY = 50;
    	this.verticalSpacing = verticalSpacing;
	    conveyerBeltSpeed = 1;
	    itemList = new ArrayList<Part> ();
	    importList = new ArrayList<Part> ();
	    queueList = new ArrayList<Part> ();
		backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
		queueFull = false;		
		for(int i = 0; i < importList.size(); i++) {
	    	importList.get(i).setX(width-80);
	    	importList.get(i).setY(maxY/2 + verticalSpacing);
	    }
	    
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
		    	}
	    	}
	    } 
	    
	    if(openGate == true && gateCounter < 50) {
	    	if(gateCounter < 25) { //opengate
	    		gate.setNodes(40 - gateCounter*0.25, verticalSpacing + 20 - gateCounter*0.25);
	    		System.out.println("Opening (" +  (40 - gateCounter*0.25) + ", " + (verticalSpacing + 20 - gateCounter*0.25) + ")");
	    		gateCounter++;
	    	}	
	    	else if(gateCounter < 50 && gateCounter > 24) {
	    		gate.setNodes(40 + gateCounter*0.25, verticalSpacing + 20 + gateCounter*0.25);
	    		System.out.println("Closing (" +  (40 + gateCounter*0.25) + ", " + (verticalSpacing + 20 + gateCounter*0.25) + ")");
	    		gateCounter++;
	    	}
	    		
	    	else if(gateCounter == 50) {
	    		gate.setNodes(40 , verticalSpacing, 100, verticalSpacing + 5);
	    		System.out.println("Gate done");
	    		gateCounter = 0;
	    		openGate = false;
	    	}	
	    }
    }
	    
    public Gate getGate() {
    	return this.gate;
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
    		Part temp = importList.remove(0);
    		temp.setY(maxY/2 + verticalSpacing);
			itemList.add(temp);
			System.out.println("release!");
		}
    }
    
    public void addPart(Part part) {
    	part.setX(maxX-80);
    	part.setY(maxY/2 + verticalSpacing);
    	importList.add(part);
    }
    
    public void releaseQueue(){
    	if(itemList.size() != 0){
    		if(itemList.get(0).getDestination() == true){
    			n.addPart(itemList.remove(0));
    			openGate = true;	
    		}
    	}
    	System.out.println("Rawr!!!!");
    	return;
    }
    
}  
