package GantryManager;

import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIGantryManager extends JFrame implements ActionListener
{
	painterPanel paintPanel; //Panel to paint the GantryManager GUI
	Gantry gantry; //Gantry robot
	ArrayList<PartsBox> parts; //Parts boxes
	ArrayList<Integer> feeders; //Indices for 4 feeders
	int speed; //Speed of the timer
	public Timer timer; //Calls actionPerformed every clock cycle
	protected BufferedImage test = null; //Test image for the part
	

	

	public GUIGantryManager() //Initializes all objects
	{
		timer = new Timer(10,this);
		gantry = new Gantry();
		paintPanel = new painterPanel();
		paintPanel.setGantry(gantry);

		this.add(paintPanel);
		try
		{
			test = ImageIO.read(new File("images/part.png"));
		}
		catch(IOException e)
		{
		}
		
		//Populates the Parts box with a base box
		parts = new ArrayList<PartsBox>();
		parts.add(new PartsBox(test,10));
		
		//Creates the four feeder indices and sets them as open
		feeders = new ArrayList<Integer>();
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		
		//links the parts boxes to the paintPanel
		paintPanel.setPartsBoxes(parts);
	}
	
	public static void main(String[] args)
	{
		GUIGantryManager gui = new GUIGantryManager();
		gui.setSize(345,600);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.startTimer();
	}
	
	public void actionPerformed(ActionEvent ae) //Controls the actual code for simulation
	{

		int i =0;
		boolean go = true;
		while(i<parts.size()) //Checks if a parts bin is waiting to be loaded or dumped
		{
			if(parts.get(i).getState() == "dump" || parts.get(i).getState()=="load")
			{
				go = false;
			}
			parts.get(i).update();
			i++;
		}
		if(go == true) //If it is not, looks for parts that are moving, or waiting to move
		{
			go=false;
			i=0;
			while(i<parts.size() && go==false)
			{
				if(parts.get(i).getState()=="ready")
				{
					go = true;
				}
				else if(parts.get(i).getState()=="wait")
				{
					parts.get(i).setState("ready");
					go = true;
				}
				i++;
			}
			if(go==false && parts.size()<4) //If there are none waiting (and there are less than 4 boxes, creates a new box)
			{
				parts.add(new PartsBox(test, 2));
			}
		}
		
		if(gantry.getState()=="free") //If the gantry is free
		{
			int c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="dump")//first look for parts bins waiting to be dumped
				{
					gantry.setX(parts.get(c).getXCurrent());
					gantry.setY(parts.get(c).getYCurrent());
					gantry.setBox(c);
					gantry.setState("dumpi");
				}
				c++;
			}
			c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="load") //then look for ones to be loaded
				{
					gantry.setX(parts.get(c).getXCurrent()+5);
					gantry.setY(parts.get(c).getYCurrent()-15);
					gantry.setBox(c);
					gantry.setState("load");
				}
				c++;
			}
		}
		else if(gantry.getState() == "load") //if the gantry  is moving towards the load station
		{
			if(gantry.getX()==gantry.getXCurrent() && gantry.getY()==gantry.getYCurrent())
			{
				gantry.setState("loading");//once it has reached it, switch to a busy signal
				int c=0;
				while(c<feeders.size())
				{
					if(feeders.get(c)==0) //find an open feeder
					{
						gantry.setFeeder(c);
						parts.get(gantry.getBox()).setX(gantry.getX()-5);
						parts.get(gantry.getBox()).setY(gantry.getY()+15);
						feeders.set(c, 1);
						break;
					}
					c++;
				}
			}
		}
		else if(gantry.getState()=="loading") //if busy
		{
			if(gantry.getX()==gantry.getXCurrent() && gantry.getY() == gantry.getYCurrent())
			{
				parts.get(gantry.getBox()).setState("feeding"); //once at the feeder, drop the box, and go back to free
				gantry.setBox(-1);
				gantry.setState("free");
			}
			
		}
		
		
		gantry.update(); //update the gantry robots position
		paintPanel.repaint();
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void setTimerDelay(int d)
	{
		timer.setDelay(d);
	}
	

	
}
		
