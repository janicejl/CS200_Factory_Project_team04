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

public class Gantry extends JPanel implements Runnable{
    double x;
    double y;
    double newX;
    double newY;
    BufferedImage gantryImage = null;
    BufferedImage crateImage = null;
    boolean rotate = false;
    boolean processing = false;
    boolean hasCrate = false;
    double angle = 0;
    boolean mouse = false;
    boolean paused = false;
    ArrayList<String> commands;
    Application app;

    public Gantry(Application a, double x, double y){
        app = a;
        this.setPreferredSize(new Dimension(50,50));
        this.x = x;
        this.y = y;
        newX = x;
        newY = y;
        commands = new ArrayList<String>();
        try {
            gantryImage = ImageIO.read(new File("gantry.png"));
            crateImage = ImageIO.read(new File("crate.png"));
        }
        catch (IOException e) {
        }
    }

    @Override
    public void run(){
        while(true){
            updateItem();
            if(commands.size() > 0 && processing == false){
                String s = commands.get(0);
                commands.remove(0);
                String[] ss = s.split("\\,");
                if(ss[0].equals("load")){
                    try{
                        int binID = Integer.parseInt(ss[1]);
                        if(binID == 1){
                            commands.add(0,"m,50,60");
                        }
                        else if(binID == 2){
                            commands.add(0,"m,50,170");
                        }
                        else if(binID == 3){
                            commands.add(0,"m,50,280");
                        }
                        else if(binID == 4){
                            commands.add(0,"m,50,390");
                        }
                        commands.add(1,"p");
                        int feederID = Integer.parseInt(ss[2]);
                        if(feederID == 1){
                            commands.add(2,"m,400,60");
                        }
                        else if(feederID == 2){
                            commands.add(2,"m,400,170");
                        }
                        else if(feederID == 3){
                            commands.add(2,"m,400,280");
                        }
                        else if(feederID == 4){
                            commands.add(2,"m,400,390");
                        }
                        if(ss[3].equals("empty")){
                            if(feederID == 1){
                                commands.add(3,"m,340,60");
                            }
                            else if(feederID == 2){
                                commands.add(3,"m,340,170");
                            }
                            else if(feederID == 3){
                                commands.add(3,"m,340,280");
                            }
                            else if(feederID == 4){
                                commands.add(3,"m,340,390");
                            }
                            commands.add(4,"d");
                        }
                        else {
                            commands.add(3,"m,225,10");
                            commands.add(4,"d");
                        }
                    }
                    catch (Exception e){}
                }
                else if(ss[0].equals("m")){
                    try{
                        newX = Integer.parseInt(ss[1]);
                        newY = Integer.parseInt(ss[2]);
                        processing = true;
                    }
                    catch (Exception e){}
                }
                else if(ss[0].equals("p")){
                    hasCrate = true;
                }
                else if(ss[0].equals("d")){
                    hasCrate = false;
                    app.commandDone();
                }
            }
            else if(commands.size() == 0 && processing == false){
                newX = 225;
                newY = 225;
            }
            try {
                Thread.sleep(2);  // milliseconds
            } catch (InterruptedException ignore) {}

        }
    }

    public void addCommand(String s){
        commands.add(s);
    }

    public void setNewXY(int x, int y){
        newX = x;
        newY = y;
        System.out.println(newX + " - " + newY);
    }

    public void rotate(){
        rotate = true;
    }

    public void followMouse(){
        mouse = !mouse;
    }

    public void clearCommands(){
        commands.clear();
        hasCrate = false;
        newX = 225;
        newY = 225;
        //commands.add("d");
        //commands.add("m,225,225");
        //app.commandListClear();
    }
    public void pause(){
        paused = !paused;

    }

    public void paintGantry(Graphics2D g2){

        AffineTransform at = new AffineTransform();
        if(mouse){
            at.translate(MouseInfo.getPointerInfo().getLocation().x-5,MouseInfo.getPointerInfo().getLocation().y-25);
        }
        else {
            at.translate(x+25,y+25);
        }
        at.scale(1,1);
        at.rotate(Math.toRadians(angle));
        at.translate(-25, -25);
        if(hasCrate){
            g2.drawImage(crateImage, at, null);
        }
        g2.drawImage(gantryImage, at, null);
        if(rotate){
            angle += 10;
            if(angle > 360){
                angle = 0;
                rotate = false;
            }
        }
    }

    public void updateItem(){
        if(!paused){
            if(x != newX || y != newY){
                double d = Math.pow((newX-x),2) + Math.pow((newY-y),2);
                if (d >= 1){
                    d = Math.sqrt(d);
                    x += (newX-x)/d;
                    y += (newY-y)/d;
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
