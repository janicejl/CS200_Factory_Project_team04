package kitAssemblyManager;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;

import server.KitAssemblyManager;

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

    int[] stationPostions = {265, 250, 265, 310, 170, 405, 110, 405, 15, 280};
    boolean emptyConveyorOn;
    boolean finishedConveyorOn;

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
    }

    public void paintComponent(Graphics g){ // Pant game background and
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(background, 0, 0, null);
        g2.drawImage(stand,255,240,null);
        g2.drawImage(stand,5,270,60,60,null);
        AffineTransform at = new AffineTransform();
        at.translate(100,395);
        at.rotate(Math.toRadians(90));
        at.translate(0,-120);
        g2.drawImage(stand,at,null);
        g2.setColor(Color.BLACK);
        g2.drawRect(375,100,50,50);
        g2.drawRect(375,150,50,50);
        g2.drawRect(375,200,50,50);
        g2.drawRect(375,250,50,50);
        g2.drawRect(375,300,50,50);
        g2.drawRect(375,350,50,50);
        g2.drawRect(375,400,50,50);
        g2.drawRect(375,450,50,50);

        for(int i = -1; i < 9; i++){
            g2.drawImage(conveyorImage,165, i * 20 + (int)emptyConveyorMove,null);
            g2.drawLine(165,20*i+(int)emptyConveyorMove,214,20*i+(int)emptyConveyorMove);

            g2.drawImage(conveyorImage,105,(i+1)*20 - (int)finishedConveyorMove,null);
            g2.drawLine(105,(i+1)*20-(int)finishedConveyorMove,154,(i+1)*20-(int)finishedConveyorMove);
        }

        g2.drawImage(conveyorImage,165,180+(int)emptyConveyorMove,215,202,0,0,50,22,null);
        g2.drawLine(165,180+(int)emptyConveyorMove,214,180+(int)emptyConveyorMove);

        g2.drawImage(conveyorImage,105,200-(int)finishedConveyorMove,155,202,0,0,50,22,null);
        g2.drawLine(105,200-(int)finishedConveyorMove,154,200-(int)finishedConveyorMove);

        for(Kit k : getBaseEmptyKits()){
            g2.drawImage(crateImage,170, (int)k.getY(),null);
        }
        for(Kit k : getBaseFinishedKits()){
            g2.drawImage(crateImage,110, (int)k.getY(),null);
        }

        for (int i = 1; i < stationOccupied.size()-1; i++) {
            if(stationOccupied.get(i)){
                g2.drawImage(crateImage,stationPostions[i*2-2],stationPostions[i*2-1],null);
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