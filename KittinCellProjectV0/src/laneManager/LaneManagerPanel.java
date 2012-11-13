package laneManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LaneManagerPanel extends JPanel implements ActionListener{
	ArrayList<JLabel> nestLabels;
	ArrayList<JLabel> stateLabels;
	ArrayList<JPanel> horizPanels;
	JComboBox nestSelectBox, laneSelectBox;
	JButton purgeButton;
	JLabel laneLabel;
	JSlider laneSpeedSlider;
	JPanel lanePanel, nestPanel;
	String[] nests, lanes;
	Timer timer;
	
	public LaneManagerPanel(){
		lanes = new String[]{"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
		nests = new String[]{"Nest 1", "Nest 2", "Nest 3", "Nest 4", "Nest 5", "Nest 6", "Nest 7", "Nest 8"};
		nestLabels = new ArrayList<JLabel>();
		stateLabels = new ArrayList<JLabel>();
		horizPanels = new ArrayList<JPanel>();
		nestSelectBox = new JComboBox(nests);
		nestSelectBox.setPreferredSize(new Dimension(500, 50));
		nestSelectBox.setMaximumSize(new Dimension(500, 50));
		nestSelectBox.setMinimumSize(new Dimension(500, 50));
		
		purgeButton = new JButton("Purge Selected");
		purgeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		purgeButton.setPreferredSize(new Dimension(300, 50));
		purgeButton.setMaximumSize(new Dimension(300, 50));
		purgeButton.setMinimumSize(new Dimension(300, 50));
		
		lanePanel = new JPanel();
		nestPanel = new JPanel();
		
		laneSelectBox = new JComboBox(lanes);
		laneSpeedSlider = new JSlider(0, 100);
		
		laneLabel = new JLabel("Vibration:");
		laneLabel.setPreferredSize(new Dimension(70, 27));
		laneLabel.setMaximumSize(new Dimension(70, 27));
		laneLabel.setMinimumSize(new Dimension(70, 27));
		laneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		for(int i=0;i<8;i++){
			nestLabels.add(new JLabel("Nest "+(i+1)));
			stateLabels.add(new JLabel("Empty"));
			nestLabels.get(i).setPreferredSize(new Dimension(70, 27));
			nestLabels.get(i).setMaximumSize(new Dimension(70, 27));
			nestLabels.get(i).setMinimumSize(new Dimension(70, 27));
			stateLabels.get(i).setPreferredSize(new Dimension(70, 27));
			stateLabels.get(i).setMaximumSize(new Dimension(70, 27));
			stateLabels.get(i).setMinimumSize(new Dimension(70, 27));
		}
		
		setLayout(new BorderLayout());
		TitledBorder border = new TitledBorder("Lanes");
		lanePanel.setBorder(border);
		border = new TitledBorder("Nests");
		nestPanel.setBorder(border);
		
		lanePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		lanePanel.setLayout(new BoxLayout(lanePanel, BoxLayout.Y_AXIS));
		lanePanel.add(laneSelectBox);
		lanePanel.add(laneLabel);
		lanePanel.add(laneSpeedSlider);
		
		for(int i=0;i<4;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(i).setSize(new Dimension(500, 27));
			horizPanels.get(i).setLayout(new BoxLayout(horizPanels.get(i), BoxLayout.X_AXIS));
			horizPanels.get(i).add(nestLabels.get(i));
			horizPanels.get(i).add(stateLabels.get(i));
			horizPanels.get(i).add(Box.createRigidArea(new Dimension(30, 27)));
			horizPanels.get(i).add(nestLabels.get(i+4));
			horizPanels.get(i).add(stateLabels.get(i+4));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(4).setLayout(new BoxLayout(horizPanels.get(4), BoxLayout.Y_AXIS));
		horizPanels.get(4).add(nestSelectBox);
		horizPanels.get(4).add(purgeButton);
		
		nestPanel.setLayout(new BoxLayout(nestPanel, BoxLayout.Y_AXIS));
		for(int i=0;i<horizPanels.size();i++){
			nestPanel.add(horizPanels.get(i));
		}
		
		add(lanePanel, BorderLayout.NORTH);
		add(nestPanel, BorderLayout.CENTER);
	
		purgeButton.addActionListener(this);
		
		timer = new Timer(40, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent ae){
		//code for updating the state of lanes
		
		if(ae.getSource()==purgeButton){
			
		}
	}
	
	//this main is for testing the panel
	public static void main(String[] args){
		JFrame test = new JFrame();
		LaneManagerPanel p1 = new LaneManagerPanel();
		test.add(p1);
		test.setSize(500, 400);
		test.setVisible(true);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
