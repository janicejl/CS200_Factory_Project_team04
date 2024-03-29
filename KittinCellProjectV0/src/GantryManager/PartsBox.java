package GantryManager;

import java.io.*;
import data.PartInfo;

//Class that holds parts box info such as destination, current location, and window size for painting
public class PartsBox implements Serializable
{
	int xFinal;
	int yFinal;
	int xCurrent;
	int yCurrent;
	int feeder;
	int speed = 2;
	String state;
	static int xdim = 50;
	static int ydim = 100;
	static int xmax = 325;
	static int ymax = 600;
	PartInfo info;
	boolean empty = false;
	
	public PartsBox(PartInfo i)
	{
		xCurrent = xmax+5;
		xFinal = xCurrent;
		yCurrent = 310;
		yFinal = yCurrent;
		state = "wait";
		feeder = -1;
		info = i;
	}
	
	//Updates the state when moving down conveyor, also updates x and y coordinates to move toward final
	public void update()
	{
		if(state == "ready")
		{
			xFinal = xmax - xdim;
			state = "loading";
		}
		else if(state == "loading")
		{
			if(xFinal==xCurrent && yFinal==yCurrent)
				state="load";
		}
		
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
	
	public void exit()
	{
		xCurrent = 275;
		yCurrent = 192;
		yFinal = 192;
		xFinal = 350;
	}

	public int getxFinal()
	{
		return xFinal;
	}

	public void setxFinal(int xFinal) 
	{
		this.xFinal = xFinal;
	}

	public int getyFinal() 
	{
		return yFinal;
	}

	public void setyFinal(int yFinal) 
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

	public int getFeeder() 
	{
		return feeder;
	}

	public void setFeeder(int feeder) 
	{
		this.feeder = feeder;
	}

	public String getState() 
	{
		return state;
	}

	public void setState(String state) 
	{
		this.state = state;
	}
	
	//checks if the parts box has reached its destination
	public boolean done()
	{
		if(xCurrent==xFinal && yFinal==yCurrent)
			return true;
		else 
			return false;
	}
	
	public PartInfo getPartInfo()
	{
		return info;
	}
	
	public boolean getEmpty()
	{
		return empty;
	}
	
	public void setEmpty(boolean b)
	{
		empty = b;
	}
	
	
}
