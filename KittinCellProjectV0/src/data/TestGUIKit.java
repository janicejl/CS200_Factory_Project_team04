package data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestGUIKit extends JPanel {

Rectangle background = new Rectangle(0, 0, 500, 500);
	
	Kit kit;
	GUIKit gKit;
	
	public TestGUIKit() {		
		kit = new Kit("1", 100, 100);
		gKit = new GUIKit(kit);
		
		for (int i = 0; i < 6; i++) {
			Part p = new Part(i+"");
			kit.addPart(p);
		}
		
		setSize(500, 500);
		setVisible(true);		
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		g2.fill(background);
		
		gKit.paintKit(g2);
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}
}
