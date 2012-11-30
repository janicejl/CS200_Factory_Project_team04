package kitAssemblyManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import data.Kit;

public class BadKitPanel extends JPanel implements ActionListener{

	KitAssemblyApp app;
	JLabel titleLabel;
	JPanel scrollPanel;
	JScrollPane scroll;
	ArrayList<BadKit> badKits;
	ArrayList<JLabel> labels;
	ArrayList<JButton> sendButtons;
	JButton toggleDropParts;
	GridBagConstraints c;
	
	public BadKitPanel(KitAssemblyApp _app){
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		app = _app;
		badKits = new ArrayList<BadKit>();
		labels = new ArrayList<JLabel>();
		sendButtons = new ArrayList<JButton>();
		toggleDropParts = new JButton("Enable Drop Parts");
		toggleDropParts.addActionListener(this);
		
		titleLabel = new JLabel("Bad Kits Interface");
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		titleLabel.setPreferredSize(new Dimension(300, 25));
		titleLabel.setMaximumSize(new Dimension(300, 25));
		titleLabel.setMinimumSize(new Dimension(300, 25));
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		scrollPanel.setPreferredSize(new Dimension(300, 200));
		scrollPanel.setMaximumSize(new Dimension(300, 200));
		scrollPanel.setMinimumSize(new Dimension(300, 200));
		
		
		scroll = new JScrollPane();
		scroll.setPreferredSize(new Dimension(330, 500));
		scroll.setMaximumSize(new Dimension(330, 500));
		scroll.setMinimumSize(new Dimension(330, 500));
		scroll.setAlignmentX(CENTER_ALIGNMENT);
		
		scroll.getViewport().add(scrollPanel);
		
		c.gridx=0;
		c.gridy=0;
		add(titleLabel,c);
		c.gridy=1;
		add(scroll,c);
//		c.insets = new Insets(10, 0, 0, 0);
//		add(toggleDropParts,c);
		
	}
	
	public void create(Kit kit){
		badKits.add(new BadKit(kit));
		labels.add(new JLabel((labels.size()+1) + ") " + kit.getID()));
		sendButtons.add(new JButton("Send"));
		update();
	}

	public void update(){
		scrollPanel.removeAll();
		int size = badKits.size();
		for(int i = 0; i < size; i++){
			scrollPanel.add(labels.get(i));
			labels.get(i).setFont(new Font("Cabrilli", Font.BOLD, 14));
			labels.get(i).setPreferredSize(new Dimension(230, 25));
			labels.get(i).setMaximumSize(new Dimension(230, 25));
			labels.get(i).setMinimumSize(new Dimension(230, 25));
			labels.get(i).setAlignmentX(CENTER_ALIGNMENT);
			scrollPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			
			scrollPanel.add(badKits.get(i));
			badKits.get(i).setAlignmentX(CENTER_ALIGNMENT);
			scrollPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			
			sendButtons.get(i).setPreferredSize(new Dimension(230, 25));
			sendButtons.get(i).setMaximumSize(new Dimension(230, 25));
			sendButtons.get(i).setMinimumSize(new Dimension(230, 25));
			sendButtons.get(i).setAlignmentX(CENTER_ALIGNMENT);
			scrollPanel.add(sendButtons.get(i));
			scrollPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		size = size*155;
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
