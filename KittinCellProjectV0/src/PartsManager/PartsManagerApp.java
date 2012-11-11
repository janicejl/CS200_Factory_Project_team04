package PartsManager;

import java.awt.*;
import javax.swing.*;

public class PartsManagerApp extends JFrame{
	PartsPanel partPanel;
	
	public PartsManagerApp(){
		partPanel = new PartsPanel(this);
		add(partPanel);
		setVisible(true);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		new PartsManagerApp();
	}
}
