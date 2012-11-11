package productionManager;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class KitPanel extends JPanel {

	TitledBorder title;
	ImageIcon background;
	ProductionManagerPanel app;
	
	JPanel content;
	
	JPanel one;
	JPanel two;
	JPanel three;
	JPanel four;
	
	JLabel part1;
	JLabel part2;
	JLabel part3;
	JLabel part4;
	JLabel part5;
	JLabel part6;
	JLabel part7;
	JLabel part8;
	
	public KitPanel(ProductionManagerPanel _app) {
		app = _app;
		background = new ImageIcon("images/background.png");
		
		title = BorderFactory.createTitledBorder("  Kit 3  ");
		title.setTitleJustification(TitledBorder.CENTER);
		Font f = new Font("Cabrilli", Font.BOLD, 16);
		title.setTitleFont(f);
		setBorder(title);
		
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		one = new JPanel();
		one.setLayout(new BoxLayout(one, BoxLayout.X_AXIS));
		two = new JPanel();
		two.setLayout(new BoxLayout(two, BoxLayout.X_AXIS));
		three = new JPanel();
		three.setLayout(new BoxLayout(three, BoxLayout.X_AXIS));
		four = new JPanel();
		four.setLayout(new BoxLayout(four, BoxLayout.X_AXIS));
		
		ImageIcon icon = new ImageIcon("images/1.png");
		part1 = new JLabel(icon);
		add(part1);
		
		icon = new ImageIcon("images/2.png");
		part2 = new JLabel(icon);
		add(part2);
		
		icon = new ImageIcon("images/3.png");
		part3 = new JLabel(icon);
		add(part3);
		
		icon = new ImageIcon("images/4.png");
		part4 = new JLabel(icon);
		add(part4);
		
		icon = new ImageIcon("images/5.png");
		part5 = new JLabel(icon);
		add(part5);
		
		icon = new ImageIcon("images/6.png");
		part6 = new JLabel(icon);
		add(part6);
		
		icon = new ImageIcon("images/7.png");
		part7 = new JLabel(icon);
		add(part7);
		
		icon = new ImageIcon("images/8.png");
		part8 = new JLabel(icon);
		add(part8);
		
		
		one.add(part1);
		one.add(new JLabel("    "));
		one.add(part2);
		two.add(part3);
		two.add(new JLabel("    "));
		two.add(part4);
		three.add(part5);
		three.add(new JLabel("    "));
		three.add(part6);
		four.add(part7);
		four.add(new JLabel("    "));
		four.add(part8);
		
		content.add(new JLabel("    "));
		content.add(new JLabel("    "));
		content.add(one);
		content.add(new JLabel("    "));
		content.add(two);
		content.add(new JLabel("    "));
		content.add(three);
		content.add(new JLabel("    "));
		content.add(four);
		
		add(content);
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
}
