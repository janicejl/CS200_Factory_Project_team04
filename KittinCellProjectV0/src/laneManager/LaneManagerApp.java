package laneManager;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

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
	LaneManagerPanel laneManagerPanel;
	private Vector<Lane> lanes = new Vector<Lane> ();
	private Vector<Feeder> feeders = new Vector<Feeder>();
	private CopyOnWriteArrayList<Nest> nests = new CopyOnWriteArrayList<Nest>();
	private Vector<Boolean> onJammeds;
	private Vector<Boolean> overFlow;
	private boolean feederSlow = false;
	
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem next;
	
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
		window.setLayout(new CardLayout());
		window.setPreferredSize(new Dimension(700, 600));
		window.setMaximumSize(new Dimension(700, 600));
		window.setMinimumSize(new Dimension(700, 600));
		laneGraphics = new LaneGraphics(1);
		laneGraphics.setPreferredSize(new Dimension(700, 600));
		laneGraphics.setMaximumSize(new Dimension(700, 600));
		laneGraphics.setMinimumSize(new Dimension(700,600));
		laneManagerPanel  = new LaneManagerPanel(this);
		laneManagerPanel.setPreferredSize(new Dimension(700, 600));
		laneManagerPanel.setMaximumSize(new Dimension(700, 600));
		laneManagerPanel.setMinimumSize(new Dimension(700, 600));
		laneManagerPanel.setVisible(false);
		menuBar = new JMenuBar();
		menu = new JMenu("Switch");
		next =  new JMenuItem("Screen");
		next.addActionListener(this);
		
		onJammeds = new Vector<Boolean>();
		for(int i=0;i<8;i++){
			onJammeds.add(new Boolean(false));
		}
		overFlow = new Vector<Boolean>();
		for(int i=0;i<8;i++){
			overFlow.add(new Boolean(false));
		}
		menuBar.add(menu);
		menu.add(next);
		setJMenuBar(menuBar);
		window.add(laneGraphics,"");
		window.add(laneManagerPanel, "");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(window, c);
		c.gridy = 1;
//		this.add(laneGraphics,c);
		this.setSize(720, 650);
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
		laneManagerPanel.repaint();
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}
	
	public void next(){
		CardLayout tempLayout = (CardLayout)window.getLayout();
		tempLayout.next(window);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == next){
			next();
		}
		if(e.getSource() == timer){
			client.updateThread();
			laneGraphics.setFeeders(feeders);
			laneGraphics.setNests(nests);
			laneGraphics.setLanes(lanes);
			laneGraphics.update();
			laneGraphics.repaint();
		}
	}

	public Vector<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(Vector<Lane> lanes) {
		this.lanes = lanes;
	}

	public Vector<Feeder> getFeeders() {
		return feeders;
	}

	public void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}

	public CopyOnWriteArrayList<Nest> getNests() {
		return nests;
	}

	public void setNests(CopyOnWriteArrayList<Nest> nests) {
		this.nests = nests;
	}
	
	public synchronized Vector<Boolean> getOnJammeds(){
		return onJammeds;
	}

	public Vector<Boolean> getOverFlow() {
		return overFlow;
	}

	public void setOverFlow(Vector<Boolean> overFlow) {
		this.overFlow = overFlow;
	}

	public void setOnJammeds(Vector<Boolean> onJammeds) {
		this.onJammeds = onJammeds;
	}

	public boolean isFeederSlow() {
		return feederSlow;
	}

	public void setFeederSlow(boolean feederSlow) {
		this.feederSlow = feederSlow;
	}
}
