package GantryManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;

public class GantryManager implements Serializable,ActionListener
{
	Gantry gantry;
	ArrayList<PartsBox> parts;
	ArrayList<Integer> feeders;
	int speed;
	Random rand;
	
	public GantryManager()
	{
		rand = new Random();
		gantry = new Gantry();
		
		parts = new ArrayList<PartsBox>();
		parts.add(new PartsBox(100));
		
		feeders = new ArrayList<Integer>();
		int i=0;
		while(i<4)
		{
			feeders.add(0);
			i++;
		}
		
		
	}
	
	public synchronized void update()
	{
		gantry.update();
		int i=0;
		boolean go = true;
		while(i<parts.size())
		{
			if(parts.get(i).getState().equals("load") || parts.get(i).getState().equals("dump") || parts.get(i).getState().equals("ready") || parts.get(i).getState().equals("loading"))
				go = false;
			parts.get(i).update();
			i++;
		}
		if(go == true)
		{
			go = false;
			i=0;
			while(i<parts.size() && go==false)
			{
				if(parts.get(i).getState()=="ready")
					go=true;
				else if(parts.get(i).getState()=="wait")
				{
					parts.get(i).setState("ready");
					go=true;
				}
				i++;	
			}
			if(go==false && parts.size()<4)
			{
				parts.add(new PartsBox((rand.nextInt(10)+1)*20));
			}
		}
		else if(gantry.getState().equals("load"))
		{
			int c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="load")
				{
					gantry.setxFinal(parts.get(c).getxCurrent()+10);
					gantry.setyFinal(parts.get(c).getyCurrent()+15);
					gantry.setBox(c);
				}
				c++;
			}
			if(gantry.done())
			{
				gantry.setState("loading");
			}
		}
		else if(gantry.getState()=="loading")
		{
			parts.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
			parts.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
			parts.get(gantry.getBox()).setState("moving");
			parts.get(gantry.getBox()).setFeeder(gantry.getFeed());
			if(gantry.done())
			{
				parts.get(gantry.getBox()).setState("feeding");
				gantry.setBox(-1);
				gantry.setState("free");
			}
		}
		else if(gantry.getState()=="dumpi")
		{
			if(gantry.done())
			{
				feeders.set(parts.get(gantry.getBox()).getFeeder(), 0);
				gantry.setState("dumpf");
				parts.get(gantry.getBox()).setState("dumpf");
				gantry.setxFinal(300);
				gantry.setyFinal(0);
				parts.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
				parts.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
				
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
	
	public void actionPerformed(ActionEvent ae)
	{
		this.update();
	}
	
	public synchronized Gantry getGantry()
	{
		return gantry;
	}
	
	public synchronized ArrayList<PartsBox> getPartsBoxes()
	{
		return parts;
	}
	
	public synchronized void setGantry(Gantry g)
	{
		gantry = g;
	}
	
	public synchronized void setParts(ArrayList<PartsBox> p)
	{
		parts = p;
	}
}