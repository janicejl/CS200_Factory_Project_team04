
package server;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import MoveableObjects.Part;

public class ServerPartTestPanel extends JPanel implements ActionListener{
	Server server; //reference to server
	ArrayList<JButton> buttons;
	
	String[] nests = { "Nest 1", "Nest 2", "Nest 3", "Nest 4", "Nest 5", "Nest 6", "Nest 7", "Nest 8" };
    String[] grips = { "Gripper 1", "Gripper 2", "Gripper 3", "Gripper 4"};
	JPanel selection;
    JComboBox nestList;
    JComboBox gripList;
	ImageIcon background; //background
	
	
	public ServerPartTestPanel(Server _server){
		buttons = new ArrayList<JButton>();
		
		//setup GUI
		server = _server;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(533, 400));
		setMaximumSize(new Dimension(533, 400));
		setMinimumSize(new Dimension(533, 400));
		
		createButton("Send Recipe");
		createButton("Get Part");
        createButton("Load Kit 1");
        createButton("Load Kit 2");
        createButton("Exit");
		nestList = new JComboBox(nests);
		nestList.setPreferredSize(new Dimension(100, 30));
		nestList.setMaximumSize(new Dimension(100, 30));
		nestList.setMinimumSize(new Dimension(100, 30));
		gripList = new JComboBox(grips);
		gripList.setPreferredSize(new Dimension(100, 30));
		gripList.setMaximumSize(new Dimension(100, 30));
		gripList.setMinimumSize(new Dimension(100, 30));
		selection = new JPanel();
		selection.setLayout(new BoxLayout(selection, BoxLayout.X_AXIS));
        
		background = new ImageIcon("images/server.jpeg");
		
		add(Box.createRigidArea(new Dimension(0, 130)),"");
		selection.add(nestList, "");
		selection.add(Box.createRigidArea(new Dimension(20, 0)), "");
		selection.add(gripList, "");
		add(selection, "");
		add(Box.createRigidArea(new Dimension(0, 20)),"");
		for(int i = 0; i < buttons.size(); i++){
			add(buttons.get(i), "");
            add(Box.createRigidArea(new Dimension(0, 20)),"");
        }
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
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		if("Send Recipe".equals(ae.getActionCommand())){
			server.getPartsRobotAgent().msgMakeThisKit(new ArrayList<Part.PartType>(1), 4);
		}
		else if("Get Part".equals(ae.getActionCommand())) {
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex());
			//server.getPartsRobotAgent().msgAnimationDone();
			//server.getPartsRobotAgent().msgAnimationDone();
        }
        else if("Load Kit 1".equals(ae.getActionCommand())) {
            server.execute("Load Kit 1");
        }
        else if("Load Kit 2".equals(ae.getActionCommand())) {
        	server.execute("Load Kit 2");
        }
        else if("Exit".equals(ae.getActionCommand())) {
        	System.exit(1);
        }
	}
}

