package server;

import java.util.*;
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
    Vector<Boolean> stationOccupied;
    Vector<Kit> emptyKits;
    Vector<Kit> finishedKits;
    Vector<Kit> stationKits;
    Vector<Nest> nests;
    boolean finishedConveyorOn;
    boolean emptyConveyorOn;
    boolean badConveyorOn;
    boolean incompleteConveyorOn;
    Boolean msg;
    int idCounter;
    double conveyorSpeed = 1;
   
    boolean checked = false;

    public KitAssemblyManager(Vector<Nest> n){
    	nests = n;
        stationOccupied = new Vector<Boolean>();
        stationKits = new Vector<Kit>();
        idCounter = 0;
        for(int i = 0; i < 7; i++){
            stationOccupied.add(false);
            stationKits.add(new Kit(""+idCounter, 80, -110));		//80, -110 from Jared's kit file. 
            if(i == 3){
                stationKits.get(i).setPosition(80,700);
            }
            else if(i == 4){
                stationKits.get(i).setPosition(10,700);
            }
            idCounter++;
        }

        emptyKits = new Vector<Kit>();
        finishedKits = new Vector<Kit>();
        /*nests = new Vector<Nest>();
        for (int i = 0; i < 8; i++) {
        	//for v0
        	nests.add(new Nest(320, 30+(i*70), i)); //Setting the position of the nest properly. 
        }*/
        msg = new Boolean(false);
    }

    public void run(){
        while(true){
            update();
            try {
                Thread.sleep(10);  // milliseconds
            } catch (InterruptedException ignore) {}
        }
    }

    public synchronized Kit getStationKit(int i){
        getStationOccupied().set(i,false);
        if(i == 0){
            Kit k = getEmptyKits().get(0);
            getEmptyKits().remove(0);
            setMsg(false);
            checked = false;
            return k;
        }
        else {
            return getStationKits().get(i);
        }
    }

    public synchronized void setStationKit(int i, Kit k){
    	getStationOccupied().set(i,true);
        if(i == 6){
            getFinishedKits().add(k);
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
        else {
        	getStationKits().set(i,k);
        }
        System.out.println(getStationKits().get(4).getY() + " - " + getStationKits().get(3).getY());
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
        setBadConveyorOn(getStationKits().get(4).getY() < 600);
        setIncompleteConveyorOn(getStationKits().get(3).getY() < 600);

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

    public synchronized Vector<Kit> getEmptyKits(){
        return emptyKits;
    }

    public synchronized Vector<Kit> getFinishedKits(){
        return finishedKits;
    }

    public synchronized Vector<Kit> getStationKits(){
        return stationKits;
    }

    public synchronized Vector<Boolean> getStationOccupied(){
        return stationOccupied;
    }
    
    public synchronized Vector<Nest> getNests() {
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

	public synchronized void setStationOccupied(Vector<Boolean> stationOccupied) {
		this.stationOccupied = stationOccupied;
	}

	public synchronized void setEmptyKits(Vector<Kit> emptyKits) {
		this.emptyKits = emptyKits;
	}

	public synchronized void setFinishedKits(Vector<Kit> finishedKits) {
		this.finishedKits = finishedKits;
	}

	public synchronized void setStationKits(Vector<Kit> stationKits) {
		this.stationKits = stationKits;
	}

	public synchronized void setFinishedConveyorOn(boolean finishedConveyorOn) {
		this.finishedConveyorOn = finishedConveyorOn;
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
}
