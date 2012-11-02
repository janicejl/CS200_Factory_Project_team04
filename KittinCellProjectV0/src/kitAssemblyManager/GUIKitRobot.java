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

import server.KitRobot;

public class GUIKitRobot {
    KitRobot kr;
    double x, y, angle, extension, jointAngle;
    double a, b, c, d, e, f; // robot joint positions
    boolean hasKit;

    BufferedImage crateImage = null;
    BufferedImage metal = null;
    BufferedImage grip = null;
    BufferedImage base = null;
    BufferedImage bar1 = null;
    BufferedImage bar2 = null;
    BufferedImage hinge = null;
    BufferedImage base2 = null;

    KitAssemblyApp app;

    public GUIKitRobot(KitAssemblyApp _app){
        app = _app;
        x = 160;
        y = 300;

        try {
            crateImage = ImageIO.read(new File("images/crate.png"));
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

    public void update(){
        kr = app.getKitRobot();
        angle = kr.getAngle();
        extension = kr.getExtension();
        hasKit = kr.getHasKit();
        jointAngle = Math.acos(extension/75);
        a = (int)(14.14 * Math.cos(Math.PI/4 - jointAngle));
        b = -(int)(85.59 * Math.cos(jointAngle + 0.1171));
        c = -(int)(85.59 * Math.cos(jointAngle - 0.1171));
        d = -(int)extension + (int)(14.1421 * Math.cos(Math.PI/4 + jointAngle));
        e = -(int)extension + (int)(14.1421 * Math.cos(jointAngle - Math.PI/4));
        f = -2*(int)extension - (int)(14.1421 * Math.cos(Math.PI/4 - jointAngle));
    }

    public void paintKitRobot(Graphics2D g2){
        AffineTransform at = new AffineTransform(); // transform object

        at.translate(x-50,y-50); // point of base image
        g2.drawImage(base2,at, null);
        at.translate(25,25); // point of base image
        g2.drawImage(base,at, null);

        at.translate(25,25); // center of rotation
        at.rotate(Math.toRadians(angle));

        g2.transform(at); // apply rotation to graphics2d object

        at = new AffineTransform(); // reset transform object
        at.translate(0,-2*(int)extension); // move to center of crate image
        at.rotate(-Math.toRadians(angle)); // rotate crate back to orthogonal
        at.translate(-20,-20); // move to point of crate image
        if(hasKit){
            g2.drawImage(crateImage,at, null); // base
        }
        at.translate(-5,-5); // move to point of grip image
        g2.drawImage(grip,at, null);

        at = new AffineTransform(); // reset transform object

        at.translate(-20,-5); // move to hinge 1 (left end)
        g2.drawImage(bar1,at, null);
        at.translate(0,-(int)extension); // move to hinge 2 (left end)
        g2.drawImage(bar1,at, null);
        at.translate(10,-(int)extension); // move to hinge 3 (left end)
        g2.drawImage(bar2,at, null);
        at.translate(5,2*(int)extension -10); // move to center
        at.scale(1,.3);
        g2.drawImage(metal,at,null);
        at = new AffineTransform(); // reset transform object

        at.translate(-15, b);
        at.scale(1,-(b - a)/100.0);
        g2.drawImage(metal,at,null);
        at.translate(20, 0);
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-15, c);
        at.scale(1,-(c - b)/100.0);
        g2.drawImage(metal,at,null);
        at.translate(20, 0);
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-5, d);
        at.scale(1,-(d - e)/100.0);
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-5, f);
        at.scale(1,-(f - d)/100.0);
        g2.drawImage(metal,at,null);
    }
}
