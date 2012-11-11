package KitCreationManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.Kit;

public class GUIKitModification implements ActionListener{
	JPanel base,partDisplay,up,back;
	//JScrollPane displayKit;
	JButton confirm;
	JComboBox kitList;
	Vector kitName;
	ArrayList<String> m;
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
		
		kitName=new Vector();
		kitName.add("");
		kitName.add("Kit XXX");
		m=new ArrayList<String>();
		kitList=new JComboBox(kitName);
		kitList.addActionListener(this);
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
		partDisplay.setPreferredSize(new Dimension(300,270));
		
		kitList.setSelectedIndex(0);
		
		/*GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		
		for(int i=0;i<8;i++){
			JButton temp=new JButton(""+i);
			temp.setPreferredSize(new Dimension (170,30));
			partList.add(temp);
			partDisplay.add(partList.get(i),c);
			c.gridy+=2;
		}*/
		
		//displayKit=new JScrollPane(partDisplay);
		//displayKit.setBackground(Color.orange);
		//displayKit.setPreferredSize(new Dimension(300,270));

		base.add(up);
		base.add(partDisplay);
		
	}

	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource().equals(kitList)){
			if(kitList.getSelectedIndex()==1){
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
			
				for(int i=0;i<8;i++){
					JButton temp=new JButton(""+i);
					temp.setPreferredSize(new Dimension (170,30));
					temp.addActionListener(this);
					partList.add(temp);
					partDisplay.add(partList.get(i),c);
					c.gridy+=2;
				}
			}
		}
		for(int i=0;i<partList.size();i++){
			if(ae.getSource().equals(partList.get(i))){
				if(remove.isSelected()){
					partDisplay.remove(partList.get(i));
					partList.remove(i);
				}
			}
		}
		partDisplay.revalidate();
		partDisplay.repaint();
		base.revalidate();
		base.repaint();
	}

}
