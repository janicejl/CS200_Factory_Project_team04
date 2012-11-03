
package server;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class ServerPartTestPanel extends JPanel implements ActionListener{
	Server server; //reference to server
	ArrayList<JButton> buttons;
	ImageIcon background; //background
	
	public ServerPartTestPanel(Server _server){
		buttons = new ArrayList<JButton>();
		
		//setup GUI
		server = _server;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(533, 400));
		setMaximumSize(new Dimension(533, 400));
		setMinimumSize(new Dimension(533, 400));
		
		createButton("Get Part");
        createButton("Load Kit 1");
        createButton("Load Kit 2");
        createButton("Exit");
		
		background = new ImageIcon("images/server.jpeg");
		
		add(Box.createRigidArea(new Dimension(0, 130)),"");
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
        temp.addActionListener(this);
        temp.setActionCommand(s);
        buttons.add(temp);
    }
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		if("Get Part".equals(ae.getActionCommand())) {
          server.execute("Get Part", 1, 1);
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

