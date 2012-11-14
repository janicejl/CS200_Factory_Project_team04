package Feeder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FeederGraphicsTest extends JPanel implements ActionListener{

	public GUIFeeder guiFeeder;
	Rectangle bg;
	
	public FeederGraphicsTest(Feeder f){
		guiFeeder = new GUIFeeder(f);
		bg = new Rectangle(0, 0, 300, 300);
		new Timer(10, this).start();
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.lightGray);
		g2.fill(bg);
		guiFeeder.paintNest(g2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}
