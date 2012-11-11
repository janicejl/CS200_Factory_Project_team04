package KitCreationManager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class KitManagerApp extends JFrame{
	GUIKitModification km;
	GUIKitCreation kc;
	JTabbedPane kp;
	
	public KitManagerApp(){
		km=new GUIKitModification();
		kc=new GUIKitCreation();
		kp=new JTabbedPane();
		kp.addTab("kit creation", kc.base);
		kp.addTab("kit modification", km.base);
		this.add(kp);
	}
	public static void main(String[] args) {
		KitManagerApp app=new KitManagerApp();
		app.setSize(300,400);
		app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		
	}

}
