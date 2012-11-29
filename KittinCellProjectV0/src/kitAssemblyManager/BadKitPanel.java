package kitAssemblyManager;
import java.awt.*;
import java.util.*;

import javax.swing.*;

import data.Kit;

public class BadKitPanel extends JPanel{

	KitAssemblyApp app;
	JPanel scrollPanel;
	JScrollPane scroll;
	Vector<BadKit> badKits;
	
	public BadKitPanel(KitAssemblyApp _app){
		
		app = _app;
		badKits = new Vector<BadKit>();
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		scrollPanel.setPreferredSize(new Dimension(250, 200));
		scrollPanel.setMaximumSize(new Dimension(250, 200));
		scrollPanel.setMinimumSize(new Dimension(250, 200));
		
		scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(280, 230));
		scroll.setMaximumSize(new Dimension(280, 230));
		scroll.setMinimumSize(new Dimension(280, 230));
		
		scroll.getViewport().add(scrollPanel);
		
		add(scroll);
		
	}
	
	public void create(Kit kit){
		scrollPanel.removeAll();
		badKits.add(new BadKit(kit));
		int size = badKits.size();
		for(int i = 0; i < size; i++){
			scrollPanel.add(badKits.get(i));
			scrollPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		size = size*65;
		scrollPanel.setPreferredSize(new Dimension(250, size));
		scrollPanel.setMaximumSize(new Dimension(250, size));
		scrollPanel.setMinimumSize(new Dimension(250, size));
	}
}
