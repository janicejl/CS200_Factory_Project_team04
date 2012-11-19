package PartsManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;

public class ManagePanel extends JPanel{
	
	Vector<ImageIcon> partImages;
	ArrayList<JButton> manageButtons;
	ArrayList<JLabel> promptLabels;
	ArrayList<JTextField> infoFields;
	ArrayList<JPanel> horizPanels;
	JComboBox imagesSelectBox;
	JTextArea descriptionArea;
	ImageIcon background;
	
	public ManagePanel(){
		manageButtons = new ArrayList<JButton>();
		promptLabels = new ArrayList<JLabel>();
		infoFields = new ArrayList<JTextField>();
		horizPanels = new ArrayList<JPanel>();
		
		partImages = new Vector<ImageIcon>();
		for(int i=0;i<10;i++){
			partImages.add(new ImageIcon("images/kt"+i+".png"));
		}
		
		imagesSelectBox = new JComboBox(partImages);
		imagesSelectBox.setPreferredSize(new Dimension(175, 25));
		imagesSelectBox.setMaximumSize(new Dimension(175, 25));
		imagesSelectBox.setMinimumSize(new Dimension(175, 25));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		promptLabels.add(new JLabel("Image:"));
		promptLabels.get(promptLabels.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
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
		descriptionArea.setPreferredSize(new Dimension(250, 90));
		descriptionArea.setMaximumSize(new Dimension(250, 90));
		descriptionArea.setMinimumSize(new Dimension(250, 90));
		descriptionArea.setLineWrap(true);
		
		manageButtons.add(new JButton("Create"));
		manageButtons.get(manageButtons.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
		manageButtons.get(manageButtons.size()-1).setPreferredSize(new Dimension(70, 40));
		manageButtons.get(manageButtons.size()-1).setMaximumSize(new Dimension(70, 40));
		manageButtons.get(manageButtons.size()-1).setMinimumSize(new Dimension(70, 40));
		
		manageButtons.add(new JButton("Clear"));
		manageButtons.get(manageButtons.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
		manageButtons.get(manageButtons.size()-1).setPreferredSize(new Dimension(70, 40));
		manageButtons.get(manageButtons.size()-1).setMaximumSize(new Dimension(70, 40));
		manageButtons.get(manageButtons.size()-1).setMinimumSize(new Dimension(70, 40));
		
		manageButtons.add(new JButton("Remove All"));
		manageButtons.get(manageButtons.size()-1).setAlignmentY(Component.CENTER_ALIGNMENT);
		manageButtons.get(manageButtons.size()-1).setPreferredSize(new Dimension(90, 40));
		manageButtons.get(manageButtons.size()-1).setMaximumSize(new Dimension(90, 40));
		manageButtons.get(manageButtons.size()-1).setMinimumSize(new Dimension(90, 40));
		
		// group components into panel 
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 30));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 30));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 30));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(0));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(125, 27)));
		horizPanels.get(horizPanels.size()-1).add(imagesSelectBox);

		for(int i=0;i<2;i++){
			horizPanels.add(new JPanel());
			horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 30));
			horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 30));
			horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 30));
			horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
			horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
			horizPanels.get(horizPanels.size()-1).add(promptLabels.get(i+1));
			if(i==0){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(158,27)));
			}
			else if(i==1){
				horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(145,27)));
			}
			horizPanels.get(horizPanels.size()-1).add(infoFields.get(i));
		}
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 110));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 110));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 110));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,27)));
		horizPanels.get(horizPanels.size()-1).add(promptLabels.get(3));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(10,20)));
		horizPanels.get(horizPanels.size()-1).add(descriptionArea);
		
		horizPanels.add(new JPanel());
		horizPanels.get(horizPanels.size()-1).setPreferredSize(new Dimension(500, 40));
		horizPanels.get(horizPanels.size()-1).setMinimumSize(new Dimension(500, 40));
		horizPanels.get(horizPanels.size()-1).setMaximumSize(new Dimension(500, 40));
		horizPanels.get(horizPanels.size()-1).setLayout(new BoxLayout(horizPanels.get(horizPanels.size()-1), BoxLayout.X_AXIS));
		horizPanels.get(horizPanels.size()-1).add(Box.createRigidArea(new Dimension(120,10)));
		for(int i=0;i<manageButtons.size();i++){
			horizPanels.get(horizPanels.size()-1).add(manageButtons.get(i));
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// add all panels to the base panel
		for(int i=0;i<horizPanels.size();i++){
			add(horizPanels.get(i));
			//add(Box.createRigidArea(new Dimension(500, 10)));
		}
		
		TitledBorder border = new TitledBorder("Part Creation");
		setBorder(border);
	}
	
	//clear all text fields and text area
	public void resetForm(){
		
		imagesSelectBox.setSelectedIndex(0);
		for(int i=0;i<infoFields.size();i++){
			infoFields.get(i).setText("");
		}
		descriptionArea.setText("");
	}
	
	//refresh
	public void paintComponent(Graphics g){
		revalidate();
	}
	
}
