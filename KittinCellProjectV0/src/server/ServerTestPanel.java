package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class ServerTestPanel extends JPanel implements ActionListener {
	
	Server server;
	ArrayList<JButton> buttons;
	ArrayList<JPanel> subPanels;
	ImageIcon background;
	JPanel feederPanel, lanePanel, kitPanel;
	JComboBox laneSelectBox, feederSelectBox;
	Vector<String> lanes, feeders;
	
	public ServerTestPanel(Server _server){
		server = _server;
		this.setSize(new Dimension(1100, 400));
		
	}
	
	public void createButton(String s){
		JButton temp = new JButton(s);
		temp.setPreferredSize(new Dimension(150, 30));
	    temp.setMaximumSize(new Dimension(150, 30));
	    temp.setMinimumSize(new Dimension(150, 30));
	    temp.setAlignmentX(CENTER_ALIGNMENT);
	    temp.addActionListener(this);
	    temp.setActionCommand(s);
	    buttons.add(temp);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//this main function is for testing
	public static void main(String[] args){
		
	}
}
