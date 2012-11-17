package GantryManager;

import java.io.*;

public class Gantry implements Serializable
{
	int xFinal;
	int yFinal;
	int xCurrent;
	int yCurrent;
	String state;
	int feed;
	int box;
	
	public Gantry()
	{
		xFinal = 100;
		yFinal = 100;
		xCurrent = 100;
		yCurrent = 100;
		state = "free";
		box = -1;
	}
	
	public void update()
	{
		if(xFinal!=xCurrent)
		{
			if(xFinal<xCurrent)
				xCurrent-=1;
			else
				xCurrent+=1;
		}
		if(yFinal!=yCurrent)
		{
			if(yFinal<yCurrent)
				yCurrent-=1;
			else
				yCurrent+=1;
		}
		
		if(feed == 0)
		{
			
			xFinal = 110;
			yFinal = 55;
		}
		else if(feed == 1)
		{
			xFinal = 35;
			yFinal = 195;
		}
		else if(feed == 2)
		{
			xFinal = 35;
			yFinal = 335;
		}
		else if(feed == 3)
		{
			xFinal = 110;
			yFinal = 475;
		}
	}

	public int getxFinal() 
	{
		return xFinal;
	}

	public synchronized void setxFinal(int xFinal) 
	{
		this.xFinal = xFinal;
	}

	public int getyFinal() {
		return yFinal;
	}

	public synchronized void setyFinal(int yFinal)
	{
		this.yFinal = yFinal;
	}

	public int getxCurrent()
	{
		return xCurrent;
	}

	public void setxCurrent(int xCurrent)
	{
		this.xCurrent = xCurrent;
	}

	public int getyCurrent()
	{
		return yCurrent;
	}

	public void setyCurrent(int yCurrent)
	{
		this.yCurrent = yCurrent;
	}

	public synchronized String getState()
	{
		return state;
	}

	public synchronized void setState(String state)
	{
		this.state = state;
	}

	public synchronized int getFeed()
	{
		return feed;
	}

	public synchronized void setFeed(int feed)
	{
		this.feed = feed;
		if(feed == 0)
		{
			
			xFinal = 110;
			yFinal = 55;
		}
		else if(feed == 1)
		{
			xFinal = 35;
			yFinal = 195;
		}
		else if(feed == 2)
		{
			xFinal = 35;
			yFinal = 335;
		}
		else if(feed == 3)
		{
			xFinal = 110;
			yFinal = 475;
		}
	}
	
	public boolean done()
	{
		if(xFinal==xCurrent && yFinal==yCurrent)
			return true;
		else 
			return false;
	}
	
	public int getBox()
	{
		return box;
	}

	public synchronized void setBox(int box)
	{
		this.box = box;
	}
	
}