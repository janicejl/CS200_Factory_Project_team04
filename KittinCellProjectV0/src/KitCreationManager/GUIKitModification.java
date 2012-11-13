package KitCreationManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.Kit;
import data.PartInfo;

public class GUIKitModification implements ActionListener{
	KitCreationApp app;
	JPanel base,partDisplay,up,back;
	//JScrollPane displayKit;
	JLabel p,k;
	JButton confirm;
	JComboBox kitList,partsList;
	Vector<String> kitNames;
	Vector<String> partNames;
	Vector<JButton> partList;
	TreeMap<Integer, Boolean> tempDeleted;
	TreeMap<Integer, PartInfo> tempKit;
	JCheckBox remove;
	public GUIKitModification(KitCreationApp _app){
		app = _app;
		base=new JPanel();
		p= new JLabel("Select part type:");
		k= new JLabel("Select kit type:");
		base.setSize(300,410);
		base.setBackground(Color.orange);
		partDisplay=new JPanel();
		remove=new JCheckBox("remove");
		remove.setOpaque(false);
		confirm=new JButton("confirm");
		confirm.addActionListener(this);
		partList=new Vector<JButton>();
		tempDeleted = new TreeMap<Integer, Boolean>();
		tempKit = new TreeMap<Integer, PartInfo>();
		for(int i = 0; i < 8; i++){
			tempDeleted.put(i, false);
		}
		
		kitNames=new Vector<String>();
		if(app.getKitsList().size() == 0){
			kitNames.add("");
		}
		for(int i = 0; i < app.getKitsList().size(); i++){
			kitNames.add(app.getKitsList().get(i).getName());
		}
		
		partNames=new Vector<String>();
		if(app.getPartsList().size() == 0){
			partNames.add("");
		}
		for (int i = 0; i < app.getPartsList().size(); i++){
			partNames.add(app.getPartsList().get(i).getName());
		}
		
		kitList=new JComboBox(kitNames);
		kitList.addActionListener(this);
		partsList=new JComboBox(partNames);
		partsList.addActionListener(this);
		
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
		up.add(k,a);
		a.gridx+=4;
		a.anchor = GridBagConstraints.LINE_END;
		up.add(kitList,a);
		a.gridx=0;
		a.gridy+=4;
		a.anchor=GridBagConstraints.LINE_START;
		up.add(p,a);
		a.gridx+=4;
		a.anchor = GridBagConstraints.LINE_END;
		up.add(partsList,a);
		a.gridx=0;
		a.gridy+=4;
		a.anchor = GridBagConstraints.LINE_START;
		up.add(remove,a);
		a.gridx+=4;
		a.anchor=GridBagConstraints.LINE_END;
		up.add(confirm,a);
		
		partDisplay.setLayout(new GridBagLayout());
		partDisplay.setOpaque(false);
		partDisplay.setPreferredSize(new Dimension(300,260));
		
		kitList.setSelectedIndex(0);
		
		

		base.add(up);
		base.add(partDisplay);
		
	}

	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource().equals(kitList)){
		
			GridBagConstraints c=new GridBagConstraints();
			c.gridx=0;
			c.gridy=0;
		
			for(int i=0;i<8;i++){
				JButton temp = new JButton(new ImageIcon(app.getKitsList().get(kitList.getSelectedIndex()).getParts().get(i).getImagePath()));
				temp.setPreferredSize(new Dimension (170,30));
				temp.addActionListener(this);
				partList.add(temp);
				partDisplay.add(partList.get(i),c);
				c.gridy+=2;
				tempKit.put(i, app.getKitsList().get(kitList.getSelectedIndex()).getParts().get(i));
			}
		}
		else if(ae.getSource().equals(confirm)){
			for(int i = 0; i < tempDeleted.size(); i++){
				if(tempDeleted.get(i) == true){
					app.getKitsList().get(kitList.getSelectedIndex()).getParts().set(i, new PartInfo(null, "images/none.png"));
					tempDeleted.put(i, false);
				}
				else{
					app.getKitsList().get(kitList.getSelectedIndex()).getParts().set(i, tempKit.get(i));
				}
			}
		}
		for(int i=0;i<partList.size();i++){
			if(ae.getSource().equals(partList.get(i))){
				if(remove.isSelected()){
					if(tempDeleted.get(i) == false){
						tempDeleted.put(i, true);
						partList.get(i).setIcon(new ImageIcon("images/none.png"));
					}
					else if(tempDeleted.get(i) == true){
						
						tempDeleted.put(i, false);
						partList.get(i).setIcon(new ImageIcon(app.getKitsList().get(kitList.getSelectedIndex()).getParts().get(i).getImagePath()));
					}
				}
				else{
					partList.get(i).setIcon(new ImageIcon(app.getPartsList().get(partsList.getSelectedIndex()).getImagePath()));
					tempKit.put(i, app.getPartsList().get(partsList.getSelectedIndex()));
					tempDeleted.put(i, false);
				}
				
			}
		}
		
		partDisplay.revalidate();
		partDisplay.repaint();
		base.revalidate();
		base.repaint();
	}

}
