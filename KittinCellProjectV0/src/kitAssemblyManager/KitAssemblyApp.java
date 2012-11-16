package kitAssemblyManager;

import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

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

    public KitAssemblyApp(){
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
        
        initControlPanel();
        add(controlPanel, "controls");
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

    public void initControlPanel(){
        controlPanel = new JPanel();
        buttons = new ArrayList<JButton>();
        createButton("Connect PartsRobot");
        createButton("Connect KitRobot");
        
        for(JButton b : buttons){
        	controlPanel.add(b);
        }
    }

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

    public void actionPerformed(ActionEvent ae) {
    	if(ae.getSource() == timer){
    		kamPanel.update();
            kamPanel.repaint();
	        
    	}
    	else if(ae.getSource() == showGraphics){
    		cl.show(this.getContentPane(), "graphics");
    	}
    	else if(ae.getSource() == showControls){
    		cl.show(this.getContentPane(), "controls");
    	}
    }
}
