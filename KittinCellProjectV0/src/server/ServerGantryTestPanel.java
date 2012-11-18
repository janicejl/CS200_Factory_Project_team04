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
		setPreferredSize(new Dimension(200,400));
		setMaximumSize(new Dimension(200,400));
		setMinimumSize(new Dimension(200,400));
		
		createButton("Load Feeder 1");
		createButton("Load Feeder 2");
		createButton("Load Feeder 3");
		createButton("Load Feeder 4");
		createButton("Dump Feeder 1");
		createButton("Dump Feeder 2");
		createButton("Dump Feeder 3");
		createButton("Dump Feeder 4");
		createButton("Purge Feeder 1");
		createButton("Purge Feeder 2");
		createButton("Purge Feeder 3");
		createButton("Purge Feeder 4");
		
		background = new ImageIcon("images/server3.jpeg");
		
		int i=0;
		while(i<buttons.size())
		{
			this.add(buttons.get(i));
			i++;
		}
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
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if("Load Feeder 1".equals(ae.getActionCommand()))
		{
			server.execute("Load Feeder",0);
		}
		else if("Load Feeder 2".equals(ae.getActionCommand()))
		{
			server.execute("Load Feeder",1);
		}
		else if("Load Feeder 3".equals(ae.getActionCommand()))
		{
			server.execute("Load Feeder",2);
		}
		else if("Load Feeder 4".equals(ae.getActionCommand()))
		{
			server.execute("Load Feeder",3);
		}
		else if("Dump Feeder 1".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder",0);
		}
		else if("Dump Feeder 2".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder",1);
		}
		else if("Dump Feeder 3".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder",2);
		}
		else if("Dump Feeder 4".equals(ae.getActionCommand()))
		{
			server.execute("Dump Feeder",3);
		}
		else if("Purge Feeder 1".equals(ae.getActionCommand()))
		{
			server.execute("Idle Bin",0);
		}
		else if("Purge Feeder 2".equals(ae.getActionCommand()))
		{
			server.execute("Idle Bin",1);
		}
		else if("Purge Feeder 3".equals(ae.getActionCommand()))
		{
			server.execute("Idle Bin",2);
		}
		else if("Purge Feeder 4".equals(ae.getActionCommand()))
		{
			server.execute("Idle Bin",3);
		}
	}
}