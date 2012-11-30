package productionManager;

import java.awt.*;

import javax.swing.*;

public class TitlePanel extends JPanel{
	
	JLabel titleText;
	ImageIcon background;
	
	public TitlePanel(){
		background = new ImageIcon("images/background1.png");
		titleText = new JLabel("Production Manager");
		Font f = new Font("Verdana", Font.BOLD, 24);
		titleText.setFont(f);
		titleText.setForeground(Color.white);
		add(titleText);
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
}
