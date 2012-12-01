package productionManager;

import fire.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

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
	
	private Vector<Lane> lanes = new Vector<Lane> ();			//Collection of the lanes	
	private Vector<Feeder> feeders = new Vector<Feeder>();		//Collection of the Feeders
	private Vector<Nest> nests = new Vector<Nest>();			//Coolection of the nests
    KitAssemblyManager kam = new KitAssemblyManager(nests);		//Reference to the kit assembly manager
    KitRobot kitRobot;				//Reference to the kit robot
    PartsRobot partsRobot;			//reference to the parts robot
    GantryManager manager;			//reference to the gantry manager 
    boolean fireOn = false;			//Boolean to set whether or not the non-normative should paint. 
    Fire fire;						//Fire for the non-normative. 
    GUIProductionClient client;		//Client to communicate with the server. 
	
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
		add(kamGraphics);
		add(laneGraphics);
		add(gantryGraphics);
		 try {
            background = ImageIO.read(new File("images/background1.png"));

        } catch (IOException e) {
        	e.printStackTrace();
        }
		
//		timer = new Timer(10, this);
//		timer.start();
	}
	
	//function to start the fire non-normative
	public void burnItDown(){
		fire = new Fire(1200, 600);
		add(fire);
		fireOn = true;
	}
	
	//function to stop the fire non-normative. 
	public void clearFire(){
		fireOn = false;
		remove(fire);
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
		if(fireOn){
			fire.paintFire(g2);
		}
		g.dispose();		
	}
	
	public synchronized void setLanes(Vector<Lane> _lanes) {
		this.lanes = _lanes;
	}
	
	public synchronized void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public synchronized void setNests(Vector<Nest> nests) {
		this.nests = nests;
	}
	
	public void setKitAssemblyManager(KitAssemblyManager kitAssemblyManager) {
		kam = kitAssemblyManager;
	}

	public void setKitRobot(KitRobot kitRobot) {
		this.kitRobot = kitRobot;
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
	
//	public void actionPerformed(ActionEvent e){
//		repaint();
//	}
}
