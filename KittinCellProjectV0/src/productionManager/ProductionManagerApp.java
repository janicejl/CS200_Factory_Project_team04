package productionManager;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;

import PartsManager.PartsManagerClient;

import data.Job;
import data.KitInfo;
import data.PartInfo;

public class ProductionManagerApp extends JFrame implements ActionListener, WindowListener {

	ProductionManagerPanel panel;
	GUIProductionManager graphics;
	JPanel card;
	javax.swing.Timer timer;
	ProductionClient client;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem next;
	
	ArrayList<KitInfo> kitsList;
	ArrayList<Job> jobs;
	
	public ProductionManagerApp(){
		setSize(1200, 645);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		addWindowListener(this);
		
		client = new ProductionClient(this);
		
		kitsList = new ArrayList<KitInfo>();
		kitsList.add(null);
		load("kitsList.sav");
		jobs = new ArrayList<Job>();
		load("jobsList.sav");

		panel = new ProductionManagerPanel(this);
		panel.setPreferredSize(new Dimension(1000, 660));
		panel.setMaximumSize(new Dimension(1000, 660));
		panel.setMinimumSize(new Dimension(1000, 660));
		panel.update();
		
		graphics = new GUIProductionManager(this);
		graphics.setPreferredSize(new Dimension(1000, 600));
		graphics.setMaximumSize(new Dimension(1000, 600));
		graphics.setMinimumSize(new Dimension(1000, 600));
		
		card = new JPanel();
		card.setLayout(new CardLayout());
		card.setPreferredSize(new Dimension(1000, 600));
		card.setMaximumSize(new Dimension(1000, 600));
		card.setMinimumSize(new Dimension(1000, 600));
		card.add(panel, "");
		card.add(graphics, "");
		
		add(card);
		
		menuBar = new JMenuBar();
		menu = new JMenu("Screen");
		next = new JMenuItem("Switch Screen");
		next.addActionListener(this);
		menu.add(next);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		
		
		//connect with server
		client = new ProductionClient(this);
		int j = client.connect();
		if(j == -1){
			System.exit(1);
		}
		else if(j == 1){
			client.getThread().start();
		}
		
		timer = new javax.swing.Timer(10, this);
	}
	
	//save
	public void save(String path){
		try{
			FileOutputStream fileOut = new FileOutputStream(path); //write to settings.sav file (will overwrite)
	        ObjectOutputStream streamOut = new ObjectOutputStream(fileOut); //outputstream
	        streamOut.writeObject(getJobs()); //save
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
			if(path.equals("kitsList.sav")){
				kitsList = ((ArrayList<KitInfo>) streamIn.readObject());
			}
			else if(path.equals("jobsList.sav")){
				jobs = ((ArrayList<Job>) streamIn.readObject());
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
	
	public void next(){
		CardLayout tempLayout = (CardLayout)card.getLayout();
		tempLayout.next(card);
	}
	
	public void paint(Graphics g){
		try{
			panel.repaint();
			graphics.repaint();
		} catch(Exception e){
			e.printStackTrace();
		}
		revalidate();
	}
	
	public void revalidate(){
		try{
			panel.revalidate();
			graphics.revalidate();
			menuBar.revalidate();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == next){
			next();
		}
		graphics.update();
		repaint();
		client.updateThread();
	}

	public static void main(String[] args){
		ProductionManagerApp app = new ProductionManagerApp();
		app.timer.start();
	}

	public synchronized ArrayList<KitInfo> getKitsList() {
		return kitsList;
	}

	public synchronized void setkitsList(ArrayList<KitInfo> kitsList) {
		this.kitsList = kitsList;
	}

	public synchronized ArrayList<Job> getJobs() {
		return jobs;
	}

	public synchronized void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}
	
	public synchronized ProductionClient getClient() {
		return client;
	}

	public synchronized void setClient(ProductionClient client) {
		this.client = client;
	}

	public ProductionManagerPanel getPanel() {
		return panel;
	}

	public void setPanel(ProductionManagerPanel panel) {
		this.panel = panel;
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
		save("jobsList.sav");
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
