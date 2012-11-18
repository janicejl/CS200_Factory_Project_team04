package GantryManager;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.awt.event.*;

//Simulation class, handles the gantry as well as all of the parts boxes and logic of states
public class GantryManager implements Serializable,ActionListener
{
	Gantry gantry;
	Vector<PartsBox> parts; //Partsboxes that are not purged or exiting
	Vector<Integer> feeders;
	Vector<PartsBox> exiting; //Partsboxes that are leaving the factory
	Vector<PartsBox> purged; //Partsboxes that have been purged
	int speed;
	Random rand;
	
	public GantryManager()
	{
		rand = new Random();
		gantry = new Gantry();
		
		parts = new Vector<PartsBox>();
		parts.add(new PartsBox(100)); //Initial box placed on conveyor, for testing only
		
		//Populating the feeders
		feeders = new Vector<Integer>();
		int i=0;
		while(i<4)
		{
			feeders.add(0);
			i++;
		}
	
		exiting = new Vector<PartsBox>();
		purged = new Vector<PartsBox>();
	}
	
	public void update()
	{
		//Temporary variable to store the state
		String state = gantry.getState();
		
		gantry.update();
		//Updating exiting parts boxes
		int i=0;
		while(i<exiting.size())
		{
			exiting.get(i).update();
			if(exiting.get(i).done())
			{
				exiting.remove(i);
			}
			else
				i++;
		}
		
		//Updating purged parts boxes
		i=0;
		while(i<purged.size())
		{
			purged.get(i).update();
			i++;
		}
		
		
		//Updating remaining parts boxes, and staging new ones if necessary
		i=0;
		boolean go = true; //Determines if a new parts box can be put on the conveyor
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
			if(go==false && parts.size()<9) //Up to 9 parts boxes visible in the factory at any one time
			{
				parts.add(new PartsBox((rand.nextInt(10)+1)*20));
			}
		}
		
		if(gantry.getState().equals("load")) //If gantry has been told to load a parts box
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
			if(gantry.getBox()>parts.size())
			{
				gantry.setState("free");
			}	
		}
		else if(gantry.getState().equals("loading")) //if gantry is currently loading a parts box
		{
			parts.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
			parts.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
			parts.get(gantry.getBox()).setState("moving");
			parts.get(gantry.getBox()).setFeeder(gantry.getFeed());	
		}
		else if(gantry.getState().equals("dumpi")) //If gantry is moving to dump a parts box
		{
			gantry.setxFinal(gantry.getxFinal()+60);
			int c=0;
			while(c<purged.size())
			{
				if(purged.get(c).getFeeder()==gantry.getFeed())
				{
					gantry.setBox(c);
				}
				c++;
			}
			if(purged.size()==0 || purged.get(gantry.getBox()).getFeeder()!=gantry.getFeed())
			{
				gantry.setState("free");
			}
			
		}
		else if(gantry.getState().equals("purgei")) //If gantry is moving to purge a parts box
		{
			int c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getFeeder()==gantry.getFeed())
				{
					gantry.setBox(c);
				}
				c++;
			}
			if(parts.get(gantry.getBox()).getFeeder()!=gantry.getFeed())
			{
				gantry.setState("free");
			}
		}
		
		if(gantry.done()) //If the gantry has reached its destination (State transitions are almost all in this block)
		{
			if(state.equals("load"))
			{
				gantry.setState("loading");
				state = "loading";
			}
			else if(state.equals("loading"))
			{
				parts.get(gantry.getBox()).setState("feeding");
				gantry.setState("free");
				state = "free";
			}
			else if(state.equals("dumpi"))
			{
				gantry.setState("dumpf");
				state = "dumpf";
				feeders.set(gantry.getFeed(), 0);
				gantry.setFeed(-1);
				purged.get(gantry.getBox()).setState("dumpf");
				gantry.setxFinal(285);
				gantry.setyFinal(172);
				purged.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
				purged.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
			}
			else if(state.equals("dumpf"))
			{
				purged.remove(gantry.getBox());
				gantry.setState("free");
				state = "free";
				exiting.add(new PartsBox(0));
			}
			else if(state.equals("purgei"))
			{
				gantry.setState("purgef");
				state="purgi";
				feeders.set(gantry.getFeed(), 0);
				gantry.setxFinal(gantry.getxFinal()+60);
				parts.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
				parts.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
				gantry.setFeed(-1);
				parts.get(gantry.getBox()).setState("purgef");
			}
			else if(state.equals("purgef"))
			{
				System.out.println("done");
				purged.add(parts.get(gantry.getBox()));
				parts.remove(gantry.getBox());
				gantry.setState("free");
			}
		}
	
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		this.update();
	}
	
	public Gantry getGantry()
	{
		return gantry;
	}
	
	public Vector<PartsBox> getPartsBoxes()
	{
		return parts;
	}
	
	public synchronized void setGantry(Gantry g)
	{
		gantry = g;
	}
	
	public synchronized void setParts(Vector<PartsBox> p)
	{
		parts = p;
	}
	
	public Vector<PartsBox> getExiting()
	{
		return exiting;
	}
	
	public Vector<PartsBox> getPurged()
	{
		return purged;
	}
}