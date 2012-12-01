package kitAssemblyManager;

import server.*;

import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
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

    ArrayList<Boolean> stationOccupied;
    ArrayList<GUIKit> emptyKits;
    ArrayList<GUIKit> finishedKits;
    ArrayList<GUIKit> stationKits;
    ArrayList<Kit> baseEmptyKits;
    ArrayList<Kit> baseFinishedKits;
    ArrayList<Kit> baseStationKits;

    ArrayList<GUINest> gNests;
    Vector<Nest> nests;
    
    int[] stationPositions = {160, 140, 160, 360, 80, 490, 10, 490, 160, 250}; // stations 1 - 2 - 3 - 4 - 5 (image corner coords)
    boolean emptyConveyorOn;
    boolean finishedConveyorOn;
    boolean badConveyorOn = true;
    boolean incompleteConveyorOn = true;
    /*
    id  name    corX  corY
    0   empty   80    10
    1   kit 1   160   140
    2   kit 2   160   360
    3   inc     80    490	//Bad Kit Out
    4   bad     10    490	//Bad Kit In
    5   check   160   250
    6   done    10    10
    */
    int managerNum;

    public GUIKitAssemblyManager(int manager){
    	managerNum = manager;
        
//    	partsClient = new PartsManagerClient(this);
    	
    	gNests = new ArrayList<GUINest>();
    	nests = new Vector<Nest>();
    	
    	kam = new KitAssemblyManager(nests);
    	kitRobot = new KitRobot(kam);
    	partsRobot = new PartsRobot(kam);    	
    	
        setPreferredSize(new Dimension (450,600));
        gKitRobot = new GUIKitRobot(this);
        gPartsRobot = new GUIPartsRobot(this);
        stationOccupied = new ArrayList<Boolean>();
        emptyKits = new ArrayList<GUIKit>();
        finishedKits = new ArrayList<GUIKit>();
        stationKits = new ArrayList<GUIKit>();
        baseEmptyKits = new ArrayList<Kit>();
        baseFinishedKits = new ArrayList<Kit>();
        baseStationKits = new ArrayList<Kit>();

        try {
            background = ImageIO.read(new File("images/background1.png"));
            kitImage = ImageIO.read(new File("images/kit.png"));
            stand = ImageIO.read(new File("images/stand.png"));
            conveyorImage = ImageIO.read(new File("images/conveyor.png"));
        } catch (IOException e) {}
        update();
			
    }

    public void update(){
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
            emptyConveyorMove += 5;
            if(emptyConveyorMove > 20.0){
                emptyConveyorMove = 0;
            }
        }

        if(finishedConveyorOn){
            finishedConveyorMove += 5;
            if(finishedConveyorMove > 20.0){
                finishedConveyorMove = 0;
            }
        }
//        if(badConveyorOn){
//            badConveyorMove += 0.5;
//            if(badConveyorMove > 20.0){
//                badConveyorMove = 0;
//            }
//        }
//        if(incompleteConveyorOn){
//            incompleteConveyorMove += 0.5;
//            if(incompleteConveyorMove > 20.0){
//                incompleteConveyorMove = 0;
//            }
//        }
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

        for(int i = -1; i < 5; i++){ // main conveyor images
            g2.drawImage(conveyorImage,75, i * 20 + (int)emptyConveyorMove,null); // empty conveyor
//            g2.drawLine(83,20*i+(int)emptyConveyorMove,126,20*i+(int)emptyConveyorMove);

            g2.drawImage(conveyorImage,5,(i+1)*20 - (int)finishedConveyorMove,null); // finished conveyor
//            g2.drawLine(13,(i+1)*20-(int)finishedConveyorMove,56,(i+1)*20-(int)finishedConveyorMove);

//            g2.drawImage(conveyorImage,75, 500 + i * 20 + (int)incompleteConveyorMove,null); // incomplete conveyor
//            g2.drawLine(75,500 + 20*i+(int)incompleteConveyorMove,134, 500 + 20*i +(int)incompleteConveyorMove);
//
//            g2.drawImage(conveyorImage,5, 500 + (i+1)*20 - (int)badConveyorMove,null); // bad conveyor
//            g2.drawLine(5,500 + (i+1)*20-(int)badConveyorMove,64,500 + (i+1)*20-(int)badConveyorMove);			//changed to - sign
        }

        g2.drawImage(conveyorImage,75,100+(int)emptyConveyorMove,135,120,0,0,60,22,null); // empty conveyor end
//        g2.drawLine(83,100+(int)emptyConveyorMove,126,100+(int)emptyConveyorMove);

        g2.drawImage(conveyorImage,5,120-(int)finishedConveyorMove,65,120,0,0,60,22,null); // finished conveyor end
//        if(finishedConveyorMove > 0){
//        	g2.drawLine(13,120-(int)finishedConveyorMove,56,120-(int)finishedConveyorMove);
//        }

//        g2.drawImage(conveyorImage,75,480,135,480+(int)incompleteConveyorMove,0,0,60,22,null); // incomplete conveyor end
//        g2.drawLine(75,480+(int)incompleteConveyorMove,134,480+(int)incompleteConveyorMove);
//
//        g2.drawImage(conveyorImage,5,480,65,500-(int)badConveyorMove,0,0,60,22,null); // bad conveyor end
//        g2.drawLine(5,500-(int)badConveyorMove,64,500-(int)badConveyorMove);

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

	public ArrayList<Kit> getBaseEmptyKits() {
		return baseEmptyKits;
	}

	public void setBaseEmptyKits(ArrayList<Kit> baseEmptyKits) {
		this.baseEmptyKits = baseEmptyKits;
	}

	public ArrayList<Kit> getBaseFinishedKits() {
		return baseFinishedKits;
	}

	public void setBaseFinishedKits(ArrayList<Kit> baseFinishedKits) {
		this.baseFinishedKits = baseFinishedKits;
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

	public KitRobot getKitRobot() {
		return kitRobot;
	}
	
	public PartsRobot getPartsRobot() {
		return partsRobot;
	}
}
