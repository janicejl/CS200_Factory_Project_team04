package GantryManager;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class GUIGantryManager extends JPanel implements ActionListener
{
	//Items that will always be painted, regardless of user input
	protected BufferedImage background = null;
	protected BufferedImage rail = null;
	protected BufferedImage station = null;
	protected BufferedImage lane = null;
	protected BufferedImage feeder = null;
	Gantry gantry;
	GUIGantry guigantry;
	ArrayList<PartsBox> boxes;
	ArrayList<GUIPartsBox> gboxes;
	javax.swing.Timer timer;
	GantryManager manager;
	
	public GUIGantryManager(GantryManager gm)
	{
		manager = gm;
		try
		{
           	background = ImageIO.read(new File("images/background.png"));
			rail = ImageIO.read(new File("images/rail.png"));
			station = ImageIO.read(new File("images/station.png"));
			feeder = ImageIO.read(new File("images/Feeder.png"));
			lane = ImageIO.read(new File("images/lanetemp.png"));
       	} 
		catch (IOException e) {}
		
		timer = new javax.swing.Timer(10,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//Draw the base images
		g2.drawImage(background,0,0,null);
		g2.drawImage(station, 275,275,null);
		g2.drawImage(feeder, 200, 35, null);
		g2.drawImage(feeder, 50, 160, null);
		g2.drawImage(feeder, 50, 315,null);
		g2.drawImage(feeder,  200, 435, null);
		g2.drawImage(lane, 0,50,null);
		g2.drawImage(lane, -150, 175, null);
		g2.drawImage(lane, -150, 325,null);
		g2.drawImage(lane, 0, 450,null);
		
		
		int i=0;
		while(i<boxes.size())
		{
			gboxes.get(i).update(boxes.get(i).getXCurrent(),boxes.get(i).getYCurrent());
			gboxes.get(i).paint(g);
			i++;
		}
		
		g2.drawImage(rail,gantry.getXCurrent()+10,0, null);
		guigantry.update(gantry.getXCurrent(), gantry.getYCurrent());
		guigantry.paint(g);
		//I will implement proper image centering instead of the +10, -5 hack, but for now there are more important aspects
	}

	public synchronized void setGantry(Gantry g)//Links gantry with GUIGantryManager
	{
		gantry = g;
	}
	
	public synchronized void setPartsBoxes(ArrayList<PartsBox> pb,ArrayList<GUIPartsBox> gpb) //links parts box with GUIGantryManager
	{
		boxes = pb;
		gboxes = gpb;
	}
	
	public synchronized void update()
	{
		gantry = manager.getGantry();
		guigantry = new GUIGantry(gantry);
		boxes = manager.getPartsBoxes();

		gboxes = new ArrayList<GUIPartsBox>();
		int i =0;
		while(i<boxes.size())
		{
			gboxes.add(new GUIPartsBox(boxes.get(i)));
			i++;
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		this.update();
		this.repaint();
	}
	
	public synchronized void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
}

		
		
