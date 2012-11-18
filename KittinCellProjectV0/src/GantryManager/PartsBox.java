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
	int count;
	int index;
	int feeder;
	String state;
	int cycles;
	static int xdim = 50;
	static int ydim = 100;
	static int xmax = 325;
	static int ymax = 600;
	PartInfo info;
	
	
	public PartsBox(int c)
	{
		//If the parts box is an exiting parts box
		if(c==0)
		{
			xCurrent = 275;
			yCurrent = 157;
			yFinal = 157;
			xFinal = 350;
		}
		else
		{
			count = c;
			xCurrent = xmax+5;
			xFinal = xCurrent;
			yCurrent = (ymax/2) - (ydim/2)+45;
			yFinal = yCurrent;
			state = "wait";
			cycles = 0;
			feeder = -1;
		}
	}
	
	public PartsBox(PartInfo i)
	{
		xCurrent = xmax+5;
		xFinal = xCurrent;
		yCurrent = (ymax/2)-(ydim/2)+45;
		yFinal = yCurrent;
		state = "wait";
		cycles = 0;
		feeder = -1;
		info = i;
	}
	
	//Updates the state when moving down conveyor, also updates x and y coordinates to move toward final
	public void update()
	{
		if(state == "ready")
		{
			xFinal = xmax - xdim;
			yFinal = (ymax/2)-(ydim/2)+45;
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

	public int getIndex() 
	{
		return index;
	}

	public void setIndex(int index) 
	{
		this.index = index;
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
}