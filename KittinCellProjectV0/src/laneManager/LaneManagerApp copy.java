package laneManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LaneManagerApp extends JFrame implements ActionListener {
	private LaneGraphics laneGraphics; //30,100,170... 
	private JPanel window;
	BufferedImage background = null;
	JPanel card;
	LaneManagerPanel laneManagerPanel;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem next;
	
	public LaneManagerApp() {
		card = new JPanel();
		card.setLayout(new CardLayout());	
		window = new JPanel();
		window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));
		window.setPreferredSize(new Dimension(800, 50));
		window.setMaximumSize(new Dimension(800, 50));
		window.setMinimumSize(new Dimension(800, 50));
		laneGraphics = new LaneGraphics(1);
		laneGraphics.setPreferredSize(new Dimension(700, 600));
		laneGraphics.setMaximumSize(new Dimension(700, 600));
		laneGraphics.setMinimumSize(new Dimension(700,600));
		laneGraphics.setVisible(true);
		laneManagerPanel  = new LaneManagerPanel(this);
		laneManagerPanel.setPreferredSize(new Dimension(700, 600));
		laneManagerPanel.setMaximumSize(new Dimension(700, 600));
		laneManagerPanel.setMinimumSize(new Dimension(700, 600));
		laneManagerPanel.setVisible(false);
		menuBar = new JMenuBar();
		menu = new JMenu("Switch");
		next =  new JMenuItem("Screen");
		next.addActionListener(this);
		
		menuBar.add(menu);
		menu.add(next);
		setJMenuBar(menuBar);
		
		//GridBagConstraints c = new GridBagConstraints();
		//c.gridx = 0;
		//c.gridy = 0;
		//add(window, c);
		//c.gridy = 1;
		card.add(laneGraphics, "Graphics");
		card.add(laneManagerPanel, "Control");
		this.add(card);
		this.setSize(700, 610);
    	this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
            background = ImageIO.read(new File("images/background.png"));
        } catch (IOException e) {}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getActionCommand().equals("Screen")){
			CardLayout tempLayout = (CardLayout)card.getLayout();
			tempLayout.next(card);
		}
	}
	
}
