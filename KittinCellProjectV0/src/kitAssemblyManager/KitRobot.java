package kitAssemblyManager;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.io.*;
import javax.imageio.*;

public class KitRobot implements Runnable{
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

    ArrayList<String> commands;
    ArrayList<String> subCommands;
    ArrayList<String> commandsSkipped;
    KitAssemblyManager kitAssemblyManager;

    public KitRobot(KitAssemblyManager kam){
        kitAssemblyManager = kam;
        commands = new ArrayList<String>();
        subCommands = new ArrayList<String>();
        commandsSkipped = new ArrayList<String>();
        x = 100;
        y = 300;
        newX = x;
        newY = y;
    }

    public boolean getHasKit(){
        return hasKit;
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

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
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
}
