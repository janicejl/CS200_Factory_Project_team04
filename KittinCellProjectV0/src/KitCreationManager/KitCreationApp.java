package KitCreationManager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class KitCreationApp extends JFrame{
	GUIKitModification km;
	GUIKitCreation kc;
	JTabbedPane kp;
	
	public KitCreationApp(){
		km=new GUIKitModification();
		kc=new GUIKitCreation();
		kp=new JTabbedPane();
		kp.addTab("kit creation", kc.bob);
		kp.addTab("kit modification", km.base);
		this.add(kp);
	}
	public static void main(String[] args) {
		KitCreationApp app=new KitCreationApp();
		app.setSize(300,400);
		app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		
	}

}
