package productionManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProductionManagerApp extends JFrame implements ActionListener {

	ProductionManagerPanel panel;
	Timer timer;
	
	JPanel blank;
	
	public ProductionManagerApp(){
		setSize(1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		panel = new ProductionManagerPanel(this);
		panel.setPreferredSize(new Dimension(1000, 600));
		panel.setMaximumSize(new Dimension(1000, 600));
		panel.setMinimumSize(new Dimension(1000, 600));
		
		add(panel);
		
		timer = new Timer(10, this);
	}
	
	public void paint(Graphics g){
		panel.repaint();
		revalidate();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}

	public static void main(String[] args){
		ProductionManagerApp app = new ProductionManagerApp();
		app.timer.start();
	}
}
