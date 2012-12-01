package server;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import javax.imageio.*;

import data.Part;
import kitAssemblyManager.Camera;

public class PartsRobot implements Runnable, Serializable{
    boolean processing;					// boolean on whether or not it is processing a command. 
    boolean paused;						//boolean to prevent updating if not needed. 
    	
    double x;						//x position of the parts robot
    double y;						//y position of the parts robot
    double newX;					//final x position of the parts robot
    double newY;					//final y position of the parts robot
    double angle;					//rotation of the parts robot
    double newAngle;				//final rotation of the parts robot
    double moveSpeed = 15;			//speed of the movement of the parts robot
    double rotationSpeed = 4;		//Speed of rotation of the parts robot. 
    double extensionSpeed = 4;		//Speed of extension of the grippers of the parts robot. 
    
    private Camera kitStandCamera;		//Camera for the kit stand
    private Camera nestCamera;			//Camera for the nest
    
    Boolean msg;							//boolean of whether or not there are msgs. 
    KitAssemblyManager app;					//reference to the kit assembly
    
    //BufferedImage partsRobotImage;			

    CopyOnWriteArrayList<Boolean> gripperHolding;		//CopyOnWriteArrayList of 4 that holds the boolean state of whether each of the four gripers are holding parts.
    CopyOnWriteArrayList<Double> gripperExtensions;	//ArrayList that holds how much each of the 4 gripers extends out	
    CopyOnWriteArrayList<Double> newGripperExtensions;	//Arraylist of the new gripper extensions. 
    CopyOnWriteArrayList<String> commands;				//Arraylist of the list of commands that it needs to process. 
    CopyOnWriteArrayList<String> subCommands;			//Arraylist to aid proccessing commands. 
    CopyOnWriteArrayList<String> nestLocations;		//Arraylist of the nest locations
    CopyOnWriteArrayList<String> kitLocations;			//Arraylist of the kit locations
    CopyOnWriteArrayList<Integer> gripperPartIDs;		//Arraylist of the ID of the parts it is holding. 
    
    
    int[] nl = {55,125,195,265,335,405,475,545};	//Array of the y coordinates of the nests
    int[] kl = {190,410};							//Array of the y coordinates of the kits. 
    CopyOnWriteArrayList<Part> partsHeld;				//Arraylist of the parts held by the grippers. 
    
    boolean movingMsg;				//boolean on whether or not it is moving. 
    boolean dumped;					//boolean on whether or not dumping.
    boolean dropParts = false;		//whether the part robot will drop parts
    boolean dropTurn = false;		//whether the parts robot needs to try a drop this turn
    Random rand = new Random();		//generate random number for dropping
    int dropRate = 50;				//percentage chance that part will be dropped per move
    double dropSrcY = 0;					// where the parts robot is coming from. used for drop purposes
    double dropDstY = 300;
    CopyOnWriteArrayList<Integer> canDrop = new CopyOnWriteArrayList<Integer>(); // randomized order for dropping

    public PartsRobot(KitAssemblyManager _app){
    	app = _app;
        y = 300;
        newY = y;
        
        //Set the location for the inspection of  kit stand camera.
        kitStandCamera = new Camera(140, 230); 		 
        nestCamera = new Camera(350, 100);
        
        //Initialization
        gripperHolding = new CopyOnWriteArrayList<Boolean>();
        gripperExtensions = new CopyOnWriteArrayList<Double>();
        newGripperExtensions = new CopyOnWriteArrayList<Double>();
        nestLocations = new CopyOnWriteArrayList<String>();
        kitLocations = new CopyOnWriteArrayList<String>();
        gripperPartIDs = new CopyOnWriteArrayList<Integer>();
        partsHeld = new CopyOnWriteArrayList<Part>();
        
        //Setup Grippers
        for(int i = 0; i < 4; i++){
        	gripperPartIDs.add(2);
            gripperHolding.add(false);
            gripperExtensions.add(0.0);
            newGripperExtensions.add(0.0);
        }
        commands = new CopyOnWriteArrayList<String>();
        subCommands = new CopyOnWriteArrayList<String>();
        msg = new Boolean(false);
        movingMsg = false;
        dumped = false;
        
/*        try {
            partsRobotImage = ImageIO.read(new File("crate.png"));
        }
        catch (IOException e) {
        }*/
    }

    //process the commands given by the agents and adds them to the subcommand list. 
    //m = move
    //r = rotate
    //e = extend
    //p = pickup
    //d = drop
    private void processCommand(String[] ss){
        int src = 0;
        int dst = 0;
        int grp = 0;
        int offset = 0;
        if(ss[0].equals("move")) {
            try{
                dst = Integer.parseInt(ss[1]);
                //dst = Integer.parseInt(ss[2]);
            }
            catch (Exception e){}
            if(dst == 1){
                subCommands.add(0,"m,0");
            }
            else {
                subCommands.add(0,"m,520");
            }
        }
        else if(ss[0].equals("grab")) {
            try{
                src = Integer.parseInt(ss[1]);
                grp = Integer.parseInt(ss[2]);
            }
            catch (Exception e){}
            gripperPartIDs.set(grp, new Integer(ss[3]));
            subCommands.add(0,"m," + (nl[src]));
            if (grp == 0) {
                subCommands.add(1,"r,90");
            }
            else if(grp == 1){
                subCommands.add(1,"r,0");
            }
            else if(grp == 2){
                subCommands.add(1,"r,270");
            }
            else if (grp == 3) {
                subCommands.add(1,"r,180");
            }

            subCommands.add(2,"e," + grp + ",40");
            //subCommands.add(3,"p," + grp);
            subCommands.add(3,"p," + grp + "," + src);
            subCommands.add(4,"e," + grp + ",0");
        }
        else if(ss[0].equals("dump")) {
            try{
                dst = Integer.parseInt(ss[1]);
            }
            catch (Exception e){}

            subCommands.add(0,"m," +(kl[dst]));
            int j = 0;
            for (int i = 0; i < 4; i++) {
                if (gripperHolding.get(i)) {
                    if(i == 0){
                        subCommands.add(j*4+1,"r,270");
                    }
                    else if(i == 1){
                        subCommands.add(j*4+1,"r,180");
                    }
                    else if(i == 2){
                        subCommands.add(j*4+1,"r,90");
                    }
                    else if(i == 3){
                        subCommands.add(j*4+1,"r,0");
                    }
                    subCommands.add(j*4+2,"e," + i + ",40");
                    //subCommands.add(j*4+3,"d," + i);
                    subCommands.add(j*4+3,"d," + i + "," + dst);
                    subCommands.add(j*4+4,"e," + i + ",0");
                    j++;
                }
            }
        }
    }

    //process the list of subcommands. 
    private void processSubCommand(String s){
    	try {
            String[] ss = s.split("\\,");
            if(ss[0].equals("m")){								//Move
                newY = Double.parseDouble(ss[1]);
                processing = true;
                if(subCommands.size() > 1){
                    String sc = subCommands.get(1);
                    String[] ssc = sc.split("\\,");
                    if("r".equals(ssc[0])){
                        newAngle = Double.parseDouble(ssc[1]);
                        subCommands.remove(0);
                        movingMsg = true;
                    }
                }
            }
            else if (ss[0].equals("r")){					//Rotate
                newAngle = Double.parseDouble(ss[1]);
                processing = true;
                if(subCommands.size() > 1){
                    String sc = subCommands.get(1);
                    String[] ssc = sc.split("\\,");
                    if("m".equals(ssc[0])){
                        newY = Double.parseDouble(ssc[1]);
                        // newAngle = Double.parseDouble(ss[1]);
                        subCommands.remove(0);
                    }
                }
            }
            else if(ss[0].equals("e")){							//extend
                newGripperExtensions.set(Integer.parseInt(ss[1]), Double.parseDouble(ss[2]));
                processing = true;
            }
            else if(ss[0].equals("p")){							//Pick            	
                gripperHolding.set(Integer.parseInt(ss[1]),true);
                partsHeld.add(app.nests.get(Integer.parseInt(ss[2])).getParts().get(0));
                app.nests.get(Integer.parseInt(ss[2])).getParts().remove(0);
                processing = true;
                msg = true;
                canDrop.add(new Integer(ss[1]));
                dropTurn = true;
                dropSrcY = y;
            }
            else if(ss[0].equals("d")){							//Drop
                gripperHolding.set(Integer.parseInt(ss[1]),false);
                app.getStationKit(Integer.parseInt(ss[2])+1).addPart(partsHeld.get(0));
                dropDstY = kl[Integer.parseInt(ss[2])];
                partsHeld.remove(0);
                if(partsHeld.size() == 0){
                	dumped = true;
                }
                System.out.println("Size : " + app.getStationKit(Integer.parseInt(ss[2])+1).getPartsList().size());
                processing = true;
                for(Integer i : canDrop){
                	if(i.equals(new Integer(ss[1])));
                	canDrop.remove(i);
                	break;
                }
            }
        } catch (Exception ignore){
        	ignore.printStackTrace();
        }

    }

    @Override
    public void run(){
        String s;
        String[] ss = null;
        while(true){
            update();
            if(processing == false){
                if(subCommands.size() > 0){
                    processSubCommand(subCommands.get(0));
                    subCommands.remove(0);
                }
                else {
                    for (int i = 0; i < commands.size(); i++){
                        s = commands.get(i);
                        ss = s.split("\\,");
                        processCommand(ss);
                        commands.remove(i);
                        break;
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {}

        }
    }

    public void addCommand(String s){
        System.out.println("add: " + s);
        commands.add(s);
    }

	//update the position of everything. 
	public void update(){
        if(!paused){
            processing = false;
 
            kitStandCamera.update();
            nestCamera.update();
            
            if(y != newY){
                processing = true;
                if(y < newY){
                    y+=moveSpeed;
                }
                else {
                    y -=moveSpeed;
                }
                if(dropParts && dropTurn){ // check if part can be dropped
                	double distDrop = Math.abs(newY-dropSrcY)*rand.nextDouble(); // beginning drop distance
                	double distMoved = Math.abs(y - dropSrcY);
                	if(distMoved > distDrop){ // drops at random place
                		Collections.shuffle(canDrop, new Random(System.nanoTime()));
		                for(Integer i : canDrop){	                	
		                	if(gripperHolding.get(i) && rand.nextInt(100) < dropRate){ // random chance of drop
		                		partsHeld.get(i).setPartDropped(true);		                		
		                		dropTurn = false; // part has been dropped this turn
		                		System.out.println("DROP: Part " + i + " was dropped at " + y + " heading to " + newY + " from " + dropSrcY);
		                		System.out.println("Drop was set for " + distDrop / Math.abs(newY-dropSrcY) + " of distance (" + distDrop + ")");
		                		System.out.println("Droppable objects was " + canDrop.size());
		                		canDrop.remove(i);
		                		break;
		                	}
		                }
		                dropTurn = false;
                	}                	
                }
                if(Math.abs(y - newY) < moveSpeed){
                    y = newY;
                }
            }
            if (angle != newAngle){
                processing = true;
                if(angle <= newAngle && newAngle - angle <= 180.0){
                    angle += rotationSpeed;
                }
                else if(angle <= newAngle && newAngle - angle > 180.0){
                    angle -= rotationSpeed;
                }
                else if(angle > newAngle && angle - newAngle <= 180.0){
                    angle -= rotationSpeed;
                }
                else if(angle > newAngle && angle - newAngle > 180.0){
                    angle += rotationSpeed;
                }
                if(Math.abs(angle - newAngle) < rotationSpeed){
                    angle = newAngle;
                }
                if(angle >= 360.0){
                    angle = 0.0;
                }
                if(angle < 0.0){
                    angle = 360.0;
                }
            }
            else {
	            for (int i = 0 ;i<4 ; i++) {
	                if(newGripperExtensions.get(i) != gripperExtensions.get(i)){
	                    processing = true;
	                    if(newGripperExtensions.get(i) > gripperExtensions.get(i)){
	                        gripperExtensions.set(i,gripperExtensions.get(i) + extensionSpeed);
	                    }
	                    else {
	                        gripperExtensions.set(i,gripperExtensions.get(i) - extensionSpeed);
	                    }
	                    if(Math.abs(newGripperExtensions.get(i) - gripperExtensions.get(i)) < extensionSpeed) {
	                        gripperExtensions.set(i,newGripperExtensions.get(i));
	                    }
	                }
	            }
            }
        }
    }
	
	
	//Getters and Setters
    public double getY(){
        return y;
    }
    
    public void setDropParts(boolean b){
    	dropParts = b;
    }
    
    public Camera getKitStandCamera() {
    	return kitStandCamera;
    }
    
    public void setKitStandCamera(Camera c) {
    	kitStandCamera = c;
    }
    
    public Camera getNestCamera() {
    	return nestCamera;
    }
    
    public void setNestCamera(Camera c) {
    	nestCamera = c;
    }

    public double getAngle(){
        return angle;
    }

    public CopyOnWriteArrayList<Boolean> getGripperHolding(){
        return gripperHolding;
    }

    public CopyOnWriteArrayList<Double> getGripperExtensions(){
        return gripperExtensions;
    }

    public Boolean getMsg() {
		return msg;
	}
    
    public CopyOnWriteArrayList<Integer> getGripperPartIDs(){
    	return gripperPartIDs;
    }

    public CopyOnWriteArrayList<Part> getPartsHeld(){
    	return partsHeld;
    }
    
	public void setMsg(Boolean msg) {
		this.msg = msg;
	}

	public boolean isDumped() {
		return dumped;
	}

	public void setDumped(boolean dumped) {
		this.dumped = dumped;
	}

	public boolean isMovingMsg() {
		return movingMsg;
	}

	public void setMovingMsg(boolean movingMsg) {
		this.movingMsg = movingMsg;
	}
}
