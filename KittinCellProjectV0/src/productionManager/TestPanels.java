package productionManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TestPanels extends JFrame implements ActionListener{

	ProductionManagerPanel kp;
	ProductionManagerApp app;
	
	public TestPanels() {
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		kp = new ProductionManagerPanel(app);
		setContentPane(kp);
		
		new Timer(10, this).start();
	}
	
	public void paint(Graphics g){
		kp.repaint();
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public static void main(String[] args) {
		new TestPanels();
	}
}
