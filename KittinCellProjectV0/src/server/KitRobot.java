package server;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;
import java.util.Vector;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.io.*;
import javax.imageio.*;

//import kitAssemblyManager.Kit;

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

    Vector<String> stationRotations;
    Vector<String> commands;
    Vector<String> subCommands;
    Vector<String> commandsSkipped;
    KitAssemblyManager kitAssemblyManager;

    Thread thread;

    public KitRobot(KitAssemblyManager kam){
        kitAssemblyManager = kam;
        commands = new Vector<String>();
        subCommands = new Vector<String>();
        commandsSkipped = new Vector<String>();
        stationRotations = new Vector<String>();
        x = 100;
        y = 300;
        newX = x;
        newY = y;
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
                kit = kitAssemblyManager.getStationKit(i);
            }
            catch (Exception ignore) {}
            hasKit = true;
        }
        else if(ss[0].equals("d")){
            int i = Integer.parseInt(ss[1]);
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
            }
            else {
                processing = false;
            }
        }
    }

    public synchronized double getX(){
        return x;
    }

    public synchronized double getY(){
        return y;
    }

    public synchronized boolean getProcessing() {
        return processing;
    }

    public synchronized void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public synchronized boolean getPaused() {
        return paused;
    }

    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
    }

    public synchronized boolean getEmptyAvailable() {
        return emptyAvailable;
    }

    public synchronized void setEmptyAvailable(boolean emptyAvailable) {
        this.emptyAvailable = emptyAvailable;
    }

    public synchronized boolean getSpawnEmpty() {
        return spawnEmpty;
    }

    public synchronized void setSpawnEmpty(boolean spawnEmpty) {
        this.spawnEmpty = spawnEmpty;
    }

    public synchronized boolean getEmptyConveyorOn() {
        return emptyConveyorOn;
    }

    public synchronized void setEmptyConveyorOn(boolean emptyConveyorOn) {
        this.emptyConveyorOn = emptyConveyorOn;
    }

    public synchronized boolean getDoneConveyorOn() {
        return doneConveyorOn;
    }

    public synchronized void setDoneConveyorOn(boolean doneConveyorOn) {
        this.doneConveyorOn = doneConveyorOn;
    }

  /*  public synchronized double getNewExtension() {
        return newExtension;
    }

    public synchronized void setNewExtension(double newExtension) {
        this.newExtension = newExtension;
    }

    public synchronized double getNewAngle() {
        return newAngle;
    }

    public synchronized void setNewAngle(double newAngle) {
        this.newAngle = newAngle;
    }*/

    public synchronized Kit getKit() {
        return kit;
    }

    public synchronized void setKit(Kit kit) {
        this.kit = kit;
    }

    public synchronized Vector<String> getStationRotations() {
        return stationRotations;
    }

    public synchronized void setStationRotations(Vector<String> stationRotations) {
        this.stationRotations = stationRotations;
    }

    public synchronized Vector<String> getCommands() {
        return commands;
    }

    public synchronized void setCommands(Vector<String> commands) {
        this.commands = commands;
    }

    public synchronized Vector<String> getSubCommands() {
        return subCommands;
    }

    public synchronized void setSubCommands(Vector<String> subCommands) {
        this.subCommands = subCommands;
    }

    public synchronized Vector<String> getCommandsSkipped() {
        return commandsSkipped;
    }

    public synchronized void setCommandsSkipped(Vector<String> commandsSkipped) {
        this.commandsSkipped = commandsSkipped;
    }

    public synchronized KitAssemblyManager getKitAssemblyManager() {
        return kitAssemblyManager;
    }

    public synchronized void setKitAssemblyManager(
            KitAssemblyManager kitAssemblyManager) {
        this.kitAssemblyManager = kitAssemblyManager;
    }

    public synchronized Thread getThread() {
        return thread;
    }

    public synchronized void setThread(Thread thread) {
        this.thread = thread;
    }

    public synchronized void setHasKit(boolean hasKit) {
        this.hasKit = hasKit;
    }
	
    public synchronized boolean getHasKit() {
	    return hasKit;
	}
  /*  
    public synchronized void setExtension(double extension) {
        this.extension = extension;
    }

    public synchronized void setAngle(double angle) {
        this.angle = angle;
    }   

    public synchronized double getExtension() {
        return extension;
    }

    public synchronized double getAngle() {
        return angle;
    }*/

	public synchronized double getNewX() {
		return newX;
	}

	public synchronized void setNewX(double newX) {
		this.newX = newX;
	}

	public synchronized double getNewY() {
		return newY;
	}

	public synchronized void setNewY(double newY) {
		this.newY = newY;
	}

	public synchronized int[] getStationX() {
		return stationX;
	}

	public synchronized void setStationX(int[] stationX) {
		this.stationX = stationX;
	}

	public synchronized int[] getStationY() {
		return stationY;
	}

	public synchronized void setStationY(int[] stationY) {
		this.stationY = stationY;
	}

	public synchronized int[] getWaypointX() {
		return waypointX;
	}

	public synchronized void setWaypointX(int[] waypointX) {
		this.waypointX = waypointX;
	}

	public synchronized int[] getWaypointY() {
		return waypointY;
	}

	public synchronized void setWaypointY(int[] waypointY) {
		this.waypointY = waypointY;
	}

	public synchronized double getSpeed() {
		return speed;
	}

	public synchronized void setSpeed(double speed) {
		this.speed = speed;
	}

	public synchronized void setX(double x) {
		this.x = x;
	}

	public synchronized void setY(double y) {
		this.y = y;
	}
}
