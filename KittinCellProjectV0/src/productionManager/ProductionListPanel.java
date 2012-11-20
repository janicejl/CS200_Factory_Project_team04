package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ProductionListPanel extends JPanel implements ActionListener{
	
	ProductionManagerApp app;
	TitledBorder title;
	JobListPanel jobs;
	JButton start;
	ImageIcon background;
	
	public ProductionListPanel(ProductionManagerApp _app){
		app = _app;
		setOpaque(true);
		background = new ImageIcon("images/background.png");
		title = new TitledBorder("Production List");
		title.setTitleJustification(title.CENTER);
		Font f = new Font("Cabrilli", Font.BOLD, 16);
		title.setTitleFont(f);
		setBorder(title);
		
		jobs = new JobListPanel(app);
		jobs.setPreferredSize(new Dimension(400, 400));
		jobs.setMaximumSize(new Dimension(400, 400));
		jobs.setMinimumSize(new Dimension(400, 400));
		jobs.setAlignmentX(CENTER_ALIGNMENT);
		
		start = new JButton("Start Production");
		start.setPreferredSize(new Dimension(150, 27));
		start.setMaximumSize(new Dimension(150, 27));
		start.setMinimumSize(new Dimension(150, 27));
		start.addActionListener(this);
		start.setAlignmentX(CENTER_ALIGNMENT);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(jobs, c);
		c.gridy = 2;
		add(start, c);
	}
		
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0,  0);
		jobs.repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == start){
			if(start.getText().equals("Start Production")){
				app.getClient().setCommandSent("Start");
				start.setText("Update Production");
			}
			else{
				app.getClient().setCommandSent("Update");
				start.setText("Update Production");
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
