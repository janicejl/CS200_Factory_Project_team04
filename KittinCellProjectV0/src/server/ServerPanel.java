package server;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ServerPanel extends JPanel implements ActionListener{
	
	JFrame server; //reference to server
	JButton kit;
	JButton parts;
	JButton lane;
	JButton exit; //exit button
	ImageIcon background; //background
	
	public ServerPanel(JFrame _server){
		
		//setup GUI
		server = _server;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(533, 400));
		setMaximumSize(new Dimension(533, 400));
		setMinimumSize(new Dimension(533, 400));
		
		Font f = new Font("Verdana", Font.BOLD, 14); //set font
		kit = new JButton("Kit Assembly");
		kit.setFont(f);
		kit.setPreferredSize(new Dimension(150, 35));
		kit.setMaximumSize(new Dimension(150, 35));
		kit.setMinimumSize(new Dimension(150, 35));
		kit.setAlignmentX(CENTER_ALIGNMENT); //alignment
		kit.addActionListener(this); //action listener
		
		parts = new JButton("Parts Robot");
		parts.setFont(f);
		parts.setPreferredSize(new Dimension(150, 35));
		parts.setMaximumSize(new Dimension(150, 35));
		parts.setMinimumSize(new Dimension(150, 35));
		parts.setAlignmentX(CENTER_ALIGNMENT); //alignment
		parts.addActionListener(this); //action listener
		
		lane = new JButton("Lane Manager");
		lane.setFont(f);
		lane.setPreferredSize(new Dimension(150, 35));
		lane.setMaximumSize(new Dimension(150, 35));
		lane.setMinimumSize(new Dimension(150, 35));
		lane.setAlignmentX(CENTER_ALIGNMENT); //alignment
		lane.addActionListener(this); //action listener
		
		exit = new JButton("Exit");
		exit.setFont(f);
		exit.setPreferredSize(new Dimension(150, 35));
		exit.setMaximumSize(new Dimension(150, 35));
		exit.setMinimumSize(new Dimension(150, 35));
		exit.setAlignmentX(CENTER_ALIGNMENT); //alignment
		exit.addActionListener(this); //action listener
		
		background = new ImageIcon("images/server.jpeg");
		
		add(Box.createRigidArea(new Dimension(0, 130)),"");
		add(kit, "");
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(parts, "");
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(lane, "");
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		add(exit, "");
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == kit){
			int i = ((Server)server).start();
			if(i == 1){
				kit.setEnabled(false);
				parts.setEnabled(false);
				lane.setEnabled(false);
			}
		}
		else if(e.getSource() == parts){
			int i = ((Server)server).start();
			if(i == 1){
				kit.setEnabled(false);
				parts.setEnabled(false);
				lane.setEnabled(false);
			}
		}
		else if(e.getSource() == lane){
			int i = ((Server)server).start();
			if(i == 1){
				kit.setEnabled(false);
				parts.setEnabled(false);
				lane.setEnabled(false);
			}
		}
		else if(e.getSource() == exit){
			System.exit(0);
		}
	}
}
