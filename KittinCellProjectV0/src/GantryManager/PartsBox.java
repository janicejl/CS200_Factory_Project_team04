package GantryManager;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class PartsBox implements Serializable
{
	protected BufferedImage part = null;
	protected BufferedImage box = null;
	int xFinal; //destination
	int yFinal;
	int xCurrent; //current position
	int yCurrent;
	int count; //parts in the box
	int index; //type of part
	int feeder;
	String state;
	int cycles; //Number of clock cycles
	static int xdim = 50;
	static int ydim = 100;
	static int xmax = 325;
	static int ymax = 600;
	
	public PartsBox(BufferedImage p, int c)
	{
		part = p;
		count = c;
		xCurrent = xmax+5;
		xFinal =xmax+5; //Initial position is off of the screen
		yFinal= (ymax/2) - (ydim/2);
		yCurrent= yFinal;
		state = "wait";
		cycles = 0;
		feeder = -1;
		try
		{
			box = ImageIO.read(new File("images/crate.png"));
		}
		catch(IOException e){}
	}
	
	public PartsBox(int c)
	{
		try
		{
			part = ImageIO.read(new File("images/part.png"));
			box = ImageIO.read(new File("images/crate.png"));
		}
		catch(IOException e){}
		count = c;
		xCurrent = xmax+5;
		xFinal =xmax+5; //Initial position is off of the screen
		yFinal=(ymax/2) - (ydim/2);
		yCurrent=yFinal;
		state = "wait";
		cycles = 0;
		feeder = -1;
		
		
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(box, xCurrent, yCurrent, null);
		g2.drawImage(part, xCurrent+13, yCurrent+35, null);
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
		//Parts box degredation
		
		
		//State checking
		if(state=="feeding")
		{
			cycles++;
			if(count ==0)
			{
				state="dump";
			}
			else if(cycles%10==0 && cycles!=0)
			{
				cycles=0;
				count--;
			}
		}
		else if(state == "ready")
		{
				xFinal = xmax - xdim;
				yFinal = (ymax/2)-(ydim/2);
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
	
	public int getFeeder()
	{
		return feeder;
	}
	
	public void setFeeder(int f)
	{
		feeder=f;
	}
}
