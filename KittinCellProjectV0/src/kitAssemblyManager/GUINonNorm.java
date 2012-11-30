package kitAssemblyManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.KitAssemblyManager;
import data.KitInfo;
import data.PartInfo;


public class GUINonNorm extends JFrame implements ActionListener, WindowListener {
	
		JPanel base,partDisplay,up,back;
		//JScrollPane displayKit;
		JLabel k;
		JButton confirm;
		JButton delete;
		JComboBox kitList;
		ArrayList<KitInfo> badList;//list of bad kits
		Vector<String> badNames;// vector of kits' names
		Vector<JButton> partList; // vector to display the parts inside the kit
		TreeMap<Integer, Boolean> tempDeleted; // temp of changes made by users
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
		
		badNames=new Vector<String>();
		
		badList = new ArrayList<KitInfo>();
		load("badKitsList.sav");
		for(int i=0;i<badList.size();i++){
			badNames.add(badList.get(i).getName());
		}
		
		tempDeleted = new TreeMap<Integer, Boolean>();
		for(int i = 0; i < 8; i++){
			tempDeleted.put(i, false);
		}
			
		
			
		kitList=new JComboBox(badNames);
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
		
		/*for(int i=0;i<8;i++){
			JButton temp = new JButton(new ImageIcon("images/none.png"));
			temp.setPreferredSize(new Dimension (170,30));
			temp.addActionListener(this);
			partList.add(temp);
			partDisplay.add(partList.get(i),c);
			c.gridy+=2;
			//tempKit.put(i, badList.get(kitList.getSelectedIndex()).getParts().get(i));
		}*/
			
		if(!(badNames.size() == 0)){
			kitList.setSelectedIndex(0);
		}
			
		base.add(up);
		base.add(partDisplay);
		this.add(base);
	}
	
	//save
		public void save(String path){
			try{
				FileOutputStream fileOut = new FileOutputStream(path); //write to settings.sav file (will overwrite)
		        ObjectOutputStream streamOut = new ObjectOutputStream(fileOut); //outputstream
		        streamOut.writeObject(badList); //save
		        streamOut.close();
		        fileOut.close();
			}
			catch(IOException e){ //if unable to write to file or something is unserializable
				JOptionPane.showMessageDialog(null, "Failed to save.", "Exception", JOptionPane.OK_OPTION);
				e.printStackTrace(); //print trace
				return;
			}
		}
		
		//update the patterns of the buttons
		public void updateLabels(){
			partDisplay.removeAll();
			partList.clear();
			GridBagConstraints c=new GridBagConstraints();
			c.gridx=0;
			c.gridy=0;		
			if(badList.size() != 0){
				for(int i=0;i<8;i++){
					JButton temp = new JButton(new ImageIcon(badList.get(kitList.getSelectedIndex()).getParts().get(i).getImagePath()));
					temp.setPreferredSize(new Dimension (170,30));
					temp.addActionListener(this);
					partList.add(temp);
					partDisplay.add(partList.get(i),c);
					c.gridy+=2;
				}
			}
			partDisplay.revalidate();
			partDisplay.repaint();
		}
		
		public void updateBox(int num){//update the jcombobox
			badNames.clear();
			if(badList.size() == 0){
				badNames.add("");
			}
			for(int i = 0; i < badList.size(); i++){
				badNames.add(badList.get(i).getName());
			}
			
			kitList.setModel(new DefaultComboBoxModel(badNames));
			if(num < badNames.size()){
				kitList.setSelectedIndex(num);
			}
			else{
				kitList.setSelectedIndex(badNames.size()-1);
			}
		}
		
	//load
	public void load(String path){
		try{
			FileInputStream fileIn = new FileInputStream(path); //access  file
			ObjectInputStream streamIn = new ObjectInputStream(fileIn); //inputstream
			if(path.equals("badKitsList.sav")){
				badList = ((ArrayList<KitInfo>) streamIn.readObject());
			}
			streamIn.close();
			fileIn.close();
		}
		catch(IOException i){ //if file not found
			System.out.println("Could not find " + path +  " file.");
			return;
		}
		catch(ClassNotFoundException e){ //if casting error
			System.out.println("Class not found");
			e.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {
		GUINonNorm app=new GUINonNorm();
		app.setSize(300,450);
		//app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(kitList)){
			updateLabels();			
		}
		else if(ae.getSource().equals(confirm)){
			
				for(int i = 0; i < tempDeleted.size(); i++){
					if(tempDeleted.get(i) == true){
						badList.get(kitList.getSelectedIndex()).getParts().set(i, new PartInfo(null, "images/none.png"));
						tempDeleted.put(i, false);
					}
					else{
						//badList.get(kitList.getSelectedIndex()).getParts().set(i, tempKit.get(i));
					}
				}

			updateBox(kitList.getSelectedIndex());
		}
		else if(ae.getSource().equals(delete)){
			int num = kitList.getSelectedIndex();
			badList.remove((int)num);
			updateBox(num);
			//app.getClient().setCommandSent("Update Kits");
		}
		
		for(int i=0;i<partList.size();i++){ //select which part to be removed 
			if(ae.getSource().equals(partList.get(i))){
				if(remove.isSelected()){
					if(tempDeleted.get(i) == false){
						tempDeleted.put(i, true);
						partList.get(i).setIcon(new ImageIcon("images/none.png"));
					}
					else if(tempDeleted.get(i) == true){
						tempDeleted.put(i, false);
						partList.get(i).setIcon(new ImageIcon(badList.get(kitList.getSelectedIndex()).getParts().get(i).getImagePath()));
					}
				}
			}
		}
		
		
		partDisplay.revalidate();
		partDisplay.repaint();
	}

	
	public synchronized ArrayList<KitInfo> getBadList() {
		return badList;
	}

	public synchronized void setBadList(ArrayList<KitInfo> badList) {
		this.badList = badList;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {//autosave
		// TODO Auto-generated method stub
		save("badKitsList.sav");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
