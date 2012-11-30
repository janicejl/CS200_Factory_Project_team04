package kitAssemblyManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import data.Kit;

public class BadKitPanel extends JPanel implements ActionListener{

	KitAssemblyApp app;
	JPanel scrollPanel;
	JScrollPane scroll;
	Vector<BadKit> badKits;
	JButton toggleDropParts;
	GridBagConstraints c;
	
	public BadKitPanel(KitAssemblyApp _app){
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		app = _app;
		badKits = new Vector<BadKit>();
		toggleDropParts = new JButton("Enable Drop Parts");
		toggleDropParts.addActionListener(this);
		
		
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
		
		c.gridx=0;
		c.gridy=0;
		add(scroll,c);
		c.gridy=1;
		c.insets = new Insets(10, 0, 0, 0);
		add(toggleDropParts,c);
		
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(toggleDropParts)){
			if(toggleDropParts.getText().equals("Enable Drop Parts")){
				toggleDropParts.setText("Disable Drop Parts");
				app.setDropParts(true);
			}
			else {
				toggleDropParts.setText("Enable Drop Parts");
				app.setDropParts(false);
			}
			app.showPanel("graphics");
		}		
	}
}
