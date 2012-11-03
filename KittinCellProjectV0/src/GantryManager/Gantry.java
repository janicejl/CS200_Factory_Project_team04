package GantryManager;

public class Gantry
{
	int xFinal;
	int yFinal;
	int xCurrent;
	int yCurrent;
	String state;
	int box;
		
	public Gantry()
	{
		
		xFinal = 100;
		yFinal=100;
		xCurrent = 100;
		yCurrent = 100;
		state = "free";
		box = -1;
	}
	
	public void setX(int p)
	{
		xFinal = p;
	}
	
	public void setY(int m)
	{
		yFinal = m;
	}
	
	public void setState(String s)
	{
		state = s;
	}
		
	public int getX()
	{
		return xFinal;
	}
	
	public int getY()
	{
		return yFinal;
	}
		
	public String getState()
	{
		return state;
	}
		
	public void setXCurrent(int p)
	{
		xCurrent = p;
	}
		
	public void setYCurrent(int m)
	{
		yCurrent = m;
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
	
	public int getBox()
	{
		return box;
	}
	
	public void setBox(int b)
	{
		box =b;
	}
	
	public void setFeeder(int f)
	{
		if(f==0)
		{
			xFinal = 210;
			yFinal = 65;
		}
		else if(f==1)
		{
			xFinal = 60;
			yFinal = 190;
		}
		else if(f==2)
		{
			xFinal = 60;
			yFinal = 340;
		}
		else if(f==3)
		{
			xFinal = 210;
			yFinal = 465;
		}
	}
}
