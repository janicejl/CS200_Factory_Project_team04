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

import data.Job;
import data.KitInfo;
import data.PartInfo;

public class ProductionManagerApp extends JFrame implements ActionListener, WindowListener {

	ProductionManagerPanel panel;
	GUIProductionManager graphics;
	JPanel card;
	javax.swing.Timer timer;
//	Boolean phase;
	
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem next;
	
	Vector<KitInfo> kitsList;
	Vector<Job> jobs;
	
	public ProductionManagerApp(){
		setSize(1000, 660);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		kitsList = new Vector<KitInfo>();
		load("kitsList.sav");
		jobs = new Vector<Job>();
		load("jobsList.sav");
//		phase = new Boolean(false);
		
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
		card.add(panel);
		card.add(graphics);
		
		add(card);
		
		menuBar = new JMenuBar();
		menu = new JMenu("Screen");
		next = new JMenuItem("Switch Screen");
		next.addActionListener(this);
		menu.add(next);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
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
				kitsList = ((Vector<KitInfo>) streamIn.readObject());
			}
			else if(path.equals("jobsList.sav")){
				jobs = ((Vector<Job>) streamIn.readObject());
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
//		phase = !phase;
	}
	
	public void paint(Graphics g){
		try{
			panel.repaint();
			graphics.repaint();
		} catch(Exception e){
			
		}
		revalidate();
	}
	
	public void revalidate(){
		try{
			panel.revalidate();
			graphics.revalidate();
		} catch(Exception e){
			
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == next){
			next();
		}
		repaint();
	}

	public static void main(String[] args){
		ProductionManagerApp app = new ProductionManagerApp();
		app.timer.start();
	}

	public synchronized Vector<KitInfo> getKitsList() {
		return kitsList;
	}

	public synchronized void setkitsList(Vector<KitInfo> kitsList) {
		this.kitsList = kitsList;
	}

	public synchronized Vector<Job> getJobs() {
		return jobs;
	}

	public synchronized void setJobs(Vector<Job> jobs) {
		this.jobs = jobs;
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
