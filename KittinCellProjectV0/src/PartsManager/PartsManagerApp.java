package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.*;

import KitCreationManager.KitCreationClient;

import data.PartInfo;

public class PartsManagerApp extends JFrame implements ActionListener, Serializable, WindowListener{
	
	PartsPanel partPanel;
	EditPanel editPanel;
	ArrayList<PartInfo> partsList;
	PartsManagerClient client;
	JTabbedPane tabbedPane;
	
	public PartsManagerApp(){
		partsList = new ArrayList<PartInfo>();
		
		addWindowListener(this);
		tabbedPane = new JTabbedPane();
		partPanel = new PartsPanel(this);
		editPanel = new EditPanel(this);
		tabbedPane.addTab("Create Parts", partPanel);
		tabbedPane.addTab("Edit Parts", editPanel);
		add(tabbedPane);
		setVisible(true);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		load("partsList.sav");
		partPanel.updateLoad();
		editPanel.updateSelectionBox(0);
		
		client = new PartsManagerClient(this);
		int j = client.connect();
		if(j == -1){
			System.exit(1);
		}
		else if(j == 1){
			client.getThread().start();
		}
		
		new Timer(10, this).start();
	}
	
	//save
	public void save(String path){
		try{
			FileOutputStream fileOut = new FileOutputStream(path); //write to settings.sav file (will overwrite)
	        ObjectOutputStream streamOut = new ObjectOutputStream(fileOut); //outputstream
	        streamOut.writeObject(getPartsList()); //save
	        streamOut.close();
	        fileOut.close();
		}
		catch(IOException e){ //if unable to write to file or something is unserializable
			JOptionPane.showMessageDialog(null, "Failed to save.", "Exception", JOptionPane.OK_OPTION);
			e.printStackTrace(); //print trace
			return;
		}
	}
	
	//load profiles
	public void load(String path){
		try{
			FileInputStream fileIn = new FileInputStream(path); //access  file
			ObjectInputStream streamIn = new ObjectInputStream(fileIn); //inputstream
			partsList = ((ArrayList<PartInfo>) streamIn.readObject()); //load
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
	
	public void actionPerformed(ActionEvent e){
		repaint();
		//revalidate();
	}
	
	public void paint(Graphics g){
		partPanel.repaint();
		editPanel.repaint();
		tabbedPane.repaint();
		revalidate();
	}
	
	public void revalidate(){
		partPanel.revalidate();
		editPanel.revalidate();
		tabbedPane.revalidate();
	}
	
	public void updateEditPanel(){
		editPanel.updateSelectionBox(0);
	}
	
	public void updatePartsPanel(){
		partPanel.updatePartList();
	}
	
	public static void main(String[] args){
		new PartsManagerApp();
	}

	public synchronized PartsPanel getPartPanel() {
		return partPanel;
	}

	public synchronized void setPartPanel(PartsPanel partPanel) {
		this.partPanel = partPanel;
	}

	public synchronized ArrayList<PartInfo> getPartsList() {
		return partsList;
	}

	public synchronized void setPartsList(ArrayList<PartInfo> partsList) {
		this.partsList = partsList;
	}

	public PartsManagerClient getClient() {
		return client;
	}

	public void setClient(PartsManagerClient client) {
		this.client = client;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		save("partsList.sav");		
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
