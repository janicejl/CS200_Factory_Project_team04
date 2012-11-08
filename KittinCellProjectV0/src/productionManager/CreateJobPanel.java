package productionManager;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class CreateJobPanel extends JPanel implements ActionListener {

	JPanel content;
	TitledBorder title;
	JPanel selectionDetails;
	JComboBox availableKits;
	JTextField kitsToMake;
	JPanel button;
	JButton addJob;
	
	public CreateJobPanel() {
		setSize(400, 200);
		setVisible(true);
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		title = BorderFactory.createTitledBorder("Create a Job");
		title.setTitleJustification(TitledBorder.CENTER);
		setBorder(title);
		
		selectionDetails = new JPanel();
		selectionDetails.setLayout(new BoxLayout(selectionDetails, BoxLayout.X_AXIS));
		
		availableKits = new JComboBox();
		
		kitsToMake = new JTextField(5);
		
		selectionDetails.add(availableKits);
		selectionDetails.add(new JLabel("                    "));
		selectionDetails.add(kitsToMake);
		
		button = new JPanel();
		button.setLayout(new BoxLayout(button, BoxLayout.X_AXIS));
		addJob = new JButton("Add to Job List");
		button.add(addJob);
		
		content.add(new JLabel("      "));
		content.add(selectionDetails);
		content.add(new JLabel("      "));
		content.add(new JLabel("      "));
		content.add(button);
		
		add(content);
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == addJob) {
			System.out.println("Add Job Button Clicked...");
		}
	}

}
