package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class PartsPanel extends JPanel implements ActionListener{
	
	PartsManagerApp app;
	ManagePanel managePanel;
	PartListPanel partListPanel;
	ImageIcon background;
	
	public PartsPanel(PartsManagerApp _app){
		app = _app;
		managePanel = new ManagePanel();
		partListPanel = new PartListPanel();
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
		partListPanel.repaint();
		managePanel.repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae){
		
		if(ae.getSource()==managePanel.manageButtons.get(0)){
			//create button
			partListPanel.addPart(managePanel.nameField.getText(), (ImageIcon)managePanel.imagesSelectBox.getSelectedItem());
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(1)){
			//clear button
			managePanel.nameField.setText("");
		}
		else if(ae.getSource()==managePanel.manageButtons.get(2)){
			//remove all button
			partListPanel.removeAll();
		}
	}
	
}
