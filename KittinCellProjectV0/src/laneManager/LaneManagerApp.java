package laneManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LaneManagerApp extends JFrame /*implements ActionListener*/ {
	private LaneGraphics laneGraphics; //30,100,170... 
	private JPanel window;
	BufferedImage background = null;

	public LaneManagerApp() {
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
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(background,0,0,null);
	}
	
	public static void main(String[] args) {
		new LaneManagerApp();	
	}
	
}
