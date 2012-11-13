package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import data.Part;
import data.PartInfo;

public class PartsPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	ManagePanel managePanel;
	PartListPanel partListPanel;
	ImageIcon background;
	
	public PartsPanel(PartsManagerApp _app){
		app = _app;
		managePanel = new ManagePanel();
		partListPanel = new PartListPanel(app);
		background = new ImageIcon("images/background.png");
		
		setLayout(new GridLayout(2,1));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(managePanel);
		add(partListPanel);
		
		//add all the buttons on managePanel to ActionListener
		for(int i=0;i<managePanel.manageButtons.size();i++){
			managePanel.manageButtons.get(i).addActionListener(this);
		}
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource()== managePanel.manageButtons.get(0)){
			//create Part
			PartInfo tempPart = new PartInfo(managePanel.nameField.getText(), "images/kt" + managePanel.imagesSelectBox.getSelectedIndex() + ".png");
			tempPart.setType(managePanel.imagesSelectBox.getSelectedIndex());
			app.getPartsList().add(tempPart);
			
			//create button
			partListPanel.addPart(managePanel.nameField.getText(), new ImageIcon(tempPart.getImagePath()));
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(1)){
			//clear button
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(2)){
			//remove all button
			partListPanel.removeAll();
			app.getPartsList().clear();
		}
	}
	
	public void updateLoad(){
		for(int i = 0; i < app.getPartsList().size(); i++){
			partListPanel.addPart(app.getPartsList().get(i).getName(), new ImageIcon(app.getPartsList().get(i).getImagePath()));
		}
	}
}
