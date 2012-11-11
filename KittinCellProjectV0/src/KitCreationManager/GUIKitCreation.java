package KitCreationManager;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	ArrayList<JButton> selectionMade;
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
		partList=new JComboBox();
		partList.setOpaque(false);
		create=new JButton("Create");
		clear=new JButton("Clear");
		selectionMade=new ArrayList<JButton>();
		for (int i=0;i<8;i++){
			selectionMade.add(new JButton(""+i));
			displaySel.add(selectionMade.get(i));
		}
		parts=new ArrayList<Part>();
		produced=new ArrayList<Kit>();
		
		
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
		
	}

	
}
