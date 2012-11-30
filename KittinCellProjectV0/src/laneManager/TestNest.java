package laneManager;

import data.Part;
import data.PartInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class TestNest extends JFrame implements ActionListener {

	Rectangle background = new Rectangle(0, 0, 500, 500);
	
	Nest nest;
	GUINest gNest;
	
	public TestNest() {
		nest = new Nest(100, 100);
		gNest = new GUINest(nest);
		
		for (int i = 0; i < 15; i++) {
			Part p = new Part(new PartInfo("" + 3, "images/kt" + 3 + ".png"));
			nest.addPart(p);
		}
		
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		g2.fill(background);
		
		gNest.paintNest(g2,1);
	}
	
	public static void main(String[] args) {
		TestNest t = new TestNest();
		new Timer(25, t).start();
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}
}