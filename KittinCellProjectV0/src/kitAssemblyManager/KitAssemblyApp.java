package kitAssemblyManager;

import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import data.Kit;
import data.Part;
import data.PartInfo;

import server.KitAssemblyManager;
import server.KitRobot;
import server.PartsRobot;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class KitAssemblyApp extends JFrame implements ActionListener{	
    JPanel controlPanel;
    CardLayout cl;
    JMenuBar jmb;
    JMenu jm;
    JMenuItem showGraphics;
    JMenuItem showControls;
    GridBagConstraints gbc;
    ArrayList<JButton> buttons;
    String[] nests = { "Nest 1", "Nest 2", "Nest 3", "Nest 4", "Nest 5", "Nest 6", "Nest 7", "Nest 8" };
    String[] grips = { "Gripper 1", "Gripper 2", "Gripper 3", "Gripper 4"};
    
    javax.swing.Timer timer;
    GUIKitAssemblyManager kamPanel;
    BadKitPanel nonNorm;
    KitAssemblyClient kitClient;
    KitAssemblyManager kam;
    KitRobot kitRobot;
    PartsRobot partsRobot;
    boolean dropParts = false;

    public KitAssemblyApp(){
    	kitClient = new KitAssemblyClient(this);
    	int i = kitClient.connect();
		if(i == -1){
			System.exit(1);
		}
		else if(i == 1){
			kitClient.getThread().start();
		}
        setTitle("Kit Assembly Manager");
        cl = new CardLayout();
        getContentPane().setLayout(cl);
        
        jmb = new JMenuBar();
        jm = new JMenu("Options");
        showGraphics = new JMenuItem("Show Graphics");
        showControls = new JMenuItem("Show Controls");
        showGraphics.addActionListener(this);
        showControls.addActionListener(this);
        jm.add(showGraphics);
        jm.add(showControls);
        jmb.add(jm);
        setJMenuBar(jmb);
        
        kamPanel = new GUIKitAssemblyManager(1);
        add(kamPanel, "graphics");
        nonNorm = new BadKitPanel(this);
        add(nonNorm, "controls");
        
        Kit temp = new Kit();
        for(int j = 0; j < 8; j++){
        	temp.addPart(new Part(new PartInfo("hi", "images/kt1.png")));
        }
        for(int j = 0; j < 8; j ++){
        	nonNorm.create(temp);
        }
        
//        initControlPanel();
//        add(controlPanel, "controls");
        timer = new javax.swing.Timer(10, this);
        timer.start();

        pack();
        setVisible(true);
    }

    public void createButton(String s){
        JButton temp = new JButton(s);
        temp.addActionListener(this);
        temp.setActionCommand(s);
        buttons.add(temp);
    }

//    public void initControlPanel(){
//        controlPanel = new JPanel();
//        buttons = new ArrayList<JButton>();
//        createButton("Connect PartsRobot");
//        createButton("Connect KitRobot");
//        
//        for(JButton b : buttons){
//        	controlPanel.add(b);
//        }
//    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignore) {}
        KitAssemblyApp frame = new KitAssemblyApp();

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void showPanel(String s){
    	cl.show(this.getContentPane(), s);    	
    }
    
    public void setDropParts(boolean b) {
		dropParts = b;
	}
    
    public boolean getDropParts() {
		return dropParts;
	}

    public void actionPerformed(ActionEvent ae) {
    	if(ae.getSource() == timer){
    		kitClient.updateThread();
    		kamPanel.setKitAssemblyManager(kam);
    		kamPanel.setKitRobot(kitRobot);
    		kamPanel.setPartsRobot(partsRobot);
    		kamPanel.update();
            kamPanel.repaint();
	        
    	}
    	else if(ae.getSource() == showGraphics){
    		showPanel("graphics");
    	}
    	else if(ae.getSource() == showControls){
    		showPanel("controls");
    	}
    }
    
    public void setKitAssemblyManager(KitAssemblyManager kitAssemblyManager) {
		kam = kitAssemblyManager;
	}

	public void setKitRobot(KitRobot kitRobot) {
		this.kitRobot = kitRobot;
	}

	public void setPartsRobot(PartsRobot partsRobot) {
		this.partsRobot = partsRobot;
	}
	
	public KitAssemblyManager getKitAssemblyManager() {
		return kam;
	}
	
	public KitRobot getKitRobot() {
		return kitRobot;
	}
	
	public PartsRobot getPartsRobot() {
		return partsRobot;
	}
}
