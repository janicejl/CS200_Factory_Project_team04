package server;

import java.net.*;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Agents.PartsRobotAgent.PartsRobotAgent;

public class Server extends JFrame implements Runnable, ActionListener{
	
	ServerPanel gui; //panel for gui
	ServerKitTestPanel kitTest; //panel for kit assembly commands
	ServerPartTestPanel partsTest; //panel for parts robot commands
	ServerLaneTestPanel laneTest; //panel for lane commands
	Integer phase;
	
	String clientType; //type of client to connect to
	
	ServerSocket ss; //serversocket
	Socket s; //socket reference to connect to each manager
	DetermineProtocol determine;
	KitAssemblyProtocol kitPro;
	LaneManagerProtocol lanePro;
	PartsRobotProtocol partsPro;
	
	PartsRobotAgent partsRobotAgent;
	
	KitRobotAgent kitRobotAgent; 
	KitStandAgent kitStandAgent;
	KitConveyorAgent kitConveyorAgent;
	KitAssemblyManager kitAssemblyManager;
	KitRobot kitRobot; //kit assembly robot
	
	PartsRobot partsRobot;
	
	Timer timer; //timer for server
	Thread thread; //thread for the server
	
	public Server(){
		
		//setup layout
		setLayout(new GridBagLayout());
		gui = new ServerPanel(this);
		phase = new Integer(0);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;	
		c.gridy = 0;
		add(gui, c);
		kitTest = new ServerKitTestPanel(this);
		partsTest = new ServerPartTestPanel(this);
		laneTest = new ServerLaneTestPanel(this);
		
		
		kitRobotAgent = new KitRobotAgent(this);
		kitStandAgent = new KitStandAgent(this); 
		kitConveyorAgent = new KitConveyorAgent(this);
		kitAssemblyManager = new KitAssemblyManager();
		kitRobot = new KitRobot(kitAssemblyManager);
		kitStandAgent.SetRobotAgent(kitRobotAgent);
		kitRobotAgent.SetConveyorAgent(kitConveyorAgent);
		kitRobotAgent.SetStandAgent(kitStandAgent);
		kitConveyorAgent.SetKitRobot(kitRobotAgent);
		kitRobotAgent.startThread();
		kitStandAgent.startThread();
		kitConveyorAgent.startThread();
		new Thread(kitAssemblyManager).start();
        new Thread(kitRobot).start();
        
        partsRobotAgent = new PartsRobotAgent(kitStandAgent, this);
		partsRobotAgent.startThread();
        partsRobot = new PartsRobot();
        new Thread(partsRobot).start();
		
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
			if(getClientType().equals("Kit Assembly")){
				kitPro = new KitAssemblyProtocol(s, this); //create proper protocol
				removeCenter();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;	
				c.gridy = 0;
				add(kitTest, c);
				phase = 1;
			}
			else if(getClientType().equals("Lane Manager")){
				lanePro = new LaneManagerProtocol(s, this);
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;	
				c.gridy = 0;
				add(laneTest, c);
				phase=3;
			}
			else if(getClientType().equals("Parts Robot")){
				partsPro = new PartsRobotProtocol(s, this);
				removeCenter();
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;	
				c.gridy = 0;
				add(partsTest, c);
				phase = 2;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void execute(String process){
    	if(process.equals("Load Stand 1")){
    		getKitRobot().addCommand("load,0,1");
    	}
    	else if(process.equals("Load Stand 2")){
    		getKitRobot().addCommand("load,0,2");
    	}
    	else if(process.equals("Check Kit 1")){
    		getKitRobot().addCommand("load,1,5");
    	}
    	else if(process.equals("Check Kit 2")){
    		getKitRobot().addCommand("load,2,5");
    	}
    	else if(process.equals("Take Picture")){
    		
    	}
    	else if(process.equals("Remove Finished")){
    		getKitRobot().addCommand("load,5,6");
    	}
    	else if(process.equals("Load Kit 1")){
    		getPartsRobot().addCommand("dump,0");
    	}
    	else if(process.equals("Load Kit 2")){
    		getPartsRobot().addCommand("dump,1");
    	}   
    }
	 
    public void execute(String process, Integer num){
    	if(process.equals("Spawn Kit")){
    		for(int i = 0; i < num; i++){
    			getKitAssemblyManager().processCommand("spawn");
    		}
    	}
    	
    	if(process.equals("Feed Feeder")){
    		
    	}
    	else if(process.equals("Feed Lane")){
    		
    	}
    	else if(process.equals("Feed Nest")){
    		
    	}
    }
    
    public void execute(String process, Integer nest, Integer grip){
    	if(process.equals("Get Part")){
    		getPartsRobot().addCommand("grab," + (nest) + "," + (grip));
    	} 		
    }
    
	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public void removeCenter(){
		if(phase.equals(0)){
			remove(gui);
		}
		else if(phase.equals(1)){
			remove(kitTest);
		}
	}
	
	public void paint(Graphics g){
		if(phase.equals(0)){
			gui.repaint();
		}
		else if(phase.equals(1)){
			kitTest.repaint();
		}
		else if(phase.equals(2)){
			partsTest.repaint();
		}
		else if(phase.equals(3)){
			laneTest.repaint();
		}
		revalidate();
	}
	
	public void revalidate(){
		gui.revalidate();
		kitTest.revalidate();
		partsTest.revalidate();
		laneTest.revalidate();
	}
	
	public static void main(String[] args) {
		Server factory = new Server();
		factory.setSize(533, 400);
		factory.setVisible(true);
		factory.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public synchronized KitAssemblyManager getKitAssemblyManager() {
		return kitAssemblyManager;
	}

	public synchronized void setKitAssemblyManager(
			KitAssemblyManager kitAssemblyManager) {
		this.kitAssemblyManager = kitAssemblyManager;
	}

	public synchronized KitRobot getKitRobot() {
		return kitRobot;
	}

	public synchronized void setKitRobot(KitRobot kitRobot) {
		this.kitRobot = kitRobot;
	}

	public synchronized String getClientType() {
		return clientType;
	}

	public synchronized void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public synchronized KitRobotAgent getKitRobotAgent() {
		return kitRobotAgent;
	}

	public synchronized void setKitRobotAgent(KitRobotAgent kitRobotAgent) {
		this.kitRobotAgent = kitRobotAgent;
	}

	public synchronized KitStandAgent getKitStandAgent() {
		return kitStandAgent;
	}

	public synchronized void setKitStandAgent(KitStandAgent kitStandAgent) {
		this.kitStandAgent = kitStandAgent;
	}

	public synchronized KitConveyorAgent getKitConveyorAgent() {
		return kitConveyorAgent;
	}

	public synchronized void setKitConveyorAgent(KitConveyorAgent kitConveyorAgent) {
		this.kitConveyorAgent = kitConveyorAgent;
	}

	public synchronized PartsRobotProtocol getPartsPro() {
		return partsPro;
	}

	public synchronized void setPartsPro(PartsRobotProtocol partsPro) {
		this.partsPro = partsPro;
	}

	public synchronized PartsRobot getPartsRobot() {
		return partsRobot;
	}

	public synchronized void setPartsRobot(PartsRobot partsRobot) {
		this.partsRobot = partsRobot;
	}

	public synchronized PartsRobotAgent getPartsRobotAgent() {
		return partsRobotAgent;
	}

	public synchronized void setPartsRobotAgent(PartsRobotAgent partsRobotAgent) {
		this.partsRobotAgent = partsRobotAgent;
	}

}
