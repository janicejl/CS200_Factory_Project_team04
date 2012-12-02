package server;

import java.util.*;
import java.util.concurrent.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;

//import kitAssemblyManager.Kit;
import data.Kit;

import laneManager.Nest;

public class KitAssemblyManager implements Runnable, Serializable{
    CopyOnWriteArrayList<Boolean> stationOccupied;
    CopyOnWriteArrayList<Kit> emptyKits;
    CopyOnWriteArrayList<Kit> finishedKits;
    CopyOnWriteArrayList<Kit> stationKits;
    CopyOnWriteArrayList<Kit> badKits;
    CopyOnWriteArrayList<Nest> nests;
    CopyOnWriteArrayList<Boolean> partsDropped;
    boolean finishedConveyorOn;
    boolean emptyConveyorOn;
    boolean badConveyorOn = true;
    boolean incompleteConveyorOn = true;
    boolean kitStandMsg;
    Boolean msg;
    int idCounter;
    double conveyorSpeed = 1;
   
    boolean checked = false;
    boolean inspected = false;

    /*
    id  name    corX  corY
    0   empty   80    10
    1   kit 1   160   140
    2   kit 2   160   360
    3   inc     80    490		//Bad Kit Out
    4   bad     10    490		//Bad Kit In
    5   check   160   250
    6   done    10    10
    */
    
    public KitAssemblyManager(CopyOnWriteArrayList<Nest> n){
    	nests = n;
        stationOccupied = new CopyOnWriteArrayList<Boolean>();
        stationKits = new CopyOnWriteArrayList<Kit>();
        partsDropped = new CopyOnWriteArrayList<Boolean>();
        idCounter = 0;
        for(int i = 0; i < 7; i++){
            stationOccupied.add(false);
            partsDropped.add(false);
            stationKits.add(new Kit(""+idCounter, 80, -110));		//80, -110 from Jared's kit file. 
            if(i == 3){
                stationKits.get(i).setPosition(80,700);
            }
            else if(i == 4){
                stationKits.get(i).setPosition(10,700);
            }
            idCounter++;
        }
        partsDropped.add(false);
        partsDropped.add(false);

        emptyKits = new CopyOnWriteArrayList<Kit>();
        finishedKits = new CopyOnWriteArrayList<Kit>();
        badKits = new CopyOnWriteArrayList<Kit>();
        msg = new Boolean(false);
        kitStandMsg = false;
    }

    public void run(){
        while(true){
            update();
            try {
                Thread.sleep(10);  // milliseconds
            } catch (InterruptedException ignore) {}
        }
    }

    public Kit getStationKit(int i){
        if(i == 0){
            Kit k = getEmptyKits().get(0);
            getEmptyKits().remove(0);
            setMsg(false);
            checked = false;
            return k;
        }
        else {
            return stationKits.get(i);
        }
    }
    
    public void setSingleStationOccupied(int i, boolean b){
        stationOccupied.set(i,b);
    }

    public void setStationKit(int i, Kit k){
    	stationOccupied.set(i,true);
        if(i == 1){
        	stationKits.set(i,k);
            stationKits.get(1).setPosition(160,140);
            kitStandMsg = true;
        }
        else if(i == 2){
        	getStationKits().set(i,k);
        	getStationKits().get(2).setPosition(160, 360);
        	kitStandMsg = true;
        }
        else if(i == 3){
            System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
            getStationKits().set(i,k);
            getStationKits().get(3).setPosition(80,490);
            System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
        }
        else if(i == 4){
            System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
            getStationKits().set(i,k);
            getStationKits().get(4).setPosition(10,490);
            System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
        }
        else if(i == 5){
        	getStationKits().set(i,k);
        	getStationKits().get(5).setPosition(160, 250);
        	for(int j = 0; j < getStationKits().get(5).getParts().size(); j++){
        		if(partsDropped.get(j+1)){
        			getStationKits().get(5).getParts().get(j).setPartDropped(true);
        			partsDropped.set(j+1, false);
        		}
        	}
        	inspected = true;
        }
        else if(i == 6){
        	k.setPosition(10, 10);
        	getFinishedKits().add(k);
        }
        System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
    }

    public boolean isInspected() {
		return inspected;
	}

	public void setInspected(boolean inspected) {
		this.inspected = inspected;
	}

	public void processCommand(String s){
        String[] ss = s.split("\\,");
        if(ss[0].equals("spawn")){
            Kit temp = new Kit(""+getIdCounter(), 80, -110);
            int sz = getEmptyKits().size();
            if(sz > 0 && getEmptyKits().get(sz-1).getY() < 0){
                temp.setPosition(80,getEmptyKits().get(sz-1).getY() - 110);
            }
            getEmptyKits().add(temp);
            setIdCounter(getIdCounter() + 1);
        }
    }

    public void update(){
        setEmptyConveyorOn(!getStationOccupied().get(0) && (getEmptyKits().size() > 0));
        setFinishedConveyorOn((getFinishedKits().size() > 0));
       // setBadConveyorOn(getStationKits().get(4).getY() < 600);
        //setIncompleteConveyorOn(getStationKits().get(3).getY() < 600);

        if(getEmptyConveyorOn()){
            for(Kit k : getEmptyKits()){
                k.setPosition(k.getX(),k.getY() + getConveyorSpeed());
            }
        }

        if(getEmptyKits().size() > 0 && getEmptyKits().get(0).getY() >= 10.0 && checked == false){ // check if empty kit is ready to pickup
            getStationOccupied().set(0,true);
            setMsg(true);
            checked = true;
        }

        if(getFinishedConveyorOn()){
            for(Kit k : getFinishedKits()){
                k.setPosition(k.getX(),k.getY() - getConveyorSpeed());
            }
        }
        if(getIncompleteConveyorOn()){
            getStationKits().get(3).setPosition(80,getStationKits().get(3).getY() + getConveyorSpeed());
        }

        if(getBadConveyorOn()){
            getStationKits().get(4).setPosition(80,getStationKits().get(4).getY() + getConveyorSpeed());
        }

        if(getFinishedKits().size() > 0){
            if(getFinishedKits().get(getFinishedKits().size()-1).getY() < -100.0){ // check if finished station is clear
                getStationOccupied().set(6,false);
            }
            if(getFinishedKits().get(0).getY() < -200.0){ // check is image is off screen
                getFinishedKits().remove(0);
            }
        }
    }
    
    public synchronized void setPartsDropped(CopyOnWriteArrayList<Boolean> v){
    	partsDropped = v;
    }

    
    public synchronized boolean getEmptyConveyorOn(){
        return emptyConveyorOn;
    }

    public synchronized boolean getFinishedConveyorOn(){
        return finishedConveyorOn;
    }

    public synchronized boolean getBadConveyorOn(){
        return badConveyorOn;
    }

    public synchronized boolean getIncompleteConveyorOn(){
        return incompleteConveyorOn;
    }

    public synchronized CopyOnWriteArrayList<Kit> getEmptyKits(){
        return emptyKits;
    }

    public synchronized CopyOnWriteArrayList<Kit> getFinishedKits(){
        return finishedKits;
    }

    public synchronized CopyOnWriteArrayList<Kit> getStationKits(){
        return stationKits;
    }

    public synchronized CopyOnWriteArrayList<Boolean> getStationOccupied(){
        return stationOccupied;
    }
    
    public synchronized CopyOnWriteArrayList<Kit> getBadKits() {
    	return badKits;
    }
    
    public synchronized CopyOnWriteArrayList<Nest> getNests() {
    	return nests;
    }
    
	public synchronized Boolean getMsg() {
		return msg;
	}

	public synchronized void setMsg(Boolean msg) {
		this.msg = msg;
	}

	public synchronized int getIdCounter() {
		return idCounter;
	}

	public synchronized void setIdCounter(int idCounter) {
		this.idCounter = idCounter;
	}

	public synchronized double getConveyorSpeed() {
		return conveyorSpeed;
	}

	public synchronized void setConveyorSpeed(double conveyorSpeed) {
		this.conveyorSpeed = conveyorSpeed;
	}

	public synchronized void setStationOccupied(CopyOnWriteArrayList<Boolean> stationOccupied) {
		this.stationOccupied = stationOccupied;
	}

	public synchronized void setEmptyKits(CopyOnWriteArrayList<Kit> emptyKits) {
		this.emptyKits = emptyKits;
	}

	public synchronized void setFinishedKits(CopyOnWriteArrayList<Kit> finishedKits) {
		this.finishedKits = finishedKits;
	}

	public synchronized void setStationKits(CopyOnWriteArrayList<Kit> stationKits) {
		this.stationKits = stationKits;
	}

	public synchronized void setFinishedConveyorOn(boolean finishedConveyorOn) {
		this.finishedConveyorOn = finishedConveyorOn;
	}

	public synchronized void setBadKits(CopyOnWriteArrayList<Kit> badKits) {
		this.badKits = badKits;
	}
	
	public synchronized void setEmptyConveyorOn(boolean emptyConveyorOn) {
		this.emptyConveyorOn = emptyConveyorOn;
	}

	public synchronized void setBadConveyorOn(boolean badConveyorOn) {
		this.badConveyorOn = badConveyorOn;
	}

	public synchronized void setIncompleteConveyorOn(boolean incompleteConveyorOn) {
		this.incompleteConveyorOn = incompleteConveyorOn;
	}

	public synchronized boolean isChecked() {
		return checked;
	}

	public synchronized void setChecked(boolean checked) {
		this.checked = checked;
	}

	public synchronized boolean isKitStandMsg() {
		return kitStandMsg;
	}

	public synchronized void setKitStandMsg(boolean kitStandMsg) {
		this.kitStandMsg = kitStandMsg;
	}

	public synchronized CopyOnWriteArrayList<Boolean> getPartsDropped() {
		return partsDropped;
	}
}
