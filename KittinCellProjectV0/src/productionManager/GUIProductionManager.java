package productionManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIProductionManager extends JPanel{
	
	ProductionManagerApp app;
	GUIProdKAM kitGraphics;
	
	Timer timer;
	
	public GUIProductionManager(ProductionManagerApp _app){
		app = _app;
		
		kitGraphics = new GUIProdKAM(this);
		
//		timer = new Timer(10, this);
//		timer.start();
	}
	
	public void paintComponent(Graphics g){
		kitGraphics.paintComponent(g);
	}
	
//	public void actionPerformed(ActionEvent e){
//		repaint();
//	}
}
