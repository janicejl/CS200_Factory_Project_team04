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
		//background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource()== managePanel.manageButtons.get(0)){
			//create Part
			PartInfo tempPart = new PartInfo(managePanel.infoFields.get(0).getText(), "images/kt" + managePanel.imagesSelectBox.getSelectedIndex() + ".png");
//			tempPart.setType(managePanel.imagesSelectBox.getSelectedIndex());
			tempPart.setIdNumber(Integer.parseInt(managePanel.infoFields.get(1).getText()));
			tempPart.setDescription(managePanel.descriptionArea.getText());
			app.getPartsList().add(tempPart);
			
			//create button
			partListPanel.addPart(managePanel.infoFields.get(0).getText(), new ImageIcon(tempPart.getImagePath()));
			managePanel.resetForm();
			app.getClient().setCommandSent("Update Parts");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(1)){
			//clear button
			managePanel.resetForm();
		}
		else if(ae.getSource()==managePanel.manageButtons.get(2)){
			//remove all button
			partListPanel.removeAll();
			app.getPartsList().clear();
			app.getClient().setCommandSent("Update Parts");
		}
		app.updateEditPanel();
	}
	
	// update the partList panel
	public void updatePartList(){
		partListPanel.updatePartList();
		revalidate();
	}
	
	//update the data
	public void updateLoad(){
		for(int i = 0; i < app.getPartsList().size(); i++){
			partListPanel.addPart(app.getPartsList().get(i).getName(), new ImageIcon(app.getPartsList().get(i).getImagePath()));
		}
	}
}
