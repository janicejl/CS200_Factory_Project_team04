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
	
	JButton part1;
	JButton part2;
	JButton part3;
	JButton part4;
	JButton part5;
	JButton part6;
	JButton part7;
	JButton part8;
	
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
		content.setOpaque(false);
		
		one = new JPanel();
		one.setLayout(new BoxLayout(one, BoxLayout.X_AXIS));
		one.setOpaque(false);
		two = new JPanel();
		two.setLayout(new BoxLayout(two, BoxLayout.X_AXIS));
		two.setOpaque(false);
		three = new JPanel();
		three.setLayout(new BoxLayout(three, BoxLayout.X_AXIS));
		three.setOpaque(false);
		four = new JPanel();
		four.setLayout(new BoxLayout(four, BoxLayout.X_AXIS));
		four.setOpaque(false);
		
		ImageIcon icon = new ImageIcon("images/kt1.png");
		part1 = new JButton(icon);
		add(part1);
		
		icon = new ImageIcon("images/kt2.png");
		part2 = new JButton(icon);
		add(part2);
		
		icon = new ImageIcon("images/kt3.png");
		part3 = new JButton(icon);
		add(part3);
		
		icon = new ImageIcon("images/kt4.png");
		part4 = new JButton(icon);
		add(part4);
		
		icon = new ImageIcon("images/kt5.png");
		part5 = new JButton(icon);
		add(part5);
		
		icon = new ImageIcon("images/kt6.png");
		part6 = new JButton(icon);
		add(part6);
		
		icon = new ImageIcon("images/kt7.png");
		part7 = new JButton(icon);
		add(part7);
		
		icon = new ImageIcon("images/kt8.png");
		part8 = new JButton(icon);
		add(part8);
		
		
		one.add(part1);
		one.add(Box.createRigidArea(new Dimension(30, 0)));
		one.add(part2);
		two.add(part3);
		two.add(Box.createRigidArea(new Dimension(30, 0)));
		two.add(part4);
		three.add(part5);
		three.add(Box.createRigidArea(new Dimension(30, 0)));
		three.add(part6);
		four.add(part7);
		four.add(Box.createRigidArea(new Dimension(30, 0)));
		four.add(part8);
		
		content.add(Box.createRigidArea(new Dimension(0, 20)));
		content.add(one);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(two);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(three);
		content.add(Box.createRigidArea(new Dimension(0, 5)));
		content.add(four);
		
		add(content);
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		revalidate();
	}
	
}
