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
	
	public synchronized void update()
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
		/*if(Math.abs(xCurrent-xFinal) < 10){
			xCurrent = xFinal;
		}
		if(Math.abs(yCurrent-yFinal) < 10){
			yCurrent = yFinal;
		}*/
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

	public synchronized int getxFinal() 
	{
		return xFinal;
	}

	public synchronized void setxFinal(int xFinal) 
	{
		this.xFinal = xFinal;
	}

	public synchronized int getyFinal() {
		return yFinal;
	}

	public synchronized void setyFinal(int yFinal)
	{
		this.yFinal = yFinal;
	}

	public synchronized int getxCurrent()
	{
		return xCurrent;
	}

	public synchronized void setxCurrent(int xCurrent)
	{
		this.xCurrent = xCurrent;
	}

	public synchronized int getyCurrent()
	{
		return yCurrent;
	}

	public synchronized void setyCurrent(int yCurrent)
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
		System.out.println("wtfffffff!!!!");
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
	
	public synchronized boolean done()
	{
		if(xFinal==xCurrent && yFinal==yCurrent)
			return true;
		else 
			return false;
	}
	
	public synchronized int getBox()
	{
		return box;
	}

	public synchronized void setBox(int box)
	{
		this.box = box;
	}
	
}