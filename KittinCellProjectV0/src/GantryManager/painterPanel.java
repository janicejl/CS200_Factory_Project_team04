package GantryManager;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class painterPanel extends JPanel
{
	//Items that will always be painted, regardless of user input
	protected BufferedImage background = null;
	protected BufferedImage rail = null;
	protected BufferedImage gantryImage = null;
	protected BufferedImage station = null;
	protected BufferedImage box =null;
	protected BufferedImage lane = null;
	protected BufferedImage feeder = null;
	Gantry gantry;
	ArrayList<PartsBox> boxes;
	
	public painterPanel()
	{
		try
		{
           	background = ImageIO.read(new File("images/background.png"));
			rail = ImageIO.read(new File("images/rail.png"));
			gantryImage = ImageIO.read(new File("images/gantryrobot.png"));
			station = ImageIO.read(new File("images/station.png"));
			box =ImageIO.read(new File("images/crate.png"));
			feeder = ImageIO.read(new File("images/base.png"));
			lane = ImageIO.read(new File("images/lanetemp.png"));
       	} 
		catch (IOException e) {}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//Draw the base images
		g2.drawImage(background,0,0,null);
		g2.drawImage(station, 275,275,null);
		g2.drawImage(feeder, 200, 75, null);
		g2.drawImage(feeder, 50, 200, null);
		g2.drawImage(feeder, 50, 350,null);
		g2.drawImage(feeder,  200, 475, null);
		g2.drawImage(lane, 0,50,null);
		g2.drawImage(lane, -150, 175, null);
		g2.drawImage(lane, -150, 325,null);
		g2.drawImage(lane, 0, 450,null);
		
		int i=0;
		while(i<boxes.size())
		{
			g2.drawImage(box, boxes.get(i).getXCurrent(),boxes.get(i).getYCurrent(),null);
			g2.drawImage(boxes.get(i).getImage(), boxes.get(i).getXCurrent()+15, boxes.get(i).getYCurrent()+15, null);
			i++;
		}
		
		g2.drawImage(rail,gantry.getXCurrent()+10,0, null);
		g2.drawImage(gantryImage,gantry.getXCurrent(), gantry.getYCurrent(),null);	
		//I will implement proper image centering instead of the +10, -5 hack, but for now there are more important aspects
	}

	public synchronized void setGantry(Gantry g)//Links gantry with GUIGantryManager
	{
		gantry = g;
	}
	
	public synchronized void setPartsBoxes(ArrayList<PartsBox> pb) //links parts box with GUIGantryManager
	{
		boxes = pb;
	}
	
}

		
		
