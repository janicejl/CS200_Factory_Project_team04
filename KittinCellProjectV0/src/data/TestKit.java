package data;


import data.Part;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class TestKit extends JFrame implements ActionListener {

	Rectangle background = new Rectangle(0, 0, 500, 500);
	
	Kit kit;
	GUIKit gKit;
	
	public TestKit() {
		kit = new Kit("1", 100, 100);
		gKit = new GUIKit(kit);
		
		for (int i = 0; i < 6; i++) {
			Part p = new Part(i+"");
			kit.addPart(p);
		}
		
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Scanner in = new Scanner (System.in); 
		String x, y;
		while(true) {
			System.out.println("Insert X coordinate: ");
			x = in.nextLine();
			System.out.println("Insert Y Coordinate: ");
			y = in.nextLine();
			
			kit.setDestinationX(Double.parseDouble(x));
			kit.setDestinationY(Double.parseDouble(y));
			
			System.out.println("(" + kit.getDestinationX() + ", " + kit.getDestinationY() + ")");
			
			while (kit.finishMoving() == false) {
				repaint();
			}
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		g2.fill(background);
		
		gKit.paintKit(g2);
	}
	
	public static void main(String[] args) {
		TestKit t = new TestKit();	
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}
}