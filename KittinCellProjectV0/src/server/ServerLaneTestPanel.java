package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.util.*;

public class ServerLaneTestPanel extends JPanel implements ActionListener{
	Server server;
	JComboBox laneSelectBox;
	JButton feedLaneButton;
	JButton feedNestButton;
	JButton feedPartButton;
	JButton exit;
	
	ImageIcon background;
	
	public ServerLaneTestPanel(Server _server){
		server = _server;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(533, 400));
		setMaximumSize(new Dimension(533, 400));
		setMinimumSize(new Dimension(533, 400));
		
		String[] lanes = {"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8" };
		laneSelectBox = new JComboBox(lanes);
		feedLaneButton = new JButton("Feed Lane");
		feedNestButton = new JButton("Feed Nest");
		feedPartButton = new JButton("Feed part into feeder");
		exit = new JButton("Exit");
		background = new ImageIcon("images/server.jpeg");
		
		feedPartButton.setPreferredSize(new Dimension(170, 25));
		feedPartButton.setMaximumSize(new Dimension(170, 25));
		feedPartButton.setMinimumSize(new Dimension(170, 25));
		feedPartButton.setAlignmentX(CENTER_ALIGNMENT);
		feedPartButton.setActionCommand("Feed Feeder");
		feedPartButton.addActionListener(this);
		
	    feedLaneButton.setPreferredSize(new Dimension(125, 25));
	    feedLaneButton.setMaximumSize(new Dimension(125, 25));
	    feedLaneButton.setMinimumSize(new Dimension(125, 25));
	    feedLaneButton.setAlignmentX(CENTER_ALIGNMENT);
	    feedLaneButton.setActionCommand("Feed Lane");
	    feedLaneButton.addActionListener(this);
	    
	    feedNestButton.setPreferredSize(new Dimension(125, 25));
	    feedNestButton.setMaximumSize(new Dimension(125, 25));
	    feedNestButton.setMinimumSize(new Dimension(125, 25));
	    feedNestButton.setAlignmentX(CENTER_ALIGNMENT);
	    feedNestButton.setActionCommand("Feed Nest");
	    feedNestButton.addActionListener(this);
	     
	    exit.setPreferredSize(new Dimension(125, 25));
	    exit.setMaximumSize(new Dimension(125, 25));
	    exit.setMinimumSize(new Dimension(125, 25));
	    exit.setAlignmentX(CENTER_ALIGNMENT);
	    exit.setActionCommand("Exit");
	    exit.addActionListener(this);
	    
		laneSelectBox.setPreferredSize(new Dimension(100, 25));
		laneSelectBox.setMaximumSize(new Dimension(100, 25));
		laneSelectBox.setMinimumSize(new Dimension(100, 25));
		
		add(Box.createRigidArea(new Dimension(0, 130)),"");
		add(laneSelectBox);
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(feedPartButton);
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(feedLaneButton);
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(feedNestButton);
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(exit);
		
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		//add code in server and figure out what should be sent to lane
		if("Feed Lane".equals(ae.getActionCommand())) {
			server.execute("Feed Lane",  (int)laneSelectBox.getSelectedIndex());
			//msg to MockLaneAgent
	    }
	    else if("Feed Nest".equals(ae.getActionCommand())) {
	    	server.execute("Feed Nest",  (int)laneSelectBox.getSelectedIndex());
	    	//msg to MockLaneAgent
	    }
	    else if("Feed Feeder".equals(ae.getActionCommand())){
	    	server.execute("Feed Feeder", (int)laneSelectBox.getSelectedIndex());
	    	//msg to MockLaneAgent
	    }
	    else if("Exit".equals(ae.getActionCommand())){
	    	System.exit(1);
	    }
	}
	
}
