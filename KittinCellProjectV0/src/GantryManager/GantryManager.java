package GantryManager;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.*;
import data.PartInfo;

//Simulation class, handles the gantry as well as all of the parts boxes and logic of states
public class GantryManager implements Serializable,ActionListener
{
	Gantry gantry;
	CopyOnWriteArrayList<PartsBox> parts;  //Partsboxes that are not purged or exiting
	CopyOnWriteArrayList<PartsBox> exiting;  //Partsboxes that are leaving the factory
	CopyOnWriteArrayList<PartsBox> purged;  //Partsboxes that have been purged
	CopyOnWriteArrayList<Integer> feeder;
	CopyOnWriteArrayList<PartInfo> feeders;
	Vector<Feeder.Feeder> feed;
	
	boolean feederMsg = false;
	boolean stationMsg = false;
	
	public GantryManager(Vector<Feeder.Feeder> f)
	{
		feed = f;
		gantry = new Gantry();
		
		parts = new CopyOnWriteArrayList<PartsBox>(); //Initial box placed on conveyor, for testing only
		
		//Populating the feeders
		feeders = new CopyOnWriteArrayList<PartInfo>();
		feeder = new CopyOnWriteArrayList<Integer>();
		int i=0;
		while(i<4)
		{
			feeder.add(0);
			feeders.add(null);
			i++;
		}
	
		exiting = new CopyOnWriteArrayList<PartsBox>();
		purged = new CopyOnWriteArrayList<PartsBox>();
	}
	
	public void update()
	{
	
		//Temporary variable to store the state
		String state = gantry.getState();
		
		gantry.update();
		//Updating exiting p)arts boxes
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
		}
		
		if(gantry.getState().equals("load")) //If gantry has been told to load a parts box
		{
			int c=0;
			boolean it = false;
			while(c<parts.size())
			{
				if(parts.get(c).getState().equals("load"))
				{
					gantry.setxFinal(parts.get(c).getxCurrent()+10);
					gantry.setyFinal(parts.get(c).getyCurrent()+15);
					gantry.setBox(c);
					it = true;
				}
				c++;
			}
			if(it == false)
			{
				gantry.setxFinal(-99);
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
				stationMsg = true;
				gantry.setState("free");
			}
			else if(state.equals("loading"))
			{ 
				feed.get(gantry.getFeed()).setHasCrate(true);
				feed.get(gantry.getFeed()).setInfo(parts.get(gantry.getBox()).getPartInfo());
				parts.get(gantry.getBox()).setState("feeding");
				gantry.setState("free");
				feederMsg = true;
				state = "free";
			}
			else if(state.equals("dumpi"))
			{
				gantry.setState("dumpf");
				state = "dumpf";
				gantry.setFeed(-1);
				purged.get(gantry.getBox()).setState("dumpf");
				gantry.setxFinal(285);
				gantry.setyFinal(207);
				purged.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
				purged.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
			}
			else if(state.equals("dumpf"))
			{
				exiting.add(purged.get(gantry.getBox()));
				exiting.get(exiting.size()-1).exit();
				purged.remove(gantry.getBox());
				gantry.setState("free");
				state = "free";
			}
			else if(state.equals("purgei"))
			{
				int c=0;
				while(c<purged.size())
				{
					if(purged.get(c).getFeeder()==gantry.getFeed())
					{
						gantry.setState("dumpf");
						gantry.setxFinal(285);
						gantry.setyFinal(172);
						purged.add(parts.get(gantry.getBox()));
						parts.remove(gantry.getBox());
						gantry.setBox(purged.size()-1);
						purged.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
						purged.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
						purged.get(gantry.getBox()).setState("dumpf");
						state="dumpf";
						break;
					}
					c++;
				}
				if(!state.equals("dumpf"))
				{
					gantry.setState("purgef");
					gantry.setxFinal(gantry.getxFinal()+60);
					parts.get(gantry.getBox()).setxFinal(gantry.getxFinal()-10);
					parts.get(gantry.getBox()).setyFinal(gantry.getyFinal()-15);
					state="purgi";
					parts.get(gantry.getBox()).setState("purgef");
				}
				feeders.set(gantry.getFeed(),null);
				feeder.set(gantry.getFeed(), 0);
				feed.get(gantry.getFeed()).setHasCrate(false);
				gantry.setFeed(-1);
			}
			else if(state.equals("purgef"))
			{
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
	
	public CopyOnWriteArrayList<PartsBox> getPartsBoxes()
	{
		return parts;
	}
	
	public synchronized void setGantry(Gantry g)
	{
		gantry = g;
	}
	
	public synchronized void setParts(CopyOnWriteArrayList<PartsBox> p)
	{
		parts = p;
	}
	
	public CopyOnWriteArrayList<PartsBox> getExiting()
	{
		return exiting;
	}
	
	public CopyOnWriteArrayList<PartsBox> getPurged()
	{
		return purged;
	}
	
	public void setFeeders(CopyOnWriteArrayList<PartInfo> f)
	{
		feeders = f;
	}
	
	public  CopyOnWriteArrayList<PartInfo> getFeeders()
	{
		return feeders;
	}
	
	public boolean isFeederMsg() 
	{
		return feederMsg;
	}

	public void setFeederMsg(boolean msg)
	{
		feederMsg = msg;
	}

	public boolean isStationMsg()
	{
		return stationMsg;
	}
	
	public void setStationMsg(boolean msg)
	{
		stationMsg = msg;
	}
	public void addPartInfo(PartInfo p)
	{
		parts.add(new PartsBox(p));
	}
}