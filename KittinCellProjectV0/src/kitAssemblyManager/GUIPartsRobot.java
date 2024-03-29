package kitAssemblyManager;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;
import javax.imageio.*;

import data.GUIPart;
import server.PartsRobot;

public class GUIPartsRobot{
	//a combination of pictures that makes up the different parts of the part robot. 
    BufferedImage partsRobotImage;				
    BufferedImage partsRobotRailImage;
    BufferedImage partsRobotGripperImage;
    BufferedImage gripperArmImage;
    BufferedImage topImage;
    BufferedImage part;

    
    CopyOnWriteArrayList<Boolean> gripperHolding;		//ArrayList of 4 that holds the boolean state of whether each of the four gripers are holding parts.
    CopyOnWriteArrayList<Double> gripperExtensions;	//ArrayList that holds how much each of the 4 gripers extends out	
    CopyOnWriteArrayList<String> commands;				//Arraylist of the list of commands that it needs to process. 
    CopyOnWriteArrayList<String> subCommands;			//Arraylist to aid proccessing commands. 
    CopyOnWriteArrayList<String> nestLocations;		//Arraylist of the nest locations
    CopyOnWriteArrayList<String> kitLocations;			//Arraylist of the kit locations
    CopyOnWriteArrayList<BufferedImage> partImages;	//Arraylist of the 4 picture parts that it is holding. 
    CopyOnWriteArrayList<Integer> gripperPartIDs;		//Arraylist of the ID of the parts it is holding. 
    
    CopyOnWriteArrayList<GUIPart> gParts; 		//Arraylist of the gui versions of the parts. 

    PartsRobot pr;			//A reference to the parts robot from the server. 

    double x;				//X position of the parts robot. 
    double y;				//Y position of the parts robot
    double angle;			//Rotation of the parts robot. 

    GUIKitAssemblyManager app;			//A reference to the graphical kit assembly manager. 
    
    GUICamera gKitStandCamera;			//Graphical version of the kit stand camera. 
    GUICamera gNestCamera;				//Graphical version of the nest camera. 

    public GUIPartsRobot(GUIKitAssemblyManager s){
        app = s;
        x = 265;
        y = 300;
        angle = 0;
        
        gripperExtensions = new CopyOnWriteArrayList<Double>();
        gripperHolding = new CopyOnWriteArrayList<Boolean>();
        commands = new CopyOnWriteArrayList<String>();
        subCommands = new CopyOnWriteArrayList<String>();
        nestLocations = new CopyOnWriteArrayList<String>();
        kitLocations = new CopyOnWriteArrayList<String>();
        partImages = new CopyOnWriteArrayList<BufferedImage>();
        gripperPartIDs = new CopyOnWriteArrayList<Integer>();
        
        gParts = new CopyOnWriteArrayList<GUIPart>();

        for (int i = 0; i < 4; i++) {
        	gripperPartIDs.add(1);
            gripperExtensions.add(0.0);
        }
        
        
        //loading the images. 
        try {
//        	for(int i=0; i<9;i++){
//        		partImages.add(ImageIO.read(new File("images/kt" + i + ".png")));
//        	}
            partsRobotImage = ImageIO.read(new File("images/partsrobot.png"));
            partsRobotRailImage = ImageIO.read(new File("images/rail.png"));
            partsRobotGripperImage = ImageIO.read(new File("images/gripper.png"));
            gripperArmImage = ImageIO.read(new File("images/grip_arm.png"));
            topImage = ImageIO.read(new File("images/top.png"));
            part = ImageIO.read(new File("images/part.png"));
        }
        catch (IOException e) {
        }
        
        gKitStandCamera = new GUICamera(new Camera());
        gNestCamera = new GUICamera(new Camera());
    }

    //call the update function of the different parts referenced. 
    //Also updates the position of the parts robot. 
    public void update(){
        pr = app.getPartsRobot();
        y = pr.getY();
        
        gripperHolding = pr.getGripperHolding();

        for(int i = 0; i < gripperHolding.size(); i++){
        	if(gripperHolding.get(i)== true){
        		for(int j = 0; j < pr.getPartsHeld().size(); j++){
        			if((i+j) >= gParts.size()){
        				gParts.add(new GUIPart(pr.getPartsHeld().get(j)));
        			}
        			else {
        				gParts.set(i+j, new GUIPart(pr.getPartsHeld().get(j)));
        			}
        		}
        		break;
        	}        	
        }
        
        gKitStandCamera.setCamera(pr.getKitStandCamera());
        gNestCamera.setCamera(pr.getNestCamera());

        angle = pr.getAngle();
        
        gripperExtensions = pr.getGripperExtensions();
        gripperPartIDs = pr.getGripperPartIDs();
        
        gKitStandCamera.update();
        gNestCamera.update();
    }
    
    //paints the part robot. This will be called at the graphics kit assembly app. 
    public void paintPartsRobot(Graphics2D g2){
        AffineTransform at = new AffineTransform(); // transform object

        for (int i = 0; i < 4; i++) {
            at = new AffineTransform();
            at.translate((int)x,(int)y);
            at.rotate(Math.toRadians(angle + i*90));
            at.translate(-18,-41 - gripperExtensions.get(i).intValue()); // gripper 0
            g2.drawImage(partsRobotGripperImage,at,null);
            if(gripperHolding.get(i)){
                at.translate(5,0);
                //g2.drawImage(partImages.get(gripperPartIDs.get(i)),at,null);
                //g2.drawImage(partImages.get(i),at,null);
                g2.drawImage(gParts.get(i).getImage(), at, null);
                at.translate(-5,0);
            }
            at.translate(13,26);
            at.scale(1,gripperExtensions.get(i)/40);
            g2.drawImage(gripperArmImage,at,null);
        }
        at = new AffineTransform();
        at.translate(x,y);
        at.rotate(Math.toRadians(angle));
        at.translate(-15,-15);

        g2.drawImage(partsRobotImage,at,null);

        g2.drawImage(partsRobotRailImage,260,0,null);
        at = new AffineTransform();
        at.translate(x-10,y-10);
        g2.drawImage(topImage,at,null);

        
        gKitStandCamera.paintCamera(g2);
        gNestCamera.paintCamera(g2);
    }

}
