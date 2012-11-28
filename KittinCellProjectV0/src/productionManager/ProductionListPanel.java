package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ProductionListPanel extends JPanel implements ActionListener{
	
	ProductionManagerApp app; //reference to production manager
	TitledBorder title; //border for panel
	JobListPanel jobs; //jobs list panel
	JButton start; //start production button
	ImageIcon background; //background image
	
	public ProductionListPanel(ProductionManagerApp _app){
		app = _app;
		setOpaque(true);
		//setup background and border and font
		background = new ImageIcon("images/background.png");
		title = new TitledBorder("Production List");
		title.setTitleJustification(title.CENTER);
		Font f = new Font("Cabrilli", Font.BOLD, 16);
		title.setTitleFont(f);
		setBorder(title);
		
		//setup job list panel
		jobs = new JobListPanel(app);
		jobs.setPreferredSize(new Dimension(400, 400));
		jobs.setMaximumSize(new Dimension(400, 400));
		jobs.setMinimumSize(new Dimension(400, 400));
		jobs.setAlignmentX(CENTER_ALIGNMENT);
		
		//setup start button
		start = new JButton("Start Production");
		start.setPreferredSize(new Dimension(150, 27));
		start.setMaximumSize(new Dimension(150, 27));
		start.setMinimumSize(new Dimension(150, 27));
		start.addActionListener(this);
		start.setAlignmentX(CENTER_ALIGNMENT);
		
		//add everything to layout
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(jobs, c);
		c.gridy = 2;
		add(start, c);
	}
		
	//repaint
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0,  0);
		jobs.repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		//handle button press
		if(e.getSource() == start){
			if(start.getText().equals("Start Production")){
				app.getClient().setCommandSent("Start");
				start.setText("Update Production"); //set text to update production
				start.setEnabled(false); //turn off button when started
				app.getClient().updateThread();
				app.next();
			}
			else{
//				app.getClient().setCommandSent("Update");
//				start.setText("Update Production");
//				app.getClient().updateThread();
			}
			jobs.update();
		}
	}


	public synchronized JobListPanel getJobs() {
		return jobs;
	}


	public synchronized void setJobs(JobListPanel jobs) {
		this.jobs = jobs;
	}


	public JButton getStart() {
		return start;
	}


	public void setStart(JButton start) {
		this.start = start;
	}
}
