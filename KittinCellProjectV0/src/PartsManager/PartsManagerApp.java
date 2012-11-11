package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import data.PartInfo;

public class PartsManagerApp extends JFrame implements ActionListener{
	PartsPanel partPanel;
	ArrayList<PartInfo> partsList;
	
	public PartsManagerApp(){
		partPanel = new PartsPanel(this);
		add(partPanel);
		setVisible(true);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		partsList = new ArrayList<PartInfo>();
		
		new Timer(10, this).start();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
		//revalidate();
	}
	
	public void paint(Graphics g){
		partPanel.repaint();
	}
	public static void main(String[] args){
		new PartsManagerApp();
	}

	public synchronized PartsPanel getPartPanel() {
		return partPanel;
	}

	public synchronized void setPartPanel(PartsPanel partPanel) {
		this.partPanel = partPanel;
	}

	public synchronized ArrayList<PartInfo> getPartsList() {
		return partsList;
	}

	public synchronized void setPartsList(ArrayList<PartInfo> partsList) {
		this.partsList = partsList;
	}
}
