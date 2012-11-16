package server;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.io.*;
import javax.imageio.*;

//import kitAssemblyManager.Kit;

import data.GUIKit;
import data.Kit;

public class KitRobot implements Runnable, Serializable{
    boolean processing = false;
    boolean hasKit = false;
    boolean paused = false;
    boolean emptyAvailable = false;
    boolean spawnEmpty = true;
    boolean emptyConveyorOn = true;
    boolean doneConveyorOn = false;
    double x;
    double y;
    double newX;
    double newY;
    Kit kit;
    int[] stationX =  {105,185,185,105, 35,185, 35};
    int[] stationY =  { 60,190,410,540,540,300, 60};
    int[] waypointX = {105,105,105,105, 35,105, 35};
    int[] waypointY = {190,190,410,410,410,300,190};
    double speed = 4.0;

    CopyOnWriteArrayList<String> stationRotations;
    CopyOnWriteArrayList<String> commands;
    CopyOnWriteArrayList<String> subCommands;
    CopyOnWriteArrayList<String> commandsSkipped;
    KitAssemblyManager kitAssemblyManager;

    Thread thread;

    public KitRobot(KitAssemblyManager kam){
        kitAssemblyManager = kam;
        commands = new CopyOnWriteArrayList<String>();
        subCommands = new CopyOnWriteArrayList<String>();
        commandsSkipped = new CopyOnWriteArrayList<String>();
        stationRotations = new CopyOnWriteArrayList<String>();
        x = 100;
        y = 300;
        newX = x;
        newY = y;
        kit = new Kit();
    }

    private void processCommand(String[] ss){
        int src = 0;
        int dst = 0;
        int i = 0;
        try{
            src = Integer.parseInt(ss[1]);
            dst = Integer.parseInt(ss[2]);
        }
        catch (Exception e){}

        subCommands.add(i,"m," + waypointX[src] + "," + waypointY[src]);
        subCommands.add(i+1,"m," + stationX[src] + "," + stationY[src]);
        subCommands.add(i+2,"p," + src);
        if(dst != 5){
        }
        if(dst == 6){
            subCommands.add(i+3,"m," + waypointX[0] + "," + waypointY[0]);
            i++;
        }
        else if(dst == 4){
            subCommands.add(i+3,"m," + waypointX[2] + "," + waypointY[2]);
            i++;
        }
        if(dst != 5){
            subCommands.add(i+3,"m," + waypointX[dst] + "," + waypointY[dst]);
            i++;
        }

        subCommands.add(i+3,"m," + stationX[dst] + "," + stationY[dst]);
        subCommands.add(i+4,"d," + dst);
        subCommands.add(i+5,"m," + waypointX[dst] + "," + waypointY[dst]);
    }

    private void processSubCommand(String s){
        String[] ss = s.split("\\,");
        if("m".equals(ss[0])){
            System.out.println(s);
            newX = Double.parseDouble(ss[1]);
            newY = Double.parseDouble(ss[2]);
            processing = true;
        }
        else if(ss[0].equals("p")){
            int i = Integer.parseInt(ss[1]);
            try {
            	kit = new Kit();
            	kit = kitAssemblyManager.getStationKit(i);
                kit.setGrabbed(true);
                kitAssemblyManager.setSingleStationOccupied(i, false);
            }
            catch (Exception ignore) {
            	ignore.printStackTrace();
            }
            hasKit = true;
        }
        else if(ss[0].equals("d")){
            int i = Integer.parseInt(ss[1]);
            kit.setGrabbed(false);
            kitAssemblyManager.setStationKit(i, kit);
            hasKit = false;
        }

    }

    public void sendCommand(String s){
        String[] ss = s.split("\\,");
        if(ss[0].equals("kitrobot")){
            if(ss[1].equals("load")){
                int i = Integer.parseInt(ss[2]);
                int j = Integer.parseInt(ss[3]);
                addCommand(s.substring(s.indexOf(',')+1));
            }
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
                        if(ss[0].equals("load")) {
                            processCommand(ss);
                            commands.remove(i);
                            break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(10);  // milliseconds
            } catch (InterruptedException ignore) {}

        }
    }

    public void addCommand(String s){
        System.out.println("add: " + s);
        commands.add(s);
    }

    public void pause(){
        paused = !paused;
    }

    public void update(){
        if(!paused){
            if(x != newX || y != newY){
            	
                double d = Math.sqrt(Math.pow((newX-x),2) + Math.pow((newY-y),2));
                if (d >= speed){
                    x += ((newX-x)/d)*speed;
                    y += ((newY-y)/d)*speed;
                }
                else {
                    x = newX;
                    y = newY;
                }
                if(hasKit){
	                kit.setX(x-25);
	            	kit.setY(y-50);
                }
            }
            else {
                processing = false;
            }
        }
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean getProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean getEmptyAvailable() {
        return emptyAvailable;
    }

    public void setEmptyAvailable(boolean emptyAvailable) {
        this.emptyAvailable = emptyAvailable;
    }

    public boolean getSpawnEmpty() {
        return spawnEmpty;
    }

    public void setSpawnEmpty(boolean spawnEmpty) {
        this.spawnEmpty = spawnEmpty;
    }

    public boolean getEmptyConveyorOn() {
        return emptyConveyorOn;
    }

    public void setEmptyConveyorOn(boolean emptyConveyorOn) {
        this.emptyConveyorOn = emptyConveyorOn;
    }

    public boolean getDoneConveyorOn() {
        return doneConveyorOn;
    }

    public void setDoneConveyorOn(boolean doneConveyorOn) {
        this.doneConveyorOn = doneConveyorOn;
    }

  /*  public double getNewExtension() {
        return newExtension;
    }

    public void setNewExtension(double newExtension) {
        this.newExtension = newExtension;
    }

    public double getNewAngle() {
        return newAngle;
    }

    public void setNewAngle(double newAngle) {
        this.newAngle = newAngle;
    }*/

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public CopyOnWriteArrayList<String> getStationRotations() {
        return stationRotations;
    }

    public void setStationRotations(CopyOnWriteArrayList<String> stationRotations) {
        this.stationRotations = stationRotations;
    }

    public CopyOnWriteArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(CopyOnWriteArrayList<String> commands) {
        this.commands = commands;
    }

    public CopyOnWriteArrayList<String> getSubCommands() {
        return subCommands;
    }

    public void setSubCommands(CopyOnWriteArrayList<String> subCommands) {
        this.subCommands = subCommands;
    }

    public CopyOnWriteArrayList<String> getCommandsSkipped() {
        return commandsSkipped;
    }

    public void setCommandsSkipped(CopyOnWriteArrayList<String> commandsSkipped) {
        this.commandsSkipped = commandsSkipped;
    }

    public KitAssemblyManager getKitAssemblyManager() {
        return kitAssemblyManager;
    }

    public void setKitAssemblyManager(
            KitAssemblyManager kitAssemblyManager) {
        this.kitAssemblyManager = kitAssemblyManager;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setHasKit(boolean hasKit) {
        this.hasKit = hasKit;
    }
	
    public boolean getHasKit() {
	    return hasKit;
	}
  /*  
    public void setExtension(double extension) {
        this.extension = extension;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }   

    public double getExtension() {
        return extension;
    }

    public double getAngle() {
        return angle;
    }*/

	public double getNewX() {
		return newX;
	}

	public void setNewX(double newX) {
		this.newX = newX;
	}

	public double getNewY() {
		return newY;
	}

	public void setNewY(double newY) {
		this.newY = newY;
	}

	public int[] getStationX() {
		return stationX;
	}

	public void setStationX(int[] stationX) {
		this.stationX = stationX;
	}

	public int[] getStationY() {
		return stationY;
	}

	public void setStationY(int[] stationY) {
		this.stationY = stationY;
	}

	public int[] getWaypointX() {
		return waypointX;
	}

	public void setWaypointX(int[] waypointX) {
		this.waypointX = waypointX;
	}

	public int[] getWaypointY() {
		return waypointY;
	}

	public void setWaypointY(int[] waypointY) {
		this.waypointY = waypointY;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
