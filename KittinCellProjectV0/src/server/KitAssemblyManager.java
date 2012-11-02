package server;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;

import kitAssemblyManager.Kit;

public class KitAssemblyManager implements Runnable, Serializable{
    Vector<Boolean> stationOccupied;
    Vector<Kit> emptyKits;
    Vector<Kit> finishedKits;
    Vector<Kit> stationKits;
    Boolean finishedConveyorOn;
    Boolean emptyConveyorOn;
    int idCounter;
    double conveyorSpeed = 1;
    Boolean msg;
    
    public KitAssemblyManager(){
        stationOccupied = new Vector<Boolean>();
        stationKits = new Vector<Kit>();
        for(int i = 0; i < 7; i++){
            stationOccupied.add(false);
            stationKits.add(new Kit(idCounter));
            idCounter++;
        }

        emptyKits = new Vector<Kit>();
        finishedKits = new Vector<Kit>();
        finishedConveyorOn = new Boolean(true);
        emptyConveyorOn = new Boolean(true);
        msg = new Boolean(false);
        idCounter = 0;
    
    }

    public void run(){
        while(true){
            update();
            try {
                Thread.sleep(10);  // milliseconds
            } catch (InterruptedException ignore) {}
        }
    }
    
	public void processCommand(String s){
        String[] ss = s.split("\\,");
        if(ss[0].equals("spawn")){
            Kit temp = new Kit(getIdCounter());
            int sz = getEmptyKits().size();
            if(sz > 0 && getEmptyKits().get(sz-1).getY() < 0){
                temp.setPosition(150,getEmptyKits().get(sz-1).getY() - 60);
            }
            getEmptyKits().add(temp);
            setIdCounter(getIdCounter() + 1);
        }
    }

    public void update(){
        setEmptyConveyorOn(!getStationOccupied().get(0) && (getEmptyKits().size() > 0)); //conveyer on if there are kits spawned and station is not occupied
        setFinishedConveyorOn((getFinishedKits().size() > 0));

        if(getEmptyConveyorOn()){
            for(Kit k : getEmptyKits()){
                k.setPosition(k.getX(),k.getY() + getConveyorSpeed());
            }
        }

        if(getEmptyKits().size() > 0 && getEmptyKits().get(0).getY() >= 155.0){
            getStationOccupied().set(0,true);
            setMsg(true);
        }

        if(getFinishedConveyorOn()){
            for(Kit k : getFinishedKits()){
                k.setPosition(k.getX(),k.getY() - getConveyorSpeed());
            }
        }

        if(getFinishedKits().size() > 0){
            if(getFinishedKits().get(getFinishedKits().size()-1).getY() < 155.0){
                getStationOccupied().set(6,false);
            }
            if(getFinishedKits().get(0).getY() < -140.0){
                getFinishedKits().remove(0);
            }
        }
    }

    public Kit getStationKit(int i){
        getStationOccupied().set(i,false);
        if(i == 0){
            Kit k = getEmptyKits().get(0);
            getEmptyKits().remove(0);
            return k;
        }
        else {
            return getStationKits().get(i);
        }
    }

    public void setStationKit(int i, Kit k){
        if(i == 6){
            getFinishedKits().add(k);
        }
        else {
            getStationOccupied().set(i,true);
            getStationKits().set(i,k);
        }
    }
    
    public synchronized boolean getEmptyConveyorOn(){
        return emptyConveyorOn;
    }

    public synchronized boolean getFinishedConveyorOn(){
        return finishedConveyorOn;
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

	public synchronized void setFinishedConveyorOn(Boolean finishedConveyorOn) {
		this.finishedConveyorOn = finishedConveyorOn;
	}

	public synchronized void setEmptyConveyorOn(Boolean emptyConveyorOn) {
		this.emptyConveyorOn = emptyConveyorOn;
	}

	public synchronized Boolean getMsg() {
		return msg;
	}

	public synchronized void setMsg(Boolean msg) {
		this.msg = msg;
	}
}
