package kitAssemblyManager;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import data.GUIKit;
import data.Kit;
import server.KitRobot;

public class GUIKitRobot {
    KitRobot kr;				//A reference to kit robot. 
    double x, y, angle, extension, jointAngle;		//variable to define the position of the robot. 
    double a, b, c, d, e, f; // robot joint positions
    boolean hasKit;		//boolean for whether or not it is holding a kit. 
    double wid;				//width of the kit robot arms
    double end;				//distance from the pivot point to the end of the arm
    double len;				//length of the kit robot arms
    double hypA;			// used for arm extension movement. 
    double hypB;			//used for arm extension movement
    double phi;				//current vertical angle of the arm 
    double gripX;			//current x position of the gripper
    double gripY;			//current y position of the gripper. 
    GUIKit gKit;			// graphical version of the kit held
    Kit kit;				//reference to the kit being held. 
    BufferedImage crateImage = null;	//image of an empty kit. 
    
    //images that make up the kit robot. 
    BufferedImage metal = null;
    BufferedImage grip = null;
    BufferedImage base = null;
    BufferedImage bar1 = null;
    BufferedImage bar2 = null;
    BufferedImage hinge = null;
    BufferedImage base2 = null;

    GUIKitAssemblyManager app;			// a reference to the kit assembly manager. 

    public GUIKitRobot(GUIKitAssemblyManager _app){
        app = _app;
        x = 25;
        y = 300;
        wid = 10;
        end = 30;
        len = 175;
        hypA = Math.sqrt(Math.pow(len+end,2)+Math.pow(wid,2));
        hypB = Math.sqrt(Math.pow(wid,2)+Math.pow(wid,2));
        phi = Math.atan(wid/(len+end));
        //System.out.println(phi);
        kit = new Kit();
        gKit = new GUIKit(kit);
        try {
            crateImage = ImageIO.read(new File("images/kit.png"));
            metal = ImageIO.read(new File("images/metal.png"));
            grip = ImageIO.read(new File("images/grip.png"));
            base = ImageIO.read(new File("images/base.png"));
            bar1 = ImageIO.read(new File("images/bar1.png"));
            bar2 = ImageIO.read(new File("images/bar2.png"));
            hinge = ImageIO.read(new File("images/hinge.png"));
            base2 = ImageIO.read(new File("images/base2.png"));
        }
        catch (IOException e) {
        }
    }

    //updates the position of the different parts. 
    public void update(){
        kr = app.getKitRobot();
        gripX = kr.getX();
        gripY = kr.getY();
        gKit.setKit(kr.getKit());
        hasKit = kr.getHasKit();
        if(gripY > 300){
            angle = Math.PI/2.0 + Math.atan((gripY-300.0)/(gripX-25.0));
        }
        else if (gripY < 300){
            angle = Math.atan2(gripX-25,300-gripY);
        }
        else if (gripY == 300) {
            angle = Math.PI/2;
        }
        extension = Math.sqrt(Math.pow(gripX-25,2)+Math.pow(gripY-300,2));
        hasKit = kr.getHasKit();
        jointAngle = Math.acos(extension/(2*len));
        a = (int)(hypB*Math.cos(Math.PI/4 - jointAngle));
        b = -(int)(hypA * Math.cos(jointAngle + phi));
        c = -(int)(hypA * Math.cos(jointAngle - phi));
        d = -(int)(extension - hypA * Math.cos(jointAngle + phi));
        e = -(int)(extension - hypA * Math.cos(jointAngle - phi));
        f = -(int)(extension + hypB * Math.cos(Math.PI/4 - jointAngle));
    }

    //paints the kit robot. This is called in the guikitassembly
    public void paintKitRobot(Graphics2D g2){
    	if(hasKit){
        	gKit.paintKit(g2);
            //g2.drawImage(crateImage,at, null); // base
        }
  
        AffineTransform at = new AffineTransform(); // transform object
        extension /= 2;
        at.translate(x-50,y-50); // point of base image
        g2.drawImage(base2,at, null);
        at.translate(25,25); // point of base image
        g2.drawImage(base,at, null);

        at.translate(25,25); // center of rotation
        at.rotate(angle);
        at.translate(0,-2*(int)extension); // move to center of crate image
        at.rotate(-angle); // rotate crate back to orthogonal
        at.translate(-30,-30); // move to point of grip image
        g2.drawImage(grip,at, null);

        at = new AffineTransform(); // reset transform object
        at.translate(x,y); // center of rotation
        at.rotate(angle);

        at.translate(-29,-5); // move to hinge 1 (left end)
        g2.drawImage(bar1,at, null);
        at.translate(0,-(int)extension); // move to hinge 2 (left end)
        g2.drawImage(bar1,at, null);
        at.translate(16,-(int)extension); // move to hinge 3 (left end)
        g2.drawImage(bar2,at, null);
        at.translate(5,2*(int)extension -15); // move to center
        at.scale(1,.2);
        g2.drawImage(metal,at,null);
        
        at = new AffineTransform(); // reset transform object
        at.translate(x,y); // center of rotation
        at.rotate(angle);

        at.translate(-24, b); // left lower
        at.scale(1,-(b - a)/200.0);
        g2.drawImage(metal,at,null);
        at.translate(32, 0); // right lower
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(x,y); // center of rotation
        at.rotate(angle);
        at.translate(-24, c); // left lower end
        at.scale(1,(b - c)/200.0);
        g2.drawImage(metal,at,null);
        at.translate(32, 0); // right lower end
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(x,y); // center of rotation
        at.rotate(angle);
        at.translate(-8, d); // upper end
        at.scale(1,-(d - e)/200.0);
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(x,y); // center of rotation
        at.rotate(angle);
        at.translate(-8, f); // upper
        at.scale(1,-(f - d)/200.0);
        g2.drawImage(metal,at,null);
    }
}
