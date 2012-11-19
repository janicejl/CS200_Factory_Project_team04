package fire;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

import java.net.*;
import java.util.concurrent.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class FireApp extends JFrame implements ActionListener{
	Fire fire;
	JButton b1;			// For adjusting fire intensity
	JButton b2;			// For adjusting fire intensity
	JButton b3;			// For adjusting fire intensity
	JButton b4;			// For adjusting fire intensity
	JButton b5;			// For adjusting fire intensity
	JButton b6;			// For adjusting fire intensity
	JButton b7;			// For adjusting fire intensity
	JButton b8;			// For adjusting fire intensity
	JButton red;			// For red flames
	JButton blue;		// For blue flames
	JButton odd;			// For odd flames
	
	public FireApp(int w, int h){
		getContentPane().setLayout(new GridBagLayout());
		b1 = new JButton("1");
		b2 = new JButton("2");
		b3 = new JButton("3");
		b4 = new JButton("4");
		b5 = new JButton("5");
		b6 = new JButton("6");
		b7 = new JButton("7");
		b8 = new JButton("8");
		red = new JButton("red");
		blue = new JButton("blue");
		odd = new JButton("odd");
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b6.addActionListener(this);
		b7.addActionListener(this);
		b8.addActionListener(this);
		fire = new Fire(w,h);
		add(fire);
		/*add(b1);
		add(b2);
		add(b3);
		add(b4);
		add(b5);
		add(b6);
		add(b7);
		add(b8);
		add(red);
		add(blue);
		add(odd);*/
		red.addActionListener(this);
		blue.addActionListener(this);
		odd.addActionListener(this);
	}
	
	public static void main(String[] args){
		int w = 1200;
		int h = 600;		
		FireApp app = new FireApp(w,h);
		app.setSize(w,h);
		app.setVisible(true);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
/*		if (source == b1)		{ color1 = 0; color2 = 9; }
		else if (source == b2)	{ color1 = 0; color2 = 39; }
		else if (source == b3)	{ color1 = 0; color2 = 69; }
		else if (source == b4)	{ color1 = 0; color2 = 99; }
		else if (source == b5)	{ color1 = 25; color2 = 99; }
		else if (source == b6)	{ color1 = 40; color2 = 99; }
		else if (source == b7)	{ color1 = 55; color2 = 99; }
		else if (source == b8)	{ color1 = 70; color2 = 99; }
		else if (source == red)		init_colors_red();
		else if (source == blue)	init_colors_blue();
		else if (source == odd)		init_colors_odd();*/
		
	}
}