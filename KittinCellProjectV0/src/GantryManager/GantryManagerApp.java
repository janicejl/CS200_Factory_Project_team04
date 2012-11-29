package GantryManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//Class that forms the main JFrame application of the GantryManager
public class GantryManagerApp extends JFrame implements ActionListener {
	private GUIGantryManager gui;
	GantryManagerClient client;
	GantryManager manager;
	
	javax.swing.Timer timer;
	
	public GantryManagerApp()
	{
		client = new GantryManagerClient(this);
		int j = client.connect();
		if(j == 1){
			client.getThread().start();
		}
		gui = new GUIGantryManager(1);
		this.add(gui);
		gui.setVisible(true);
		timer = new javax.swing.Timer(9,this);
		timer.start();
	}
	
	public static void main(String[] args)
	{
		GantryManagerApp app = new GantryManagerApp();
		app.setSize(345,620);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(false);
	}
	
	public void setManager(GantryManager gm)
	{
		gui.setGantryManager(gm);
	}
	
	public void setGantryManager(GantryManager gm)
	{
		manager = gm;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==timer)
		{
			client.update();
			gui.setGantryManager(manager);
			gui.repaint();
		}
	}
}