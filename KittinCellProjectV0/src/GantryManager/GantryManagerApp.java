package GantryManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GantryManagerApp extends JFrame implements ActionListener
{
	private GUIGantryManager gui;
	
	public GantryManagerApp()
	{
		gui = new GUIGantryManager(1);
		this.add(gui);
		gui.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		GantryManagerApp app = new GantryManagerApp();
		app.setSize(345,620);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		app.setResizable(false);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		
	}
	
	public   void setManager(GantryManager gm)
	{
		gui.setGantryManager(gm);
	}
}