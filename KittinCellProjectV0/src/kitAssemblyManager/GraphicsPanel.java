import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;


public class KitAssemblyManager extends JPanel implements ActionListener {
    GUIKitRobot kitRobot;
    Application app;
    int conveyor = 0;
    int crateSpawn = 0;
    int crateX = 700;
    protected BufferedImage crateImage = null;
    protected BufferedImage background = null;
    protected BufferedImage stand = null;
    protected BufferedImage rail = null;
    ArrayList<Integer> kitAvailable;

    public KitAssemblyManager(Application a){ // Game constructor
        app = a;
        kitAvailable = new ArrayList<Integer>();
        setPreferredSize(new Dimension (320,600));
        new javax.swing.Timer(20, this).start();
        kitRobot = new KitRobot(app, kitAvailable);
        new Thread(kitRobot).start();

        try {
            background = ImageIO.read(new File("background.png"));
            crateImage = ImageIO.read(new File("crate.png"));
            stand = ImageIO.read(new File("stand.png"));
            rail = ImageIO.read(new File("rail.png"));
        } catch (IOException e) {}
    }

    public void updateGantryXY(int x, int y){
        kitRobot.setNewXY(x,y);
    }

    public void sendGantryCommand(String s){
        kitRobot.addCommand(s);
    }

    public void paintComponent(Graphics g){ // Paint game background and
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //background.paintIcon(this, g2,0,0);
        g2.drawImage(background, 0, 0, null);
        g2.setColor(Color.BLACK);
        g2.fillRect(400,115,300,60);
        g2.setFont(new Font("Verdana", Font.BOLD, 12));
        // g2.drawString("Done",225,110);
        // g2.drawString("Miss",285,110);
        // g2.drawString("Bad",345,110);
        // g2.drawString("New",405,110);
        // g2.drawString("Kit Stand 1",510,215);
        // g2.drawString("Kit Stand 2",510,355);
        // g2.drawString("Kit Check",120,245);
        g2.setColor(Color.GRAY);
        if(conveyor == -1){
            conveyor = 9;
        }
        for(int i = conveyor; i < 300; i+=10){
            g2.drawLine(400+i,115,400+i,175);
        }
        conveyor--;

        g2.drawImage(crateImage, crateX, 120, null);
        if(kitAvailable.get(0) == 0){
            if(crateX == 405){
                crateX = 701;
            }
            else if(crateX == 406){
                kitAvailable.set(0,1);
            }
            crateX--;
        }
        else {

            crateX = 405;
        }
        g2.drawImage(stand,250,240,null);
        g2.drawImage(stand,0,240,null);
        AffineTransform at = new AffineTransform();
        at.translate(220,390);
        at.rotate(Math.toRadians(90));
        g2.drawImage(stand,at,null);
        g2.setColor(Color.BLACK);

        kitRobot.paintKitRobot(g2);
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();

   }
}
