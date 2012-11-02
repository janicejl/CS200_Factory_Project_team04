package data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class TestParts extends JFrame implements ActionListener {

	Rectangle background = new Rectangle(0, 0, 500, 500);
	
	Part part;
	GUIPart gPart;
	
	public TestParts() {
		part = new Part("1");
		gPart = new GUIPart(part);
		
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
			
			part.setX(Double.parseDouble(x));
			part.setY(Double.parseDouble(y));
			
			System.out.println("(" + part.getX() + ", " + part.getY() + ")");
			repaint();
		}

	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		g2.fill(background);
		
		gPart.paintPart(g2);
	}
	
	public static void main(String[] args) {
		TestParts t = new TestParts();
		//new Timer(25, t).start();
	}

	public void actionPerformed(ActionEvent ae) {
		repaint();
	}
}