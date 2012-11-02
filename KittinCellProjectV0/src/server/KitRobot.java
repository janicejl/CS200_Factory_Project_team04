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

import kitAssemblyManager.Kit;

public class KitRobot implements Runnable, Serializable{
    boolean processing = false;
    boolean hasKit = false;
    boolean paused = false;
    boolean emptyAvailable = false;
    boolean spawnEmpty = true;
    boolean emptyConveyorOn = true;
    boolean doneConveyorOn = false;
    double extension = 37;
    double newExtension = 37;
    double angle = 0.0;
    double newAngle = 0;
    Kit kit;

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
        stationRotations.add("r,13.34"); // 0 Empty Kit Station
        stationRotations.add("r,76.66"); // 1 Kit Stand 1
        stationRotations.add("r,103.34"); // 2 Kit stand 2
        stationRotations.add("r,166.66"); // 3 Bad Kit Station
        stationRotations.add("r,193.34"); // 4 incomplete kit station
        stationRotations.add("r,270"); // 5 Kit Check Station
        stationRotations.add("r,346.66"); // 6 finished kit station
    }

    private boolean processCommand(String[] ss){
        int src = 0;
        int dst = 0;
        boolean flag = true;
        try{
            src = Integer.parseInt(ss[1]);
            dst = Integer.parseInt(ss[2]);
        }
        catch (Exception e){}

        getSubCommands().add(0,stationRotations.get(src));
        getSubCommands().add(1,"e,65");
        getSubCommands().add(2,"p," + src);
        getSubCommands().add(3,"e,37");
        getSubCommands().add(4,stationRotations.get(dst));
        getSubCommands().add(5,"e,65");
        getSubCommands().add(6,"d," + dst);
        getSubCommands().add(7,"e,37");
        return true;
    }

    private void processSubCommand(String s){
        String[] ss = s.split("\\,");
        if (ss[0].equals("r")){
            try{
                setNewAngle(Double.parseDouble(ss[1]));
                setProcessing(true);
            }
            catch (Exception e){}
        }
        else if(ss[0].equals("e")){
            try{
                setNewExtension(Integer.parseInt(ss[1]));
                setProcessing(true);
            }
            catch (Exception e){}
        }
        else if(ss[0].equals("p")){
            int i = Integer.parseInt(ss[1]);
            try {
                setKit(getKitAssemblyManager().getStationKit(i));
            }
            catch (Exception ignore) {}
            setHasKit(true);
        }
        else if(ss[0].equals("d")){
            int i = Integer.parseInt(ss[1]);
            getKitAssemblyManager().setStationKit(i, getKit());
            setHasKit(false);
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
            if(getProcessing() == false){
                if(getSubCommands().size() > 0){
                    processSubCommand(getSubCommands().get(0));
                    getSubCommands().remove(0);
                }
                else {
                    for (int i = 0; i < getCommands().size(); i++){
                        s = getCommands().get(i);
                        ss = s.split("\\,");
                        if(ss[0].equals("load")) {
                            processCommand(ss);
                            getCommands().remove(i);
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
        getCommands().add(s);
    }

    public void pause(){
        setPaused(!getPaused());
    }

    public void update(){
        if(!getPaused()){
            if (getAngle() != getNewAngle()){
                if(getAngle() <= getNewAngle() && getNewAngle() - getAngle() <= 180.0){
                	setAngle(getAngle() + 5);
                }
                else if(getAngle() <= getNewAngle() && getNewAngle() - getAngle() > 180.0){
                	setAngle(getAngle() - 5);
                }
                else if(getAngle() > getNewAngle() && getAngle() - getNewAngle() <= 180.0){
                	setAngle(getAngle() - 5);
                }
                else if(getAngle() > getNewAngle() && getAngle() - getNewAngle() > 180.0){
                	setAngle(getAngle() + 5);
                }
                if(Math.abs(getAngle() - getNewAngle()) < 5){
                    setAngle(getNewAngle());
                }
                if(getAngle() >= 360.0){
                    setAngle(0.0);
                }
                if(getAngle() < 0.0){
                    setAngle(360.0);
                }

            }
            else if(getExtension() != getNewExtension()){
                if (getNewExtension() > getExtension()){
                	setExtension(getExtension() + 2);
                }
                else {
                	setExtension(getExtension() - 2);
                }
                if(Math.abs(getExtension() - getNewExtension()) < 2){
                	setExtension(getNewExtension());
                }
            }
            else {
                setProcessing(false);
            }
        }
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

	public synchronized double getNewExtension() {
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
	}

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

	public synchronized void setExtension(double extension) {
		this.extension = extension;
	}

	public synchronized void setAngle(double angle) {
		this.angle = angle;
	}

	public synchronized boolean getHasKit() {
		return hasKit;
	}

	public synchronized double getExtension() {
		return extension;
	}

	public synchronized double getAngle() {
		return angle;
	}
}
