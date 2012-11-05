package GantryManager;

import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;

public class GantryManager
{
	Gantry gantry; //Gantry robot
	ArrayList<PartsBox> parts; //Parts boxes
	ArrayList<Integer> feeders; //Indices for 4 feeders
	int speed; //Speed of the timer
	Random rand;
	
	public GantryManager() //Initializes all objects
	{
		rand = new Random();
		
		gantry = new Gantry();
		
		//Populates the Parts box with a base box
		parts = new ArrayList<PartsBox>();
		parts.add(new PartsBox(100));
		
		//Creates the four feeder indices and sets them as open
		feeders = new ArrayList<Integer>();
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		
		//links the parts boxes to the gui
	}

	public void update()
	{
		gantry.update();
		int i =0;
		boolean go = true;
		while(i<parts.size()) //Checks if a parts bin is waiting to be loaded or dumped
		{
			if(parts.get(i).getState() == "dump" || parts.get(i).getState()=="loading" || parts.get(i).getState()=="ready" || parts.get(i).getState()=="load" || parts.get(i).getState()=="dumpf")
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
				parts.add(new PartsBox((rand.nextInt(10)+1)*20));
			}
		}
		
		if(gantry.getState()=="free") //If the gantry is free
		{
			boolean flip = true;
			int c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="load") //then look for ones to be loaded
				{
					gantry.setX(parts.get(c).getXCurrent()+10);
					gantry.setY(parts.get(c).getYCurrent()+15);
					gantry.setBox(c);
					gantry.setState("load");
					flip = false;
				}
				c++;
			}
			
			c=0;
			if(flip==true)
			{
				while(c<parts.size())
				{
					if(parts.get(c).getState()=="dump")//first look for parts bins waiting to be dumped
					{
						gantry.setBox(c);
						gantry.setFeeder(parts.get(c).getFeeder());
						gantry.setState("dumpi");
					}
					c++;
				}
			}
		}
		else if(gantry.getState() == "load") //if the gantry  is moving towards the load station
		{
			if(gantry.done())
			{
				gantry.setState("loading");//once it has reached it, switch to a busy signal
				int c=0;
				while(c<feeders.size())
				{
					if(feeders.get(c)==0) //find an open feeder
					{
						gantry.setFeeder(c);
						parts.get(gantry.getBox()).setX(gantry.getX()-10);
						parts.get(gantry.getBox()).setY(gantry.getY()-15);
						parts.get(gantry.getBox()).setState("moving");
						parts.get(gantry.getBox()).setFeeder(c);
						feeders.set(c, 1);
						break;
					}
					c++;
				}
			}
		}
		else if(gantry.getState()=="loading") //if busy
		{
			if(gantry.done())
			{
				parts.get(gantry.getBox()).setState("feeding"); //once at the feeder, drop the box, and go back to free
				gantry.setBox(-1);
				gantry.setState("free");
			}
			
		}
		else if(gantry.getState()=="dumpi")
		{
			if(gantry.done())
			{
				feeders.set(parts.get(gantry.getBox()).getFeeder(),0);
				gantry.setState("dumpf");
				parts.get(gantry.getBox()).setState("dumpf");
				gantry.setX(300);
				gantry.setY(0);
				parts.get(gantry.getBox()).setX(gantry.getX()-10);
				parts.get(gantry.getBox()).setY(gantry.getY()-15);
			}
		}
		else if(gantry.getState()=="dumpf")
		{
			if(gantry.done())
			{
				gantry.setState("free");
				parts.remove(gantry.getBox());
				gantry.setBox(-1);
			}
		}
	}
	
	public Gantry getGantry()
	{
		return gantry;
	}
	
	public ArrayList<PartsBox> getPartsBoxes()
	{
		return parts;
	}
}
		
