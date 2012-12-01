package kitAssemblyManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import data.Kit;
import data.Part;
import data.PartInfo;

public class BadKitPanel extends JPanel implements ActionListener{

	KitAssemblyApp app;
	JTabbedPane tabs;
	
	JPanel badModification;
	JLabel titleLabel;
	JPanel scrollPanel;
	JScrollPane scroll;
	ArrayList<BadKit> badKits;
	ArrayList<JLabel> labels;
	ArrayList<JButton> sendButtons;
	JLabel sent;
	
	JPanel nonNorms;
	JButton toggleDropParts;
	GridBagConstraints c;
	
	public BadKitPanel(KitAssemblyApp _app){
		setLayout(new GridBagLayout());
		tabs = new JTabbedPane();
		tabs.setPreferredSize(new Dimension(500, 600));
		tabs.setMaximumSize(new Dimension(500, 600));
		tabs.setMinimumSize(new Dimension(500, 600));
		
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
		
		badModification = new JPanel();
		badModification.setLayout(new GridBagLayout());
		badModification.setPreferredSize(new Dimension(500, 600));
		badModification.setMaximumSize(new Dimension(500, 600));
		badModification.setMinimumSize(new Dimension(500, 600));
		
		c.gridx=0;
		c.gridy=0;
		badModification.add(titleLabel,c);
		c.gridy=1;
		badModification.add(scroll,c);
		
		c.gridy = 0;
		
		nonNorms = new JPanel();
		nonNorms.setLayout(new GridBagLayout());
		
		c.insets = new Insets(10, 0, 0, 0);
		nonNorms.add(toggleDropParts,c);
		
		tabs.add(nonNorms, "Non-Normatives");
		tabs.add(badModification, "Bad Kits Interface");
		
		add(tabs,c);
		
		Kit temp = new Kit();
		temp.setID("Bad Kit to Make");
        for(int j = 0; j < 8; j++){
        	temp.addPart(new Part(new PartInfo(null, "images/good.png")));
        }
        create(temp);
        
        sent = new JLabel("Non-Norm Sent");
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
		}
		
		if(!sendButtons.isEmpty()){
			if(e.getSource() == sendButtons.get(0)){
				for(int i = 0; i < 8; i++){
					if(badKits.get(0).getConfigClicks().get(i) == false){
						app.getKitDrops().set(i, true);
					}
					else{
						app.getKitDrops().set(i, false);
					}
				}
				app.setKitDropUpdate(true);
			}
		}
	}
}
