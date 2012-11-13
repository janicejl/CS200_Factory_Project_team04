package GantryManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

public class GantryManagerPanel extends JPanel implements ActionListener{
	JPanel feederPanel, gantryPanel;
	ArrayList<JPanel> horizPanels;
	ArrayList<JLabel> feederLabels;
	ArrayList<JLabel> stateLabels;
	ArrayList<JLabel> coLabels;
	ArrayList<JLabel> gantryLabels;
	JButton moveGantryButton, purgeButton;
	ArrayList<JTextField> coField;
	JComboBox feederSelectBox;
	String[] feeders;
	Timer timer;
	
	public GantryManagerPanel(){
		feeders = new String[]{"Feeder 1", "Feeder 2", "Feeder 3", "Feeder 4"};
		feederPanel = new JPanel();
		gantryPanel = new JPanel();
		horizPanels = new ArrayList<JPanel>();
		feederLabels = new ArrayList<JLabel>();
		stateLabels = new ArrayList<JLabel>();
		gantryLabels = new ArrayList<JLabel>();
		coLabels = new ArrayList<JLabel>();
		feederSelectBox = new JComboBox(feeders);
		feederSelectBox.setAlignmentY(Component.TOP_ALIGNMENT);
		feederSelectBox.setPreferredSize(new Dimension(250, 50));
		feederSelectBox.setMaximumSize(new Dimension(250, 50));
		feederSelectBox.setMinimumSize(new Dimension(250, 50));
		
		moveGantryButton = new JButton("Move Gantry");
		moveGantryButton.setAlignmentY(Component.TOP_ALIGNMENT);
		moveGantryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		moveGantryButton.setPreferredSize(new Dimension(250,50));
		moveGantryButton.setMaximumSize(new Dimension(250, 50));
		moveGantryButton.setMinimumSize(new Dimension(250, 50));
		
		purgeButton = new JButton("Purge Selected");
		purgeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		purgeButton.setAlignmentY(Component.TOP_ALIGNMENT);
		purgeButton.setPreferredSize(new Dimension(250,40));
		purgeButton.setMaximumSize(new Dimension(250, 40));
		purgeButton.setMinimumSize(new Dimension(250, 40));
		
		coField = new ArrayList<JTextField>();
		
		for(int i=0;i<2;i++){
			coField.add(new JTextField());
			coField.get(i).setAlignmentX(Component.LEFT_ALIGNMENT);
			coField.get(i).setPreferredSize(new Dimension(100, 27));
			coField.get(i).setMinimumSize(new Dimension(100, 27));
			coField.get(i).setMaximumSize(new Dimension(100, 27));
			coField.get(i).setHorizontalAlignment(JTextField.CENTER);
		}
		
		for(int i=0;i<4;i++){
			feederLabels.add(new JLabel("Feeder"+(i+1)));
			feederLabels.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);
			feederLabels.get(i).setPreferredSize(new Dimension(70, 27));
			feederLabels.get(i).setMinimumSize(new Dimension(70, 27));
			feederLabels.get(i).setMaximumSize(new Dimension(70, 27));
			stateLabels.add(new JLabel("Idle"));
			stateLabels.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);
			stateLabels.get(i).setPreferredSize(new Dimension(30, 27));
			stateLabels.get(i).setMinimumSize(new Dimension(30, 27));
			stateLabels.get(i).setMaximumSize(new Dimension(30, 27));
		}
		
		gantryLabels.add(new JLabel("X Coordinates"));
		gantryLabels.add(new JLabel("Y Coordinates"));
		gantryLabels.add(new JLabel("X"));
		gantryLabels.add(new JLabel("Y"));
		coLabels.add(new JLabel("-1"));
		coLabels.add(new JLabel("-1"));
		
		
		for(int i=0;i<2;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(gantryLabels.get(i));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(100, 27)));
			horizPanels.get(horizPanels.size()-1).add(coLabels.get(i));
		}
		
		for(int i=2;i<4;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(gantryLabels.get(i));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(100, 27)));
			horizPanels.get(horizPanels.size()-1).add(coField.get(i-2));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.Y_AXIS));
		horizPanels.get(horizPanels.size()-1).add(moveGantryButton);
		
		for(int i=5;i<9;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
			horizPanels.get(horizPanels.size()-1).setAlignmentX(Component.CENTER_ALIGNMENT);
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(feederLabels.get(i-5));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(60, 27)));
			horizPanels.get(horizPanels.size()-1).add(stateLabels.get(i-5));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
		horizPanels.get(horizPanels.size()-1).setAlignmentX(Component.CENTER_ALIGNMENT);
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.Y_AXIS));
		horizPanels.get(horizPanels.size()-1).add(feederSelectBox);
		horizPanels.get(horizPanels.size()-1).add(purgeButton);
		
		
		
		gantryPanel.setLayout(new BoxLayout(gantryPanel, BoxLayout.Y_AXIS));
		gantryPanel.setBorder(new TitledBorder("Gantry Options"));
		feederPanel.setLayout(new BoxLayout(feederPanel, BoxLayout.Y_AXIS));
		feederPanel.setBorder(new TitledBorder("Feeder Options"));
		
		gantryPanel.add(Box.createRigidArea(new Dimension(250, 20)));
		for(int i=0;i<5;i++){
			gantryPanel.add(horizPanels.get(i));
			gantryPanel.add(Box.createRigidArea(new Dimension(250,20)));
		}
		feederPanel.add(Box.createRigidArea(new Dimension(250, 20)));
		for(int i=5;i<10;i++){
			feederPanel.add(horizPanels.get(i));
			feederPanel.add(Box.createRigidArea(new Dimension(250,20)));
		}
		
		setLayout(new GridLayout(1, 2));
		add(gantryPanel);
		add(feederPanel);
		
		moveGantryButton.addActionListener(this);
		purgeButton.addActionListener(this);
		timer = new Timer(40, this);
		timer.start();
		
	}
	
	public void actionPerformed(ActionEvent ae){
		//code for updating the coordinate of gantry robot
		
		//code for updating the state of feeders
		
		if(ae.getSource()==moveGantryButton){
			
		}
		else if(ae.getSource()==purgeButton){
			
		}
	}
	
	//This main function is for testing the panel
	public static void main(String[] args){
		JFrame frame = new JFrame();
		GantryManagerPanel p1 = new GantryManagerPanel();
		frame.add(p1);
		frame.setVisible(true);
		frame.setSize(500,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
