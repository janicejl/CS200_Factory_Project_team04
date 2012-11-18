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

public class LaneGraphics extends JPanel implements ActionListener {
	private ArrayList<Lane> lanes = new ArrayList<Lane> ();
	private int maxX;
	private int maxY;
	private ArrayList<Boolean> emptyConveyorOnList;
	private ArrayList<Double> emptyConveyorMoveList;
	private BufferedImage conveyorImage; 
	private BufferedImage background;
	private ArrayList<GUIFeeder> gFeeders;
	private ArrayList<Feeder> feeders = new ArrayList<Feeder>();
	private ArrayList<GUINest> gNests = new ArrayList<GUINest>();
	private ArrayList<Nest> nests = new ArrayList<Nest>();
	private LaneManagerClient client;
	private javax.swing.Timer timer;
	int managerNum;
	
    public LaneGraphics(int m) {
    	managerNum = m;
    	client = new LaneManagerClient(this);
    	
		int j = client.connect();
		if(j == -1){
			System.exit(1);
		}
		else if(j == 1){
			client.getThread().start();
		}
	
		for (int i = 0; i < 8; i++) {
    		nests.add(new Nest(0, 30+(i*70)));
    	}
    	
    	lanes.add(new Lane(600,-10, nests.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,60, nests.get(1))); 
    	lanes.add(new Lane(600,120, nests.get(2))); 
    	lanes.add(new Lane(600,170, nests.get(3)));
    	lanes.add(new Lane(600,230, nests.get(4))); 
    	lanes.add(new Lane(600,290, nests.get(5)));
    	lanes.add(new Lane(600,350, nests.get(6))); 
    	lanes.add(new Lane(600,410, nests.get(7)));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	maxX = 600;
    	maxY = 600;
    	
    	this.setSize(maxX, maxY);
    	this.setVisible(true);
		emptyConveyorOnList  = new ArrayList<Boolean>(); 
    	emptyConveyorMoveList = new ArrayList<Double> ();
    	gFeeders = new ArrayList<GUIFeeder> ();
    	
    	for(int i = 0; i < 8; i++) {
    		emptyConveyorOnList.add(true);
    		emptyConveyorMoveList.add(0.0);	
    	}
    	
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
		
		try {
            conveyorImage = ImageIO.read(new File("images/conveyerLong.png"));
            background = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {
        	System.out.println("Image load issue");
        }	
		timer = new javax.swing.Timer(10, this);
		timer.start();
    }
    
    public void updateGUIFeeders(){
    	for(int i = 0; i < 4; i++){
    		gFeeders.get(i).setFeeder(feeders.get(i));
    	}
    }
    
    public void updateGUINests() {
    	for (int i = 0; i < 8; i++) {
    		gNests.get(i).setNest(nests.get(i));
    	}
    }
    
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.LIGHT_GRAY);
		if(managerNum == 1){
			g2.drawImage(background, 0, 0, null);
		}
		for (int i = 0; i < 8; i++) {
			 if(emptyConveyorOnList.get(i)){
		            emptyConveyorMoveList.set(i,emptyConveyorMoveList.get(i) + 0.23 * lanes.get(i).getConveyerBeltSpeed()); //magic ratio
		            if(emptyConveyorMoveList.get(i) > 20.0){
		                emptyConveyorMoveList.set(i, 0.0);
		            }
		        }
		}
		
        for(int i = 1; i < 29; i++){ // main conveyor images
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
        updateGUIFeeders();
		for (int i = 0; i < lanes.size(); i++) {
			if(i < 4) //only four nests
				gFeeders.get(i).paintNest(g2);
			ArrayList<GUIPart> guiPart = new ArrayList<GUIPart>();
			
			for (int j = 0; j < lanes.get(i).getItemList().size(); j++){
				guiPart.add(new GUIPart(lanes.get(i).getItemList().get(j)));
				guiPart.get(j).paintPart(g2);
			}
		}
		
		updateGUINests();
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
    
    public synchronized void setLanes(ArrayList<Lane> _lanes) {
		this.lanes = _lanes;
	}
    
	public synchronized ArrayList<Lane> getLanes() {
		return lanes;
	}

	public synchronized ArrayList<Feeder> getFeeders() {
		return feeders;
	}

	public synchronized void setFeeders(ArrayList<Feeder> feeders) {
		this.feeders = feeders;
		updateGUIFeeders();
	}
	
	public synchronized ArrayList<Nest> getNests() {
		return nests;
	}
	
	public synchronized void setNests(ArrayList<Nest> nests) {
		this.nests = nests;
		updateGUINests();
	}

	public void addPartToLane(int lane, Part part) {
    	lanes.get(lane).addPart(part);
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == timer){
			client.updateThread();
			repaint();
		}
	}
	
    
}
