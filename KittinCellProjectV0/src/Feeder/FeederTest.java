package Feeder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class FeederTest extends JFrame implements ActionListener{

	Feeder feeder;
	FeederGraphicsTest feederGraphicsTest;
	JPanel buttons = new JPanel();
	JPanel main = new JPanel();
	JButton up, down;
	
	public FeederTest(){
		feeder = new Feeder(50, 50);
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		feederGraphicsTest = new FeederGraphicsTest(feeder);
		main.add(feederGraphicsTest);
		up = new JButton("UP");
		up.addActionListener(this);
		down = new JButton("DOWN");
		down.addActionListener(this);
		buttons = new JPanel();
		buttons.add(up);
		buttons.add(down);
		main.add(buttons);
		add(main);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		feederGraphicsTest.guiFeeder.setFeeder(feeder);
		if (ae.getSource() == up){
			feeder.divertLane(true);
		}
		else if (ae.getSource() == down){
			feeder.divertLane(false);
		}
		System.out.println(feeder.getLane());
	}
	
	public static void main(String[] args){
		FeederTest fTest = new FeederTest();
		fTest.setSize(500,500);
		fTest.setVisible(true);
		fTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
