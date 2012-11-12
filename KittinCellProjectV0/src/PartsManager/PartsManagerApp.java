package PartsManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;

import javax.swing.*;

import data.PartInfo;

public class PartsManagerApp extends JFrame implements ActionListener, Serializable, WindowListener{
	PartsPanel partPanel;
	Vector<PartInfo> partsList;
	
	public PartsManagerApp(){
		addWindowListener(this);
		partPanel = new PartsPanel(this);
		add(partPanel);
		setVisible(true);
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		partsList = new Vector<PartInfo>();
		
		load("partsList.sav");
		partPanel.updateLoad();
		
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
			partsList = ((Vector<PartInfo>) streamIn.readObject()); //load
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

	public synchronized Vector<PartInfo> getPartsList() {
		return partsList;
	}

	public synchronized void setPartsList(Vector<PartInfo> partsList) {
		this.partsList = partsList;
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
