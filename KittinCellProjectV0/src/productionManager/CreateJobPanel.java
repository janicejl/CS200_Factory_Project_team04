package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;

import data.Job;

public class CreateJobPanel extends JPanel implements ActionListener {

	ProductionManagerPanel app;			//Reference to the ProductionManagerPanel	
	Vector<String> kitNames;			//Collection of kit names
	ImageIcon background;				//Background image of the panel. 
	JPanel content;						//Panel to hold all the different components of the panel 
	TitledBorder title;					//Setting the title for the panel section
	JPanel selectionDetails;			//Panel of all the selection details. 
	JLabel kitLabel;					//Label for the kit
	JComboBox kitBox;					//Drop Down list of all the available premade kits to add to job list. 
	JLabel amtLabel;					//Ammount label
	JTextField amtText;					//Textfield for the user to put the amount of kits to make for the job. 
	JButton addJob;						//Button for the user to add the job to the job list. 
	
	//Constructor for CreateJobPanel
	public CreateJobPanel(ProductionManagerPanel _app) {		
		//background = new ImageIcon("images/background.png");
		this.setOpaque(false);
		app = _app;
		content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		title = new TitledBorder("Create a Job");
		title.setTitleJustification(title.CENTER);
		Font f = new Font("Cabrilli", Font.BOLD, 16);
		title.setTitleFont(f);
		setBorder(title);
		
		selectionDetails = new JPanel();
		selectionDetails.setLayout(new BoxLayout(selectionDetails, BoxLayout.X_AXIS));
		selectionDetails.setOpaque(false);
		selectionDetails.setPreferredSize(new Dimension(370, 30));
		selectionDetails.setMaximumSize(new Dimension(370, 30));
		selectionDetails.setMinimumSize(new Dimension(370, 30));
		selectionDetails.setAlignmentX(CENTER_ALIGNMENT);
		
		kitLabel = new JLabel("Kits:");
		kitLabel.setPreferredSize(new Dimension(40, 30));
		kitLabel.setMaximumSize(new Dimension(40, 30));
		kitLabel.setMinimumSize(new Dimension(40, 30));
		
		kitNames = new Vector<String>();
		if(app.getApp().getKitsList().get(0) != null){
			for(int i = 0; i < app.getApp().getKitsList().size(); i++){
				kitNames.add(app.getApp().getKitsList().get(i).getName());
			}
		}
		else{
			kitNames.add("");
		}
		
		kitBox = new JComboBox(kitNames);
		kitBox.setPreferredSize(new Dimension(100, 30));
		kitBox.setMaximumSize(new Dimension(100, 30));
		kitBox.setMinimumSize(new Dimension(100, 30));
		kitBox.addActionListener(this);
		
		amtLabel = new JLabel("Amount:");
		amtLabel.setPreferredSize(new Dimension(70, 30));
		amtLabel.setMaximumSize(new Dimension(70, 30));
		amtLabel.setMinimumSize(new Dimension(70, 30));
		
		amtText = new JTextField(10);
		amtText.setPreferredSize(new Dimension(70, 30));
		amtText.setMaximumSize(new Dimension(70, 30));
		amtText.setMinimumSize(new Dimension(70, 30));
		
		selectionDetails.add(Box.createRigidArea(new Dimension(20, 0)));
		selectionDetails.add(kitLabel);
		selectionDetails.add(kitBox);
		selectionDetails.add(Box.createRigidArea(new Dimension(50, 0)));
		selectionDetails.add(amtLabel);
		selectionDetails.add(amtText);
		selectionDetails.add(Box.createRigidArea(new Dimension(20, 0)));
		
		addJob = new JButton("Add to Job List");
		addJob.addActionListener(this);
		addJob.setPreferredSize(new Dimension(150, 30));
		addJob.setMaximumSize(new Dimension(150, 30));
		addJob.setMinimumSize(new Dimension(150, 30));
		addJob.setAlignmentX(CENTER_ALIGNMENT);
		
		content.add(Box.createRigidArea(new Dimension(0, 50)));
		content.add(selectionDetails);
		content.add(Box.createRigidArea(new Dimension(0, 35)));
		content.add(addJob);
		
		add(content);
		
	}
	
	public void paintComponent(Graphics g){
		//background.paintIcon(this, g, 0, 0);
	}
	
	//Action perform for button click. 
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == addJob) {
			//app.snowy.getTimer().stop();
			Integer amt = new Integer(0);
			try{
				amt = Integer.parseInt(amtText.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(null, "Invalid Amount", "Exception", JOptionPane.OK_OPTION);
				return;
			}
			Job temp = new Job(app.getApp().getKitsList().get(kitBox.getSelectedIndex()), amt);
			//update jobs
			app.getApp().getJobs().add(temp);
			app.getListPanel().getJobs().create(temp.getKit().getName(), amt);
			//update server jobs
			app.getApp().getClient().setCommandSent("Update");
			app.getApp().getClient().updateThread();
			//app.snowy.getTimer().start();
		}
		else if (ae.getSource() == kitBox){
			int index = kitBox.getSelectedIndex();
			app.updateKitPanel(index);
		}
	}
	
	//Update the list of kits available to make for jobs. 
	public void update(){
		for(int i = 0; i < app.getApp().getJobs().size(); i++){
			app.getListPanel().getJobs().create(app.getApp().getJobs().get(i).getKit().getName(), app.getApp().getJobs().get(i).getAmount());
		}
	}

}
