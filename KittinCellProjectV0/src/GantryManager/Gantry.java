package GantryManager;

import java.io.*;

//Class that keeps track of GantryRobot position, destination, and state information
public class Gantry implements Serializable 
{
	//Destination Coordinates
	int xFinal; 
	int yFinal;
	//Current Coordinates
	int xCurrent;
	int yCurrent;
	//State Information
	String state;
	int feed; //Feeder #
	int box; //Array index of box gantry is picking up
	int speed = 2;
	
	public Gantry()
	{
		xFinal = 100;
		yFinal = 100;
		xCurrent = 100;
		yCurrent = 100;
		state = "free";
		box = -1;
	}
	
	//Function to update position 
	public void update()
	{
		if(xFinal==-99)
		{
			
		}
		else
		{
			if(xFinal!=xCurrent)
			{
				if(xFinal<xCurrent)
					xCurrent-=speed;
				else	
					xCurrent+=speed;
				if(Math.abs(xFinal-xCurrent) < speed)
					xCurrent = xFinal;
			}
			if(yFinal!=yCurrent)
			{
				if(yFinal<yCurrent)
					yCurrent-=speed;
				else
					yCurrent+=speed;
				if(Math.abs(yFinal-yCurrent) < speed)
					yCurrent = yFinal;
			}
		}
		
		//Depending on feeder information, will change final destination
		if(feed == 0)
		{
			
			xFinal = 101;
			yFinal = 55;
		}
		else if(feed == 1)
		{
			xFinal = 26;
			yFinal = 195;
		}
		else if(feed == 2)
		{
			xFinal = 26;
			yFinal = 335;
		}
		else if(feed == 3)
		{
			xFinal = 101;
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

	//Same as second half of update, updates destination based on provided feeder
	public synchronized void setFeed(int feed)
	{
		this.feed = feed;
		if(feed == 0)
		{
			
			xFinal = 101;
			yFinal = 55;
		}
		else if(feed == 1)
		{
			xFinal = 26;
			yFinal = 195;
		}
		else if(feed == 2)
		{
			xFinal = 26;
			yFinal = 335;
		}
		else if(feed == 3)
		{
			xFinal = 101;
			yFinal = 475;
		}
	}
	
	//Function for determining if the gantry has reached its destination
	public boolean done()
	{
		if(xFinal==-99)
			return false;
		else if(xFinal==xCurrent && yFinal==yCurrent)
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