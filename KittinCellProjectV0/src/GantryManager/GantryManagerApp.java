package GantryManager;

import javax.swing.*;
import java.awt.event.*;

public class GantryManagerApp extends JFrame implements ActionListener
{
	GUIGantryManager gui;
	GantryManager manager;
	GantryManagerClient client;
	int speed;
	public Timer timer;
	
	public GantryManagerApp()
	{
		timer = new Timer(2,this);
		manager = new GantryManager();

		gui = new GUIGantryManager();
		gui.update(manager);
		
		client = new GantryManagerClient(this);
		client.connect();
		this.add(gui);
	}
	
	public static void main(String[] args)
	{
		GantryManagerApp app = new GantryManagerApp();
		app.setSize(345,600);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.startTimer();
		app.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		manager.update();
		gui.repaint();
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void setTimerDelay(int d)
	{
		timer.setDelay(d);
	}
	
	public void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
	
}