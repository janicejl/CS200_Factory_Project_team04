package kitAssemblyManager;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;

import server.KitAssemblyManager;
import data.Kit;

import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;

public class GUIKitAssemblyManager extends JPanel implements ActionListener {
    KitAssemblyApp app;
    KitAssemblyManager kam;
    GUIKitRobot kitRobot;
    GUIPartsRobot partsRobot;

    double emptyConveyorMove = 0;
    double finishedConveyorMove = 0;
    double badConveyorMove = 0;
    double incompleteConveyorMove = 0;
    BufferedImage crateImage = null;
    BufferedImage background = null;
    BufferedImage stand = null;
    BufferedImage conveyorImage = null;

    Vector<Boolean> stationOccupied;
    Vector<GUIKit> emptyKits;
    Vector<GUIKit> finishedKits;
    Vector<Kit> baseEmptyKits;
    Vector<Kit> baseFinishedKits;
    Vector<Kit> baseStationKits;

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

    javax.swing.Timer timer;

    public GUIKitAssemblyManager(KitAssemblyApp _app){
        app = _app;
        setPreferredSize(new Dimension (450,600));

        //kitAssemblyManager = kam;
        kitRobot = new GUIKitRobot(app);
        partsRobot = new GUIPartsRobot(app);
        stationOccupied = new Vector<Boolean>();
        emptyKits = new Vector<GUIKit>();
        finishedKits = new Vector<GUIKit>();
        baseEmptyKits = new Vector<Kit>();
        baseFinishedKits = new Vector<Kit>();
        baseStationKits = new Vector<Kit>();

        timer = new javax.swing.Timer(10, this);
        timer.start();

        try {
            background = ImageIO.read(new File("images/background.png"));
            crateImage = ImageIO.read(new File("images/crate.png"));
            stand = ImageIO.read(new File("images/stand.png"));
            conveyorImage = ImageIO.read(new File("images/conveyor.png"));
        } catch (IOException e) {}
    }

    public void update(){
        kam = app.getKitAssemblyManager();
        emptyConveyorOn = kam.getEmptyConveyorOn();
        finishedConveyorOn = kam.getFinishedConveyorOn();
        //badConveyorOn = kam.getBadConveyorOn();
        //incompleteConveyorOn = kam.getIncompleteConveyorOn();
        // System.out.println(incompleteConveyorOn);
        baseEmptyKits = kam.getEmptyKits();
        baseFinishedKits = kam.getFinishedKits();
        baseStationKits = kam.getStationKits();
        stationOccupied = kam.getStationOccupied();
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

    public void paintComponent(Graphics g){ // Pant game background and
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(background, 0, 0, null);
        g2.drawImage(stand,150,130,70,340,null); // kit stand
        g2.setColor(Color.BLACK);
        g2.drawRect(320,100,50,50);
        g2.drawRect(320,150,50,50);
        g2.drawRect(320,200,50,50);
        g2.drawRect(320,250,50,50);
        g2.drawRect(320,300,50,50);
        g2.drawRect(320,350,50,50);
        g2.drawRect(320,400,50,50);
        g2.drawRect(320,450,50,50);

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



        for(Kit k : baseEmptyKits){ // draw empty kits
            g2.drawImage(crateImage,80, (int)k.getY(),null);
        }
        for(Kit k : baseFinishedKits){ // draw finished kits
            g2.drawImage(crateImage,10, (int)k.getY(),null);
        }

        for (int i = 1; i < stationOccupied.size()-1; i++) { // draw station kits
            if(stationOccupied.get(i)){
                if(i == 3) {
                    g2.drawImage(crateImage,stationPositions[i*2-2],(int)baseStationKits.get(i).getY(),null);

                }
                else if (i == 4) {
                    g2.drawImage(crateImage,stationPositions[i*2-2],(int)baseStationKits.get(i).getY(),null);
                    //System.out.println(baseStationKits.get(4).getY());
                }
                else {
                    g2.drawImage(crateImage,stationPositions[i*2-2],stationPositions[i*2-1],null);
                }

            }
        }
        partsRobot.paintPartsRobot(g2);
        kitRobot.paintKitRobot(g2);
    }

    public void actionPerformed(ActionEvent ae) {
        update();
        partsRobot.update();
        kitRobot.update();
        repaint();
    }

	public synchronized Vector<Kit> getBaseEmptyKits() {
		return baseEmptyKits;
	}

	public synchronized void setBaseEmptyKits(Vector<Kit> baseEmptyKits) {
		this.baseEmptyKits = baseEmptyKits;
	}

	public synchronized Vector<Kit> getBaseFinishedKits() {
		return baseFinishedKits;
	}

	public synchronized void setBaseFinishedKits(Vector<Kit> baseFinishedKits) {
		this.baseFinishedKits = baseFinishedKits;
	}
}
