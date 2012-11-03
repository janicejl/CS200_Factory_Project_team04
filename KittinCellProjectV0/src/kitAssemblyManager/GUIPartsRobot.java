package kitAssemblyManager;


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

import server.PartsRobot;

public class GUIPartsRobot{
    BufferedImage partsRobotImage;
    BufferedImage partsRobotRailImage;
    BufferedImage partsRobotGripperImage;
    BufferedImage gripperArmImage;
    BufferedImage topImage;
    BufferedImage part;
    BufferedImage flash;
    boolean takePicture, flashUp, flashDown;

    Vector<Boolean> gripperHolding;
    Vector<Double> gripperExtensions;
    Vector<String> commands;
    Vector<String> subCommands;
    Vector<String> nestLocations;
    Vector<String> kitLocations;
    float opacity, flashCounter;

    PartsRobot pr;

    double x;
    double y;
    double angle;

    KitAssemblyApp server;

    public GUIPartsRobot(KitAssemblyApp s){
        server = s;
        x = 265;
        y = 300;
        angle = 0;
        opacity = 0.01f;
        flashCounter = 1.0f;
        gripperExtensions = new Vector<Double>();
        gripperHolding = new Vector<Boolean>();
        commands = new Vector<String>();
        subCommands = new Vector<String>();
        nestLocations = new Vector<String>();
        kitLocations = new Vector<String>();

        for (int i = 0; i < 4; i++) {
            gripperExtensions.add(0.0);
        }

        try {
            partsRobotImage = ImageIO.read(new File("images/partsrobot.png"));
            partsRobotRailImage = ImageIO.read(new File("images/rail.png"));
            partsRobotGripperImage = ImageIO.read(new File("images/gripper.png"));
            gripperArmImage = ImageIO.read(new File("images/grip_arm.png"));
            topImage = ImageIO.read(new File("images/top.png"));
            part = ImageIO.read(new File("images/part.png"));
            flash = ImageIO.read(new File("images/flash.png"));
        }
        catch (IOException e) {
        }
    }

    public void update(){
        pr = server.getPartsRobot();
        y = pr.getY();
        angle = pr.getAngle();
        gripperExtensions = pr.getGripperExtensions();
        gripperHolding = pr.getGripperHolding();
        if(takePicture == false && pr.getTakePicture()){
            takePicture = true;
            flashUp = true;
            flashCounter = 0;
            opacity = 0;
        }
    }

    public void takePicture(Graphics2D g2){

    }

    public void paintPartsRobot(Graphics2D g2){
        AffineTransform at = new AffineTransform(); // transform object

        for (int i = 0; i < 4; i++) {
            at = new AffineTransform();
            at.translate((int)x,(int)y);
            at.rotate(Math.toRadians(angle + i*90));
            at.translate(-18,-41 - gripperExtensions.get(i).intValue()); // gripper 0
            g2.drawImage(partsRobotGripperImage,at,null);
            if(gripperHolding.get(i)){
                at.translate(5,0);
                g2.drawImage(part,at,null);
                at.translate(-5,0);
            }
            at.translate(13,26);
            at.scale(1,gripperExtensions.get(i)/40);
            g2.drawImage(gripperArmImage,at,null);
        }
        at = new AffineTransform();
        at.translate(x,y);
        at.rotate(Math.toRadians(angle));
        at.translate(-15,-15);

        g2.drawImage(partsRobotImage,at,null);

        g2.drawImage(partsRobotRailImage,260,0,null);
        at = new AffineTransform();
        at.translate(x-8,y-10);
        g2.drawImage(topImage,at,null);

            //System.out.println(takePicture);
        if(takePicture){
            // System.out.println(takePicture);
            opacity = (float)Math.exp(flashCounter) - 1;
            System.out.println(opacity);
            if(opacity > 1){
                flashUp = false;
                flashDown = true;
                opacity = (float)0.99;
                System.out.println("hit");
            }
            if(flashUp){
                flashCounter += 0.15;
            }
            else if (flashDown){
                System.out.println("down");
                flashCounter -= 0.007;
                if(opacity < 0){
                    takePicture = false;
                    flashCounter = 1;
                }
            }


            try {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(flash,350,100,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
            catch (Exception ignore){}
        }
    }
}
