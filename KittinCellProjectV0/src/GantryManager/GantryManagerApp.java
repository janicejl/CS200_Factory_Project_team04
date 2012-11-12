package GantryManager;

import javax.swing.*;
import java.awt.event.*;

public class GantryManagerApp extends JFrame
{
	GUIGantryManager gui;
	GantryManager manager;
	GantryManagerClient client;
	int speed;
	public Timer timer;
	
	public GantryManagerApp()
	{
		manager = new GantryManager();

		gui = new GUIGantryManager(manager);
		gui.update();
		
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
		app.setResizable(false);
		app.start();
	}
	
	public void update()
	{
		gui.repaint();
	}
	
	public synchronized void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
	
	public synchronized GantryManager getGantryManager()
	{
		return manager;
	}
	
	public synchronized void start()
	{
		manager.run();
	}
	
}