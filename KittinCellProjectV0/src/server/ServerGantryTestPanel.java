package server;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ServerGantryTestPanel extends JPanel implements ActionListener
{
	Server server;
	ArrayList<JButton> buttons;
	ArrayList<JPanel> subPanels;
	ImageIcon background;
	
	public ServerGantryTestPanel(Server _server)
	{
		buttons = new ArrayList<JButton>();
		subPanels = new ArrayList<JPanel>();
		server = _server;
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(100,400));
		setMaximumSize(new Dimension(100,400));
		setMinimumSize(new Dimension(100,400));
		
		createButton("Load Config");
		createButton("Load Parts Box 1");
		createButton("Load Parts Box 2");
		createButton("Load Parts Box 3");
		createButton("Load Parts Box 4");
		createButton("Dump Feeder 1");
		createButton("Dump Feeder 2");
		createButton("Dump Feeder 3");
		createButton("Dump Feeder 4");
		background = new ImageIcon("images/server.jpeg");
		
		int i=0;
		while(i<buttons.size())
		{
			this.add(buttons.get(i));
			i++;
		}
		server.add(this);
	}
	
	public void createButton(String s)
	{
		JButton temp = new JButton(s);
		temp.setPreferredSize(new Dimension(150, 30));
	    temp.setMaximumSize(new Dimension(150, 30));
	    temp.setMinimumSize(new Dimension(150, 30));
	    temp.setAlignmentX(CENTER_ALIGNMENT);
	    temp.addActionListener(this);
	    temp.setActionCommand(s);
	    buttons.add(temp);
	}
	
	public void paintComponent(Graphics g)
	{
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if("Load Parts Box 1".equals(ae.getActionCommand()))
		{
			server.execute("Load Parts Box 1");
			System.out.println("WOOHOO");
			//server.getGantryManager().getGantry().setState("load");
			//server.getGantryManager().getGantry().setFeeder(0);
		}
		else if("Load Parts Box 2".equals(ae.getActionCommand()))
		{
			server.execute("Load Parts Box 2");
			//server.getGantryManager().getGantry().setState("load");
			//server.getGantryManager().getGantry().setFeeder(1);
		}
		else if("Load Parts Box 3".equals(ae.getActionCommand()))
		{
			server.execute("Load Parts Box 3");
			//server.getGantryManager().getGantry().setState("load");
			//server.getGantryManager().getGantry().setFeeder(2);
		}
		else if("Load Parts Box 4".equals(ae.getActionCommand()))
		{
			server.execute("Load Parts Box 4");
			//server.getGantryManager().getGantry().setState("load");
			//server.getGantryManager().getGantry().setFeeder(3);
		}
		else if("Dump Feeder 1".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder 1");
			//server.getGantryManager().getGantry().setState("dumpi");
			//server.getGantryManager().getGantry().setFeeder(0);
		}
		else if("Dump Feeder 2".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder 2");
			//server.getGantryManager().getGantry().setState("dumpi");
			//server.getGantryManager().getGantry().setFeeder(1);
		}
		else if("Dump Feeder 3".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder 3");
			//server.getGantryManager().getGantry().setState("dumpi");
			//server.getGantryManager().getGantry().setFeeder(2);
		}
		else if("Dump Feeder 4".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder 4");
			//server.getGantryManager().getGantry().setState("dumpi");
			//server.getGantryManager().getGantry().setFeeder(3);
		}
	}
}