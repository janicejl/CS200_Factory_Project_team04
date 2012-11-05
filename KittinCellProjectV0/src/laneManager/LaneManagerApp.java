package laneManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import server.Lane;

import Feeder.Feeder;
import Feeder.GUIFeeder;

public class LaneManagerApp extends JFrame implements ActionListener {
	private LaneGraphics laneGraphics;
	private Vector<JButton> releaseButtons;
	private JPanel window;
	private LaneManagerClient client;
	private JButton connect;
	private Timer timer;
	
	public LaneManagerApp() {
		this.setLayout(new GridBagLayout());
		
		releaseButtons = new Vector<JButton> ();
		window = new JPanel();
		window.setLayout(new BoxLayout(window, BoxLayout.X_AXIS));
		connect = new JButton("Connect Lane");
		connect.addActionListener(this);
		window.add(connect);
		for(int i = 0; i < 8; i++) {
			releaseButtons.add(new JButton("Lane: " + (i+1)));
			releaseButtons.get(i).addActionListener(this);
			window.add(releaseButtons.get(i));
			System.out.println("OAdding button: " + (i + 1));
		}
		window.setPreferredSize(new Dimension(800, 30));
		window.setMaximumSize(new Dimension(800, 30));
		window.setMinimumSize(new Dimension(800,30));
		laneGraphics = new LaneGraphics();
		laneGraphics.setPreferredSize(new Dimension(600, 600));
		laneGraphics.setMaximumSize(new Dimension(600, 600));
		laneGraphics.setMinimumSize(new Dimension(600,600));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(window, c);
		c.gridy = 1;
		this.add(laneGraphics,c);
		this.setSize(800, 800);
    	this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		client = new LaneManagerClient(this);
		timer = new Timer(10, this);
		timer.start();
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}

	public synchronized Vector<Lane> getLanes() {
		return laneGraphics.getLanes();
	}

	public synchronized void setLanes(Vector<Lane> lanes) {
		this.laneGraphics.setLanes(lanes);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == connect){
			int i = client.connect();
			if(i == -1){
				System.exit(1);
			}
			else if(i == 1){
				client.getThread().start();
			}
		}
		for (int i = 0; i < 8; i++){
			if(e.getSource() == releaseButtons.get(i)) 
				laneGraphics.releaseItem(i);		
		}
		laneGraphics.repaint();
	}
	
}
