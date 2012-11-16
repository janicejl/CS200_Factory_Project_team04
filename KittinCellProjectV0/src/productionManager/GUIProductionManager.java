package productionManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import kitAssemblyManager.*;
import laneManager.*;
import GantryManager.*;

public class GUIProductionManager extends JPanel{
	
	ProductionManagerApp app;
	GUIKitAssemblyManager kamGraphics;
	LaneGraphics laneGraphics;
	GUIGantryManager gantryGraphics;
	BufferedImage background = null;
	
	Timer timer;
	
	public GUIProductionManager(ProductionManagerApp _app){
		app = _app;
		
		kamGraphics = new GUIKitAssemblyManager(2);
		kamGraphics.setOpaque(false);
		laneGraphics = new LaneGraphics();
		laneGraphics.setOpaque(false);
		gantryGraphics = new GUIGantryManager(2);
		gantryGraphics.setOpaque(false);
		add(kamGraphics);
		add(laneGraphics);
		add(gantryGraphics);
		 try {
            background = ImageIO.read(new File("images/background1.png"));

        } catch (IOException e) {}
		
//		timer = new Timer(10, this);
//		timer.start();
	}
	public void update(){
		kamGraphics.update();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
		g.translate(320, 0);
		laneGraphics.paintComponent(g);
		g.translate(-320, 0);
		kamGraphics.paintComponent(g);
		g.translate(830, 0);
		gantryGraphics.paintComponent(g);
		g.dispose();
		
	}
	
//	public void actionPerformed(ActionEvent e){
//		repaint();
//	}
}
