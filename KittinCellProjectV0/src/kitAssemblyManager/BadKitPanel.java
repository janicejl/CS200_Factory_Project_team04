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
	JLabel titleLabel;
	BadKit badKit;
	JLabel label;
	JButton sendButton;
	JLabel sent;
	boolean labelOn = false;
	JButton toggleDropParts;
	GridBagConstraints c;
	
	public BadKitPanel(KitAssemblyApp _app){
		app = _app;
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		label = new JLabel();
		sendButton = new JButton("Send Kit Non-Norm");
		sendButton.addActionListener(this);
		toggleDropParts = new JButton("Enable Parts Robot Drop Parts");
		toggleDropParts.addActionListener(this);
		
		titleLabel = new JLabel("Kit Assembly Non-Normatives");
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		titleLabel.setPreferredSize(new Dimension(300, 25));
		titleLabel.setMaximumSize(new Dimension(300, 25));
		titleLabel.setMinimumSize(new Dimension(300, 25));
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		Kit temp = new Kit();
		temp.setID("Bad Kit to Make");
        for(int j = 0; j < 8; j++){
        	temp.addPart(new Part(new PartInfo(null, "images/good.png")));
        }
        badKit = new BadKit(temp);
		label = new JLabel(temp.getID());
		
		sent = new JLabel("");
        sent.setPreferredSize(new Dimension(230, 25));
        sent.setMaximumSize(new Dimension(230, 25));
        sent.setMinimumSize(new Dimension(230, 25));
        sent.setAlignmentX(CENTER_ALIGNMENT);
		
		c.gridx=0;
		c.gridy=0;
		add(titleLabel,c);
		c.gridy=1;
		
		add(badKit,c);
		c.gridy=2;
		c.insets = new Insets(10, 0, 0, 0);
		add(sendButton,c);
		c.gridy=3;
		add(toggleDropParts,c);
		c.gridy=4;
		add(sent,c);
	}
	
	public void resetSentMessage(){
		sent.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(toggleDropParts)){
			if(toggleDropParts.getText().equals("Enable Parts Robot Drop Parts")){
				toggleDropParts.setText("Disable Parts Robot Drop Parts");
				app.setDropParts(true);
			}
			else {
				toggleDropParts.setText("Enable Parts Robot Drop Parts");
				app.setDropParts(false);
			}
		}
		
		if(e.getSource() == sendButton){
			for(int i = 1; i < 9; i++){
				if(badKit.getConfigClicks().get(i-1) == false){
					app.getKitDrops().set(i, true);
				}
				else{
					app.getKitDrops().set(i, false);
				}
			}
			app.getKitDrops().set(0, true);
			sent.setText("Non-Norm Sent");
		}		
	}
}
