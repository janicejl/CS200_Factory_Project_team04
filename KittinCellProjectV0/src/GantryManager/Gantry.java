package GantryManager;

import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

public class Gantry
{
	int xFinal;
	int yFinal;
	int xCurrent;
	int yCurrent;
	String state;
	int feed;
	int box; //Array index of the box it is handling
	protected BufferedImage image = null;
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, xCurrent, yCurrent,null);
		
	}
		
	public Gantry()
	{
		
		xFinal = 100;
		yFinal=100;
		xCurrent = 100;
		yCurrent = 100;
		state = "free";
		box = -1; //default value indicates free
		try
		{
			image = ImageIO.read(new File("images/gantryrobot.png"));
		}
		catch(IOException e){}
	}
	
	public void setX(int p) //set destination x
	{
		xFinal = p;
	}
	
	public void setY(int m)//set destination y
	{
		yFinal = m;
	}
	
	public void setState(String s) //set State
	{
		state = s;
	}
		
	public int getX() //get destination x
	{
		return xFinal;
	}
	
	public int getY() //get destination y
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
	
	public void update() //updates the current to move toward the destination
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
	
	public void setFeeder(int f) //Given the feeder number, will adjust it's coordinates, this function will also be made more adaptable later
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
	
	public boolean done()
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
	public void checkFeeder()
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
	public int getFeeder()
	{
		return feed;
	}
}
