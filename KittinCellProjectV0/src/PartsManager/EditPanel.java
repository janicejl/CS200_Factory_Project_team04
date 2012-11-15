package PartsManager;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import data.PartInfo;

public class EditPanel extends JPanel implements ActionListener{
	
	JComboBox partsSelectBox;
	ArrayList<JLabel> promptLabels;
	ArrayList<JTextField> editFields;
	Vector<PartInfo> partList;
	JButton editButton, submitButton, resetButton;
	ArrayList<JPanel> horizPanels;
	JTextArea descriptionArea;
	Timer timer;
	String[] parts;
	
	public EditPanel(Vector<PartInfo> _partList){
		partList = _partList;
		updateSelectionBox();
		promptLabels = new ArrayList<JLabel>();
		editFields = new ArrayList<JTextField>();
		horizPanels = new ArrayList<JPanel>();
		partsSelectBox = new JComboBox(parts);
		partsSelectBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		partsSelectBox.setPreferredSize(new Dimension(290, 27));
		partsSelectBox.setMinimumSize(new Dimension(290, 27));
		partsSelectBox.setMaximumSize(new Dimension(290, 27));
		
		promptLabels.add(new JLabel("Part:"));
		promptLabels.get(promptLabels.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
		promptLabels.add(new JLabel("Name:"));
		promptLabels.add(new JLabel("Number:"));
		promptLabels.add(new JLabel("Description:"));
		
		promptLabels.get(promptLabels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
		
		for(int i=0;i<2;i++){
			editFields.add(new JTextField());
			editFields.get(i).setAlignmentX(Component.LEFT_ALIGNMENT);
			editFields.get(i).setPreferredSize(new Dimension(140, 27));
			editFields.get(i).setMaximumSize(new Dimension(140, 27));
			editFields.get(i).setMinimumSize(new Dimension(140, 27));
		}
		
		descriptionArea = new JTextArea(100, 5);
		descriptionArea.setAlignmentY(Component.TOP_ALIGNMENT);
		descriptionArea.setPreferredSize(new Dimension(270, 180));
		descriptionArea.setMaximumSize(new Dimension(270, 180));
		descriptionArea.setMinimumSize(new Dimension(270, 180));
		
		editButton = new JButton("Edit");
		editButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		editButton.setPreferredSize(new Dimension(70, 27));
		editButton.setMaximumSize(new Dimension(70, 27));
		editButton.setMinimumSize(new Dimension(70, 27));
		editButton.setEnabled(true);
		
		submitButton = new JButton("Submit");
		submitButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		submitButton.setPreferredSize(new Dimension(90, 40));
		submitButton.setMaximumSize(new Dimension(90, 40));
		submitButton.setMinimumSize(new Dimension(90, 40));
		submitButton.setEnabled(false);
		
		resetButton = new JButton("Reset");
		submitButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		resetButton.setPreferredSize(new Dimension(90, 40));
		resetButton.setMaximumSize(new Dimension(90, 40));
		resetButton.setMinimumSize(new Dimension(90, 40));
		resetButton.setEnabled(false);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(0));
		horizPanels.get(horizPanels.size()-1).add(partsSelectBox);
		horizPanels.get(horizPanels.size()-1).add(editButton);
		
		for(int i=0;i<2;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
			horizPanels.get(horizPanels.size()-1).add(promptLabels.get(i+1));
			if(i==0){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(200,27)));
			}
			else if(i==1){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(188,27)));
			}
			horizPanels.get(horizPanels.size()-1).add(editFields.get(i));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(3));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(30,20)));
		horizPanels.get(horizPanels.size()-1).add(descriptionArea);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setAlignmentX(Component.LEFT_ALIGNMENT);
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(resetButton);
		horizPanels.get(horizPanels.size()-1).add(submitButton);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(400, 20)));
		setAlignmentX(Component.CENTER_ALIGNMENT);

		for(int i=0;i<horizPanels.size();i++){
			add(horizPanels.get(i));
			add(Box.createRigidArea(new Dimension(500, 20)));
		}
	}
	
	public void updateSelectionBox (){
		parts = new String[partList.size()];
		for(int i=0;i<partList.size();i++){
			parts[i] = partList.get(i).getName();
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		updateSelectionBox();
		if(ae.getSource()==editButton){
			editFields.get(0).setText(partList.get(partsSelectBox.getSelectedIndex()).getName());
			editButton.setEnabled(false);
			resetButton.setEnabled(true);
			submitButton.setEnabled(true);
		}
		else if(ae.getSource()==resetButton){
			editFields.get(0).setText(partList.get(partsSelectBox.getSelectedIndex()).getName());
			editButton.setEnabled(true);
			resetButton.setEnabled(false);
			submitButton.setEnabled(false);
		}
		else if(ae.getSource()==submitButton){
			PartInfo temp = partList.get(partsSelectBox.getSelectedIndex());
			temp.setName(editFields.get(0).getText());
			editButton.setEnabled(true);
			resetButton.setEnabled(false);
			submitButton.setEnabled(false);
		}
	}
	
	public static void main(String[] args){
		Vector<PartInfo> partlist = new Vector<PartInfo>();
		JFrame frame = new JFrame();
		EditPanel p1 = new EditPanel(partlist);
		frame.add(p1);
		frame.setSize(400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
	}
}
