package productionManager;

import fire.*;
import DecorateAnimations.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import server.KitAssemblyManager;
import server.KitRobot;
import server.Lane;
import server.PartsRobot;
import kitAssemblyManager.*;
import laneManager.*;
import Feeder.Feeder;
import GantryManager.*;

public class GUIProductionManager extends JPanel{		//The animation panel for the production manager. 
	
	ProductionManagerApp app;					//A reference to the production manager
	GUIKitAssemblyManager kamGraphics;			//reference to the kit assembly graphics
	LaneGraphics laneGraphics;					//reference to the lane graphics
	GUIGantryManager gantryGraphics;			//reference to the gantry graphics
	BufferedImage background = null;			//image of the background of the factory. 
	
	KittenInFactory ks;                         //panel for kittens sneaking around
	Image catGIF;
	Image catGIF2;
	boolean catGIFOn;
	JLabel label;
	JPanel base;                                //base of those factory panels
	
	private Vector<Lane> lanes = new Vector<Lane> ();			//Collection of the lanes	
	private Vector<Feeder> feeders = new Vector<Feeder>();		//Collection of the Feeders
	private CopyOnWriteArrayList<Nest> nests = new CopyOnWriteArrayList<Nest>();			//Coolection of the nests
    KitAssemblyManager kam = new KitAssemblyManager(nests);		//Reference to the kit assembly manager
    KitRobot kitRobot;				//Reference to the kit robot
    PartsRobot partsRobot;			//reference to the parts robot
    GantryManager manager;			//reference to the gantry manager 
    boolean fireOn = false;			//Boolean to set whether or not the non-normative should paint. 
    Fire fire;						//Fire for the non-normative. 
    GUIProductionClient client;		//Client to communicate with the server. 
	int gifCounter;
    
    
    //constructor for the animations for the production manager
	public GUIProductionManager(ProductionManagerApp _app){
		app = _app;
		
		client = new GUIProductionClient(this);
		int j = client.connect();
		if(j == -1){
			System.exit(1);
		}
		else if(j == 1){
			client.getThread().start();
		}
		
		
		kamGraphics = new GUIKitAssemblyManager(2);
		kamGraphics.setOpaque(false);
		laneGraphics = new LaneGraphics(2);
		laneGraphics.setOpaque(false);
		gantryGraphics = new GUIGantryManager(2);
		gantryGraphics.setOpaque(false);
		base = new JPanel();
		
		base.add(kamGraphics);
		base.add(laneGraphics);
		base.add(gantryGraphics);
		 try {
            background = ImageIO.read(new File("images/background1.png"));
            catGIF = Toolkit.getDefaultToolkit().createImage("images/KittyGIF.gif");
            catGIF2 = Toolkit.getDefaultToolkit().createImage("images/movingcat.gif");
            
        	System.out.println("Kitty load!");
            //image = Toolkit.getDefaultToolkit().createImage("e:/java/spin.gif");
            //label = new JLabel(new ImageIcon(image));

        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println("Kitty no load :(");
        }
		
		ks = new KittenInFactory();
		add(ks);
		
		add(base);
		
	}
	
	//function to start the fire non-normative
	public void burnItDown(){
		fire = new Fire(1200, 600);
		add(fire);
		fireOn = true;
		catGIFOn = true;
		
	}
	
	//function to stop the fire non-normative. 
	public void clearFire(){
		fireOn = false;
		remove(fire);
		fire = null;
	}
	
	//update function for all the different sections. 
	public void update(){
		client.updateThread();
		kamGraphics.setKitAssemblyManager(kam);
		kamGraphics.setKitRobot(kitRobot);
		kamGraphics.setPartsRobot(partsRobot);
		kamGraphics.update();
		laneGraphics.setFeeders(feeders);
		laneGraphics.setNests(nests);
		laneGraphics.setLanes(lanes);
		laneGraphics.update();
		gantryGraphics.setGantryManager(manager);
		ks.update();
		if(fireOn){
			fire.update();
			
		}
	}
	
	//paint function. 
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
		g.translate(320, 0);
		laneGraphics.paintComponent(g);
		g.translate(-320, 0);
		kamGraphics.paintComponent(g);
		g.translate(830, 0);
		gantryGraphics.paintComponent(g);
		g.translate(-830, 0);
		ks.paintComponent(g);
		if(fireOn){
			fire.paintFire(g2);
			
		}

		
		if(catGIFOn) {
			g2.drawImage(catGIF, 1000, 0, this);
			
			if(catGIF2 != null && gifCounter < 15)
				g2.drawImage(catGIF2, 0, 0, this);
			else catGIF2 = null;
				
			if(gifCounter % 68 == 0) {
				catGIF = Toolkit.getDefaultToolkit().createImage("images/KittyGIF.gif");
			}
			
			//if(gifCounter % 100 == 0) {
			//	catGIF2 = Toolkit.getDefaultToolkit().createImage("images/movingcat.gif");   	
			//s}
			gifCounter++;
		}
		
		
		
		
		g.dispose();	
	}
	
	public synchronized void setLanes(Vector<Lane> _lanes) {
		this.lanes = _lanes;
	}
	
	public synchronized void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public synchronized void setNests(CopyOnWriteArrayList<Nest> nests) {
		this.nests = nests;
	}
	
	public void setKitAssemblyManager(KitAssemblyManager kitAssemblyManager) {
		kam = kitAssemblyManager;
	}

	public void setKitRobot(KitRobot kitRobot) {
		this.kitRobot = kitRobot;
	}

	public void setCatGIFs(boolean b) {
		catGIFOn = b;
	}
	
	public void setPartsRobot(PartsRobot partsRobot) {
		this.partsRobot = partsRobot;
	}
	
	public KitAssemblyManager getKitAssemblyManager() {
		return kam;
	}
	public void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}

	public synchronized KittenInFactory getKs() {
		return ks;
	}

	public synchronized void setKs(KittenInFactory ks) {
		this.ks = ks;
	}
	
//	public void actionPerformed(ActionEvent e){
//		repaint();
//	}
}
