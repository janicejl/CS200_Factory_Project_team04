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
	
	
	public ServerLaneTestPanel(Server _server){
		server = _server;
		String[] lanes = {"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8" };
		laneSelectBox = new JComboBox(lanes);
		feedLaneButton = new JButton("Feed Lane");
		feedNestButton = new JButton("Feed Nest");
		
	    feedLaneButton.setPreferredSize(new Dimension(150, 30));
	    feedLaneButton.setMaximumSize(new Dimension(150, 30));
	    feedLaneButton.setMinimumSize(new Dimension(150, 30));
	    feedLaneButton.setAlignmentX(CENTER_ALIGNMENT);
	    feedLaneButton.setActionCommand("Feed Lane");
	    
	    feedNestButton.setPreferredSize(new Dimension(150, 30));
	    feedNestButton.setMaximumSize(new Dimension(150, 30));
	    feedNestButton.setMinimumSize(new Dimension(150, 30));
	    feedNestButton.setAlignmentX(CENTER_ALIGNMENT);
	    feedNestButton.setActionCommand("Feed Nest");
	    
		laneSelectBox.setPreferredSize(new Dimension(100, 30));
		laneSelectBox.setMaximumSize(new Dimension(100, 30));
		laneSelectBox.setMinimumSize(new Dimension(100, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(laneSelectBox);
		add(feedLaneButton);
		add(feedNestButton);
		
		feedLaneButton.addActionListener(this);
		feedNestButton.addActionListener(this);
		
	}
	
	
	
	public void actionPerformed(ActionEvent ae){
		//add code in server and figure out what should be sent to lane
		if("Feed Lane".equals(ae.getActionCommand())) {
			server.execute("Feed Lane",  (int)laneSelectBox.getSelectedIndex());
	    }
	    else if("Feed Nest".equals(ae.getActionCommand())) {
	    	server.execute("Feed Nest",  (int)laneSelectBox.getSelectedIndex());
	    }
	}
	
}
