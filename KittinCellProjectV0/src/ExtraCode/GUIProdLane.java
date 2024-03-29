//package ExtraCode;
//
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.event.ActionEvent;
//import java.awt.geom.Rectangle2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.Vector;
//
//import javax.imageio.ImageIO;
//
//import laneManager.GUINest;
//import laneManager.LaneManagerClient;
//import laneManager.Nest;
//import server.Lane;
//import Feeder.Feeder;
//import Feeder.GUIFeeder;
//import data.GUIPart;
//import data.Part;
//
//public class GUIProdLane {
//	private Vector<Lane> lanes = new Vector<Lane> ();
//	private int maxX;
//	private int maxY;
//	private Rectangle2D.Double backgroundRectangle;
//	private Vector<Boolean> emptyConveyorOnList;
//	private Vector<Double> emptyConveyorMoveList;
//	private BufferedImage conveyorImage; 
//	private Vector<GUIFeeder> gFeeders;
//	private Vector<Feeder> feeders = new Vector<Feeder>();
//	
//	private Vector<GUINest> gNests = new Vector<GUINest>();
//	private Vector<Nest> nests = new Vector<Nest>();
//	private LaneManagerClient client;
//	private javax.swing.Timer timer;
//	
//    public GUIProdLane() {
////    	client = new LaneManagerClient(this);
//    	
//
//		int j = client.connect();
//		if(j == -1){
//			System.exit(1);
//		}
//		else if(j == 1){
//			client.getThread().start();
//		}
//	
//    	
//    	
//    	for (int i = 0; i < 8; i++) {
//    		nests.add(new Nest(0, 30+(i*70)));
//    	}
//    	
//    	lanes.add(new Lane(600,-10, nests.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
//    	lanes.add(new Lane(600,60, nests.get(1))); 
//    	lanes.add(new Lane(600,120, nests.get(2))); 
//    	lanes.add(new Lane(600,170, nests.get(3)));
//    	lanes.add(new Lane(600,230, nests.get(4))); 
//    	lanes.add(new Lane(600,290, nests.get(5)));
//    	lanes.add(new Lane(600,350, nests.get(6))); 
//    	lanes.add(new Lane(600,410, nests.get(7)));
//    	lanes.get(1).setConveyerBeltSpeed(4);
//    	lanes.get(2).setConveyerBeltSpeed(3);
//    	maxX = 600;
//    	maxY = 600;
//    	
//    	
//    	this.backgroundRectangle = new Rectangle2D.Double( 0, 0, maxX, maxY );
////    	this.setSize(maxX, maxY);
////    	this.setVisible(true);
//		
//    	emptyConveyorOnList  = new Vector<Boolean>(); 
//    	emptyConveyorMoveList = new Vector<Double> ();
//    	gFeeders = new Vector<GUIFeeder> ();
//    	
//    	for(int i = 0; i < 8; i++) {
//    		emptyConveyorOnList.add(true);
//    		emptyConveyorMoveList.add(0.0);	
//    	}
//    	
//    	for (int i = 0; i < 4; i++) {
//    		feeders.add(new Feeder(406,30 + i*140));
//    		gFeeders.add(new GUIFeeder(feeders.get(i)));
//    	}
//    	
//    	for (int i = 0; i < 8; i++) {
//    		nests.add(new Nest(0, 30+(i*70)));
//    		gNests.add(new GUINest(nests.get(i)));
//    	}
//		
//		try {
//            conveyorImage = ImageIO.read(new File("images/conveyerLong.png"));
//        } catch (IOException e) {
//        	System.out.println("Image load issue");
//        }	
//    }
//    
//    public void updateGUIFeeders(){
//    	for(int i = 0; i < 4; i++){
//    		gFeeders.get(i).setFeeder(feeders.get(i));
//    	}
//    }
//    
//    public void updateGUINests() {
//    	for (int i = 0; i < 8; i++) {
//    		gNests.get(i).setNest(nests.get(i));
//    	}
//    }
//    
//    public void paintComponent(Graphics g) {
//    	Graphics2D g2 = (Graphics2D)g;
//		g2.setColor(Color.LIGHT_GRAY);
//		g2.fill( backgroundRectangle );
//		for (int i = 0; i < 8; i++) {
//			 if(emptyConveyorOnList.get(i)){
//		            emptyConveyorMoveList.set(i,emptyConveyorMoveList.get(i) + 0.23 * lanes.get(i).getConveyerBeltSpeed()); //magic ratio
//		            if(emptyConveyorMoveList.get(i) > 20.0){
//		                emptyConveyorMoveList.set(i, 0.0);
//		            }
//		        }
//		}
//		
//		//window needs to be 600
//		
//        for(int i = -1; i < 40; i++){ // main conveyor images
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(0).intValue(),30,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(1).intValue(),100,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(2).intValue(),170,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(3).intValue(),240,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(4).intValue(),310,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(5).intValue(),380,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(6).intValue(),450,null); // empty conveyor   
//        	g2.drawImage(conveyorImage, i * 20 - emptyConveyorMoveList.get(7).intValue(),520,null); // empty conveyor   
//        }  
//        
//        g2.setColor(Color.BLUE);		
//        updateGUIFeeders();
//		for (int i = 0; i < lanes.size(); i++) {
//			if(i < 4) //only four nests
//				gFeeders.get(i).paintNest(g2);
//			Vector<GUIPart> guiPart = new Vector<GUIPart>();
//			
//			for (int j = 0; j < lanes.get(i).getItemList().size(); j++){
//				guiPart.add(new GUIPart(lanes.get(i).getItemList().get(j)));
//				guiPart.get(j).paintPart(g2);
//				//g2.drawImage(p, (int)lanes.get(i).getItemList().get(j).getX(), (int)lanes.get(i).getItemList().get(j).getY(), null);
//			}
//				//g2.fill(new Ellipse2D.Double(lanes.get(i).getItemList().get(j).getX(),lanes.get(i).getItemList().get(j).getY(),20,20));
//		}
//		
//		updateGUINests();
//		for (int i = 0; i < gNests.size(); i++) {
//			gNests.get(i).paintNest(g2, 1);
//		}
//    }
//    
//    public void setVibration() { //Unimplimented
//    	System.out.println("Unimplemented");
//    }
//    
//  /*  public void feedItem(int lane){
//    	lanes.get(lane).addPart(new Part("" + lane, "images/kt" + lane + ".png"));
//    }*/
//      
//    public void releaseItem(int lane) {
//    	lanes.get(lane).releasePart();
//    }
//    
//    public void removeItem(int lane){
//    	lanes.get(lane).releaseQueue();
//    }
//    
//    public synchronized void setLanes(Vector<Lane> _lanes) {
//		this.lanes = _lanes;
//	}
//    
//	public synchronized Vector<Lane> getLanes() {
//		return lanes;
//	}
//
//	public synchronized Vector<Feeder> getFeeders() {
//		return feeders;
//	}
//
//	public synchronized void setFeeders(Vector<Feeder> feeders) {
//		this.feeders = feeders;
//		updateGUIFeeders();
//	}
//	
//	public synchronized Vector<Nest> getNests() {
//		return nests;
//	}
//	
//	public synchronized void setNests(Vector<Nest> nests) {
//		this.nests = nests;
//		updateGUINests();
//	}
//
//	public void addPartToLane(int lane, Part part) {
//    	lanes.get(lane).addPart(part);
//    }
//	
//	public void actionPerformed(ActionEvent e){
////		if(e.getSource() == timer){
////			client.updateThread();
////			repaint();
////		}
//	}
//}
