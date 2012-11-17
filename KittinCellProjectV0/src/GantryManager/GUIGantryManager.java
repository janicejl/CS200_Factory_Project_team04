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
	protected BufferedImage feeder = null;
	protected BufferedImage gantryImage = null;
	protected BufferedImage crate = null;
	protected BufferedImage part = null;
	GantryManager manager;
	GantryManagerClient client;
	javax.swing.Timer timer;
	int managerNum;
	
	public GUIGantryManager(int m)
	{
		client = new GantryManagerClient(this);
		managerNum = m;
		int j= client.connect();
		if(j==1)
			client.getThread().start();
		try
		{
			background = ImageIO.read(new File("images/background.png"));
			rail = ImageIO.read(new File("images/rail.png"));
			station = ImageIO.read(new File("images/station.png"));
			feeder = ImageIO.read(new File("images/Feeder.png"));
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
		/*	g2.drawImage(feeder, -28, 8, null);
		g2.drawImage(feeder, -100, 146, null);
		g2.drawImage(feeder, -100, 284,null);
		g2.drawImage(feeder,  -28, 422, null);*/
		if(managerNum == 1){
			g2.drawImage(background,0,0,null);
			g2.drawImage(feeder, -35, 30, null);
			g2.drawImage(feeder, -110, 170, null);
			g2.drawImage(feeder, -110, 310,null);
			g2.drawImage(feeder,  -35, 450, null);
		}
		g2.drawImage(station, 275,157, null);
			g2.drawImage(station, 325, 157,null);
		g2.drawImage(station, 275,207,null);
			g2.drawImage(station, 325, 207,null);
		g2.drawImage(station, 275,295,null);
			g2.drawImage(station, 325, 295,null);
		g2.drawImage(station, 275,345,null);
			g2.drawImage(station, 325,345,null);
	
		
		
		
		int i=0;
		while(i<manager.getPartsBoxes().size())
		{
			g2.drawImage(crate, manager.getPartsBoxes().get(i).getxCurrent(), manager.getPartsBoxes().get(i).getyCurrent(), null);
			g2.drawImage(part, manager.getPartsBoxes().get(i).getxCurrent()+13, manager.getPartsBoxes().get(i).getyCurrent()+35,null);
			i++;
		}
		i=0;
		while(i<manager.getExiting().size())
		{
			g2.drawImage(crate,manager.getExiting().get(i).getxCurrent(),manager.getExiting().get(i).getyCurrent(),null);
			g2.drawImage(part,manager.getExiting().get(i).getxCurrent()+13, manager.getExiting().get(i).getyCurrent()+35,null);
			i++;
		}
		i=0;
		while(i<manager.getPurged().size())
		{
			g2.drawImage(crate,manager.getPurged().get(i).getxCurrent(),manager.getPurged().get(i).getyCurrent(),null);
			g2.drawImage(part, manager.getPurged().get(i).getxCurrent()+13, manager.getPurged().get(i).getyCurrent()+35,null);
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