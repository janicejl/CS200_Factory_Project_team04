package GantryManager;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class GUIPartsBox
{
	protected BufferedImage part = null;
	protected BufferedImage box = null;
	int x;
	int y;
	
	public GUIPartsBox(BufferedImage p, PartsBox pb)
	{
		part = p;
		try
		{
			box = ImageIO.read(new File("images/crate.png"));
		}
		catch(IOException e){}
		x = pb.getXCurrent();
		y = pb.getYCurrent();
	}
	
	public GUIPartsBox(PartsBox pb)
	{
		try
		{
			box = ImageIO.read(new File("images/crate.png"));
			part = ImageIO.read(new File("images/part.png"));
		}
		catch(IOException e){}
		x = pb.getXCurrent();
		y = pb.getYCurrent();
	}
	
	public void update(int xn, int yn)
	{
		x = xn;
		y= yn;
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(box, x,y,null);
		g2.drawImage(part, x+13, y+35,null);
	}
	
	public BufferedImage getImage()
	{
		return part;
	}
}