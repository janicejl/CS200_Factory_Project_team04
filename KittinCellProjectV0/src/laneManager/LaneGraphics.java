package laneManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import server.Lane;
import data.GUIPart;
import data.Part;
import Feeder.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//lane animations
public class LaneGraphics extends JPanel {
	private Vector<Lane> lanes = new Vector<Lane> ();		//Collection of all the lanes. 
	private int maxX;				//Maximum horizontal distance for the panel
	private int maxY;				//Maximum vertical distance for the panel
	private ArrayList<Boolean> emptyConveyorOnList;			//Boolean for each lane on whether or not they are on
	private ArrayList<Double> emptyConveyorMoveList;		//Boolean for each lane on their speeds. 
	private BufferedImage conveyorImage; 					//Image for the conveyer. 
	private BufferedImage background;						//Image for the background of the lane animation
	private BufferedImage greenlightImage;					//Image for the green light when the lanes are not jammed
	private BufferedImage redlightImage;					//Image for the red flashing light when the lanes are jammed. 
	private ArrayList<GUIFeeder> gFeeders = new ArrayList<GUIFeeder>();			//Collection of all the graphical feeders. 
	private Vector<Feeder> feeders = new Vector<Feeder>();						//Collection of all the feeders
	private ArrayList<GUINest> gNests = new ArrayList<GUINest>();				//Collection of all the graphical nests
	private CopyOnWriteArrayList<Nest> nests = new CopyOnWriteArrayList<Nest>();							//Collection of all the nests
	
	private javax.swing.Timer timer;
	int managerNum;					//To know where it is being painted. (KAM or LM)
	
	boolean flashUp;				//Boolean for whether or not the red light is flashing up
	boolean flashDown;				//Boolean for whether or not the red light is flashing down
	float opacity;					//Opacity for the flashing red light. 
	
	//Constructor for lane graphics. 
    public LaneGraphics(int m) {
    	managerNum = m;
    	
	
		for (int i = 0; i < 4; i++) {
    		if(i == 0 || i == 3){
    			feeders.add(new Feeder(475,30 + i*140));
    		}
    		else {
    			feeders.add(new Feeder(400,30 + i*140));    			
    		}
    		gFeeders.add(new GUIFeeder(feeders.get(i)));
    	}
    	
    	for (int i = 0; i < 8; i++) {
    		nests.add(new Nest(0, 30+(i*70)));
    		gNests.add(new GUINest(nests.get(i)));
    	}
    	
    	lanes.add(new Lane(600,-10, nests.get(0), feeders.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,60, nests.get(1), feeders.get(0))); 
    	lanes.add(new Lane(600,120, nests.get(2), feeders.get(1))); 
    	lanes.add(new Lane(600,170, nests.get(3), feeders.get(1)));
    	lanes.add(new Lane(600,230, nests.get(4), feeders.get(2))); 
    	lanes.add(new Lane(600,290, nests.get(5), feeders.get(2)));
    	lanes.add(new Lane(600,350, nests.get(6), feeders.get(3))); 
    	lanes.add(new Lane(600,410, nests.get(7), feeders.get(3)));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 600;
    	
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		emptyConveyorOnList  = new ArrayList<Boolean>(); 
    	emptyConveyorMoveList = new ArrayList<Double> ();
    	
    	for(int i = 0; i < 8; i++) {
    		emptyConveyorOnList.add(true);
    		emptyConveyorMoveList.add(0.0);	
    	}
		
		try {
			redlightImage = ImageIO.read(new File("images/redlight.png"));
            greenlightImage = ImageIO.read(new File("images/greenlight.png"));
            conveyorImage = ImageIO.read(new File("images/conveyerLong.png"));
            background = ImageIO.read(new File("images/background1.png"));
        } catch (IOException e) {
        	System.out.println("Image load issue");
        }	
		
		flashUp = true;
		flashDown = false;
		opacity = 0.0f;
    }
    
    //update feeders and nest from the server. 
    public void update(){
    	for(int i = 0; i < 4; i++){
    		gFeeders.get(i).setFeeder(feeders.get(i));
    	}
    	for (int i = 0; i < 8; i++) {
    		gNests.get(i).setNest(nests.get(i));
    	}
    	
    	//increment or decrement the opacity of the flashing red light. 
    	if (flashUp) {
    		opacity = opacity + 0.1f;
    		if (opacity >= 0.9) {
    			flashUp = false;
    			flashDown = true;
    		}
    	} else if (flashDown) {
    		opacity = opacity - 0.1f;
    		if (opacity <= 0.4) {
    			flashDown = false;
    			flashUp = true;
    		}
    	}
    }
    
    //Painting the content of the lane animation.  
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.LIGHT_GRAY);
		if(managerNum == 1){
			g2.drawImage(background, 0, 0, null);
		}
		for (int i = 0; i < 8; i++) {
			 if(emptyConveyorOnList.get(i)){
		            emptyConveyorMoveList.set(i,emptyConveyorMoveList.get(i) + 0.59 * lanes.get(i).getConveyerBeltSpeed()); //magic ratio
		            if(emptyConveyorMoveList.get(i) > 20.0){
		                emptyConveyorMoveList.set(i, 0.0);
		            }
		        }
		}
		
        for(int i = 1; i < 30; i++){ // main conveyor images
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(0).intValue(),30,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(1).intValue(),100,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(2).intValue(),170,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(3).intValue(),240,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(4).intValue(),310,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(5).intValue(),380,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(6).intValue(),450,null); // empty conveyor   
        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(7).intValue(),520,null); // empty conveyor   
        }  
        
        g2.setColor(Color.BLUE);		
        //updateGUIFeeders();
		for (int i = 0; i < lanes.size(); i++) {
			if(i < 4){ //only four nests
				gFeeders.get(i).paintNest(g2);
				if(managerNum == 1){
					gFeeders.get(i).paintCrate(g2);
				}
			}
			ArrayList<GUIPart> guiPart = new ArrayList<GUIPart>();
			
			//For painting the parts vibrating down the lane. 
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++){
				guiPart.add(new GUIPart(lanes.get(i).getItemList().get(j)));
				if(lanes.get(i).isVibrating()){
					
					guiPart.get(j).paintVibratingPart(g2, lanes.get(i).getVibrationAmplitude());
				}
				else {
					guiPart.get(j).paintPart(g2);
				}
			}
		}
		
		//updateGUINests();
		for (int i = 0; i < gNests.size(); i++) {
			gNests.get(i).paintNest(g2, 1);
		}
		
		double x1, y1, x2, y2 = 0;
		double x11, y11, x21, y21 = 0;
		
		//Display gates
		for(int i = 0; i < lanes.size(); i++) {		
			x1 = lanes.get(i).getGate1().getBottomNodeX();
			y1 = lanes.get(i).getGate1().getBottomNodeY();
			x2 = lanes.get(i).getGate1().getTopNodeX();
			y2 = lanes.get(i).getGate1().getTopNodeY();
			
			x11 = lanes.get(i).getGate2().getBottomNodeX();
			y11 = lanes.get(i).getGate2().getBottomNodeY();
			x21 = lanes.get(i).getGate2().getTopNodeX();
			y21 = lanes.get(i).getGate2().getTopNodeY();
			
			Shape l0 = new Line2D.Double(x1,y1,x2,y2);
			Shape l1 = new Line2D.Double(x1+1, y1, x2+1, y2);
			Shape l2 = new Line2D.Double(x1+2, y1, x2+2, y2);
			Shape l3 = new Line2D.Double(x1+3, y1, x2+3, y2);
			
			Shape l01 = new Line2D.Double(x11,y11,x21,y21);
			Shape l11 = new Line2D.Double(x11+1,y11,x21+1,y21);
			Shape l21 = new Line2D.Double(x11+2,y11,x21+2,y21);
			Shape l31 = new Line2D.Double(x11+2,y11,x21+2,y21);
			
			g2.setColor(Color.LIGHT_GRAY);
            
			g2.draw(l0);
			g2.draw(l1);
			g2.draw(l2);
			g2.draw(l3);
			
			g2.draw(l01);
			g2.draw(l11);
			g2.draw(l21);
			g2.draw(l31);
		}
		//Jam Semaphores:
		if(lanes.get(0).isJammed()) { 	//Paint the red flashing lights
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 584, 34, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {						//Paint the green static light
			g2.drawImage(greenlightImage, 584, 34, this);
		}
		if(lanes.get(1).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 584, 138, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 584, 138, this);
		}
		if(lanes.get(2).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 509, 174, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 509, 174, this);
		}
		if(lanes.get(3).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 509, 278, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 509, 278, this);
		}
		if(lanes.get(4).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 509, 314, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 509, 314, this);
		}
		if(lanes.get(5).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 509, 338+80, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 509, 338+80, this);
		}
		if(lanes.get(6).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 584, 454, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 584, 454, this);
		}
		if(lanes.get(7).isJammed()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(redlightImage, 584, 558, this);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		} else {
			g2.drawImage(greenlightImage, 584, 558, this);	
		}
	
    }
    
    public void setVibration() { //Unimplimented
    	System.out.println("Unimplemented");
    }
    
    public void releaseItem(int lane) {
    	lanes.get(lane).releasePart();
    }
    
    public void removeItem(int lane){
    	lanes.get(lane).releaseQueue();
    }
    
    public void setLanes(Vector<Lane> _lanes) {
		this.lanes = _lanes;
	}
    
	public Vector<Lane> getLanes() {
		return lanes;
	}

	public Vector<Feeder> getFeeders() {
		return feeders;
	}

	public void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public CopyOnWriteArrayList<Nest> getNests() {
		return nests;
	}
	
	public void setNests(CopyOnWriteArrayList<Nest> nests) {
		this.nests = nests;
	}

	public void addPartToLane(int lane, Part part) {
    	lanes.get(lane).addPart(part);
    }
}
