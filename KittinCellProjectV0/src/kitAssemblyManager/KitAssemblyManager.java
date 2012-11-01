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
    int idCounter;
    double conveyorSpeed = 1;

    public KitAssemblyManager(){
        stationOccupied = new ArrayList<Boolean>();
        stationKits = new ArrayList<Kit>();
        for(int i = 0; i < 7; i++){
            stationOccupied.add(false);
            stationKits.add(new Kit(idCounter));
            idCounter++;
        }

        emptyKits = new ArrayList<Kit>();
        finishedKits = new ArrayList<Kit>();
        finishedConveyorOn = true;
        emptyConveyorOn = true;
        idCounter = 0;
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

    public Kit getStationKit(int i){
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

    public void setStationKit(int i, Kit k){
        if(i == 6){
            finishedKits.add(k);
        }
        else {
            stationOccupied.set(i,true);
            stationKits.set(i,k);
        }
    }

    public void processCommand(String s){
        String[] ss = s.split("\\,");
        if(ss[0].equals("spawn")){
            Kit temp = new Kit(idCounter);
            int sz = emptyKits.size();
            if(sz > 0 && emptyKits.get(sz-1).getY() < 0){
                temp.setPosition(150,emptyKits.get(sz-1).getY() - 60);
            }
            emptyKits.add(temp);
            idCounter++;
        }
    }

    public void update(){
        emptyConveyorOn = !stationOccupied.get(0) && (emptyKits.size() > 0);
        finishedConveyorOn = (finishedKits.size() > 0);

        if(emptyConveyorOn){
            for(Kit k : getEmptyKits()){
                k.setPosition(k.getX(),k.getY() + conveyorSpeed);
            }
        }

        if(emptyKits.size() > 0 && emptyKits.get(0).getY() >= 155.0){
            stationOccupied.set(0,true);
        }

        if(finishedConveyorOn){
            for(Kit k : finishedKits){
                k.setPosition(k.getX(),k.getY() - conveyorSpeed);
            }
        }

        if(finishedKits.size() > 0){
            if(finishedKits.get(finishedKits.size()-1).getY() < 155.0){
                stationOccupied.set(6,false);
            }
            if(finishedKits.get(0).getY() < -140.0){
                finishedKits.remove(0);
            }
        }
    }
}
