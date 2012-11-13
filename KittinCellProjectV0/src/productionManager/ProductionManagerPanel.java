package productionManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ProductionManagerPanel extends JPanel implements ActionListener {

	ProductionManagerApp app;
	ProductionListPanel listPanel;
	TitlePanel title;
	KitPanel kitPanel;
	CreateJobPanel createPanel;
	
	public ProductionManagerPanel(ProductionManagerApp _app){
		app = _app;
		
		listPanel = new ProductionListPanel(app);
		listPanel.setPreferredSize(new Dimension(500, 500));
		listPanel.setMaximumSize(new Dimension(500, 500));
		listPanel.setMinimumSize(new Dimension(500, 500));
		
		title = new TitlePanel();
		title.setPreferredSize(new Dimension(1000, 60));
		title.setMaximumSize(new Dimension(1000, 60));
		title.setMinimumSize(new Dimension(1000, 60));
		
		createPanel = new CreateJobPanel(this);
		createPanel.setPreferredSize(new Dimension(500, 250));
		createPanel.setMaximumSize(new Dimension(500, 250));
		createPanel.setMinimumSize(new Dimension(500, 250));
		
		kitPanel = new KitPanel(this);
		kitPanel.setPreferredSize(new Dimension(500, 250));
		kitPanel.setMaximumSize(new Dimension(500, 250));
		kitPanel.setMinimumSize(new Dimension(500, 250));
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		add(title, c);
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 2;
		c.gridwidth = 1;
		add(listPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		add(createPanel, c);
		c.gridy = 2;
		add(kitPanel, c);
	}
	
	public void update(){
		createPanel.update();
	}
	public void paintComponent(Graphics g){
		listPanel.repaint();
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
