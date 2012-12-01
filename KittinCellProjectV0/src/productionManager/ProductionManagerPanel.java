package productionManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import data.KitInfo;

public class ProductionManagerPanel extends JPanel implements ActionListener {

	ProductionManagerApp app;			//a reference to the main production manager 
	JPanel base,bg;						//panels for all the different smaller panels and the background. 
	ProductionListPanel listPanel;		//Panel to show the list of jobs. 
	TitlePanel title;					//Panel to show the title of the window.
	KitPanel kitPanel;					//Panel to show the list of premade panels
	CreateJobPanel createPanel;			//Panel to create a job. 
	
	public ProductionManagerPanel(ProductionManagerApp _app){
		app = _app;
		
		//setting up the panel for all the smaller panels.
		base = new JPanel();
		base.setLayout(new GridBagLayout());
		base.setPreferredSize(new Dimension(1200,600));
		base.setMinimumSize(new Dimension(1200,600));
		base.setMaximumSize(new Dimension(1200,600));
		base.setOpaque(false);
		
		//Setting up the background of the panels. 
		bg = new JPanel();
		bg.setLayout(new FlowLayout());
		bg.setPreferredSize(new Dimension(1200,600));
		bg.setMinimumSize(new Dimension(1200,600));
		bg.setMaximumSize(new Dimension(1200,600));
		bg.add(new JLabel(new ImageIcon("images/background.png")));
		
		//Setting up the panel for the job list. 
		listPanel = new ProductionListPanel(app);
		listPanel.setPreferredSize(new Dimension(600, 500));
		listPanel.setMaximumSize(new Dimension(600, 500));
		listPanel.setMinimumSize(new Dimension(600, 500));
		
		//Setting up the panel for the title
		title = new TitlePanel();
		title.setPreferredSize(new Dimension(1200, 120));
		title.setMaximumSize(new Dimension(1200, 120));
		title.setMinimumSize(new Dimension(1200, 120));
		
		//Setting up the panel for the create job panel. 
		createPanel = new CreateJobPanel(this);
		createPanel.setPreferredSize(new Dimension(600, 250));
		createPanel.setMaximumSize(new Dimension(600, 250));
		createPanel.setMinimumSize(new Dimension(600, 250));
		
		//Setting up the panel of the list of kits. 
		kitPanel = new KitPanel(this, app.getKitsList().get(0));
		kitPanel.setPreferredSize(new Dimension(600, 250));
		kitPanel.setMaximumSize(new Dimension(600, 250));
		kitPanel.setMinimumSize(new Dimension(600, 250));
		
		//Using a grid bag layout to place all the panels at the right place. 
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		base.add(title, c);
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 2;
		c.gridwidth = 1;
		base.add(listPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		base.add(createPanel, c);
		c.gridy = 2;
		base.add(kitPanel, c);
		
		
		GridBagConstraints a = new GridBagConstraints();
		a.gridx = 0;
		a.gridy = 0;
		add(base,a);
		add(bg,a);
	}
	
	//Dynamically updates the job list. 
	public void update(){
		createPanel.update();
	}
	
	//repaints the panels. 
	public void paintComponent(Graphics g){
		listPanel.repaint();
		createPanel.repaint();
		kitPanel.repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent ae) {
	}

	public synchronized ProductionListPanel getListPanel() {
		return listPanel;
	}

	public synchronized void setListPanel(ProductionListPanel listPanel) {
		this.listPanel = listPanel;
	}

	public synchronized TitlePanel getTitle() {
		return title;
	}

	public synchronized void setTitle(TitlePanel title) {
		this.title = title;
	}

	public synchronized KitPanel getKitPanel() {
		return kitPanel;
	}

	public synchronized void setKitPanel(KitPanel kitPanel) {
		this.kitPanel = kitPanel;
	}
	public synchronized void updateKitPanel(int index){
		kitPanel.updateKit(app.getKitsList().get(index));
	}

	public synchronized CreateJobPanel getCreatePanel() {
		return createPanel;
	}

	public synchronized void setCreatePanel(CreateJobPanel createPanel) {
		this.createPanel = createPanel;
	}

	public synchronized ProductionManagerApp getApp() {
		return app;
	}

	public synchronized void setApp(ProductionManagerApp app) {
		this.app = app;
	}

}
