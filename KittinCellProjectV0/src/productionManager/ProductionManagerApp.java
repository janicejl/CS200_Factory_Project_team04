package productionManager;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import data.Job;

public class ProductionManagerApp extends JFrame implements ActionListener {

	ProductionManagerPanel panel;
	javax.swing.Timer timer;
	
	ArrayList<Job> jobs;
	
	public ProductionManagerApp(){
		setSize(1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		jobs = new ArrayList<Job>();
		
		panel = new ProductionManagerPanel(this);
		panel.setPreferredSize(new Dimension(1000, 600));
		panel.setMaximumSize(new Dimension(1000, 600));
		panel.setMinimumSize(new Dimension(1000, 600));
		
		add(panel);
		
		timer = new javax.swing.Timer(10, this);
	}
	
	public void paint(Graphics g){
		try{
			panel.repaint();
		} catch(Exception e){
			
		}
		revalidate();
	}
	
	public void revalidate(){
		panel.revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}

	public static void main(String[] args){
		ProductionManagerApp app = new ProductionManagerApp();
		app.timer.start();
	}
}
