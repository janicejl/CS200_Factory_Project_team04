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
	JButton jamButton;
	JButton overFlowButton;
	JLabel laneLabel;
	JSlider vibrationSlider;
	String[] lanes;
	ImageIcon background; //background
	
	public LaneManagerPanel(LaneManagerApp _app){
		app = _app;
		lanes = new String[]{"Lane 1", "Lane 2", "Lane 3", "Lane 4", "Lane 5", "Lane 6", "Lane 7", "Lane 8"};
		horizPanels  = new ArrayList<JPanel>();
		background = new ImageIcon("images/server4.jpg");
		jamButton = new JButton("Jam Selected");
		jamButton.setPreferredSize(new Dimension(130, 27));
		jamButton.setMaximumSize(new Dimension(130, 27));
		jamButton.setMinimumSize(new Dimension(130, 27));
		jamButton.addActionListener(this);
		overFlowButton = new JButton("Overflow Nest");
		overFlowButton.setPreferredSize(new Dimension(130, 27));
		overFlowButton.setMaximumSize(new Dimension(130, 27));
		overFlowButton.setMinimumSize(new Dimension(130, 27));
		overFlowButton.addActionListener(this);
		
		laneSelectBox = new JComboBox(lanes);
		laneSelectBox.setPreferredSize(new Dimension(400, 27));
		laneSelectBox.setMinimumSize(new Dimension(400, 27));
		laneSelectBox.setMaximumSize(new Dimension(400, 27));
		laneSelectBox.addActionListener(this);
		
		vibrationSlider = new JSlider(0, 10);
		vibrationSlider.addChangeListener(this);
		vibrationSlider.setPreferredSize(new Dimension(690, 40));
		vibrationSlider.setMinimumSize(new Dimension(690, 40));
		vibrationSlider.setMaximumSize(new Dimension(690, 40));
		vibrationSlider.setMajorTickSpacing(10);
		vibrationSlider.setMinorTickSpacing(1);
		vibrationSlider.setPaintTicks(true);
		vibrationSlider.setPaintLabels(true);
		vibrationSlider.setSnapToTicks(true);
		vibrationSlider.setValue(0);
		
		laneLabel = new JLabel("Vibration:");
		laneLabel.setPreferredSize(new Dimension(70, 27));
		laneLabel.setMaximumSize(new Dimension(70, 27));
		laneLabel.setMinimumSize(new Dimension(70, 27));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).add(laneSelectBox);
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
		horizPanels.get(horizPanels.size()-1).add(jamButton);
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
		horizPanels.get(horizPanels.size()-1).add(overFlowButton);
		horizPanels.get(horizPanels.size()-1).setOpaque(false);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10, 50)));
		horizPanels.get(horizPanels.size()-1).add(laneLabel);
		horizPanels.get(horizPanels.size()-1).setOpaque(false);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(700, 50));
		horizPanels.get(horizPanels.size()-1).add(vibrationSlider);
		horizPanels.get(horizPanels.size()-1).setOpaque(false);
		
		for(int i=0;i<horizPanels.size();i++){
			this.add(horizPanels.get(i));
		}
		this.setBorder(new TitledBorder("Lane"));		
	}
	
	public void actionPerformed(ActionEvent ae){		
		if(ae.getSource()==jamButton){
			app.getOnJammeds().set(laneSelectBox.getSelectedIndex(),!app.getOnJammeds().get(laneSelectBox.getSelectedIndex()));
		}
		if(ae.getSource()==overFlowButton){
			app.getOverFlow().set(laneSelectBox.getSelectedIndex(),!app.getOverFlow().get(laneSelectBox.getSelectedIndex()));
		}
		if(laneSelectBox.getSelectedItem().equals("Lane 1")){
			if(app.getOnJammeds().get(0).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(0).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(0).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(0).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 2")){
			if(app.getOnJammeds().get(1).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(1).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(1).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(1).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 3")){
			if(app.getOnJammeds().get(2).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(2).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(2).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(2).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
			
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 4")){
			if(app.getOnJammeds().get(3).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(3).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(3).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(3).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 5")){
			if(app.getOnJammeds().get(4).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(4).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(4).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(4).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 6")){
			if(app.getOnJammeds().get(5).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(5).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(5).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(5).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 7")){
			if(app.getOnJammeds().get(6).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(6).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(6).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(6).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
		else if(laneSelectBox.getSelectedItem().equals("Lane 8")){
			if(app.getOnJammeds().get(7).equals(true)){
				jamButton.setText("Unjam Selected");
			}
			else if(app.getOnJammeds().get(7).equals(false)){
				jamButton.setText("Jam Selected");
			}
			if(app.getOverFlow().get(7).equals(true)){
				overFlowButton.setText("Fix Overflow");
			}
			else if(app.getOverFlow().get(7).equals(false)){
				overFlowButton.setText("Overflow Nest");
			}
		}
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
	}
	
//	//this main is for testing the panel
//	public static void main(String[] args){
//		JFrame test = new JFrame();
//		LaneManagerPanel p1 = new LaneManagerPanel();
//		test.add(p1);
//		test.setSize(700, 400);
//		test.setVisible(true);
//		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
