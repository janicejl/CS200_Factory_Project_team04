package kitAssemblyManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import KitCreationManager.KitCreationApp;
import data.PartInfo;

public class GUINonNorm extends JFrame implements ActionListener {
	
		JPanel base,partDisplay,up,back;
		//JScrollPane displayKit;
		JLabel k;
		//JTextField changeName;
		JButton confirm;
		JButton delete;
		JComboBox kitList;
		Vector<String> kitNames;
		//Vector<String> partNames;
		Vector<JButton> partList;
		TreeMap<Integer, Boolean> tempDeleted;
		TreeMap<Integer, PartInfo> tempKit;
		JCheckBox remove;
	public GUINonNorm() {
		base=new JPanel();
		k= new JLabel("Select kit type:");
		base.setSize(300,410);
		base.setBackground(Color.orange);
		partDisplay=new JPanel();
		remove=new JCheckBox("remove");
		remove.setOpaque(false);
		
		confirm=new JButton("Confirm");
		confirm.setPreferredSize(new Dimension(90, 25));
		confirm.setMaximumSize(new Dimension(90, 25));
		confirm.setMinimumSize(new Dimension(90, 25));
		confirm.addActionListener(this);
			
		delete = new JButton("Delete Kit");
		delete.setPreferredSize(new Dimension(90, 25));
		delete.setMaximumSize(new Dimension(90, 25));
		delete.setMinimumSize(new Dimension(90, 25));
		delete.addActionListener(this);
			
		partList=new Vector<JButton>();
		
		tempDeleted = new TreeMap<Integer, Boolean>();
		tempKit = new TreeMap<Integer, PartInfo>();
		for(int i = 0; i < 8; i++){
			tempDeleted.put(i, false);
		}
			
		kitNames=new Vector<String>();
		/*if(app.getKitsList().size() == 0){
			kitNames.add("");
		}
		for(int i = 0; i < app.getKitsList().size(); i++){
			kitNames.add(app.getKitsList().get(i).getName());
		}
		*/	
		/*if(app.getPartsList().size() == 0){
			partNames.add("");
		}
		else{
			for (int i = 0; i < app.getPartsList().size(); i++){
				partNames.add(app.getPartsList().get(i).getName());
			}
		}*/
			
		kitList=new JComboBox(kitNames);
		kitList.addActionListener(this);
		kitList.setPreferredSize(new Dimension(140,25));
		kitList.setMaximumSize(new Dimension(140,25));
		kitList.setMinimumSize(new Dimension(140,25));
			
			
		partDisplay.setOpaque(false);
		up=new JPanel();
			
		base.setLayout(new BoxLayout(base,BoxLayout.Y_AXIS));
			
			
		up.setLayout(new GridBagLayout());
		up.setPreferredSize(new Dimension(300,120));
		up.setMinimumSize(new Dimension(300,120));
		//up.setBackground(Color.blue);
		up.setOpaque(false);
		GridBagConstraints a=new GridBagConstraints();
		a.gridx=0;
		a.gridy=0;
			
		kitList.setOpaque(false);
		a.insets=new Insets(0,0,10,0);
		a.anchor=GridBagConstraints.LINE_START;
		up.add(k,a);
		a.gridx+=8;
		a.anchor = GridBagConstraints.LINE_END;
		up.add(kitList,a);

		a.gridx=0;
		a.gridy+=8;
		a.anchor = GridBagConstraints.LINE_START;
		up.add(remove,a);
		a.gridx+=8;
		a.anchor=GridBagConstraints.LINE_END;
		up.add(confirm,a);
		a.gridy+=8;
			
		a.anchor=GridBagConstraints.LINE_END;
		up.add(delete,a);
			
		partDisplay.setLayout(new GridBagLayout());
		partDisplay.setOpaque(false);
		partDisplay.setPreferredSize(new Dimension(300,260));
		
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;	
		
		for(int i=0;i<8;i++){
			JButton temp = new JButton(new ImageIcon("images/none.png"));
			temp.setPreferredSize(new Dimension (170,30));
			temp.addActionListener(this);
			partList.add(temp);
			partDisplay.add(partList.get(i),c);
			c.gridy+=2;
			//tempKit.put(i, app.getKitsList().get(kitList.getSelectedIndex()).getParts().get(i));
		}
			
		if(!(kitNames.size() == 0)){
			kitList.setSelectedIndex(0);
		}
			
		base.add(up);
		base.add(partDisplay);
		this.add(base);
	}


	public static void main(String[] args) {
		GUINonNorm app=new GUINonNorm();
		app.setSize(300,450);
		//app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
