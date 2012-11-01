package server;

import java.net.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame implements Runnable, ActionListener{
	
	ServerPanel gui; //panel for gui
	
	ServerSocket ss; //serversocket
	Socket s; //socket reference to connect to each manager
	DetermineProtocol determine;
	KitAssemblyProtocol kitPro;
	LaneManagerProtocol lanePro;
	PartsRobotProtocol partsPro;
	
	Timer timer; //timer for server
	Thread thread; //thread for the server
	
	public Server(){
		
		//setup layout
		setLayout(new GridBagLayout());
		gui = new ServerPanel(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;	
		c.gridy = 0;
		add(gui, c);
		
		//start threads and timer
		thread = new Thread(this, "ServerThread");
		timer = new Timer(25, this);
		timer.start();
	}
	
	public Integer start(){
		try{
			ss = new ServerSocket(61337); //attempt to start at indicated port #
		} catch (IOException e) {
			System.err.println("Port in use");
			e.printStackTrace();
			return -1;
		}
		
		//start server thread
		thread.start();
		//get connections
		System.out.println("Server running.");
		return 1;
	}
	
	public void run(){
		try {
			s = ss.accept();
			determine = new DetermineProtocol(s, this); //create proper protocol
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public void paint(Graphics g){
		gui.repaint();
	}
	
	public static void main(String[] args) {
		Server factory = new Server();
		factory.setSize(533, 400);
		factory.setVisible(true);
		factory.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
