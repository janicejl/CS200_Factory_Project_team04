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
    double extension = 37;
    double newExtension = 37;
    double angle = 0.0;
    double newAngle = 0;
    Kit kit;

    ArrayList<String> stationRotations;
    ArrayList<String> commands;
    ArrayList<String> subCommands;
    ArrayList<String> commandsSkipped;
    KitAssemblyManager kitAssemblyManager;

    public KitRobot(KitAssemblyManager kam){
        kitAssemblyManager = kam;
        commands = new ArrayList<String>();
        subCommands = new ArrayList<String>();
        commandsSkipped = new ArrayList<String>();
        stationRotations = new ArrayList<String>();
        stationRotations.add("r,13.34"); // Empty Kit Station
        stationRotations.add("r,76.66"); // Kit Stand 1
        stationRotations.add("r,103.34"); // Kit stand 2
        stationRotations.add("r,166.66"); // Bad Kit Station
        stationRotations.add("r,193.34"); // incomplete kit station
        stationRotations.add("r,270"); // Kit Check Station
        stationRotations.add("r,346.66"); // finished kit station
    }

    public double getAngle(){
        return angle;
    }

    public double getExtension(){
        return extension;
    }

    public boolean getHasKit(){
        return hasKit;
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

        subCommands.add(0,stationRotations.get(src));
        subCommands.add(1,"e,65");
        subCommands.add(2,"p," + src);
        subCommands.add(3,"e,37");
        subCommands.add(4,stationRotations.get(dst));
        subCommands.add(5,"e,65");
        subCommands.add(6,"d," + dst);
        subCommands.add(7,"e,37");
        return true;
    }

    private void processSubCommand(String s){
        String[] ss = s.split("\\,");
        if (ss[0].equals("r")){
            try{
                newAngle = Double.parseDouble(ss[1]);
                processing = true;
            }
            catch (Exception e){}
        }
        else if(ss[0].equals("e")){
            try{
                newExtension = Integer.parseInt(ss[1]);
                processing = true;
            }
            catch (Exception e){}
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
            if (angle != newAngle){
                if(angle <= newAngle && newAngle - angle <= 180.0){
                    angle += 5;
                }
                else if(angle <= newAngle && newAngle - angle > 180.0){
                    angle -= 5;
                }
                else if(angle > newAngle && angle - newAngle <= 180.0){
                    angle -= 5;
                }
                else if(angle > newAngle && angle - newAngle > 180.0){
                    angle += 5;
                }
                if(Math.abs(angle - newAngle) < 5){
                    angle = newAngle;
                }
                if(angle >= 360.0){
                    angle = 0.0;
                }
                if(angle < 0.0){
                    angle = 360.0;
                }

            }
            else if(extension != newExtension){
                if (newExtension > extension){
                    extension+= 2;
                }
                else {
                    extension-= 2;
                }
                if(Math.abs(extension - newExtension) < 2){
                    extension = newExtension;
                }
            }
            else {
                processing = false;
            }
        }
    }
}
