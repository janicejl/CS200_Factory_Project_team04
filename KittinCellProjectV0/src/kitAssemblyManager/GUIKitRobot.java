package kitAssemblyManager;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import server.KitRobot;

public class GUIKitRobot {
    KitRobot kr;
    double x, y, angle, extension, jointAngle;
    double a, b, c, d, e, f; // robot joint positions
    boolean hasKit;
    double wid;
    double end;
    double len;
    double hypA;
    double hypB;
    double phi;
    double gripX;
    double gripY;
    BufferedImage crateImage = null;
    BufferedImage metal = null;
    BufferedImage grip = null;
    BufferedImage base = null;
    BufferedImage bar1 = null;
    BufferedImage bar2 = null;
    BufferedImage hinge = null;
    BufferedImage base2 = null;

    GUIKitAssemblyManager app;

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
        System.out.println(phi);
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
        gripX = kr.getX();
        gripY = kr.getY();
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

    public void paintKitRobot(Graphics2D g2){
        AffineTransform at = new AffineTransform(); // transform object
        extension /= 2;
        at.translate(x-50,y-50); // point of base image
        g2.drawImage(base2,at, null);
        at.translate(25,25); // point of base image
        g2.drawImage(base,at, null);

        at.translate(25,25); // center of rotation
        at.rotate(angle);

        g2.transform(at); // apply rotation to graphics2d object

        at = new AffineTransform(); // reset transform object
        at.translate(0,-2*(int)extension); // move to center of crate image
        at.rotate(-angle); // rotate crate back to orthogonal
        at.translate(-25,-50); // move to point of crate image
        if(hasKit){
            g2.drawImage(crateImage,at, null); // base
        }
        at.translate(-5,20); // move to point of grip image
        g2.drawImage(grip,at, null);

        at = new AffineTransform(); // reset transform object

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

        at.translate(-24, b); // left lower
        at.scale(1,-(b - a)/200.0);
        g2.drawImage(metal,at,null);
        at.translate(32, 0); // right lower
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-24, c); // left lower end
        at.scale(1,(b - c)/200.0);
        g2.drawImage(metal,at,null);
        at.translate(32, 0); // right lower end
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-8, d); // upper end
        at.scale(1,-(d - e)/200.0);
        g2.drawImage(metal,at,null);

        at = new AffineTransform();
        at.translate(-8, f); // upper
        at.scale(1,-(f - d)/200.0);
        g2.drawImage(metal,at,null);
    }
}
