package kitAssemblyManager;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Vector;
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
    Vector<BufferedImage> partImages;
    Vector<Integer> gripperPartIDs;
    float opacity, flashCounter;
    double cameraX;
    double cameraY;

    PartsRobot pr;

    double x;
    double y;
    double angle;

    GUIKitAssemblyManager app;

    public GUIPartsRobot(GUIKitAssemblyManager s){
        app = s;
        x = 265;
        y = 300;
        angle = 0;
        opacity = 0.0f;
        flashCounter = 1.0f;
        cameraX = 350;
        cameraY = 100;
        gripperExtensions = new Vector<Double>();
        gripperHolding = new Vector<Boolean>();
        commands = new Vector<String>();
        subCommands = new Vector<String>();
        nestLocations = new Vector<String>();
        kitLocations = new Vector<String>();
        partImages = new Vector<BufferedImage>();
        gripperPartIDs = new Vector<Integer>();

        for (int i = 0; i < 4; i++) {
        	gripperPartIDs.add(1);
            gripperExtensions.add(0.0);
        }

        try {
        	for(int i=0; i<9;i++){
        		partImages.add(ImageIO.read(new File("images/" + i + ".png")));
        	}
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
        pr = app.getPartsRobot();
        y = pr.getY();
        cameraX = pr.getCameraX();
        cameraY = pr.getCameraY();
        angle = pr.getAngle();
        opacity = pr.getOpacity();
        
        takePicture = pr.getTakePicture();        
        gripperExtensions = pr.getGripperExtensions();
        gripperHolding = pr.getGripperHolding();
        gripperPartIDs = pr.getGripperPartIDs();
        if(takePicture){
            takePicture = true;
            flashUp = true;
        }
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
                g2.drawImage(partImages.get(gripperPartIDs.get(i)),at,null);
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

     
        if(takePicture){
            try {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2.drawImage(flash,(int)cameraX,(int)cameraY,null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
            catch (Exception ignore){}
        }
    }

	public synchronized boolean getTakePicture() {
		return takePicture;
	}

	public synchronized void setTakePicture(boolean takePicture) {
		this.takePicture = takePicture;
	}
}
