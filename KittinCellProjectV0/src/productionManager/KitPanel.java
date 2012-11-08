package productionManager;

import javax.swing.*;
import javax.swing.border.*;

public class KitPanel extends JPanel {

	TitledBorder title;
	JLabel part1;
	JLabel part2;
	JLabel part3;
	JLabel part4;
	JLabel part5;
	JLabel part6;
	JLabel part7;
	JLabel part8;
	
	public KitPanel() {
		setSize(400, 300);
		setVisible(true);
		
		title = BorderFactory.createTitledBorder("Kit 3");
		title.setTitleJustification(TitledBorder.CENTER);
		setBorder(title);
		
		ImageIcon icon = new ImageIcon("images/1.png");
		part1 = new JLabel(icon);
		add(part1);
	}
	
}
