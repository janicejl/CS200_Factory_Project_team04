package laneManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

import server.Lane;

import Feeder.Feeder;
import Feeder.GUIFeeder;

public class LaneManagerApp extends JFrame implements ActionListener {
	private LaneGraphics laneGraphics; //30,100,170... 
	private JPanel window;
	BufferedImage background = null;
	private LaneManagerClient client;
	
	private Vector<Lane> lanes = new Vector<Lane> ();
	private Vector<Feeder> feeders = new Vector<Feeder>();
	private Vector<Nest> nests = new Vector<Nest>();
	
	private javax.swing.Timer timer;

	public LaneManagerApp() {
		client = new LaneManagerClient(this);
    	
		int j = client.connect();
		if(j == -1){
			System.exit(1);
		}
		else if(j == 1){
			client.getThread().start();
		}
		this.setLayout(new GridBagLayout());	
		window = new JPanel();
		window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
		window.setPreferredSize(new Dimension(800, 50));
		window.setMaximumSize(new Dimension(800, 50));
		window.setMinimumSize(new Dimension(800, 50));
		laneGraphics = new LaneGraphics(1);
		laneGraphics.setPreferredSize(new Dimension(700, 600));
		laneGraphics.setMaximumSize(new Dimension(700, 600));
		laneGraphics.setMinimumSize(new Dimension(700,600));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(window, c);
		c.gridy = 1;
		this.add(laneGraphics,c);
		this.setSize(700, 610);
    	this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
            background = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {}
		
		timer = new javax.swing.Timer(10, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}
	
	public synchronized void setLanes(Vector<Lane> _lanes) {
		this.lanes = _lanes;
	}
	
	public synchronized void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public synchronized void setNests(Vector<Nest> nests) {
		this.nests = nests;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == timer){
			client.updateThread();
			laneGraphics.setFeeders(feeders);
			laneGraphics.setNests(nests);
			laneGraphics.setLanes(lanes);
			laneGraphics.update();
			laneGraphics.repaint();
		}
	}	
}
