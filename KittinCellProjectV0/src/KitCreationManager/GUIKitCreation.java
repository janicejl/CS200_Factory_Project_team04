package KitCreationManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Kit;
import data.Part;

public class GUIKitCreation implements ActionListener{
	JPanel base, partSelect, displaySel;
	JButton create, clear;
	JTextField enterName;
	JLabel name,sap;
	JComboBox partList;
	Vector partsName;
	ArrayList<JButton> selectionMade;
	ArrayList<Part> selection;
	ArrayList<Part> parts;
	ArrayList<Kit> produced;
	
	public GUIKitCreation(){
		base=new JPanel();
		base.setSize(300,370);
		partSelect=new JPanel();
		displaySel=new JPanel();
		create=new JButton("Create");
		create.addActionListener(this);
		clear=new JButton("Clear");
		clear.addActionListener(this);
		enterName=new JTextField(10);
		name=new JLabel("Enter the kit's name:");
		sap=new JLabel("Select parts");
		
		partsName=new Vector();
		partsName.add("");
		partsName.add("part XXX");
		partList=new JComboBox(partsName);
		partList.addActionListener(this);
		partList.setOpaque(false);
		
		
		create=new JButton("Create");
		create.addActionListener(this);
		clear=new JButton("Clear");
		clear.addActionListener(this);
		selection=new ArrayList<Part>();
		selectionMade=new ArrayList<JButton>();
		for (int i=0;i<8;i++){
			JButton temp=new JButton();
			temp.addActionListener(this);
			temp.setPreferredSize(new Dimension(50,30));
			selectionMade.add(temp);
			displaySel.add(selectionMade.get(i));
		}
		parts=new ArrayList<Part>();
		produced=new ArrayList<Kit>();
		partList.setSelectedIndex(0);
		
		
		base.setLayout(new GridBagLayout());
		base.setBackground(Color.orange);
		partSelect.setLayout(new BoxLayout(partSelect,BoxLayout.Y_AXIS));
		partSelect.setOpaque(false);
		displaySel.setLayout(new GridLayout(2,4));
		displaySel.setSize(250,100);
		displaySel.setOpaque(false);
		partSelect.add(name);
		partSelect.add(enterName);
		partSelect.add(sap);
		partSelect.add(partList);
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.anchor=GridBagConstraints.PAGE_START;
		c.insets=new Insets(0,0,0,0);
		base.add(partSelect,c);
		c.insets=new Insets(100,0,0,0);
		base.add(displaySel,c);
		c.insets=new Insets(180,0,0,110);
		base.add(clear,c);
		c.insets=new Insets(180,100,0,0);
		base.add(create,c);
	}

	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource().equals(clear)){
			selection.clear();
			for(int i=0;i<8;i++){
				selectionMade.get(i).setText("");
			}
			
		}
		
		if(ae.getSource().equals(partList)){
			if(partList.getSelectedIndex()==1){
				for (int i=0;i<8;i++){
					selectionMade.get(i).setText(""+i);
				}
			}
		}
		base.revalidate();
		base.repaint();
		
	}
	
	public void displayButtons(){
		
	}
	
}
