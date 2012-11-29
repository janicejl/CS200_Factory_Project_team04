package productionManager;

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

public class GUIProductionManager extends JPanel{
	
	ProductionManagerApp app;
	GUIKitAssemblyManager kamGraphics;
	LaneGraphics laneGraphics;
	GUIGantryManager gantryGraphics;
	BufferedImage background = null;
	
	private Vector<Lane> lanes = new Vector<Lane> ();
	private Vector<Feeder> feeders = new Vector<Feeder>();
	private Vector<Nest> nests = new Vector<Nest>();
    KitAssemblyManager kam = new KitAssemblyManager(nests);
    KitRobot kitRobot;
    PartsRobot partsRobot;
    GantryManager manager;
    
    GUIProductionClient client;
	
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
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
		g.translate(320, 0);
		laneGraphics.paintComponent(g);
		g.translate(-320, 0);
		kamGraphics.paintComponent(g);
		g.translate(830, 0);
		gantryGraphics.paintComponent(g);
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
