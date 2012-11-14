package server;

import java.net.*;
import java.util.concurrent.*;

import java.util.Vector;
import java.util.Scanner;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Agents.GantryFeederAgents.FeederAgent;
import Agents.GantryFeederAgents.FeederLaneAgent;
import Agents.GantryFeederAgents.GantryAgent;
import Agents.GantryFeederAgents.GantryControllerAgent;
import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Agents.PartsRobotAgent.NestAgent;
import Agents.PartsRobotAgent.PartsRobotAgent;
import Agents.VisionAgent.VisionAgent;
import Feeder.Feeder;
import data.Part;
import laneManager.Nest;
import GantryManager.GantryManager;

public class Server extends JFrame implements Runnable, ActionListener{
	
	ServerPanel gui; //panel for gui
	ServerKitTestPanel kitTest; //panel for kit assembly commands
	ServerPartTestPanel partsTest; //panel for parts robot commands
	ServerLaneTestPanel laneTest; //panel for lane commands
	ServerGantryTestPanel gantryTest;
	Integer phase;
	
	String clientType; //type of client to connect to
	
	ServerSocket ss; //serversocket
	Socket s; //socket reference to connect to each manager
	DetermineProtocol determine;
	KitAssemblyProtocol kitPro;
	LaneManagerProtocol lanePro;
	PartsRobotProtocol partsPro;
	GantryManagerProtocol gantryPro;
	
	
	PartsRobot partsRobot;
	PartsRobotAgent partsRobotAgent;
	Vector<NestAgent> nests = new Vector<NestAgent>();
	VisionAgent nestvisionagent1;
	VisionAgent nestvisionagent2;
	VisionAgent nestvisionagent3;
	VisionAgent nestvisionagent4;
	Vector<VisionAgent> visions = new Vector<VisionAgent>();
	Semaphore flashpermit;
	
	GantryManager gantryManager;
	
	KitRobotAgent kitRobotAgent; 
	KitStandAgent kitStandAgent;
	KitConveyorAgent kitConveyorAgent;
	KitAssemblyManager kitAssemblyManager;
	KitRobot kitRobot; //kit assembly robot

	FeederAgent feeder1;
	FeederAgent feeder2;
	FeederAgent feeder3;
	FeederAgent feeder4;
	FeederLaneAgent fLane1;
	FeederLaneAgent fLane2;
	FeederLaneAgent fLane3;
	FeederLaneAgent fLane4;
	FeederLaneAgent fLane5;
	FeederLaneAgent fLane6;
	FeederLaneAgent fLane7;
	FeederLaneAgent fLane8;
	GantryAgent gantry1;
	GantryAgent gantry2;
	GantryControllerAgent gantryController;

	Vector<Feeder> feeders;
	Vector<Lane> lanes;
	Vector<Nest> nestList;

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
		kitTest.setPreferredSize(new Dimension(300, 400));
		kitTest.setMaximumSize(new Dimension(300, 400));
		kitTest.setMinimumSize(new Dimension(300, 400));
		partsTest = new ServerPartTestPanel(this);
		partsTest.setPreferredSize(new Dimension(300, 400));
		partsTest.setMaximumSize(new Dimension(300, 400));
		partsTest.setMinimumSize(new Dimension(300, 400));
		laneTest = new ServerLaneTestPanel(this);
		laneTest.setPreferredSize(new Dimension(300, 400));
		laneTest.setMaximumSize(new Dimension(300, 400));
		laneTest.setMinimumSize(new Dimension(300, 400));
		gantryTest = new ServerGantryTestPanel(this);
		
		feeder1 = new FeederAgent("feeder1", 5, fLane1, fLane2, 1, this);
		fLane1 = new FeederLaneAgent("left", 1, this);
		fLane2 = new FeederLaneAgent("right", 2, this);
		fLane1.setFeeder(feeder1);
		fLane2.setFeeder(feeder1);
		
		feeder2 = new FeederAgent("feeder2", 5, fLane3, fLane4, 2, this);
		fLane3 = new FeederLaneAgent("left", 3, this);
		fLane4 = new FeederLaneAgent("right", 4, this);
		fLane3.setFeeder(feeder2);
		fLane4.setFeeder(feeder2);
		
		feeder3 = new FeederAgent("feeder3", 5, fLane5, fLane6, 3, this);
		fLane5 = new FeederLaneAgent("left", 5, this);
		fLane6 = new FeederLaneAgent("right", 6, this);
		fLane5.setFeeder(feeder3);
		fLane6.setFeeder(feeder3);
		
		feeder4 = new FeederAgent("feeder4", 5, fLane7, fLane8, 4, this);
		fLane7 = new FeederLaneAgent("left", 7, this);
		fLane8 = new FeederLaneAgent("right", 8, this);
		fLane7.setFeeder(feeder4);
		fLane8.setFeeder(feeder4);
		
		/**gantryController = new GantryControllerAgent(this);
		gantry1 = new GantryAgent("gantry1", this);
		gantry2 = new GantryAgent("gantry2", this);
		gantry1.setGantryController(gantryController);
		gantry2.setGantryController(gantryController);
		gantryController.msgGantryAdded(gantry1);
		gantryController.msgGantryAdded(gantry2);**/
		gantryManager = new GantryManager();
		gantryManager.getGantry().setState("free");
		gantryManager.getGantry().setBox(1);

		feeders = new Vector<Feeder>();
		for(int i = 0; i < 4; i++){
			feeders.add(new Feeder(406,30 + i*140));
		}
		nestList = new Vector<Nest>();
    	
    	
    	nestList.add(new Nest(0, 30));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 100));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 170));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 240));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 310));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 380));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 450));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 520));	//x coordinate is zero for laneManagerApp
    	
    	
		lanes = new Vector<Lane>();
		lanes.add(new Lane(600,30, nestList.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,100, nestList.get(1))); 
    	lanes.add(new Lane(600,170, nestList.get(2))); 
    	lanes.add(new Lane(600,240, nestList.get(3)));
    	lanes.add(new Lane(600,310, nestList.get(4))); 
    	lanes.add(new Lane(600,380, nestList.get(5)));
    	lanes.add(new Lane(600,450, nestList.get(6))); 
    	lanes.add(new Lane(600,520, nestList.get(7)));
    	lanes.get(1).setConveyerBeltSpeed(4);
    	lanes.get(2).setConveyerBeltSpeed(3);
    	
    	for (int i = 0; i < 8; i ++) {
    		lanes.get(i).setConveyerBeltSpeed(15);
    	}
    	
    	NestAgent nest1 = new NestAgent(1,this);
    	NestAgent nest2 = new NestAgent(2,this);
    	NestAgent nest3 = new NestAgent(3,this);
    	NestAgent nest4 = new NestAgent(4,this);
    	NestAgent nest5 = new NestAgent(5,this);
    	NestAgent nest6 = new NestAgent(6,this);
    	NestAgent nest7 = new NestAgent(7,this);
    	NestAgent nest8 = new NestAgent(8,this);
    	nests.add(nest1);
    	nests.add(nest2);
    	nests.add(nest3);
    	nests.add(nest4);
    	nests.add(nest5);
    	nests.add(nest6);
    	nests.add(nest7);
    	nests.add(nest8);
    	
		kitRobotAgent = new KitRobotAgent(this);
		kitStandAgent = new KitStandAgent(this); 
		kitConveyorAgent = new KitConveyorAgent(this);
		kitAssemblyManager = new KitAssemblyManager(nestList);
		kitRobot = new KitRobot(kitAssemblyManager);
		kitStandAgent.SetRobotAgent(kitRobotAgent);
		kitRobotAgent.SetConveyorAgent(kitConveyorAgent);
		kitRobotAgent.SetStandAgent(kitStandAgent);
		kitConveyorAgent.SetKitRobot(kitRobotAgent);
		
		
		new Thread(kitAssemblyManager).start();
        new Thread(kitRobot).start();
        
        partsRobotAgent = new PartsRobotAgent(nests, kitStandAgent, this);
        kitStandAgent.SetPartsRobotAgent(partsRobotAgent);
        kitRobotAgent.startThread();
		kitStandAgent.startThread();
		kitConveyorAgent.startThread();
		
		
        for(NestAgent nest : nests){
        	nest.setPartsRobotAgent(partsRobotAgent);
        	nest.startThread();
        }
        nestvisionagent1 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent2 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent3 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent4 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        
        flashpermit = new Semaphore(1);
        nestvisionagent1.setFlashPermit(flashpermit);
        nestvisionagent2.setFlashPermit(flashpermit);
        nestvisionagent3.setFlashPermit(flashpermit);
        nestvisionagent4.setFlashPermit(flashpermit);
        
        nests.get(0).setVisionAgent(nestvisionagent1);
        nests.get(1).setVisionAgent(nestvisionagent1);
        nests.get(2).setVisionAgent(nestvisionagent2);
        nests.get(3).setVisionAgent(nestvisionagent2);
        nests.get(4).setVisionAgent(nestvisionagent3);
        nests.get(5).setVisionAgent(nestvisionagent3);
        nests.get(6).setVisionAgent(nestvisionagent4);
        nests.get(7).setVisionAgent(nestvisionagent4);
        visions.add(nestvisionagent1);
        visions.add(nestvisionagent2);
        visions.add(nestvisionagent3);
        visions.add(nestvisionagent4);
        partsRobotAgent.setVisionAgents(visions);

        
        nestvisionagent1.startThread();
        nestvisionagent2.startThread();
        nestvisionagent3.startThread();
        nestvisionagent4.startThread();


        
		partsRobotAgent.startThread();
        partsRobot = new PartsRobot(kitAssemblyManager);
        new Thread(partsRobot).start();
		
		//start threads and timer
		thread = new Thread(this, "ServerThread");
		timer = new Timer(10, this);
		timer.start();
	}
	
	public Integer start(){
		removeCenter();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;	
		c.gridy = 0;
		add(kitTest, c);
		c.gridx = 1;
		add(partsTest, c);
		c.gridx = 2;
		add(laneTest, c);
		c.gridx = 3;
		add(gantryTest,c);
		phase = 1;
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
		
		while(true){
			try {
				s = ss.accept();
				new Protocols(s, this);
				
				/*determine = new DetermineProtocol(s, this);
				if(getClientType().equals("Kit Assembly")){
					//kitPro = new KitAssemblyProtocol(s, this); //create proper protocol
					removeCenter();
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = 0;	
					c.gridy = 0;
					add(kitTest, c);
					phase = 1;
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
				else if(getClientType().equals("Lane Manager")){
					lanePro = new LaneManagerProtocol(s, this);
					removeCenter();
					GridBagConstraints c = new GridBagConstraints();
					c.gridx = 0;	
					c.gridy = 0;
					add(laneTest, c);
					phase = 3;
				}
	*/		} catch (IOException e) {
				e.printStackTrace();
			}
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
    	
    	else if(process.equals("Remove Finished")){
    		getKitRobot().addCommand("load,5,6");
    	}
    	else if(process.equals("Load Kit 1")){
    		getPartsRobot().addCommand("dump,0");
    	}
    	else if(process.equals("Load Kit 2")){
    		getPartsRobot().addCommand("dump,1");
    	}   
    	else if(process.equals("Load Parts Box 1")){
    		gantryManager.getGantry().setState("load");
			gantryManager.getGantry().setFeed(0);
    	}
    	else if(process.equals("Load Parts Box 2")){
    		gantryManager.getGantry().setState("load");
    		gantryManager.getGantry().setFeed(1);
    	}
    	else if(process.equals("Load Parts Box 3")){
    		gantryManager.getGantry().setState("load");
    		gantryManager.getGantry().setFeed(2);
    	}
    	else if(process.equals("Load Parts Box 4")){
    		gantryManager.getGantry().setState("load");
    		gantryManager.getGantry().setFeed(3);
    	}
    	else if(process.equals("Dump Feeder 1"))
    	{
    		gantryManager.getGantry().setState("dumpi");
    		gantryManager.getGantry().setFeed(0);
    	}
    	else if(process.equals("Dump Feeder 2"))
    	{
    		gantryManager.getGantry().setState("dumpi");
    		gantryManager.getGantry().setFeed(1);
    	}
    	else if(process.equals("Dump Feeder 3"))
    	{
    		gantryManager.getGantry().setState("dumpi");
    		gantryManager.getGantry().setFeed(2);
    	}
    	else if(process.equals("Dump Feeder 4"))
    	{
    		gantryManager.getGantry().setState("dumpi");
    		gantryManager.getGantry().setFeed(3);
    	}
    	
    }
	 
    public void execute(String process, Integer num){
    	if(process.equals("Spawn Kit")){
    		for(int i = 0; i < num; i++){
    			getKitAssemblyManager().processCommand("spawn");
    		}
    	}
    	else if(process.equals("Take Picture")){
    		partsRobot.takePicture(320, 40 + 140*((num-1)/2));
    	}
    	else if(process.equals("Feed Feeder")){
    		Part temp = new Part("" + num, "images/kt" + num + ".png");
    		temp.setImagePath("images/kt" + temp.getId() + ".png");
    		lanes.get(num).addPart(temp);
    		feeders.get(num/2).addParts(temp);
    	}
    	else if(process.equals("Feed Lane")){
    		lanes.get(num).releasePart();
    		feeders.get(num/2).removePart();
    	}
    	else if(process.equals("Feed Nest")){
    		lanes.get(num).releaseQueue();
    	}
    }
    
    public void execute(String process, Integer nest, Integer grip){
    	if(process.equals("Get Part")){
    		getPartsRobot().addCommand("grab," + (nest) + "," + (grip) + "," + nest);
    	} 		
    }
    
	public void actionPerformed(ActionEvent e){
		for(int i = 0; i < lanes.size(); i++){
			lanes.get(i).actionPerformed(e);
		}
		gantryManager.actionPerformed(e);
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
			partsTest.repaint();
			laneTest.repaint();
			gantryTest.repaint();
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
		gantryTest.revalidate();
	}
	
	public static void main(String[] args) {
		Server factory = new Server();
		factory.setSize(1100, 400);
		factory.setVisible(true);
		factory.setDefaultCloseOperation(EXIT_ON_CLOSE);
		factory.start();
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


	public synchronized NestAgent getNestAgent(int index)
	{
		return nests.get(index);
	}
		
	public synchronized Vector<Lane> getLanes() {
		return lanes;
	}

	public synchronized void setLanes(Vector<Lane> lanes) {
		this.lanes = lanes;
	}
	public synchronized Vector<VisionAgent> getVisions(){
		return this.visions;
	}

	public synchronized Vector<Feeder> getFeeders() {
		return feeders;
	}

	public synchronized void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public synchronized Vector<Nest> getNests() {
		return nestList;
	}
	
	public synchronized void setNests(Vector<Nest> nests) {
		this.nestList = nests;
	}
	
	public synchronized GantryManager getGantryManager()
	{
		return gantryManager;
	}
	
	public synchronized void setGantryManager(GantryManager g)
	{
		gantryManager = g;
	}
}
