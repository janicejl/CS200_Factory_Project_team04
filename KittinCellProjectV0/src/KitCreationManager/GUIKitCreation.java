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
import java.util.TreeMap;
import java.util.Vector;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Kit;
import data.KitInfo;
import data.Part;
import data.PartInfo;

public class GUIKitCreation implements ActionListener{
	KitCreationApp app;
	JPanel base, partSelect, displaySel,back,bob;
	JButton create, clear;
	JTextField enterName;
	JLabel name,sap;
	JComboBox partsList;
	Vector<JButton> selectionMade;
	TreeMap<Integer, PartInfo> selection;
	Vector<String> parts;
	Vector<Kit> produced;
	
	public GUIKitCreation(KitCreationApp _app){
		app = _app;
		bob=new JPanel();
		bob.setSize(300,370);
		base=new JPanel();
		base.setSize(300,370);
		back=new JPanel();
		back.setSize(300,370);
		partSelect=new JPanel();
		displaySel=new JPanel();
		
		enterName=new JTextField(10);
		name=new JLabel("Enter the kit's name:");
		sap=new JLabel("Select parts");
		
		parts=new Vector<String>();
		if(app.getPartsList().size() == 0){
			parts.add("");
		}
		for(int i = 0; i < app.getPartsList().size(); i++){
			parts.add(app.getPartsList().get(i).getName());
		}
		
		partsList=new JComboBox(parts);
		partsList.addActionListener(this);
		partsList.setOpaque(false);
		
		
		create=new JButton("Create");
		create.addActionListener(this);
		clear=new JButton("Clear");
		clear.addActionListener(this);
		selection=new TreeMap<Integer, PartInfo>();
		selectionMade=new Vector<JButton>();
		
		
		produced=new Vector<Kit>();
		partsList.setSelectedIndex(0);
		
		bob.setLayout(new GridBagLayout());
		base.setLayout(new GridBagLayout());
		back.setLayout(new FlowLayout());
		//base.setOpaque(false);
		
		base.setBackground(Color.orange);
		JLabel bg=new JLabel(new ImageIcon("images/b1.png"));
		back.add(bg);
		partSelect.setLayout(new BoxLayout(partSelect,BoxLayout.Y_AXIS));
		partSelect.setOpaque(false);
		displaySel.setLayout(new GridLayout(2,4));
		displaySel.setPreferredSize(new Dimension(230,65));
		displaySel.setMaximumSize(new Dimension(230,65));
		displaySel.setMinimumSize(new Dimension(230,65));
		displaySel.setOpaque(false);
		for (int i=0;i<8;i++){
			JButton temp=new JButton();
			temp.addActionListener(this);
			temp.setSize(60,30);
			selectionMade.add(temp);
			displaySel.add(selectionMade.get(i));
		}
		partSelect.add(name);
		partSelect.add(enterName);
		partSelect.add(sap);
		partSelect.add(partsList);
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
		
		GridBagConstraints x=new GridBagConstraints();
		x.gridx=0;
		x.gridy=0;
		x.insets=new Insets(0,0,0,0);
		bob.add(base,x);
		bob.add(back,x);
		bob.revalidate();
		bob.repaint();
		
		clear();
	}

	public void clear(){
		enterName.setText("");
		for(int i=0;i<8;i++){
			selectionMade.get(i).setIcon(new ImageIcon("images/none.png"));
			selection.put(i, null);
		}
	}
	
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getSource().equals(clear)){
			clear();
		}
		else if(ae.getSource().equals(create)){
			Vector<PartInfo> temp = new Vector<PartInfo>();
			for(int i = 0; i < selection.size(); i++){
				if(selection.get(i) == null){
					temp.add(new PartInfo(null, "images/none.png"));
				}
				else{
					temp.add(selection.get(i));
				}
			}
			KitInfo tempKit = new KitInfo(enterName.getText());
			tempKit.setParts(temp);
			app.getKitsList().add(tempKit);
			clear();
		}
		
		for(int i=0;i<selectionMade.size();i++){
			if(ae.getSource().equals(selectionMade.get(i))){
				selectionMade.get(i).setIcon(new ImageIcon(app.getPartsList().get(partsList.getSelectedIndex()).getImagePath()));
				selection.put(i, app.getPartsList().get(partsList.getSelectedIndex()));
			}
		}
		
		
		base.revalidate();
		base.repaint();
		
	}
	
	public void displayButtons(){
		
	}
	
}
