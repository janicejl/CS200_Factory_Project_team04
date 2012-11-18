package server;


import java.net.*;
import java.util.concurrent.*;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import Agents.GantryFeederAgents.FeederAgent;
import Agents.GantryFeederAgents.FeederLaneAgent;
import Agents.GantryFeederAgents.GantryAgent;
import Agents.GantryFeederAgents.GantryControllerAgent;
import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Agents.PartsRobotAgent.LaneAgent;
import Agents.PartsRobotAgent.NestAgent;
import Agents.PartsRobotAgent.PartsRobotAgent;
import Agents.VisionAgent.VisionAgent;
import Interface.*;
import Interface.VisionAgent.Vision;
import Feeder.Feeder;
import data.Job;
import data.KitInfo;
import data.Part;
import data.PartInfo;
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
//	DetermineProtocol determine;
//	KitAssemblyProtocol kitPro;
//	LaneManagerProtocol lanePro;
//	PartsRobotProtocol partsPro;
//	GantryManagerProtocol gantryPro;
	
	ArrayList<PartInfo> partsList;
	ArrayList<KitInfo> kitsList;
	ArrayList<Job> jobsList;
	String kitCreateCommand = "Idle";
	String productionCommand = "Idle";
	String partsCommand = "Idle";
	
	PartsRobot partsRobot;
	PartsRobotAgent partsRobotAgent;
	ArrayList<LaneAgent> laneagents = new ArrayList<LaneAgent>();
	ArrayList<NestAgent> nests = new ArrayList<NestAgent>();
	VisionAgent nestvisionagent1;
	VisionAgent nestvisionagent2;
	VisionAgent nestvisionagent3;
	VisionAgent nestvisionagent4;
	ArrayList<VisionAgent> visions = new ArrayList<VisionAgent>();
	Semaphore flashpermit;
	
	GantryManager gantryManager;
	ArrayList<String> gantryWaitList;
	ArrayList<Integer> gantryFeedList;
	
	KitRobotAgent kitRobotAgent; 
	KitStandAgent kitStandAgent;
	KitConveyorAgent kitConveyorAgent;
	KitAssemblyManager kitAssemblyManager;
	KitRobot kitRobot; //kit assembly robot

	FeederAgent feeder1;
	FeederAgent feeder2;
	FeederAgent feeder3;
	FeederAgent feeder4;
	GantryAgent gantry1;
	GantryAgent gantry2;
	GantryControllerAgent gantryController;

	ArrayList<Feeder> feeders;
	ArrayList<Lane> lanes;
	ArrayList<Nest> nestList;

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
		
		gantryController = new GantryControllerAgent(this);
		gantry1 = new GantryAgent("gantry1", this);
		gantry2 = new GantryAgent("gantry2", this);
		gantry1.setGantryController(gantryController);
		gantry2.setGantryController(gantryController);
		gantryController.msgGantryAdded(gantry1);
		gantryController.msgGantryAdded(gantry2);
		

		feeders = new ArrayList<Feeder>();
		for(int i = 0; i < 4; i++){
			if(i == 0 || i == 3){
    			feeders.add(new Feeder(475,30 + i*140));
    		}
    		else {
    			feeders.add(new Feeder(400,30 + i*140));    			
    		}
		}
		
		gantryManager = new GantryManager(feeders);
		gantryManager.getGantry().setState("free");
		gantryManager.getGantry().setBox(1);
		gantryFeedList = new ArrayList<Integer>();
		gantryWaitList = new ArrayList<String>();
		
		nestList = new ArrayList<Nest>();
    	nestList.add(new Nest(0, 30));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 100));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 170));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 240));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 310));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 380));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 450));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 520));	//x coordinate is zero for laneManagerApp
    	
    	
		lanes = new ArrayList<Lane>();
		lanes.add(new Lane(600,30, nestList.get(0), feeders.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,100, nestList.get(1), feeders.get(0))); 
    	lanes.add(new Lane(600,170, nestList.get(2),feeders.get(1))); 
    	lanes.add(new Lane(600,240, nestList.get(3), feeders.get(1)));
    	lanes.add(new Lane(600,310, nestList.get(4), feeders.get(2))); 
    	lanes.add(new Lane(600,380, nestList.get(5), feeders.get(2)));
    	lanes.add(new Lane(600,450, nestList.get(6), feeders.get(3))); 
    	lanes.add(new Lane(600,520, nestList.get(7), feeders.get(3)));
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
    	LaneAgent lane1 = new LaneAgent(nest1,this,"laneagent1",0);
    	LaneAgent lane2 = new LaneAgent(nest2,this,"laneagent2",1);
    	LaneAgent lane3 = new LaneAgent(nest3,this,"laneagent3",2);
    	LaneAgent lane4 = new LaneAgent(nest4,this,"laneagent4",3);
    	LaneAgent lane5 = new LaneAgent(nest5,this,"laneagent5",4);
    	LaneAgent lane6 = new LaneAgent(nest6,this,"laneagent6",5);
    	LaneAgent lane7 = new LaneAgent(nest7,this,"laneagent7",6);
    	LaneAgent lane8 = new LaneAgent(nest8,this,"laneagent8",7);
    	laneagents.add(lane1);
    	laneagents.add(lane2);
    	laneagents.add(lane3);
    	laneagents.add(lane4);
    	laneagents.add(lane5);
    	laneagents.add(lane6);
    	laneagents.add(lane7);
    	laneagents.add(lane8);
    	nest1.setLane(lane1);
    	nest2.setLane(lane2);
    	nest3.setLane(lane3);
    	nest4.setLane(lane4);
    	nest5.setLane(lane5);
    	nest6.setLane(lane6);
    	nest7.setLane(lane7);
    	nest8.setLane(lane8);
    	feeder1 = new FeederAgent("feeder1", 5, lane1, lane2, 0, this);		
		feeder2 = new FeederAgent("feeder2", 5, lane3, lane4, 1, this);		
		feeder3 = new FeederAgent("feeder3", 5, lane5, lane6, 2, this);		
		feeder4 = new FeederAgent("feeder4", 5, lane7, lane8, 3, this);
		feeder1.setGantryController(gantryController);
		feeder2.setGantryController(gantryController);
		feeder3.setGantryController(gantryController);
		feeder4.setGantryController(gantryController);
		/*
		lane1.setFeeder(feeder1);
		lane2.setFeeder(feeder1);
		lane3.setFeeder(feeder2);
		lane4.setFeeder(feeder2);
		lane5.setFeeder(feeder3);
		lane6.setFeeder(feeder3);
		lane7.setFeeder(feeder4);
		lane8.setFeeder(feeder4);
		*/

    	

    	
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
        for(LaneAgent lane : laneagents){
        	lane.startThread();
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
    	
    	else if (process.equals("Take Picture")) {
    		partsRobot.getKitStandCamera().takePicture();
    	}
    }
	 
	public void execute(String process, Integer num, PartInfo info){
		if(process.equals("Feed Feeder")){
    		Part temp = new Part(info);
    		if (num % 2 == 0) {
    			feeders.get(num/2).setTopLane(true);
    		} else {
    			feeders.get(num/2).setTopLane(false);
    		}
    		System.out.println("TOPLANE: " + feeders.get(num/2).getTopLane());
    		feeders.get(num/2).addParts(temp);
    	}
	}
    public void execute(String process, Integer num){
    	if(process.equals("Spawn Kit")){
    		for(int i = 0; i < num; i++){
    			getKitAssemblyManager().processCommand("spawn");
    		}
    	}
    	else if(process.equals("Take Picture")){
    		partsRobot.getNestCamera().takePicture(320, 40 + 140*((num-1)/2));
    	}
    	else if(process.equals("Feed Feeder")){
    		Part temp = new Part("" + num, "images/kt" + num + ".png");
    		temp.setImagePath("images/kt" + temp.getId() + ".png");
    		
    		System.out.println("TOPLANE: " + feeders.get(num/2).getTopLane());
    		feeders.get(num/2).addParts(temp);
    	}
    	else if(process.equals("Feed Lane")){
    		if (feeders.get(num/2).getPartAmount() > 0){
    			lanes.get(num).addPart(feeders.get(num/2).getParts().get(0));
    			lanes.get(num).setRelease(true);
    			lanes.get(num).setReleaseCount(lanes.get(num).getReleaseCount() + 1);
    		}
    		feeders.get(num/2).setMoving(true);
    		if (num % 2 == 0) {
    			feeders.get(num/2).setTopLane(true);
    		} else {
    			feeders.get(num/2).setTopLane(false);
    		}
    		
    		//feeders.get(num/2).removePart();
    	}
    	else if(process.equals("Feed Nest")){
    		lanes.get(num).releaseQueue();
    	}
    	else if(process.equals("Load Feeder"))
    	{
    		if(gantryManager.getGantry().getState().equals("free"))
    		{
    			gantryManager.getGantry().setState("load");
    			gantryManager.getGantry().setFeed(num);
    		}
       		else
    		{
    			gantryWaitList.add("load");
    			gantryFeedList.add(num);
    		}
    	}
    	else if(process.equals("Dump Feeder"))
    	{
    		if(gantryManager.getGantry().getState().equals("free"))
    		{
    			gantryManager.getGantry().setState("dumpi");
    			gantryManager.getGantry().setFeed(num);
    		}
       		else
    		{
    			gantryWaitList.add("dumpi");
    			gantryFeedList.add(num);
    		}
    	}
    	else if(process.equals("Idle Bin"))
    	{
    		if(gantryManager.getGantry().getState().equals("free"))
    		{
    			gantryManager.getGantry().setState("purgei");
    			gantryManager.getGantry().setFeed(num);
    		}
    		else
    		{
    			gantryWaitList.add("purgei");
    			gantryFeedList.add(num);
    		}
    	}
    }
    
    public void execute(String process, Integer nest, Integer grip){
    	if(process.equals("Get Part")){
    		getPartsRobot().addCommand("grab," + (nest) + "," + (grip) + "," + nest);
    	} 		
    }
    
	public void actionPerformed(ActionEvent e){
		if(getKitAssemblyManager().getMsg().equals(true)){
			getKitAssemblyManager().setMsg(false);
			getKitConveyorAgent().msgKitHasArrived();
		}
		if(getPartsRobot().getMsg().equals(true)){
			getPartsRobotAgent().msgAnimationDone();
			getPartsRobot().setMsg(false);
		}
		if(getPartsRobot().isMovingMsg()){
			getPartsRobotAgent().msgMovementDone();
			getPartsRobot().setMovingMsg(false);
		}
		if(getPartsRobot().isDumped()){
			getPartsRobotAgent().msgPartsDropped();
			getPartsRobot().setDumped(false);
		}
		if(getPartsRobot().getNestCamera().getAnimationDone()){
			for(int i = 0; i<4; i++){
				getVisions().get(i).msgCameraAvailable();
			}
			getPartsRobot().getNestCamera().setAnimationDone(false);
		}	
		for(int i = 0; i < lanes.size(); i++){
			lanes.get(i).actionPerformed(e);
			if(lanes.get(i).isAtQueue()){
				laneagents.get(i).msgPartAtEndOfLane();
			}
			//check feeder movement
			if(lanes.get(i).isRelease()){
				if(!lanes.get(i).getFeeder().isMoving()){
					lanes.get(i).releasePart();
					lanes.get(i).setReleaseCount(lanes.get(i).getReleaseCount() - 1);
				}
				//determine if it can stop releasing
				if(lanes.get(i).getReleaseCount() == 0){
					lanes.get(i).setRelease(false);
				}
			}
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

	public KitAssemblyManager getKitAssemblyManager() {
		return kitAssemblyManager;
	}

	public void setKitAssemblyManager(
			KitAssemblyManager kitAssemblyManager) {
		this.kitAssemblyManager = kitAssemblyManager;
	}

	public KitRobot getKitRobot() {
		return kitRobot;
	}

	public void setKitRobot(KitRobot kitRobot) {
		this.kitRobot = kitRobot;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public KitRobotAgent getKitRobotAgent() {
		return kitRobotAgent;
	}

	public void setKitRobotAgent(KitRobotAgent kitRobotAgent) {
		this.kitRobotAgent = kitRobotAgent;
	}

	public KitStandAgent getKitStandAgent() {
		return kitStandAgent;
	}

	public void setKitStandAgent(KitStandAgent kitStandAgent) {
		this.kitStandAgent = kitStandAgent;
	}

	public KitConveyorAgent getKitConveyorAgent() {
		return kitConveyorAgent;
	}

	public void setKitConveyorAgent(KitConveyorAgent kitConveyorAgent) {
		this.kitConveyorAgent = kitConveyorAgent;
	}

//	public PartsRobotProtocol getPartsPro() {
//		return partsPro;
//	}
//
//	public void setPartsPro(PartsRobotProtocol partsPro) {
//		this.partsPro = partsPro;
//	}

	public PartsRobot getPartsRobot() {
		return partsRobot;
	}

	public void setPartsRobot(PartsRobot partsRobot) {
		this.partsRobot = partsRobot;
	}

	public PartsRobotAgent getPartsRobotAgent() {
		return partsRobotAgent;
	}

	public void setPartsRobotAgent(PartsRobotAgent partsRobotAgent) {
		this.partsRobotAgent = partsRobotAgent;
	}


	public NestAgent getNestAgent(int index)
	{
		return nests.get(index);
	}
		
	public ArrayList<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(ArrayList<Lane> lanes) {
		this.lanes = lanes;
	}
	public ArrayList<VisionAgent> getVisions(){
		return this.visions;
	}

	public ArrayList<Feeder> getFeeders() {
		return feeders;
	}

	public void setFeeders(ArrayList<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public ArrayList<Nest> getNests() {
		return nestList;
	}
	
	public void setNests(ArrayList<Nest> nests) {
		this.nestList = nests;
	}
	
	public GantryManager getGantryManager()
	{
		return gantryManager;
	}
	
	public void setGantryManager(GantryManager g)
	{
		gantryManager = g;
	}

	public ArrayList<PartInfo> getPartsList() {
		return partsList;
	}

	public void setPartsList(ArrayList<PartInfo> partsList) {
		this.partsList = partsList;
	}

	public ArrayList<KitInfo> getKitsList() {
		return kitsList;
	}

	public void setKitsList(ArrayList<KitInfo> kitsList) {
		this.kitsList = kitsList;
	}

	public ArrayList<Job> getJobsList() {
		return jobsList;
	}

	public void setJobsList(ArrayList<Job> jobsList) {
		this.jobsList = jobsList;
	}

	public String getKitCreateCommand() {
		return kitCreateCommand;
	}

	public void setKitCreateCommand(String kitCreateCommand) {
		this.kitCreateCommand = kitCreateCommand;
	}

	public String getProductionCommand() {
		return productionCommand;
	}

	public void setProductionCommand(String productionCommand) {
		this.productionCommand = productionCommand;
	}

	public String getPartsCommand() {
		return partsCommand;
	}

	public void setPartsCommand(String partsCommand) {
		this.partsCommand = partsCommand;
	}
	
	public ArrayList<String> getGantryWaitList()
	{
		return gantryWaitList;
	}
	
	public ArrayList<Integer> getGantryFeedList()
	{
		return gantryFeedList;
	}
	
	public void addGantryPart(PartInfo p)
	{
		gantryManager.addPartInfo(p);
	}
}
