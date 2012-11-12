package KitCreationManager;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import data.KitInfo;
import data.PartInfo;

import java.util.*;

public class KitCreationApp extends JFrame implements Serializable, WindowListener{
	GUIKitModification km;
	GUIKitCreation kc;
	JTabbedPane kp;
	ArrayList<PartInfo> partsList;
	ArrayList<KitInfo> kitsList;
	
	public KitCreationApp(){
		km=new GUIKitModification();
		kc=new GUIKitCreation();
		kp=new JTabbedPane();
		kp.addTab("Kit Creation", kc.bob);
		kp.addTab("Kit Modification", km.base);
		this.add(kp);
		addWindowListener(this);
		partsList = new ArrayList<PartInfo>();
		load("partsList.sav");
		kitsList = new ArrayList<KitInfo>();
		load("kitsList.sav");
	}
	public static void main(String[] args) {
		KitCreationApp app=new KitCreationApp();
		app.setSize(300,400);
		app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		
	}

	//save
	public void save(String path){
		try{
			FileOutputStream fileOut = new FileOutputStream(path); //write to settings.sav file (will overwrite)
	        ObjectOutputStream streamOut = new ObjectOutputStream(fileOut); //outputstream
	        streamOut.writeObject(getKitsList()); //save
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
			if(path.equals("partsList.sav")){
				partsList = ((ArrayList<PartInfo>) streamIn.readObject()); //load
			}
			else if(path.equals("kitsList.sav")){
				kitsList = ((ArrayList<KitInfo>) streamIn.readObject());
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
	public synchronized ArrayList<PartInfo> getPartsList() {
		return partsList;
	}
	public synchronized void setPartsList(ArrayList<PartInfo> partsList) {
		this.partsList = partsList;
	}
	public synchronized ArrayList<KitInfo> getKitsList() {
		return kitsList;
	}
	public synchronized void setKitsList(ArrayList<KitInfo> kitsList) {
		this.kitsList = kitsList;
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
		// TODO Auto-generated method stub
		save("kitList.sav");
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
