package kitAssemblyManager;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;

public class KitAssemblyManager implements Runnable{
    ArrayList<Boolean> stationOccupied;
    ArrayList<Kit> emptyKits;
    ArrayList<Kit> finishedKits;
    ArrayList<Kit> stationKits;
    boolean finishedConveyorOn;
    boolean emptyConveyorOn;
    boolean badConveyorOn;
    boolean incompleteConveyorOn;
    int idCounter;
    double conveyorSpeed = 1;

    public KitAssemblyManager(){
        stationOccupied = new ArrayList<Boolean>();
        stationKits = new ArrayList<Kit>();
        idCounter = 0;
        for(int i = 0; i < 7; i++){
            stationOccupied.add(false);
            stationKits.add(new Kit(idCounter));
            if(i == 3){
                stationKits.get(i).setPosition(80,700);
            }
            else if(i == 4){
                stationKits.get(i).setPosition(10,700);
            }
            idCounter++;
        }

        emptyKits = new ArrayList<Kit>();
        finishedKits = new ArrayList<Kit>();
    }

    @Override
    public void run(){
        while(true){
            update();
            try {
                Thread.sleep(10);  // milliseconds
            } catch (InterruptedException ignore) {}
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

    public synchronized ArrayList<Kit> getEmptyKits(){
        return emptyKits;
    }

    public synchronized ArrayList<Kit> getFinishedKits(){
        return finishedKits;
    }

    public synchronized ArrayList<Kit> getStationKits(){
        return stationKits;
    }

    public synchronized ArrayList<Boolean> getStationOccupied(){
        return stationOccupied;
    }

    public synchronized Kit getStationKit(int i){
        stationOccupied.set(i,false);
        if(i == 0){
            Kit k = emptyKits.get(0);
            emptyKits.remove(0);
            return k;
        }
        else {
            return stationKits.get(i);
        }
    }

    public synchronized void setStationKit(int i, Kit k){
        stationOccupied.set(i,true);
        if(i == 6){
            finishedKits.add(k);
        }
        else if(i == 3){
            System.out.println(stationKits.get(4).getY() + " - " + stationKits.get(3).getY());
            stationKits.set(i,k);
            stationKits.get(3).setPosition(80,490);
            System.out.println(stationKits.get(4).getY() + " - " + stationKits.get(3).getY());
        }
        else if(i == 4){
            System.out.println(stationKits.get(4).getY() + " - " + stationKits.get(3).getY());
            stationKits.set(i,k);
            stationKits.get(4).setPosition(10,490);
            System.out.println(stationKits.get(4).getY() + " - " + stationKits.get(3).getY());
        }
        else {
            stationKits.set(i,k);
        }
        System.out.println(stationKits.get(4).getY() + " - " + stationKits.get(3).getY());
    }

    public void processCommand(String s){
        String[] ss = s.split("\\,");
        if(ss[0].equals("spawn")){
            Kit temp = new Kit(idCounter);
            int sz = emptyKits.size();
            if(sz > 0 && emptyKits.get(sz-1).getY() < 0){
                temp.setPosition(80,emptyKits.get(sz-1).getY() - 110);
            }
            emptyKits.add(temp);
            idCounter++;
        }
    }

    public void update(){
        emptyConveyorOn = !stationOccupied.get(0) && (emptyKits.size() > 0);
        finishedConveyorOn = (finishedKits.size() > 0);
        badConveyorOn = stationKits.get(4).getY() < 600;
        incompleteConveyorOn = stationKits.get(3).getY() < 600;

        if(emptyConveyorOn){
            for(Kit k : getEmptyKits()){
                k.setPosition(k.getX(),k.getY() + conveyorSpeed);
            }
        }

        if(emptyKits.size() > 0 && emptyKits.get(0).getY() >= 10.0){ // check if empty kit is ready to pickup
            stationOccupied.set(0,true);
        }

        if(finishedConveyorOn){
            for(Kit k : finishedKits){
                k.setPosition(k.getX(),k.getY() - conveyorSpeed);
            }
        }
        if(incompleteConveyorOn){
            stationKits.get(3).setPosition(80,stationKits.get(3).getY() + conveyorSpeed);
        }

        if(badConveyorOn){
            stationKits.get(4).setPosition(80,stationKits.get(4).getY() + conveyorSpeed);
        }

        if(finishedKits.size() > 0){
            if(finishedKits.get(finishedKits.size()-1).getY() < -100.0){ // check if finished station is clear
                stationOccupied.set(6,false);
            }
            if(finishedKits.get(0).getY() < -200.0){ // check is image is off screen
                finishedKits.remove(0);
            }
        }
    }
}
