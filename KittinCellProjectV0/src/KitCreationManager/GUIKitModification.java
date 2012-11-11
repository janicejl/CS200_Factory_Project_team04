package KitCreationManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.Kit;

public class GUIKitModification{
	JPanel base,partDisplay,up;
	JScrollPane displayKit;
	JButton confirm;
	JComboBox kitList;
	ArrayList<JButton> partList;
	JCheckBox remove;
	public GUIKitModification(){
		base=new JPanel();
		base.setSize(300,370);
		base.setBackground(Color.orange);
		partDisplay=new JPanel();
		remove=new JCheckBox("remove");
		remove.setOpaque(false);
		confirm=new JButton("confirm");
		partList=new ArrayList<JButton>();
		kitList=new JComboBox();
		partDisplay.setOpaque(false);
		up=new JPanel();
		
		base.setLayout(new BoxLayout(base,BoxLayout.Y_AXIS));
		
		
		up.setLayout(new GridBagLayout());
		up.setOpaque(false);
		GridBagConstraints a=new GridBagConstraints();
		a.gridx=0;
		a.gridy=0;
		
		kitList.setPreferredSize(new Dimension(180,30));
		kitList.setOpaque(false);
		a.anchor=GridBagConstraints.LINE_START;
		up.add(kitList,a);
		a.gridy+=2;
		a.anchor=GridBagConstraints.LINE_START;
		up.add(remove,a);
		a.gridx+=4;
		a.anchor=GridBagConstraints.LINE_END;
		up.add(confirm,a);
		
		partDisplay.setLayout(new GridBagLayout());
		partDisplay.setOpaque(false);
		
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		
		for(int i=0;i<8;i++){
			JButton temp=new JButton(""+i);
			temp.setPreferredSize(new Dimension (170,30));
			partList.add(temp);
			partDisplay.add(partList.get(i),c);
			c.gridy+=2;
		}
		
		displayKit=new JScrollPane(partDisplay);
		displayKit.setBackground(Color.orange);
		displayKit.setPreferredSize(new Dimension(300,270));

		base.add(up);
		base.add(displayKit);
		
	}

}
