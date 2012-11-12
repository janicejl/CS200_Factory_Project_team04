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
	int box; //Array index of the box it is handling

	public Gantry()
	{
		xFinal = 100;
		yFinal=100;
		xCurrent = 100;
		yCurrent = 100;
		state = "free";
		box = -1; //default value indicates free
	}
	
	public synchronized void setX(int p) //set destination x
	{
		xFinal = p;
	}
	
	public synchronized void setY(int m)//set destination y
	{
		yFinal = m;
	}
	
	public synchronized void setState(String s) //set State
	{
		state = s;
	}
		
	public synchronized int getX() //get destination x
	{
		return xFinal;
	}
	
	public synchronized int getY() //get destination y
	{
		return yFinal;
	}
		
	public synchronized String getState() 
	{
		return state;
	}
		
	public synchronized void setXCurrent(int p)
	{
		xCurrent = p;
	}
		
	public synchronized void setYCurrent(int m)
	{
		yCurrent = m;
	}
	
	public synchronized int getXCurrent()
	{
		return xCurrent;
	}
	
	public synchronized int getYCurrent()
	{
		return yCurrent;
	}
	
	public synchronized void update() //updates the current to move toward the destination
	{
		if(xFinal!=xCurrent)
		{
			if(xFinal<xCurrent)
			{
				xCurrent-=1;
			}
			else
			{
				xCurrent+=1;
			}
		}
		if(yFinal != yCurrent)
		{
			if(yFinal<yCurrent)
			{
				yCurrent-=1;
			}
			else
			{
				yCurrent+=1;
			}
		}
	}
	
	public synchronized int getBox()
	{
		return box;
	}
	
	public synchronized void setBox(int b)
	{
		box =b;
	}
	
	public synchronized void setFeeder(int f) //Given the feeder number, will adjust it's coordinates, this function will also be made more adaptable later
	{
		feed = f;
		if(f==0)
		{
			xFinal = 230;
			yFinal = 65;
		}
		else if(f==1)
		{
			xFinal = 80;
			yFinal = 190;
		}
		else if(f==2)
		{
			xFinal = 80;
			yFinal = 340;
		}
		else if(f==3)
		{
			xFinal = 230;
			yFinal = 465;
		}
	}
	
	public synchronized boolean done()
	{
		if(xFinal==xCurrent && yFinal==yCurrent)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public synchronized void checkFeeder()
	{
		if(feed==0)
		{
			xFinal = 230;
			yFinal = 65;
		}
		else if(feed==1)
		{
			xFinal = 80;
			yFinal = 190;
		}
		else if(feed==2)
		{
			xFinal = 80;
			yFinal = 340;
		}
		else if(feed==3)
		{
			xFinal = 230;
			yFinal = 465;
		}
	}
	public synchronized int getFeeder()
	{
		return feed;
	}
}
