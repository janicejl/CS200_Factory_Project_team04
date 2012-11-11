package productionManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class CreateJobPanel extends JPanel implements ActionListener {

	ProductionManagerPanel app;
	ImageIcon background;
	JPanel content;
	TitledBorder title;
	JPanel selectionDetails;
	JLabel kitLabel;
	JComboBox availableKits;
	JLabel textLabel;
	JTextField kitsToMake;
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
		
		availableKits = new JComboBox();
		availableKits.setPreferredSize(new Dimension(100, 30));
		availableKits.setMaximumSize(new Dimension(100, 30));
		availableKits.setMinimumSize(new Dimension(100, 30));
		
		textLabel = new JLabel("Amount:");
		textLabel.setPreferredSize(new Dimension(70, 30));
		textLabel.setMaximumSize(new Dimension(70, 30));
		textLabel.setMinimumSize(new Dimension(70, 30));
		
		kitsToMake = new JTextField(10);
		kitsToMake.setPreferredSize(new Dimension(70, 30));
		kitsToMake.setMaximumSize(new Dimension(70, 30));
		kitsToMake.setMinimumSize(new Dimension(70, 30));
		
		selectionDetails.add(Box.createRigidArea(new Dimension(20, 0)));
		selectionDetails.add(kitLabel);
		selectionDetails.add(availableKits);
		selectionDetails.add(Box.createRigidArea(new Dimension(50, 0)));
		selectionDetails.add(textLabel);
		selectionDetails.add(kitsToMake);
		
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
			app.getListPanel().getJobs().create("Hello", 5);
		}
	}

}
