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
	ArrayList<JPanel> horizPanels;
	ImageIcon background;
	JComboBox laneSelectBox, feederSelectBox;
	GUIServer feedBackPanel;
	
	public ServerTestPanel(Server _server){
		server = _server;
		background = new ImageIcon("images/server4.jpg");
		String[] lanes = {"Lane 1", "Lane 2","Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
		String[] feeders = {"Feeder 1", "Feeder 2", "Feeder 3", "Feeder 4"};
		buttons = new ArrayList<JButton>();
		horizPanels = new ArrayList<JPanel>();
		
		feedBackPanel = new GUIServer();
		feedBackPanel.setPreferredSize(new Dimension(800, 300));
		feedBackPanel.setMaximumSize(new Dimension(800,300));
		feedBackPanel.setMinimumSize(new Dimension(800,300));
		
		laneSelectBox = new JComboBox(lanes);
		laneSelectBox.setPreferredSize(new Dimension(400,30));
		laneSelectBox.setMaximumSize(new Dimension(400,30));
		laneSelectBox.setMinimumSize(new Dimension(400,30));
		
		feederSelectBox = new JComboBox(feeders);
		feederSelectBox.setPreferredSize(new Dimension(400,30));
		feederSelectBox.setMaximumSize(new Dimension(400,30));
		feederSelectBox.setMinimumSize(new Dimension(400,30));
		
		buttons.add(new JButton("Load"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Load");
		
		buttons.add(new JButton("Purge"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Purge");
		
		buttons.add(new JButton("Feed Feeder"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Feed Feeder");
		
		buttons.add(new JButton("Feed Lane"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Feed Lane");
		
		buttons.add(new JButton("Feed Nest"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Feed Nest");
		
		buttons.add(new JButton("Remove Finished"));
		buttons.get(buttons.size()-1).setPreferredSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMaximumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setMinimumSize(new Dimension(150, 40));
		buttons.get(buttons.size()-1).setActionCommand("Remove Finished");
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(feederSelectBox);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(0));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(1));
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(laneSelectBox);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(2));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(3));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(4));
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(buttons.get(5));
		
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(20,100)));
		for(int i=0;i<horizPanels.size();i++){
			
			horizPanels.get(i).setOpaque(false);
			add(horizPanels.get(i));
		}
		add(feedBackPanel);
		
		for(int i=0;i<buttons.size();i++){
			buttons.get(i).addActionListener(this);
		}
		
		
	}
	
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getActionCommand().equals("Load")){
			server.execute("Load Feeder",feederSelectBox.getSelectedIndex());
		}
		else if(ae.getActionCommand().equals("Purge")){
			server.execute("Idle Bin",feederSelectBox.getSelectedIndex());
		}	
		else if(ae.getActionCommand().equals("Feed Feeder")){
			server.execute("Feed Lane",  (int)laneSelectBox.getSelectedIndex());
		}
		else if(ae.getActionCommand().equals("Feed lane")){
			server.execute("Feed Nest",  (int)laneSelectBox.getSelectedIndex());
		}
		else if(ae.getActionCommand().equals("Feed Nest")){
			server.execute("Feed Feeder", (int)laneSelectBox.getSelectedIndex());
		}
		else if(ae.getActionCommand().equals("Remove Finished")){
			//server.getKitRobotAgent().msgKitInspected(true);
		}
	}
	
	//this main function is for testing
	public static void main(String[] args){
		
	}
}
