package kitAssemblyManager;

import data.Kit;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class BadKit extends JPanel implements ActionListener{

	Kit kit;
	Vector<JButton> config;
	Vector<Boolean> configClicks;
	JPanel configPanel;
	
	public BadKit(Kit _kit){
		kit = _kit;
		
		setPreferredSize(new Dimension(230, 65));
		setMaximumSize(new Dimension(230,65));
		setMinimumSize(new Dimension(230,65));
		setLayout(new GridBagLayout());
		
		configPanel = new JPanel();
		configPanel.setLayout(new GridLayout(4,2));
		configPanel.setPreferredSize(new Dimension(230,65));
		configPanel.setMaximumSize(new Dimension(230,65));
		configPanel.setMinimumSize(new Dimension(230,65));
		configPanel.setOpaque(false);
		
		config = new Vector<JButton>();
		configClicks = new Vector<Boolean>();
		
		for(int i = 0; i < 8; i++){
			config.add(new JButton(new ImageIcon(kit.getParts().get(i).getImagePath())));
			config.get(i).addActionListener(this);
			configPanel.add(config.get(i));
			
			configClicks.add(new Boolean(true));
		}
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(configPanel, c);
	}
	
	public void actionPerformed(ActionEvent e){
		for(int i = 0; i < config.size(); i++){
			if(e.getSource() == config.get(i)){
				if(configClicks.get(i) == true){
					config.get(i).setIcon(new ImageIcon("images/none.png"));
					configClicks.set(i, false);
				}
				else if(configClicks.get(i) == false){
					config.get(i).setIcon(new ImageIcon("images/good.png"));
					configClicks.set(i, true);
				}
			}
		}
	}

	public Vector<Boolean> getConfigClicks() {
		return configClicks;
	}

	public void setConfigClicks(Vector<Boolean> configClicks) {
		this.configClicks = configClicks;
	}
}
