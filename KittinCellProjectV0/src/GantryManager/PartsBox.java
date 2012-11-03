package GantryManager;

import java.awt.image.*;

public class PartsBox 
{
	protected BufferedImage part = null;
	int xFinal;
	int yFinal;
	int xCurrent;
	int yCurrent;
	int count;
	int index;
	String state;
	
	public PartsBox(BufferedImage p, int c)
	{
		part = p;
		count = c;
		xCurrent = 330;
		xFinal =330;
		yFinal=280;
		yCurrent=280;
		state = "wait";
	}
	
	public BufferedImage getImage()
	{
		return part;
	}
	public void setX(int m)
	{
		xFinal =m;
	}
	
	public void setY(int p)
	{
		yFinal =p;
	}
	
	public void setXCurrent(int m)
	{
		xCurrent = m;
	}
	public void setYCurrent(int p)
	{
		yCurrent = p;
	}
	
	public int getX()
	{
		return xFinal;
	}
	
	public int getY()
	{
		return yFinal;
	}
	
	public int getXCurrent()
	{
		return xCurrent;
	}
	
	public int getYCurrent()
	{
		return yCurrent;
	}
	
	public void update()
	{
		//State checking
		if(state == "ready")
		{
				xFinal = 280;
				yFinal = 280;
				state = "loading";
		}
		else if(state =="loading")
		{
			if(xFinal == xCurrent && yFinal==yCurrent)
			{
				state = "load";
			}
		}
		
		
		//Position updating
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
	
	public String getState()
	{
		return state;
	}
	
	public void setState(String s)
	{
		state = s;
	}
}
