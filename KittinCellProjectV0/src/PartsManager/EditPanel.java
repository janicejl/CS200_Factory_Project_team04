package PartsManager;

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import data.PartInfo;

public class EditPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	JComboBox partsSelectBox, imagesSelectBox;
	ArrayList<JLabel> promptLabels;
	ArrayList<JTextField> infoFields;
	JButton editButton, submitButton, resetButton;
	ArrayList<JPanel> horizPanels;
	JTextArea descriptionArea;
//	String[] parts;
	Vector<String> partNames;
	Vector<ImageIcon> partImages;
	
	public EditPanel(PartsManagerApp _app){
		app = _app;
		promptLabels = new ArrayList<JLabel>();
		infoFields = new ArrayList<JTextField>();
		horizPanels = new ArrayList<JPanel>();
		
		partNames = new Vector<String>();
		partsSelectBox = new JComboBox(partNames);
//		partsSelectBox = new JComboBox(parts);
		partsSelectBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		partsSelectBox.setPreferredSize(new Dimension(250, 27));
		partsSelectBox.setMinimumSize(new Dimension(250, 27));
		partsSelectBox.setMaximumSize(new Dimension(250, 27));
		updateSelectionBox(0);
		
		partImages = new Vector<ImageIcon>();
		for(int i=0;i<10;i++){
			partImages.add(new ImageIcon("images/kt"+i+".png"));
		}
		imagesSelectBox = new JComboBox(partImages);
		imagesSelectBox.setAlignmentY(Component.CENTER_ALIGNMENT);
		imagesSelectBox.setPreferredSize(new Dimension(140, 27));
		imagesSelectBox.setMinimumSize(new Dimension(140, 27));
		imagesSelectBox.setMaximumSize(new Dimension(140, 27));
		imagesSelectBox.setEnabled(false);
		
		promptLabels.add(new JLabel("Part:"));
		promptLabels.get(promptLabels.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
		promptLabels.add(new JLabel("Image:"));
		promptLabels.add(new JLabel("Name:"));
		promptLabels.add(new JLabel("Number:"));
		promptLabels.add(new JLabel("Description:"));
		
		promptLabels.get(promptLabels.size()-1).setAlignmentY(Component.TOP_ALIGNMENT);
		
		for(int i=0;i<2;i++){
			infoFields.add(new JTextField());
			infoFields.get(i).setAlignmentX(Component.LEFT_ALIGNMENT);
			infoFields.get(i).setPreferredSize(new Dimension(140, 27));
			infoFields.get(i).setMaximumSize(new Dimension(140, 27));
			infoFields.get(i).setMinimumSize(new Dimension(140, 27));
		}
		
		descriptionArea = new JTextArea(100, 5);
		descriptionArea.setAlignmentY(Component.TOP_ALIGNMENT);
		descriptionArea.setPreferredSize(new Dimension(270, 180));
		descriptionArea.setMaximumSize(new Dimension(270, 180));
		descriptionArea.setMinimumSize(new Dimension(270, 180));
		descriptionArea.setLineWrap(true);
		
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
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(partsSelectBox);
		horizPanels.get(horizPanels.size()-1).add(editButton);
		
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 50));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(1));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(177, 27)));
		horizPanels.get(horizPanels.size()-1).add(imagesSelectBox);

		for(int i=0;i<2;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 50));
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
			horizPanels.get(horizPanels.size()-1).add(promptLabels.get(i+2));
			if(i==0){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(180,27)));
			}
			else if(i==1){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(168,27)));
			}
			horizPanels.get(horizPanels.size()-1).add(infoFields.get(i));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 180));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(4));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,20)));
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
			add(Box.createRigidArea(new Dimension(500, 16)));
		}
		
		editButton.addActionListener(this);
		submitButton.addActionListener(this);
		resetButton.addActionListener(this);
	}
	
	public void updateSelectionBox(int num){
//		parts = new String[partList.size()];
		partNames.clear();
		if(app.getPartsList().size() == 0){
			partNames.add("");
		}
		for(int i=0; i< app.getPartsList().size(); i++){
			partNames.add(app.getPartsList().get(i).getName());
		}
		
		partsSelectBox.setModel(new DefaultComboBoxModel(partNames));
		if(num < partNames.size()){
			partsSelectBox.setSelectedIndex(num);
		}
		else{
			partsSelectBox.setSelectedIndex(partNames.size()-1);
		}
	}
	
	public void actionPerformed(ActionEvent ae){
		updateSelectionBox(partsSelectBox.getSelectedIndex());
		if(ae.getSource()==editButton){
			infoFields.get(0).setText(app.getPartsList().get(partsSelectBox.getSelectedIndex()).getName());
			imagesSelectBox.setSelectedIndex(Integer.parseInt(app.getPartsList().get(partsSelectBox.getSelectedIndex()).getImagePath().substring(9, 10)));
			infoFields.get(1).setText(""+app.getPartsList().get(partsSelectBox.getSelectedIndex()).getIdNumber());
			descriptionArea.setText(app.getPartsList().get(partsSelectBox.getSelectedIndex()).getDescription());
			imagesSelectBox.setEnabled(true);
			partsSelectBox.setEnabled(false);
			editButton.setEnabled(false);
			resetButton.setEnabled(true);
			submitButton.setEnabled(true);
		}
		else if(ae.getSource()==resetButton){
			resetEditForm();
			partsSelectBox.setSelectedIndex(0);
			partsSelectBox.setEnabled(true);
			imagesSelectBox.setEnabled(false);
			editButton.setEnabled(true);
			resetButton.setEnabled(false);
			submitButton.setEnabled(false);
		}
		else if(ae.getSource()==submitButton){
			PartInfo temp = app.getPartsList().get(partsSelectBox.getSelectedIndex());
			temp.setImagePath("images/kt"+imagesSelectBox.getSelectedIndex()+".png");
			temp.setName(infoFields.get(0).getText());
			temp.setIdNumber(Integer.parseInt(infoFields.get(1).getText()));
			temp.setDescription(descriptionArea.getText());
			resetEditForm();
			app.updatePartsPanel();
			updateSelectionBox(0);
			partsSelectBox.setEnabled(true);
			imagesSelectBox.setEnabled(false);
			editButton.setEnabled(true);
			resetButton.setEnabled(false);
			submitButton.setEnabled(false);
		}
	}
	
	public void resetEditForm(){
		imagesSelectBox.setSelectedIndex(0);
		for(int i=0;i<infoFields.size();i++){
			infoFields.get(i).setText("");
		}
		descriptionArea.setText("");
	}
	
//	public static void main(String[] args){
//		Vector<PartInfo> partlist = new Vector<PartInfo>();
//		JFrame frame = new JFrame();
//		EditPanel p1 = new EditPanel(partlist);
//		frame.add(p1);
//		frame.setSize(400, 500);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
}
