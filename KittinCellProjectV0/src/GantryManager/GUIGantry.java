package GantryManager;

import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

public class GUIGantry
{
	int x;
	int y;
	protected BufferedImage image= null;
	
	public GUIGantry(Gantry g)
	{
		try
		{
			image = ImageIO.read(new File("images/gantryrobot.png"));
		}
		catch(IOException e){}
		x = g.getXCurrent();
		y = g.getYCurrent();
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image,x,y,null);
	}
	
	public synchronized void update(int xn, int yn)
	{
		x = xn;
		y = yn;
	}
}