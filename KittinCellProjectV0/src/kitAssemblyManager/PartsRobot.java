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

public class PartsRobot implements Runnable{
    boolean processing;
    boolean paused;
    boolean hasCrate;
    double x;
    double y;
    double newX;
    double newY;
    double angle;
    double newAngle;
    boolean takePicture;

    BufferedImage partsRobotImage;

    ArrayList<Boolean> gripperHolding;
    ArrayList<Double> gripperExtensions;
    ArrayList<Double> newGripperExtensions;
    ArrayList<String> commands;
    ArrayList<String> subCommands;
    ArrayList<String> nestLocations;
    ArrayList<String> kitLocations;
    int[] nl = {125,175,225,275,325,375,425,475};
    int[] kl = {270,330};

    public PartsRobot(){
        y = 265;
        newY = 265;
        takePicture = false;
        gripperHolding = new ArrayList<Boolean>();
        gripperExtensions = new ArrayList<Double>();
        newGripperExtensions = new ArrayList<Double>();
        nestLocations = new ArrayList<String>();
        kitLocations = new ArrayList<String>();
        for(int i = 0; i < 4; i++){
            gripperHolding.add(false);
            gripperExtensions.add(0.0);
            newGripperExtensions.add(0.0);
        }
        commands = new ArrayList<String>();
        subCommands = new ArrayList<String>();

        try {
            partsRobotImage = ImageIO.read(new File("crate.png"));
        }
        catch (IOException e) {
        }
    }

    private void processCommand(String[] ss){
        int src = 0;
        int dst = 0;
        int grp = 0;
        int offset = 0;
        if(ss[0].equals("move")) {
            try{
                dst = Integer.parseInt(ss[1]);
                //dst = Integer.parseInt(ss[2]);
            }
            catch (Exception e){}
            if(dst == 1){
                subCommands.add(0,"m,0");
            }
            else {
                subCommands.add(0,"m,520");
            }
        }
        else if(ss[0].equals("grab")) {
            try{
                src = Integer.parseInt(ss[1]);
                grp = Integer.parseInt(ss[2]);
            }
            catch (Exception e){}
            if (grp == 0) {
                offset = 24;
            }
            else if(grp == 1){
                offset = 8;
            }
            else if(grp == 2){
                offset = -8;
            }
            else if (grp == 3) {
                offset = -24;
            }

            subCommands.add(0,"r,0");
            subCommands.add(1,"m," + (nl[src] + offset));
            subCommands.add(2,"e," + grp + ",20");
            subCommands.add(3,"p," + grp);
            subCommands.add(4,"e," + grp + ",0");
        }
        else if(ss[0].equals("dump")) {
            try{
                dst = Integer.parseInt(ss[1]);
                //grp = Integer.parseInt(ss[2]);
            }
            catch (Exception e){}

            subCommands.add(0,"r,180");
            int j = 0;
            for (int i = 0; i < 4; i++) {
                if (gripperHolding.get(i)) {
                    if (i == 0) {
                        offset = 24;
                    }
                    else if(i == 1){
                        offset = 8;
                    }
                    else if(i == 2){
                        offset = -8;
                    }
                    else if (i == 3) {
                        offset = -24;
                    }
                    subCommands.add(j*4+1,"m," +(kl[dst] - offset));
                    subCommands.add(j*4+2,"e," + i + ",20");
                    subCommands.add(j*4+3,"d," + i);
                    subCommands.add(j*4+4,"e," + i + ",0");
                    j++;
                }
            }
        }
    }

    public void takePicture(){
        takePicture = true;
    }

    private void processSubCommand(String s){
        try {
            String[] ss = s.split("\\,");
            if(ss[0].equals("m")){
                newY = Double.parseDouble(ss[1]);
                processing = true;
                if(subCommands.size() > 1){
                    String sc = subCommands.get(1);
                    String[] ssc = sc.split("\\,");
                    if("r".equals(ssc[0])){
                        newAngle = Double.parseDouble(ssc[1]);
                        subCommands.remove(0);
                    }
                }
            }
            else if (ss[0].equals("r")){
                newAngle = Double.parseDouble(ss[1]);
                processing = true;
                if(subCommands.size() > 1){
                    String sc = subCommands.get(1);
                    String[] ssc = sc.split("\\,");
                    if("m".equals(ssc[0])){
                        newY = Double.parseDouble(ssc[1]);
                        // newAngle = Double.parseDouble(ss[1]);
                        subCommands.remove(0);
                    }
                }
            }
            else if(ss[0].equals("e")){
                newGripperExtensions.set(Integer.parseInt(ss[1]), Double.parseDouble(ss[2]));
                processing = true;
            }
            else if(ss[0].equals("p")){
                gripperHolding.set(Integer.parseInt(ss[1]),true);
                processing = true;
            }
            else if(ss[0].equals("d")){
                gripperHolding.set(Integer.parseInt(ss[1]),false);
                processing = true;
            }
        } catch (Exception ignore){}

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
                        processCommand(ss);
                        commands.remove(i);
                        break;
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {}

        }
    }

    public void addCommand(String s){
        System.out.println("add: " + s);
        commands.add(s);
    }

    public double getY(){
        return y;
    }

    public double getAngle(){
        return angle;
    }

    public ArrayList<Boolean> getGripperHolding(){
        return gripperHolding;
    }

    public ArrayList<Double> getGripperExtensions(){
        return gripperExtensions;
    }
    public boolean getTakePicture(){
        if(takePicture){
            takePicture = false;
            return true;
        }
        else {
            return false;
        }
    }

    public void update(){
        if(!paused){
            processing = false;
            if(y != newY){
                processing = true;
                if(y < newY){
                    y+=17;
                }
                else {
                    y -=17;
                }
                if(Math.abs(y - newY) < 17){
                    y = newY;
                }
            }
            if (angle != newAngle){
                processing = true;
                if(angle < newAngle){
                    angle += 7;
                }
                else {
                    angle += 7;
                    if(angle >= 360.0){
                        angle = 0.0;
                    }
                }
                if(Math.abs(angle - newAngle) <= 7){
                    angle = newAngle;
                }
            }
            for (int i = 0 ;i<4 ; i++) {
                if(newGripperExtensions.get(i) != gripperExtensions.get(i)){
                    processing = true;
                    if(newGripperExtensions.get(i) > gripperExtensions.get(i)){
                        gripperExtensions.set(i,gripperExtensions.get(i) + 1.5);
                    }
                    else {
                        gripperExtensions.set(i,gripperExtensions.get(i) - 1.5);
                    }
                    if(Math.abs(newGripperExtensions.get(i) - gripperExtensions.get(i)) < 1) {
                        gripperExtensions.set(i,newGripperExtensions.get(i));
                    }
                }
            }
        }
    }
}