package productionManager;

import javax.swing.*;

public class TestPanels extends JFrame {

	KitPanel kp;
	
	public TestPanels() {
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		kp = new KitPanel();
		setContentPane(kp);
	}
	
	
	public static void main(String[] args) {
		new TestPanels();
	}
}
