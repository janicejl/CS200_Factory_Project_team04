package GantryManager;

import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIGantryManager extends JFrame implements ActionListener
{
	painterPanel paintPanel;
	JPanel testPanel;
	JTextField xText;
	JTextField yText;
	JButton moveButton;
	Gantry gantry;
	ArrayList<PartsBox> parts;
	ArrayList<Integer> feeders;
	int speed;
	public Timer timer;
	protected BufferedImage test = null;
	

	

	public GUIGantryManager()
	{
		timer = new Timer(10,this);
		gantry = new Gantry();
		paintPanel = new painterPanel();
		paintPanel.setGantry(gantry);

		xText = new JTextField(20);
		yText = new JTextField(20);
		moveButton = new JButton("Move Gantry");
		moveButton.addActionListener(this);

		this.add(paintPanel);
		try
		{
			test = ImageIO.read(new File("images/part.png"));
		}
		catch(IOException e)
		{
		}
		
		parts = new ArrayList<PartsBox>();
		parts.add(new PartsBox(test,10));
		
		feeders = new ArrayList<Integer>();
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		feeders.add(0);
		
		paintPanel.setPartsBoxes(parts);
	}
	
	public static void main(String[] args)
	{
		GUIGantryManager gui = new GUIGantryManager();
		gui.setSize(345,600);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
		gui.startTimer();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==moveButton)
		{
			String xString = xText.getText();
			if(xString!="")
			{
				gantry.setX(Integer.parseInt(xString));
			}
			String yString = yText.getText();
			if(yString!="")
			{
				gantry.setY(Integer.parseInt(yString));
			}
		}

		int i =0;
		boolean go = true;
		while(i<parts.size())
		{
			if(parts.get(i).getState() == "dump" || parts.get(i).getState()=="load")
			{
				go = false;
			}
			parts.get(i).update();
			i++;
		}
		if(go == true)
		{
			go=false;
			i=0;
			while(i<parts.size() && go==false)
			{
				if(parts.get(i).getState()=="ready")
				{
					go = true;
				}
				else if(parts.get(i).getState()=="wait")
				{
					parts.get(i).setState("ready");
					go = true;
				}
				i++;
			}
			if(go==false && parts.size()<4)
			{
				parts.add(new PartsBox(test, 2));
			}
		}
		
		if(gantry.getState()=="free")
		{
			int c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="dump")
				{
					gantry.setX(parts.get(c).getXCurrent());
					gantry.setY(parts.get(c).getYCurrent());
					gantry.setBox(c);
					gantry.setState("dumpi");
				}
				c++;
			}
			c=0;
			while(c<parts.size())
			{
				if(parts.get(c).getState()=="load")
				{
					gantry.setX(parts.get(c).getXCurrent()+5);
					gantry.setY(parts.get(c).getYCurrent()-15);
					gantry.setBox(c);
					gantry.setState("load");
				}
				c++;
			}
		}
		else if(gantry.getState() == "load")
		{
			if(gantry.getX()==gantry.getXCurrent() && gantry.getY()==gantry.getYCurrent())
			{
				gantry.setState("loading");
				int c=0;
				while(c<feeders.size())
				{
					if(feeders.get(c)==0)
					{
						gantry.setFeeder(c);
						parts.get(gantry.getBox()).setX(gantry.getX()-5);
						parts.get(gantry.getBox()).setY(gantry.getY()+15);
						feeders.set(c, 1);
						break;
					}
					c++;
				}
			}
		}
		else if(gantry.getState()=="loading")
		{
			if(gantry.getX()==gantry.getXCurrent() && gantry.getY() == gantry.getYCurrent())
			{
				parts.get(gantry.getBox()).setState("feeding");
				gantry.setBox(-1);
				gantry.setState("free");
			}
			
		}
		
		
		gantry.update();
		paintPanel.repaint();
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void setTimerDelay(int d)
	{
		timer.setDelay(d);
	}
	

	
}
		
