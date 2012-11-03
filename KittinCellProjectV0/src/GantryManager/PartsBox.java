package GantryManager;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class PartsBox 
{
	protected BufferedImage part = null;
	protected BufferedImage box = null;
	int xFinal; //destination
	int yFinal;
	int xCurrent; //current position
	int yCurrent;
	int count; //parts in the box
	int index; //type of part
	String state;
	
	public PartsBox(BufferedImage p, int c)
	{
		part = p;
		count = c;
		xCurrent = 330;
		xFinal =330; //Initial position is off of the screen
		yFinal=280;
		yCurrent=280;
		state = "wait";
		try
		{
			box = ImageIO.read(new File("images/crate.png"));
		}
		catch(IOException e){}
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(box, xCurrent, yCurrent, null);
		g2.drawImage(part, xCurrent+15, yCurrent+15, null);
	}
	
	public BufferedImage getImage()
	{
		return part;
	}
	public void setX(int m) //Set the destination x
	{
		xFinal =m;
	}
	
	public void setY(int p)// "  "  " y
	{
		yFinal =p;
	}
	
	public void setXCurrent(int m)//Set the current x position
	{
		xCurrent = m;
	}
	public void setYCurrent(int p)//"  "   " y position
	{
		yCurrent = p;
	}
	
	public int getX() //returns destination x
	{
		return xFinal;
	}
	
	public int getY() // resturns destination y
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
	
	public void update() //Given the state, it will determine what to do next;
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
