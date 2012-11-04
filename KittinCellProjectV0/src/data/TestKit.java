package data;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class TestKit extends JFrame {

	TestGUIKit graphics;
	
	public TestKit() {
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(new BorderLayout());
		graphics = new TestGUIKit();
		add(graphics, BorderLayout.CENTER);
		

		Scanner in = new Scanner (System.in); 
		String x, y;
		while(true) {
			System.out.println("Insert X coordinate: ");
			x = in.nextLine();
			System.out.println("Insert Y Coordinate: ");
			y = in.nextLine();
			
			graphics.kit.setDestinationX(Double.parseDouble(x));
			graphics.kit.setDestinationY(Double.parseDouble(y));
			
			System.out.println("(" + graphics.kit.getDestinationX() + ", " + graphics.kit.getDestinationY() + ")");
			
			while (graphics.kit.finishMoving() == false) {
				graphics.repaint();
			}
		}
	
	}
	
	public static void main(String[] args) {
		TestKit t = new TestKit();	
	}

}