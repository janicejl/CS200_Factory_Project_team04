
package server;

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

import MoveableObjects.Bin;

import data.*;

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
		
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(150, 400));
		setMaximumSize(new Dimension(150, 400));
		setMinimumSize(new Dimension(150, 400));
		
		createButton("Send Recipe");
		createButton("Get Part");
        createButton("Take Picture");
		//createButton("Load Kit 1");
        //createButton("Load Kit 2");
        createButton("Exit");
		nestList = new JComboBox(nests);
		nestList.setPreferredSize(new Dimension(100, 20));
		nestList.setMaximumSize(new Dimension(100, 20));
		nestList.setMinimumSize(new Dimension(100, 20));
		gripList = new JComboBox(grips);
		gripList.setPreferredSize(new Dimension(100, 20));
		gripList.setMaximumSize(new Dimension(100, 20));
		gripList.setMinimumSize(new Dimension(100, 20));
		selection = new JPanel();
		selection.setLayout(new BoxLayout(selection, BoxLayout.X_AXIS));
        
		background = new ImageIcon("images/server2.jpeg");
		
		add(Box.createRigidArea(new Dimension(0, 120)),"");
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
        temp.setPreferredSize(new Dimension(150, 20));
        temp.setMaximumSize(new Dimension(150, 20));
        temp.setMinimumSize(new Dimension(150, 20));
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
			KitInfo kit = new KitInfo("TestKit");
			Vector<Bin> bins = new Vector<Bin>();
			for(int i = 0; i<2; i++){
				PartInfo p = new PartInfo("p" + i+1,"images/kt" + (i + 1) + ".png");
				kit.add(p);
				bins.add(new Bin(p, 3));
			}
		//	server.getGantryController().msgBinConfiguration(bins);
			server.getPartsRobotAgent().msgMakeThisKit(kit, 4);
			server.getKitRobotAgent().msgGetKits(4);
		}
		else if("Get Part".equals(ae.getActionCommand())) {
			/*server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex());
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+1);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+2);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+3);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+4);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+5);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+6);
			server.getPartsRobotAgent().msgPartsApproved((int)nestList.getSelectedIndex()+7);
			*/
//			for(int i = 0; i<8; i++){
//				for(int j = 0; j<4; j++){
//				server.getNestAgent(i).msgHereIsPart();
//				}
//			}


			//server.getPartsRobotAgent().msgAnimationDone();
			//server.getPartsRobotAgent().msgAnimationDone();
        }
		else if("Take Picture".equals(ae.getActionCommand())){
			server.execute("Take Picture", nestList.getSelectedIndex());
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

