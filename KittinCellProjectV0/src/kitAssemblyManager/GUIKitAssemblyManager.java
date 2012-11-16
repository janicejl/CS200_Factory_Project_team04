package kitAssemblyManager;

import server.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;

import server.KitAssemblyManager;
import data.GUIKit;
import data.Kit;
import laneManager.Nest;
import laneManager.GUINest;

import java.awt.image.*;
import java.io.*;

public class GUIKitAssemblyManager extends JPanel{
	KitAssemblyClient kitClient;
//	PartsManagerClient partsClient;
	
    KitAssemblyApp app;
    KitAssemblyManager kam;
    GUIKitRobot gKitRobot;
    GUIPartsRobot gPartsRobot;
    KitRobot kitRobot;
    PartsRobot partsRobot;

    double emptyConveyorMove = 0;
    double finishedConveyorMove = 0;
    double badConveyorMove = 0;
    double incompleteConveyorMove = 0;
    BufferedImage kitImage = null;
    BufferedImage background = null;
    BufferedImage stand = null;
    BufferedImage conveyorImage = null;

    Vector<Boolean> stationOccupied;
    Vector<GUIKit> emptyKits;
    Vector<GUIKit> finishedKits;
    Vector<GUIKit> stationKits;
    Vector<Kit> baseEmptyKits;
    Vector<Kit> baseFinishedKits;
    Vector<Kit> baseStationKits;

    Vector<GUINest> gNests;
    Vector<Nest> nests;
    
    int[] stationPositions = {160, 140, 160, 360, 80, 490, 10, 490, 160, 250}; // stations 1 - 2 - 3 - 4 - 5 (image corner coords)
    boolean emptyConveyorOn;
    boolean finishedConveyorOn;
    boolean badConveyorOn;
    boolean incompleteConveyorOn;
    /*
    id  name    corX  corY
    0   empty   80    10
    1   kit 1   160   140
    2   kit 2   160   360
    3   inc     80    490
    4   bad     10    490
    5   check   160   250
    6   done    10    10
    */
    int managerNum;

    public GUIKitAssemblyManager(int manager){
    	managerNum = manager;
        kitClient = new KitAssemblyClient(this);
//    	partsClient = new PartsManagerClient(this);
    	
    	gNests = new Vector<GUINest>();
    	nests = new Vector<Nest>();
    	
    	kam = new KitAssemblyManager(nests);
    	kitRobot = new KitRobot(kam);
    	partsRobot = new PartsRobot(kam);    	
    	
        setPreferredSize(new Dimension (450,600));
        gKitRobot = new GUIKitRobot(this);
        gPartsRobot = new GUIPartsRobot(this);
        stationOccupied = new Vector<Boolean>();
        emptyKits = new Vector<GUIKit>();
        finishedKits = new Vector<GUIKit>();
        stationKits = new Vector<GUIKit>();
        baseEmptyKits = new Vector<Kit>();
        baseFinishedKits = new Vector<Kit>();
        baseStationKits = new Vector<Kit>();

        int i = kitClient.connect();
		if(i == -1){
			System.exit(1);
		}
		else if(i == 1){
			kitClient.getThread().start();
		}


        try {
            background = ImageIO.read(new File("images/background.png"));
            kitImage = ImageIO.read(new File("images/crate.png"));
            stand = ImageIO.read(new File("images/stand.png"));
            conveyorImage = ImageIO.read(new File("images/conveyor.png"));
        } catch (IOException e) {}
        update();
			
    }

    public void update(){
//    	partsClient.updateThread();
    	kitClient.updateThread();
    	gPartsRobot.update();
        gKitRobot.update();
        emptyConveyorOn = kam.getEmptyConveyorOn();
        finishedConveyorOn = kam.getFinishedConveyorOn();
        baseEmptyKits = kam.getEmptyKits();
        baseFinishedKits = kam.getFinishedKits();
        baseStationKits = kam.getStationKits();
        stationOccupied = kam.getStationOccupied();
        
        //GUINests
        gNests.clear();
        for (Nest n: kam.getNests()) {
        	gNests.add(new GUINest(n));
        }
        //GUIKits
        emptyKits.clear();
        for(Kit k : baseEmptyKits){
        	emptyKits.add(new GUIKit(k));
        }
        stationKits.clear();
        for(Kit k : baseStationKits){
        	stationKits.add(new GUIKit(k));
        }
        finishedKits.clear();
        for(Kit k : baseFinishedKits){
        	finishedKits.add(new GUIKit(k));
        }
        
        if(emptyConveyorOn){
            emptyConveyorMove += 0.5;
            if(emptyConveyorMove > 20.0){
                emptyConveyorMove = 0;
            }
        }

        if(finishedConveyorOn){
            finishedConveyorMove += 0.5;
            if(finishedConveyorMove > 20.0){
                finishedConveyorMove = 0;
            }
        }
        if(badConveyorOn){
            badConveyorMove += 0.5;
            if(badConveyorMove > 20.0){
                badConveyorMove = 0;
            }
        }
        if(incompleteConveyorOn){
            incompleteConveyorMove += 0.5;
            if(incompleteConveyorMove > 20.0){
                incompleteConveyorMove = 0;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if(managerNum == 1){
        	g2.drawImage(background, 0, 0, null);
	        for (GUINest gN : gNests) {
	        	gN.paintNest(g2, 0);
	        }
        }
        g2.drawImage(stand,150,130,70,340,null); // kit stand
        g2.setColor(Color.BLACK);
        
        

        for(int i = -1; i < 5; i++){ // main conveyor images
            g2.drawImage(conveyorImage,75, i * 20 + (int)emptyConveyorMove,null); // empty conveyor
            g2.drawLine(75,20*i+(int)emptyConveyorMove,134,20*i+(int)emptyConveyorMove);

            g2.drawImage(conveyorImage,5,(i+1)*20 - (int)finishedConveyorMove,null); // finished conveyor
            g2.drawLine(5,(i+1)*20-(int)finishedConveyorMove,64,(i+1)*20-(int)finishedConveyorMove);

            g2.drawImage(conveyorImage,75, 500 + i * 20 + (int)incompleteConveyorMove,null); // incomplete conveyor
            g2.drawLine(75,500 + 20*i+(int)incompleteConveyorMove,134, 500 + 20*i +(int)incompleteConveyorMove);

            g2.drawImage(conveyorImage,5, 500 + i*20 + (int)badConveyorMove,null); // bad conveyor
            g2.drawLine(5,500 + i*20+(int)badConveyorMove,64,500 + i*20+(int)badConveyorMove);
        }

        g2.drawImage(conveyorImage,75,100+(int)emptyConveyorMove,135,120,0,0,60,22,null); // empty conveyor end
        g2.drawLine(75,100+(int)emptyConveyorMove,134,100+(int)emptyConveyorMove);

        g2.drawImage(conveyorImage,5,120-(int)finishedConveyorMove,65,120,0,0,60,22,null); // finished conveyor end
        g2.drawLine(5,120-(int)finishedConveyorMove,64,120-(int)finishedConveyorMove);

        g2.drawImage(conveyorImage,75,480,135,480+(int)incompleteConveyorMove,0,0,60,22,null); // incomplete conveyor end
        g2.drawLine(75,480+(int)incompleteConveyorMove,134,480+(int)incompleteConveyorMove);

        g2.drawImage(conveyorImage,5,480,65,480+(int)badConveyorMove,0,0,60,22,null); // bad conveyor end
        g2.drawLine(5,480+(int)badConveyorMove,64,480+(int)badConveyorMove);

        for(GUIKit k : emptyKits){
        	k.paintKit(g2);
        }
        for(int i = 1; i < 6; i++){
        	if(stationOccupied.get(i)){
        		stationKits.get(i).paintKit(g2);
        	}
        }
        for(GUIKit k : finishedKits){
        	k.paintKit(g2);
        }
        gPartsRobot.paintPartsRobot(g2);
        gKitRobot.paintKitRobot(g2);
    }

	public Vector<Kit> getBaseEmptyKits() {
		return baseEmptyKits;
	}

	public void setBaseEmptyKits(Vector<Kit> baseEmptyKits) {
		this.baseEmptyKits = baseEmptyKits;
	}

	public Vector<Kit> getBaseFinishedKits() {
		return baseFinishedKits;
	}

	public void setBaseFinishedKits(Vector<Kit> baseFinishedKits) {
		this.baseFinishedKits = baseFinishedKits;
	}
	
//	public PartsManagerClient getPartsClient() {
//		return partsClient;
//	}	

	public KitRobot getKitRobot() {
		return kitRobot;
	}
	
	public PartsRobot getPartsRobot() {
		return partsRobot;
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
}
