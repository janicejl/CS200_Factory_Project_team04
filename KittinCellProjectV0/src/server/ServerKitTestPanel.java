package server;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class ServerKitTestPanel extends JPanel implements ActionListener{
	Server server; //reference to server
	ArrayList<JButton> buttons;
	ArrayList<JPanel> subPanels; //for double boxlayout;
	ImageIcon background; //background
	
	public ServerKitTestPanel(Server _server){
		buttons = new ArrayList<JButton>();
		subPanels = new ArrayList<JPanel>();
		
		//setup GUI
		server = _server;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(533, 400));
		setMaximumSize(new Dimension(533, 400));
		setMinimumSize(new Dimension(533, 400));
		
		createButton("Load Config");
        createButton("Spawn Kit");
        createButton("Load Stand 1");
        createButton("Load Stand 2");
        createButton("Check 1");
        createButton("Check 2");
        createButton("Remove Finished");
        createButton("Exit");
		
		background = new ImageIcon("images/server.jpeg");
		
		add(Box.createRigidArea(new Dimension(0, 130)),"");
		for(int i = 0; i < buttons.size(); i++){
			subPanels.add(new JPanel());
			subPanels.get(subPanels.size()-1).setLayout(new BoxLayout(subPanels.get(subPanels.size()-1), BoxLayout.X_AXIS));
			subPanels.get(subPanels.size()-1).add(buttons.get(i), "");
			i++;
			subPanels.get(subPanels.size()-1).add(Box.createRigidArea(new Dimension(20, 0)),"");
			subPanels.get(subPanels.size()-1).add(buttons.get(i), "");
			add(subPanels.get(subPanels.size()-1), "");
            add(Box.createRigidArea(new Dimension(0, 20)),"");
        }
	}
	
	public void createButton(String s){
        JButton temp = new JButton(s);
        temp.setPreferredSize(new Dimension(150, 30));
        temp.setMaximumSize(new Dimension(150, 30));
        temp.setMinimumSize(new Dimension(150, 30));
        temp.addActionListener(this);
        temp.setActionCommand(s);
        buttons.add(temp);
    }
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		if("Load Config".equals(ae.getActionCommand())) {
          
        }
        else if("Spawn Kit".equals(ae.getActionCommand())) {
            server.execute("Spawn Kit", 1);
        }
        else if("Load Stand 1".equals(ae.getActionCommand())) {
        	server.execute("Load Stand 1");
        }
        else if("Load Stand 2".equals(ae.getActionCommand())) {
        	server.execute("Load Stand 2");
        }
        else if("Check 1".equals(ae.getActionCommand())) {
        	server.execute("Check Kit 1");
        }
        else if("Check 2".equals(ae.getActionCommand())) {
        	server.execute("Check Kit 2");
        }
        else if("Remove Finished".equals(ae.getActionCommand())) {
        	server.execute("Remove Finished");
        }
        else if("Exit".equals(ae.getActionCommand())) {
        	System.exit(1);
        }
	}
}
