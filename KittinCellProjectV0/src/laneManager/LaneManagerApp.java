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
	private LaneGraphics laneGraphics; //30,100,170... 
	private Vector<JButton> releaseButtons;
	private JLabel releaseLabel;
	private JPanel releaseSection;
	private Vector<JButton> feedButtons;
	private JLabel feedLabel;
	private JPanel feedSection;
	private Vector<JButton> removeButtons;
	private JLabel removeLabel;
	private JPanel removeSection;
	private JPanel window;
	
	//private JButton connect;
	
	
	public LaneManagerApp() {
		this.setLayout(new GridBagLayout());
		
//		releaseButtons = new Vector<JButton> ();
//		feedButtons = new Vector<JButton> ();
//		removeButtons = new Vector<JButton> ();
//		releaseSection = new JPanel();
//		releaseSection.setLayout(new BoxLayout(releaseSection, BoxLayout.X_AXIS));
//		feedSection = new JPanel();
//		feedSection.setLayout(new BoxLayout(feedSection, BoxLayout.X_AXIS));
//		removeSection = new JPanel();
//		removeSection.setLayout(new BoxLayout(removeSection, BoxLayout.X_AXIS));
//		
//		releaseLabel = new JLabel("Release");
//		feedLabel = new JLabel("Feed");
//		removeLabel = new JLabel("Remove");
		
		window = new JPanel();
		window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
		//connect = new JButton("Connect Lane");
		//connect.addActionListener(this);
		//window.add(connect);
//		feedSection.add(feedLabel);
//		for(int i = 0; i < 8; i++) {
//			feedButtons.add(new JButton("Lane: " + (i+1)));
//			feedButtons.get(i).addActionListener(this);
//			feedSection.add(feedButtons.get(i));
//		}
//		releaseSection.add(releaseLabel);
//		for(int i = 0; i < 8; i++) {
//			releaseButtons.add(new JButton("Lane: " + (i+1)));
//			releaseButtons.get(i).addActionListener(this);
//			releaseSection.add(releaseButtons.get(i));
//			
//		}
//		removeSection.add(removeLabel);
//		for(int i = 0; i < 8; i++) {
//			removeButtons.add(new JButton("Lane: " + (i+1)));
//			removeButtons.get(i).addActionListener(this);
//			removeSection.add(removeButtons.get(i));
//		}
//		window.add(feedSection);
//		window.add(releaseSection);
//		window.add(removeSection);
		window.setPreferredSize(new Dimension(800, 50));
		window.setMaximumSize(new Dimension(800, 50));
		window.setMinimumSize(new Dimension(800, 50));
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
		this.setSize(800, 750);
    	this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}

	/*public synchronized Vector<Lane> getLanes() {
		return laneGraphics.getLanes();
	}

	public synchronized void setLanes(Vector<Lane> lanes) {
		this.laneGraphics.setLanes(lanes);
	}

	
	
	public synchronized LaneGraphics getLaneGraphics() {
		return laneGraphics;
	}

	public synchronized void setLaneGraphics(LaneGraphics laneGraphics) {
		this.laneGraphics = laneGraphics;
	}*/

	public void actionPerformed(ActionEvent e) {

	
	}
	
}
