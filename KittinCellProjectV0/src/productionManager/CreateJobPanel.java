package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;

import data.Job;

public class CreateJobPanel extends JPanel implements ActionListener {

	ProductionManagerPanel app;
	Vector<String> kitNames;
	ImageIcon background;
	JPanel content;
	TitledBorder title;
	JPanel selectionDetails;
	JLabel kitLabel;
	JComboBox kitBox;
	JLabel amtLabel;
	JTextField amtText;
	JButton addJob;
	
	public CreateJobPanel(ProductionManagerPanel _app) {		
		background = new ImageIcon("images/background.png");
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
		for(int i = 0; i < app.getApp().getKitsList().size(); i++){
			kitNames.add(app.getApp().getKitsList().get(i).getName());
		}
		
		kitBox = new JComboBox(kitNames);
		kitBox.setPreferredSize(new Dimension(100, 30));
		kitBox.setMaximumSize(new Dimension(100, 30));
		kitBox.setMinimumSize(new Dimension(100, 30));
		
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
		background.paintIcon(this, g, 0, 0);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == addJob) {
			Integer amt = new Integer(0);
			try{
				amt = Integer.parseInt(amtText.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(null, "Invalid Amount", "Exception", JOptionPane.OK_OPTION);
				return;
			}
			Job temp = new Job(app.getApp().getKitsList().get(kitBox.getSelectedIndex()), amt);
			app.getApp().getJobs().add(temp);
			app.getListPanel().getJobs().create(temp.getKit().getName(), amt);
		}
	}
	
	public void update(){
		for(int i = 0; i < app.getApp().getJobs().size(); i++){
			app.getListPanel().getJobs().create(app.getApp().getJobs().get(i).getKit().getName(), app.getApp().getJobs().get(i).getAmount());
		}
	}

}
