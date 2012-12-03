package laneManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LaneManagerPanel extends JPanel implements ActionListener, ChangeListener{
	//ArrayList<JLabel> nestLabels;
	LaneManagerApp app;
	ArrayList<JPanel> horizPanels;
	JComboBox laneSelectBox;
	JButton jamButton;  //jam non-normative button
	JButton overFlowButton;
	JButton feederSlow;
	JLabel laneLabel;
	JSlider vibrationSlider;
	String[] lanes;
	ImageIcon background; //background
	ArrayList<JButton> jam;
	ArrayList<JButton> overflow;
	
//	private LaneManagerPanel() {
//		//app = _app;
//		lanes = new String[]{"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
//		background = new ImageIcon("images/server4.jpg");
//		
//		jam = new ArrayList<JButton>();
//		overflow = new ArrayList<JButton>();
//		
//		for (int i = 0; i < 8; i++) {
//			jam.add(new JButton("Jam"));
//			jam.get(i).addActionListener(this);
//			overflow.add(new JButton("Overflow"));
//			overflow.get(i).addActionListener(this);
//		}
//		
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		this.add(Box.createRigidArea(new Dimension(700, 80)));
//		for (int i = 0; i < 8; i++) {
//			JPanel temp = new JPanel();
//			temp.setOpaque(false);
//			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
//			temp.add(new JLabel(lanes[i]));
//			temp.add(new JLabel("                 "));
//			temp.add(jam.get(i));
//			temp.add(new JLabel("                 "));
//			temp.add(overflow.get(i));
//			
//			add(temp);
//			add(Box.createRigidArea(new Dimension(700, 20)));
//		}
//		
//		feederSlow = new JButton("Make Feeder Slow");
//		feederSlow.addActionListener(this);
//		
//		add(Box.createRigidArea(new Dimension(700, 20)));
//		JPanel temp = new JPanel();
//		temp.setOpaque(false);
//		temp.add(feederSlow);
//		add(temp);
//		this.setBorder(new TitledBorder("Lane"));
//		this.setPreferredSize(new Dimension(700, 600));
//		this.setMaximumSize(new Dimension(700, 600));
//		this.setMinimumSize(new Dimension(700, 600));
//	}
	
	public LaneManagerPanel(LaneManagerApp _app) {
		app = _app;
		lanes = new String[]{"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
		background = new ImageIcon("images/server4.jpg");
		
		jam = new ArrayList<JButton>();
		overflow = new ArrayList<JButton>();
		
		for (int i = 0; i < 8; i++) {
			jam.add(new JButton("Jam Lane"));
			jam.get(i).addActionListener(this);
			overflow.add(new JButton("Overflow Nest"));
			overflow.get(i).addActionListener(this);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createRigidArea(new Dimension(700, 80)));
		for (int i = 0; i < 8; i++) {
			JPanel temp = new JPanel();
			temp.setOpaque(false);
			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));
			temp.add(new JLabel(lanes[i]));
			temp.add(new JLabel("                 "));
			temp.add(jam.get(i));
			temp.add(new JLabel("                 "));
			temp.add(overflow.get(i));
			
			add(temp);
			add(Box.createRigidArea(new Dimension(700, 20)));
		}
		
		feederSlow = new JButton("Make Feeder Slow");
		feederSlow.addActionListener(this);
		
		add(Box.createRigidArea(new Dimension(700, 20)));
		JPanel temp = new JPanel();
		temp.setOpaque(false);
		temp.add(feederSlow);
		add(temp);
		this.setBorder(new TitledBorder("Lane"));
		this.setPreferredSize(new Dimension(700, 600));
		this.setMaximumSize(new Dimension(700, 600));
		this.setMinimumSize(new Dimension(700, 600));
	}
	
//	public LaneManagerPanel(LaneManagerApp _app){
//		app = _app;
//		lanes = new String[]{"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
//		horizPanels  = new ArrayList<JPanel>();
//		background = new ImageIcon("images/server4.jpg");
//		jamButton = new JButton("Jam Selected");
//		jamButton.setPreferredSize(new Dimension(130, 27));
//		jamButton.setMaximumSize(new Dimension(130, 27));
//		jamButton.setMinimumSize(new Dimension(130, 27));
//		jamButton.addActionListener(this);
//		overFlowButton = new JButton("Overflow Nest");
//		overFlowButton.setPreferredSize(new Dimension(130, 27));
//		overFlowButton.setMaximumSize(new Dimension(130, 27));
//		overFlowButton.setMinimumSize(new Dimension(130, 27));
//		overFlowButton.addActionListener(this);
//		feederSlow = new JButton("Make Feeder Slow");
//		feederSlow.setPreferredSize(new Dimension(150, 27));
//		feederSlow.setMaximumSize(new Dimension(150, 27));
//		feederSlow.setMinimumSize(new Dimension(150, 27));
//		feederSlow.addActionListener(this);
//		
//		laneSelectBox = new JComboBox(lanes);
//		laneSelectBox.setPreferredSize(new Dimension(400, 27));
//		laneSelectBox.setMinimumSize(new Dimension(400, 27));
//		laneSelectBox.setMaximumSize(new Dimension(400, 27));
//		laneSelectBox.addActionListener(this);
//		
//		vibrationSlider = new JSlider(0, 10);
//		vibrationSlider.addChangeListener(this);
//		vibrationSlider.setPreferredSize(new Dimension(690, 40));
//		vibrationSlider.setMinimumSize(new Dimension(690, 40));
//		vibrationSlider.setMaximumSize(new Dimension(690, 40));
//		vibrationSlider.setMajorTickSpacing(10);
//		vibrationSlider.setMinorTickSpacing(1);
//		vibrationSlider.setPaintTicks(true);
//		vibrationSlider.setPaintLabels(true);
//		vibrationSlider.setSnapToTicks(true);
//		vibrationSlider.setValue(0);
//		
//		laneLabel = new JLabel("Vibration:");
//		laneLabel.setPreferredSize(new Dimension(70, 27));
//		laneLabel.setMaximumSize(new Dimension(70, 27));
//		laneLabel.setMinimumSize(new Dimension(70, 27));
//		
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		horizPanels.add(new JPanel());
//		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
//		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).add(laneSelectBox);
//		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
//		horizPanels.get(horizPanels.size()-1).add(jamButton);
//		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
//		horizPanels.get(horizPanels.size()-1).add(overFlowButton);
//		//set opaque to false in order to show the background
//		horizPanels.get(horizPanels.size()-1).setOpaque(false);
//		
//		horizPanels.add(new JPanel());
//		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
//		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
//		horizPanels.get(horizPanels.size()-1).add(laneLabel);
//		horizPanels.get(horizPanels.size()-1).setOpaque(false);
//		
//		horizPanels.add(new JPanel());
//		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
//		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
//		horizPanels.get(horizPanels.size()-1).add(vibrationSlider);
//		horizPanels.get(horizPanels.size()-1).setOpaque(false);
//		
//		for(int i=0;i<horizPanels.size();i++){
//			this.add(horizPanels.get(i));
//		}
//		this.add(feederSlow);
//		this.setBorder(new TitledBorder("Lane"));		
//	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == feederSlow){
			app.setFeederSlow(!app.isFeederSlow());
			if(app.isFeederSlow()){
				feederSlow.setText("Fix Feeder Slow");
			}
			else{
				feederSlow.setText("Make Feeder Slow");
			}
		}
		for (int i = 0; i < 8; i++) {
			if (ae.getSource() == jam.get(i)) {
				app.getOnJammeds().set(i, !app.getOnJammeds().get(i));
				if (app.getOnJammeds().get(i).equals(true)) {
					jam.get(i).setText("Unjam Lane");
				} else {
					jam.get(i).setText("Jam Lane");
				}
			} else if (ae.getSource() == overflow.get(i)) {
				app.getOverFlow().set(i,!app.getOverFlow().get(i));
				if (app.getOverFlow().get(i).equals(true)) {
					jam.get(i).setText("Fix Overflow");
				} else {
					jam.get(i).setText("Overflow Nest");
				}
			}
		}
	}
	
//	public void actionPerformed(ActionEvent ae){		
//		if(ae.getSource()==jamButton){
//			app.getOnJammeds().set(laneSelectBox.getSelectedIndex(),!app.getOnJammeds().get(laneSelectBox.getSelectedIndex()));
//		}
//		if(ae.getSource()==overFlowButton){
//			app.getOverFlow().set(laneSelectBox.getSelectedIndex(),!app.getOverFlow().get(laneSelectBox.getSelectedIndex()));
//		}
//		if(ae.getSource() == feederSlow){
//			app.setFeederSlow(!app.isFeederSlow());
//			if(app.isFeederSlow()){
//				feederSlow.setText("Fix Feeder Slow");
//			}
//			else{
//				feederSlow.setText("Make Feeder Slow");
//			}
//		}
//		//listen to the selection box in order to change the text of the jamButton
//		if(laneSelectBox.getSelectedItem().equals("Lane 1")){
//			if(app.getOnJammeds().get(0).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(0).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(0).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(0).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 2")){
//			if(app.getOnJammeds().get(1).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(1).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(1).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(1).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 3")){
//			if(app.getOnJammeds().get(2).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(2).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(2).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(2).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//			
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 4")){
//			if(app.getOnJammeds().get(3).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(3).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(3).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(3).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 5")){
//			if(app.getOnJammeds().get(4).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(4).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(4).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(4).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 6")){
//			if(app.getOnJammeds().get(5).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(5).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(5).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(5).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 7")){
//			if(app.getOnJammeds().get(6).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(6).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(6).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(6).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//		else if(laneSelectBox.getSelectedItem().equals("Lane 8")){
//			if(app.getOnJammeds().get(7).equals(true)){
//				jamButton.setText("Unjam Selected");
//			}
//			else if(app.getOnJammeds().get(7).equals(false)){
//				jamButton.setText("Jam Selected");
//			}
//			if(app.getOverFlow().get(7).equals(true)){
//				overFlowButton.setText("Fix Overflow");
//			}
//			else if(app.getOverFlow().get(7).equals(false)){
//				overFlowButton.setText("Overflow Nest");
//			}
//		}
//	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
	}
	
//	//this main is for testing the panel
//	public static void main(String[] args){
//		JFrame test = new JFrame();
//		LaneManagerPanel p1 = new LaneManagerPanel();
//		test.add(p1);
//		test.setSize(700, 600);
//		test.setVisible(true);
//		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
