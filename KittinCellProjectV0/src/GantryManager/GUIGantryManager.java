package GantryManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class GUIGantryManager extends JPanel implements ActionListener
{
	protected BufferedImage background = null;
	protected BufferedImage rail = null;
	protected BufferedImage station = null;
	protected BufferedImage lane = null;
	protected BufferedImage feeder = null;
	protected BufferedImage gantryImage = null;
	protected BufferedImage crate = null;
	protected BufferedImage part = null;
	GantryManager manager;
	GantryManagerClient client;
	javax.swing.Timer timer;
	
	public GUIGantryManager()
	{
		client = new GantryManagerClient(this);
		int j= client.connect();
		if(j==1)
			client.getThread().start();
		try
		{
			background = ImageIO.read(new File("images/background.png"));
			rail = ImageIO.read(new File("images/rail.png"));
			station = ImageIO.read(new File("images/station.png"));
			feeder = ImageIO.read(new File("images/Feeder.png"));
			lane = ImageIO.read(new File("images/lanetemp.png"));
			gantryImage = ImageIO.read(new File("images/gantryrobot.png"));
			crate = ImageIO.read(new File("images/crate.png"));
			part = ImageIO.read(new File("images/part.png"));
		}
		catch(IOException e) {}
		
		manager = new GantryManager();
		timer = new javax.swing.Timer(9,this);
		timer.start();
	}
		
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
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
		
		//System.out.println(manager.getGantry().getState());
		
		int i=0;
		while(i<manager.getPartsBoxes().size())
		{
			//manager.getPartsBoxes().get(i).update();
			g2.drawImage(crate, manager.getPartsBoxes().get(i).getxCurrent(), manager.getPartsBoxes().get(i).getyCurrent(), null);
			g2.drawImage(part, manager.getPartsBoxes().get(i).getxCurrent()+13, manager.getPartsBoxes().get(i).getyCurrent()+35,null);
			i++;
		}
		g2.drawImage(rail, manager.getGantry().getxCurrent()+10,0,null);
		g2.drawImage(gantryImage,manager.getGantry().getxCurrent(), manager.getGantry().getyCurrent(),null);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==timer)
		{
			client.update();
			manager.actionPerformed(ae);
			this.repaint();
		}
	}
	
	public synchronized GantryManager getGantryManager()
	{
		return manager;
	}
	public synchronized void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
}