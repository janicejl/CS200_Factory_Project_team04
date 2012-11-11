package PartsManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PartsManagerApp extends JFrame implements ActionListener{
	PartsPanel partPanel;
	
	public PartsManagerApp(){
		partPanel = new PartsPanel(this);
		add(partPanel);
		setVisible(true);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new Timer(10, this).start();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
		revalidate();
	}
	
	public void paint(Graphics g){
		partPanel.repaint();
	}
	public static void main(String[] args){
		new PartsManagerApp();
	}
}
