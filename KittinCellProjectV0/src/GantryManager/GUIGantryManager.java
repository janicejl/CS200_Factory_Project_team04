package GantryManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

//Class that handles all of the painting, as well as containing the client and the gantry manager
public class GUIGantryManager extends JPanel {
	protected BufferedImage background = null;
	protected BufferedImage rail = null;
	protected BufferedImage station = null;
	protected BufferedImage conveyor = null;
	protected BufferedImage feeder = null;
	protected BufferedImage gantryImage = null;
	protected BufferedImage crate = null;
	protected Vector<BufferedImage> partImages;
	protected Vector<String> partImagesPath;
	GantryManager manager;	
	int managerNum;
	float conveyorMove = 0;
	
	public GUIGantryManager(int m)
	{
		partImages = new Vector<BufferedImage>();
		partImagesPath = new Vector<String>();
		managerNum = m;
		try
		{
			//Static images
			background = ImageIO.read(new File("images/background1.png"));
			rail = ImageIO.read(new File("images/rail.png"));
			station = ImageIO.read(new File("images/station.png"));
			feeder = ImageIO.read(new File("images/Feeder.png"));
			gantryImage = ImageIO.read(new File("images/gantryrobot.png"));
			crate = ImageIO.read(new File("images/crate.png"));
			conveyor = ImageIO.read(new File("images/conveyerParts.png"));
			int i=0;
			while(i<9)
			{
				partImages.add(ImageIO.read(new File("images/kt"+ i + ".png")));
				partImagesPath.add("images/kt" + i + ".png");
				i++;
			}
		}
		catch(IOException e) {}
	}
		
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//If the GUI is not a part of the Production manager, draw extra parts of the factory
		if(managerNum == 1)
		{
			g2.drawImage(background,0,0,null);
			g2.drawImage(feeder, -35, 30, null);
			g2.drawImage(feeder, -110, 170, null);
			g2.drawImage(feeder, -110, 310,null);
			g2.drawImage(feeder,  -35, 450, null);
		}
		
		conveyorMove+= 0.5;
		if(conveyorMove > 20){
			conveyorMove = 0;
		}
		
		for(int i = 0; i < 5; i++){
			g2.drawImage(conveyor, 265 + i*20 + (int)conveyorMove, 187,null);
			g2.drawImage(conveyor, 285 + i*20 - (int)conveyorMove, 305,null);			
		}
		
		g2.drawImage(conveyor, 265, 187, 266 + (int)conveyorMove, 297, 0,0, 22,110,null);
		g2.drawImage(conveyor, 265, 305, 286 - (int)conveyorMove, 415, 0,0, 22,110,null);
		
//		g2.drawImage(conveyor, 275,192, null);
//		g2.drawImage(conveyor, 325, 192,null);
//		g2.drawImage(conveyor, 275,242,null);
//		g2.drawImage(conveyor, 325, 242,null);
//		g2.drawImage(conveyor, 275,310,null);
//		g2.drawImage(conveyor, 325, 310,null);
//		g2.drawImage(conveyor, 275,360,null);
//		g2.drawImage(conveyor, 325,360,null);
	
		
		
		//These blocks iterate through the different parts boxes and paint them if necessary
		int i=0;
		while(i<manager.getPartsBoxes().size())
		{
			
			g2.drawImage(crate, manager.getPartsBoxes().get(i).getxCurrent(), manager.getPartsBoxes().get(i).getyCurrent(), null);
			if(manager.getPartsBoxes().get(i).getEmpty()==false)
			{
				g2.drawImage(partImages.get(partImagesPath.indexOf(manager.getPartsBoxes().get(i).getPartInfo().getImagePath())), manager.getPartsBoxes().get(i).getxCurrent()+13, manager.getPartsBoxes().get(i).getyCurrent()+35,null);
			}
				i++;
		}
		i=0;
		while(i<manager.getExiting().size())
		{
			g2.drawImage(crate,manager.getExiting().get(i).getxCurrent(),manager.getExiting().get(i).getyCurrent(),null);
			if(manager.getExiting().get(i).getEmpty()==false)
			{
				//g2.drawImage(partImages.get(partImagesPath.indexOf(manager.getExiting().get(i).getPartInfo().getImagePath())),manager.getExiting().get(i).getxCurrent()+13, manager.getExiting().get(i).getyCurrent()+35,null);
				//Image for drawing bows on boxes
			}
			i++;
		}
		i=0;
		while(i<manager.getPurged().size())
		{
			g2.drawImage(crate,manager.getPurged().get(i).getxCurrent(),manager.getPurged().get(i).getyCurrent(),null);
			if(manager.getPurged().get(i).getEmpty()==false)
			{
				//g2.drawImage(partImages.get(partImagesPath.indexOf(manager.getPurged().get(i).getPartInfo().getImagePath())), manager.getPurged().get(i).getxCurrent()+13, manager.getPurged().get(i).getyCurrent()+35,null);
				//Image for drawing bows on boxes
			}
				i++;
		}
		g2.drawImage(rail, manager.getGantry().getxCurrent()+10,0,null);
		g2.drawImage(gantryImage,manager.getGantry().getxCurrent(), manager.getGantry().getyCurrent(),null);
	}
	
	public GantryManager getGantryManager()
	{
		return manager;
	}
	public void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
}