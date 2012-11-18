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

public class PartsRobot implements Runnable, Serializable{
    boolean processing;
    boolean paused;
    boolean hasCrate;
    double x;
    double y;
    double newX;
    double newY;
    double angle;
    double newAngle;
    double moveSpeed = 10;
    double rotationSpeed = 2;
    double extensionSpeed = 2;
    boolean takePicture;
    double cameraX;
    double cameraY;
    Boolean msg;
    boolean flashUp, flashDown;
    KitAssemblyManager app;
    
    BufferedImage partsRobotImage;

    ArrayList<Boolean> gripperHolding;
    ArrayList<Double> gripperExtensions;
    ArrayList<Double> newGripperExtensions;
    ArrayList<String> commands;
    ArrayList<String> subCommands;
    ArrayList<String> nestLocations;
    ArrayList<String> kitLocations;
    ArrayList<Integer> gripperPartIDs;
    int[] nl = {55,125,195,265,335,405,475,545};
    int[] kl = {190,410};
    ArrayList<Part> partsHeld;
    float opacity;
    boolean animationDone;
    boolean movingMsg;
    boolean dumped;

    public PartsRobot(KitAssemblyManager _app){
    	app = _app;
        y = 300;
        newY = y;
        cameraX = 350;
        cameraY = 100;
        takePicture = false;
        gripperHolding = new ArrayList<Boolean>();
        gripperExtensions = new ArrayList<Double>();
        newGripperExtensions = new ArrayList<Double>();
        nestLocations = new ArrayList<String>();
        kitLocations = new ArrayList<String>();
        gripperPartIDs = new ArrayList<Integer>();
        partsHeld = new ArrayList<Part>();
        flashDown = false;
        flashUp = false;
        animationDone = false;
        opacity = 0.0f;
        for(int i = 0; i < 4; i++){
        	gripperPartIDs.add(2);
            gripperHolding.add(false);
            gripperExtensions.add(0.0);
            newGripperExtensions.add(0.0);
        }
        commands = new ArrayList<String>();
        subCommands = new ArrayList<String>();
        msg = new Boolean(false);
        movingMsg = false;
        dumped = false;
        
        try {
            partsRobotImage = ImageIO.read(new File("crate.png"));
        }
        catch (IOException e) {
        }
    }

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

    public void takePicture(double x, double y){
        takePicture = true;
        flashUp = true;
        cameraX = x;
        cameraY = y;
    }

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
            }
            else if(ss[0].equals("d")){							//Drop
                gripperHolding.set(Integer.parseInt(ss[1]),false);
                app.getStationKit(Integer.parseInt(ss[2])+1).addPart(partsHeld.get(0));
                partsHeld.remove(0);
                if(partsHeld.size() == 0){
                	dumped = true;
                }
                System.out.println("Size : " + app.getStationKit(Integer.parseInt(ss[2])+1).getPartsList().size());
                processing = true;
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

    public double getY(){
        return y;
    }

    public double getAngle(){
        return angle;
    }

    public ArrayList<Boolean> getGripperHolding(){
        return gripperHolding;
    }

    public ArrayList<Double> getGripperExtensions(){
        return gripperExtensions;
    }
    public boolean getTakePicture(){
        /*if(takePicture){
            takePicture = false;
            return true;
        }
        else {
            return false;
        }*/
    	return takePicture;
    }

    public Boolean getMsg() {
		return msg;
	}
    
    public ArrayList<Integer> getGripperPartIDs(){
    	return gripperPartIDs;
    }

    public ArrayList<Part> getPartsHeld(){
    	return partsHeld;
    }
    
	public void setMsg(Boolean msg) {
		this.msg = msg;
	}

	public double getCameraX() {
		return cameraX;
	}

	public void setCameraX(double cameraX) {
		this.cameraX = cameraX;
	}

	public double getCameraY() {
		return cameraY;
	}

	public void setCameraY(double cameraY) {
		this.cameraY = cameraY;
	}

	public void setTakePicture(boolean takePicture) {
		this.takePicture = takePicture;
	}
	
	public float getOpacity(){
		return opacity;
		
	}
	
	public boolean getAnimationDone(){
		return animationDone;
	}
	
	public void setAnimationDone(boolean b){
		animationDone = b;
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

	public void update(){
        if(!paused){
            processing = false;
            if(takePicture){
	            if(flashUp){
	                opacity += 0.1;
	                if(opacity >= 1){
	                	flashUp = false;
	                	flashDown = true;
	                }
	            }
	            else if(flashDown){
	            	opacity -= 0.1;
	            	if(opacity <= 0){
	            		opacity = 0;
	            		flashDown = false;
	            		takePicture = false;
	            		animationDone = true;
	            	}	            			
	            }
            }
            
            if(y != newY){
                processing = true;
                if(y < newY){
                    y+=moveSpeed;
                }
                else {
                    y -=moveSpeed;
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
