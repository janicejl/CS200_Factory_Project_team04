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
import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Agents.PartsRobotAgent.LaneAgent;
import Agents.PartsRobotAgent.NestAgent;
import Agents.PartsRobotAgent.PartsRobotAgent;
import Agents.VisionAgent.VisionAgent;
import Agents.FCSAgent.FCSAgent;
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
	
	GUIServer serverTest;
	Integer phase; //phase to determine which panel to display (old)
	
	
	String clientType; //type of client to connect to
	
	ServerSocket ss; //serversocket
	Socket s; //socket reference to connect to each manager

	ArrayList<PartInfo> partsList; //available parts to make
	ArrayList<KitInfo> kitsList; //available kit configurations
	ArrayList<Job> jobsList; //current jobs
	String kitCreateCommand = "Idle"; //Kit Creation Client Commands
	String productionCommand = "Idle"; //Production Client Commands
	String partsCommand = "Idle"; //Parts Creation Client Commands
	
	//200 Hardware
	Vector<Feeder> feeders; //Feeders
	Vector<Lane> lanes; //Lanes
	CopyOnWriteArrayList<Nest> nestList; //Nests
	Vector<Boolean> overFlow; //nests that are overflowed
	TreeMap<Integer, PartInfo> feederPartInfo; //Map the partinfo that the feeder should make
	TreeMap<Integer, Integer> feederPartNum; //Map the amount of parts to make
	TreeMap<Integer, Integer> feederDelay; //Map to delay feeder dumping speed
	Vector<Integer> laneCounter; //counter for delaying lane release
	TreeMap<Integer, Integer> laneQueue; //counter for holding feed lane commands
	TreeMap<Integer, Integer> feederPartCount; //count how many parts have been fed in feeder
	boolean feederSlow = false;
	
	GantryManager gantryManager; //Gantry Manager
	Integer gantryDelay; //counter to delay message sent
	//Gantry queued commands
	Vector<String> gantryWaitList; //command 
	Vector<Integer> gantryFeedList; //cooresponding feeder for the command
	
	KitAssemblyManager kitAssemblyManager; //kit assembly manager
	KitRobot kitRobot; //kit assembly robot
	PartsRobot partsRobot; //parts robot
	
	//201 Agents
	FCSAgent FCSAgent; //Factory Control System
	PartsRobotAgent partsRobotAgent; // Parts Robot Agent
	ArrayList<LaneAgent> laneagents = new ArrayList<LaneAgent>(); //Lanes
	ArrayList<NestAgent> nests = new ArrayList<NestAgent>(); //Nests
	//Vision Agents
	VisionAgent nestvisionagent1;
	VisionAgent nestvisionagent2;
	VisionAgent nestvisionagent3;
	VisionAgent nestvisionagent4;
	VisionAgent kitvisionagent;
	ArrayList<VisionAgent> visions = new ArrayList<VisionAgent>();
	Semaphore flashpermit;
	
	//Kit Assembly Agents
	KitRobotAgent kitRobotAgent; 
	KitStandAgent kitStandAgent;
	KitConveyorAgent kitConveyorAgent;

	//Gantry and Feeder Agents
	FeederAgent feeder1;
	FeederAgent feeder2;
	FeederAgent feeder3;
	FeederAgent feeder4;
	GantryAgent gantry1;
	//GantryControllerAgent gantryController;

	boolean running; //state to see if production has started
	
	Timer timer; //timer for server
	Thread thread; //thread for the server
	
	//Constructor
	public Server(){
		serverTest = new GUIServer(this);

		//Console output to server gui
		OutputStream outConsole = new OutputStream() { // DO NOT REMOVE THIS BLOCK!!!
			@Override
			public void write(int b) throws IOException {
				serverTest.updateTextPane( String.valueOf((char) b));
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				serverTest.updateTextPane( new String(b, off, len));
			}
			@Override
			public void write(byte[] b) throws IOException {
			write(b, 0, b.length);
			}
		};
			 
		System.setOut(new PrintStream(outConsole, true));
		System.setErr(new PrintStream(outConsole, true));
		//end code for console output
		
		//setup layout
		setLayout(new GridBagLayout());
		phase = new Integer(0);
		running = false; //set running state to false
		
		//200 Hardware Setup
		
		//Feeders
		feeders = new Vector<Feeder>();
		feederPartInfo = new TreeMap<Integer, PartInfo>();
		feederPartNum = new TreeMap<Integer, Integer>();
		feederDelay = new TreeMap<Integer, Integer>();
		feederPartCount = new TreeMap<Integer, Integer>();
		for(int i = 0; i < 4; i++){
			if(i == 0 || i == 3){
    			feeders.add(new Feeder(475,30 + i*140));
    		}
    		else {
    			feeders.add(new Feeder(400,30 + i*140));    			
    		}
			
			feederPartInfo.put(i, null);
			feederPartNum.put(i, 0);
			feederDelay.put(i, 0);
			feederPartCount.put(i, 0);
		}
		
		
		
		//Nests
		nestList = new CopyOnWriteArrayList<Nest>();
    	nestList.add(new Nest(0, 30));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 100));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 170));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 240));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 310));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 380));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 450));	//x coordinate is zero for laneManagerApp
    	nestList.add(new Nest(0, 520));	//x coordinate is zero for laneManagerApp
    	overFlow = new Vector<Boolean>();
    	for(int i = 0; i < 8; i++){
    		overFlow.add(new Boolean(false));
    	}
    	
    	//Lanes
		lanes = new Vector<Lane>();
		lanes.add(new Lane(600,30, nestList.get(0), feeders.get(0))); //MUST SPACE EACH LANE BY 100 PIXELS OR ELSE!
    	lanes.add(new Lane(600,100, nestList.get(1), feeders.get(0))); 
    	lanes.add(new Lane(600,170, nestList.get(2),feeders.get(1))); 
    	lanes.add(new Lane(600,240, nestList.get(3), feeders.get(1)));
    	lanes.add(new Lane(600,310, nestList.get(4), feeders.get(2))); 
    	lanes.add(new Lane(600,380, nestList.get(5), feeders.get(2)));
    	lanes.add(new Lane(600,450, nestList.get(6), feeders.get(3))); 
    	lanes.add(new Lane(600,520, nestList.get(7), feeders.get(3)));
    	laneCounter = new Vector<Integer>();
    	laneQueue = new TreeMap<Integer, Integer>();
    	//Set Lane Speeds
    	for (int i = 0; i < 8; i ++) {
    		lanes.get(i).setConveyerBeltSpeed(2);
    		laneCounter.add(0);
    		laneQueue.put(i, 0);
    	}
    	
    	//Gantry
		gantryManager = new GantryManager(feeders);
		gantryManager.getGantry().setState("free");
		gantryManager.getGantry().setBox(1);
		gantryFeedList = new Vector<Integer>();
		gantryWaitList = new Vector<String>();
		gantryDelay = new Integer(0);
			
    	//Kit Assembly and Robots
    	kitAssemblyManager = new KitAssemblyManager(nestList);
		kitRobot = new KitRobot(kitAssemblyManager);
		partsRobot = new PartsRobot(kitAssemblyManager);
        new Thread(partsRobot).start();
		new Thread(kitAssemblyManager).start();
        new Thread(kitRobot).start();
		

		
    	//201 Agents Setup
    	
        //Gantry Controller and Agent
		gantry1 = new GantryAgent("gantry1", this);
		
		
		//Nest Agents
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
    	
    	//Lane Agents
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
    	
    	//Feeders
    	feeder1 = new FeederAgent("feeder1", lane1, lane2, 0, this);		
		feeder2 = new FeederAgent("feeder2", lane3, lane4, 1, this);		
		feeder3 = new FeederAgent("feeder3", lane5, lane6, 2, this);		
		feeder4 = new FeederAgent("feeder4", lane7, lane8, 3, this);
		feeder1.SetGantry(gantry1);
		feeder2.SetGantry(gantry1);
		feeder3.SetGantry(gantry1);
		feeder4.SetGantry(gantry1);
		lane1.setFeeder(feeder1);
		lane2.setFeeder(feeder1);
		lane3.setFeeder(feeder2);
		lane4.setFeeder(feeder2);
		lane5.setFeeder(feeder3);
		lane6.setFeeder(feeder3);
		lane7.setFeeder(feeder4);
		lane8.setFeeder(feeder4);

    	//KitAssembly Agents
		kitRobotAgent = new KitRobotAgent(this);
		kitStandAgent = new KitStandAgent(this); 
		kitConveyorAgent = new KitConveyorAgent(this);
		kitStandAgent.SetRobotAgent(kitRobotAgent);
		kitRobotAgent.SetConveyorAgent(kitConveyorAgent);
		kitRobotAgent.SetStandAgent(kitStandAgent);
		kitConveyorAgent.SetKitRobot(kitRobotAgent);
		
		//Parts Robot Agent
        partsRobotAgent = new PartsRobotAgent(nests, kitStandAgent, this);
        kitStandAgent.SetPartsRobotAgent(partsRobotAgent);
		
        //Vision Agents
        nestvisionagent1 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent2 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent3 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        nestvisionagent4 = new VisionAgent("nests",kitRobotAgent,partsRobotAgent,this);
        kitvisionagent = new VisionAgent("kit",kitRobotAgent,partsRobotAgent,this);
        //vision connections
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
        kitStandAgent.SetVisionAgent(kitvisionagent);
        //limit camera use to only one flash at a time
        flashpermit = new Semaphore(1);
        nestvisionagent1.setFlashPermit(flashpermit);
        nestvisionagent2.setFlashPermit(flashpermit);
        nestvisionagent3.setFlashPermit(flashpermit);
        nestvisionagent4.setFlashPermit(flashpermit);
        
        //Factory Control System Agent
        FCSAgent = new FCSAgent(this, partsRobotAgent, kitRobotAgent);
       // gantryController.setFCS(FCSAgent);
        
        
        
        //Start Agent Threads
        
        //Nest Threads
        for(NestAgent nest: nests){
        	nest.setPartsRobotAgent(partsRobotAgent);
        	nest.startThread();
        }
        
        //Lane Threads
        for(LaneAgent lane: laneagents){
        	lane.startThread();
        }
        
        //Nest Vision Agent Threads
        nestvisionagent1.startThread();
        nestvisionagent2.startThread();
        nestvisionagent3.startThread();
        nestvisionagent4.startThread();
        kitvisionagent.startThread();
        
        //Parts Robot Thread
		partsRobotAgent.startThread();
		
		//Kit Assembly Threads
		kitRobotAgent.startThread();
		kitStandAgent.startThread();
		kitConveyorAgent.startThread();
        
		//Gantry and Feeder Threads
        
        gantry1.startThread();
        feeder1.startThread();
        feeder2.startThread();
        feeder3.startThread();
        feeder4.startThread();
        
        //FCS Agent Thread
        FCSAgent.startThread();
        
        
        //Server Threads
        
		//setup thread and timer
		thread = new Thread(this, "ServerThread");
		timer = new Timer(10, this);
		//start timer
		timer.start();
		this.setSize(800, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	//Start serversocket
	public Integer start(){
		removeCenter();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;	
		c.gridy = 0;
		add(serverTest,c);
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
	
	//Run function that constantly waits for clients to connect
	public void run(){
		
		while(true){
			try {
				s = ss.accept();
				new Protocols(s, this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	//Server Execute Functions (DoXXX API's)
	public void execute(String process){
		
		//Kit Robot Commands
		
		//load empty kit into stand 1
    	if(process.equals("Load Stand 1")){
    		getKitRobot().addCommand("load,0,1");
    	}
    	//load empty kit into stand 2
    	else if(process.equals("Load Stand 2")){
    		getKitRobot().addCommand("load,0,2");
    	}
    	else if(process.equals("Redo 1")){
    		getKitRobot().addCommand("load,5,1");
    	}
    	else if(process.equals("Redo 2")){
    		getKitRobot().addCommand("load,5,2");
    	}
    	//move kit in stand 1 to inspection
    	else if(process.equals("Check Kit 1")){
    		getKitRobot().addCommand("load,1,5");
    	}
    	//move kit in stand 2 to inspection
    	else if(process.equals("Check Kit 2")){
    		getKitRobot().addCommand("load,2,5");
    	}
    	//move kit in inspection to finished conveyer
    	else if(process.equals("Remove Finished")){
    		getKitRobot().addCommand("load,5,6");
    	}
    	//moving bad kit to bad conveyor
    	else if(process.equals("BadOut")){
    		getKitRobot().addCommand("load,5,3");
    	}
    	//moving bad kit back using incomplete conveyor. 
    	else if(process.equals("BadIn 1")){
    		getKitRobot().addCommand("load,4,1");
    	}
    	else if(process.equals("BadIn 2")){
    		getKitRobot().addCommand("load,4,2");
    	}
    	
    	//Parts Robot Commands (Partial)
    	
    	//Place all parts in gripper to kit 1
    	else if(process.equals("Load Kit 1")){
    		getPartsRobot().addCommand("dump,0");
    	}
    	//Place all parts in gripper to kit 2
    	else if(process.equals("Load Kit 2")){
    		getPartsRobot().addCommand("dump,1");
    	}     	
    	//Take picture for kit inspection stand
    	else if (process.equals("Take Picture")) {
    		partsRobot.getKitStandCamera().takePicture();
    	}
    	
    	//Gantry Command (Just for Partsbox Pickup)
    	else if(process.equals("Pickup Box"))
    	{
    		if(gantryManager.getGantry().getState().equals("free"))
    		{
    			gantryManager.getGantry().setState("load");
    		}
       		else
    		{
    			gantryWaitList.add("load");
    			gantryFeedList.add(-1);
    		}
    	}
    	
    	//FCS Commands
    	
    	//Kit Complete, adjust server joblist
    	else if(process.equals("Kit Finished")){
    		if(jobsList.size() != 0){
    			if(jobsList.get(0).getAmount() > 1){
	    	    	 jobsList.get(0).setAmount(jobsList.get(0).getAmount() - 1);
	    	    	 setProductionCommand("Update Jobs");
	    	     }
	    	     else{
	    	    	 jobsList.remove(0);
	    	    	 setProductionCommand("Job Finished");
	    	     }
	    	     getFCSAgent().msgKitCompleted();
   	     	}
	   	}
    	//Give FCS Agent next job
	   	else if(process.equals("Get Job")){
	   		if(jobsList.size() != 0){
	   			getFCSAgent().msgHereIsKitConfig(jobsList.get(0).getKit(), jobsList.get(0).getAmount());
	   		}
	   		else{
	   			running = false;
	   		}
	   	}
    	
    	System.err.println(process);
    }
	//Overloaded execute functions
	public void execute(String process, Integer feederNum, PartInfo info, Integer partNum){
		//Dump part in feeder (one part)
		if(process.equals("Feed Feeder")){
			//add dump parts to feeder queue
    		feederPartInfo.put(feederNum, info);
    		feederPartNum.put(feederNum, partNum);
    	}
		System.err.println(process + feederNum);
	}
    public void execute(String process, Integer num){
    	//Spawn an empty kit for the kit conveyer
    	if(process.equals("Spawn Kit")){
    		for(int i = 0; i < num; i++){
    			getKitAssemblyManager().processCommand("spawn");
    		}
    	}
    	//Remove parts from feeder
    	if(process.equals("Purge Feeder"))
    	{
    		for (int i=0; i<gantryManager.getPurged().size(); i++)
    		{
    			if (gantryManager.getPurged().get(i).getFeeder() == num)
    			{
    				gantryManager.getPurged().get(i).setEmpty(false);
    				break;
    			}
    		}
    	}
    	//Purge the nest corresponding to num
    	else if(process.equals("Purge Nest"))
    	{
    		nestList.get(num).purgeNest();
    		lanes.get(num).purge();
    	}
    	//Take picture for specified nest number
    	else if(process.equals("Take Picture")){
    		partsRobot.getNestCamera().takePicture(320, 40 + 140*((num-1)/2));
    	}
    	else if(process.equals("Feed Feeder")){
    		Part temp = new Part(new PartInfo("" + num, "images/kt" + num + ".png"));
    		temp.setImagePath("images/kt" + temp.getId() + ".png");
    		
    		System.out.println("TOPLANE: " + feeders.get(num/2).getTopLane());
    		feeders.get(num/2).addParts(temp);
    	}
    	//Release one part from lane
    	else if(process.equals("Feed Lane")){
    		feeders.get(num/2).setMoving(true); //diverter state is moving so part isn't immediately released
    		//Determine what state the feeder's diverter should be in
    		if (num % 2 == 0) {
    			feeders.get(num/2).setTopLane(true);
    		} else {
    			feeders.get(num/2).setTopLane(false);
    		}
    		//add part to lane if feeder has parts, else this function improperly called
    		if(feederSlow == true){
    			if(num%2 == 0){ //if top lane
    				if(feeders.get(num/2).isPreviousPosition() == false){ //if diverter on bottom lane 
    					lanes.get(num+1).releasePart();
    				}
    				else{
    					if (feeders.get(num/2).getPartAmount() > 0){
			    			lanes.get(num).setRelease(true);
			    			lanes.get(num).setReleaseCount(lanes.get(num).getReleaseCount() + 1);
			    		}
    				}
    			}
    			else{ //if bottom lane
    				if(feeders.get(num/2).isPreviousPosition() == true){ //if diverter on top lane
    					lanes.get(num-1).releasePart();
    				}
    				else{
    					if (feeders.get(num/2).getPartAmount() > 0){
			    			lanes.get(num).setRelease(true);
			    			lanes.get(num).setReleaseCount(lanes.get(num).getReleaseCount() + 1);
			    		}
    				}
    			}
    		}
    		else{
	    		if (feeders.get(num/2).getPartAmount() > 0){
	    			lanes.get(num).setRelease(true);
	    			lanes.get(num).setReleaseCount(lanes.get(num).getReleaseCount() + 1);
	    		}
    		}
    		feederPartCount.put(num/2, feederPartCount.get(num/2) - 1);
    	}
    	else if(process.equals("Try Feed Lane")){
    		//if feederagent calls too quickly
    		laneQueue.put(num, laneQueue.get(num) + 1);
    	}
    	//Move part from lane queue to nest
    	else if(process.equals("Feed Nest")){
    		lanes.get(num).releaseQueue();
    	}
    	//Move parts box to designated feeder
    	else if(process.equals("Load Feeder"))
    	{
    		if(gantryManager.getGantry().getState().equals("free"))
    		{
    			gantryManager.getGantry().setState("loading");
    			gantryManager.getGantry().setFeed(num);
    		}
    		//command queue
       		else
    		{
    			gantryWaitList.add("loading");
    			gantryFeedList.add(num);
    		}
    	}
    	//Move bin to purge state
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
    	//Move purged bin from factory
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
    	System.err.println(process + num);
    }
    public void execute(String process, Integer nest, Integer grip){
    	//Tell parts robot to grab part at designated nest with designated gripper
    	if(process.equals("Get Part")){
    		getPartsRobot().addCommand("grab," + (nest) + "," + (grip) + "," + nest);
    	} 		
    	//Lane Vibration
    	else if(process.equals("Vibrate")){
    		lanes.get(nest).vibrateLane(grip);
    		//nest = lane #
    		//grip = vibration amplitude
    	}
    	System.err.println(process);
    }
    public void execute(String process,PartInfo p)
    {
    	//spawn new parts box with designated partinfo
    	if(process.equals("Make PartsBox"))
    		gantryManager.addPartInfo(p);
    	System.err.println(process);
    }
    
    //Add a new part to gantry manager
  	public void addGantryPart(PartInfo p)
  	{
  		gantryManager.addPartInfo(p);
  	}
    
	public void actionPerformed(ActionEvent e){
		
		int c =0;
		while(c<nestList.size())
		{
			if(nestList.get(c).getPurged()==true)
			{
				nestList.get(c).flip();
				if(nestList.get(c).getPurgedCount()<100 || lanes.get(c).isPurging())
				{
					nestList.get(c).setPurgedCount(nestList.get(c).getPurgedCount()+1);
				}
				else
				{
					nestList.get(c).clear();
					nestList.get(c).setPurged(false);
					nestList.get(c).setPurgedCount(0);
				}
	
			}
			c++;
		}
		if(gantryManager.getGantry().getState().equals("free") && gantryWaitList.size()!=0)
		{
			//update gantry robot queue commands
			gantryManager.getGantry().setState(gantryWaitList.get(0));
			gantryManager.getGantry().setFeed(gantryFeedList.get(0));
			gantryWaitList.remove(0);
			gantryFeedList.remove(0);
		}
		//Check animation states to see if message should be sent
		
		//Empty Kit Ready for Pickup
		if(getKitAssemblyManager().getMsg().equals(true)){
			getKitAssemblyManager().setMsg(false);
			getKitConveyorAgent().msgKitHasArrived();
		}
		//Kit has been placed on stand by kit robot
		if(getKitAssemblyManager().isKitStandMsg()){
			getKitAssemblyManager().setKitStandMsg(false);
			getKitStandAgent().msgKitAnimationOnStand();
		}
		//Parts Robot has finished moving
		if(getPartsRobot().getMsg().equals(true)){
			getPartsRobotAgent().msgAnimationDone();
			getPartsRobot().setMsg(false);
		}
		//Parts Robot has finished moving
		if(getPartsRobot().isMovingMsg()){
			getPartsRobotAgent().msgMovementDone();
			getPartsRobot().setMovingMsg(false);
		}
		//Parts Robot has finished putting parts in kit
		if(getPartsRobot().isDumped()){
			getPartsRobotAgent().msgPartsDropped();
			getPartsRobot().setDumped(false);
		}
		//Camera has finished flashing
		if(getPartsRobot().getNestCamera().getAnimationDone()){
			for(int i = 0; i<4; i++){
				getVisions().get(i).msgCameraAvailable();
			}
			getPartsRobot().getNestCamera().setAnimationDone(false);
		}
		
		//Kit Robot Placed Kit on Inspection
		if(getKitAssemblyManager().isInspected()){
			getKitRobotAgent().msgKitAtInspection();
			getKitAssemblyManager().setInspected(false);
		}
		//Gantry has picked up parts box from station
		if(getGantryManager().isStationMsg())
		{
			getGantryManager().setStationMsg(false);
			gantry1.msgReadyForParts();
			gantryDelay =0;
		}
		//Gantry has placed parts box on feeder
		if(getGantryManager().isFeederMsg())
		{
			getGantryManager().setFeederMsg(false);
			gantry1.msgGantryAtFeeder();
			gantryDelay = 0;
		}
		
		//Feeder dump parts
		for(int i = 0; i < 4; i++){
			if(feederPartInfo.get(i) != null){ //if there is a part to create
				if(feederPartNum.get(i) != 0){ //if there is more than 0 parts to create
					if(feederDelay.get(i) < 20){ //if delayed
						feederDelay.put(i, feederDelay.get(i)+1);
					}
					else{ //if not delayed
						Part temp = new Part(feederPartInfo.get(i)); //create part
						feeders.get(i).addParts(temp); //add part to feeder
						feederPartNum.put(i, feederPartNum.get(i)-1); //decrement count
						feederDelay.put(i, 0); //reset delay
						feederPartCount.put(i, feederPartCount.get(i)+1); //increment feederCount
					}
				}
				else{ //no more parts to create
					feederPartInfo.put(i, null); //remove partinfo
					switch(i){
					case 0:
						gantry1.msgNeedBinPurged(feeder1); //tell gantry parts dumped
						break;
					case 1:
						gantry1.msgNeedBinPurged(feeder2); //tell gantry parts dumped
						break;
					case 2:
						gantry1.msgNeedBinPurged(feeder3); //tell gantry parts dumped
						break;
					case 3:
						gantry1.msgNeedBinPurged(feeder4); //tell gantry parts dumped
						break;
					}
					
				}
			}
		}
		
		//Lane's Action Performed
		for(int i = 0; i < lanes.size(); i++){
			lanes.get(i).actionPerformed(e);
			
			//lane has parts queued
			if(lanes.get(i).isAtQueue()){
				laneagents.get(i).msgPartAtEndOfLane();
			}
			
			//if lane is jammed
			if(lanes.get(i).getJamMessage() == true){
				lanes.get(i).setJamMessage(false);
				laneagents.get(i).msgLaneJammed();
			}
			//if lane unjammed
			if(lanes.get(i).isUnjamMessage() == true){
				lanes.get(i).setUnjamMessage(false);
				laneagents.get(i).msgLaneUnJammed();
			}
			
			
			//deal with queued commands
			if(laneQueue.get(i) > 0){
				if(feederPartCount.get(i/2) > 0){
					execute("Feed Lane", i);
					laneQueue.put(i, laneQueue.get(i)-1);
				}
			}
			
			//Release part into lane
			//check feeder movement, prevent movement release if diverter still moving
			if(lanes.get(i).isRelease()){
				if(!lanes.get(i).getFeeder().isMoving()){
					if(laneCounter.get(i) < 20){
						laneCounter.set(i, laneCounter.get(i)+1);
					}
					else{
						laneCounter.set(i, 0);
						lanes.get(i).releasePart();
						lanes.get(i).setReleaseCount(lanes.get(i).getReleaseCount() - 1);
					}
				}
				//determine if it can stop releasing
				if(lanes.get(i).getReleaseCount() == 0){
					lanes.get(i).setRelease(false);
				}
			}
		}
		
		//Gantry Manager's Action Performed
		gantryManager.actionPerformed(e);
		repaint();
	}
	
	//Old GUI removal function
	public void removeCenter(){
//		if(phase.equals(0)){
//			remove(gui);
//		}
//		else if(phase.equals(1)){
//			remove(kitTest);
//		}
	}
	
	//Paint Function for Test Panels
	public void paint(Graphics g){
		if(phase.equals(1)){
			serverTest.repaint();
		}
		revalidate();
	}
	
	//Revalidate functions for certain IDE's that don't support it for JFrames
	public void revalidate(){
		serverTest.revalidate();
	}
	
	//Main Function
	public static void main(String[] args) {
		Server factory = new Server();
		factory.start();
	}

	//Getters and setters
	public synchronized KitAssemblyManager getKitAssemblyManager() {
		return kitAssemblyManager;
	}

	public void setKitAssemblyManager(KitAssemblyManager kitAssemblyManager) {
		this.kitAssemblyManager = kitAssemblyManager;
	}

	public synchronized KitRobot getKitRobot() {
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

	public synchronized PartsRobot getPartsRobot() {
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
		
	public synchronized Vector<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(Vector<Lane> lanes) {
		this.lanes = lanes;
	}
	public ArrayList<VisionAgent> getVisions(){
		return this.visions;
	}

	public synchronized Vector<Feeder> getFeeders() {
		return feeders;
	}

	public void setFeeders(Vector<Feeder> feeders) {
		this.feeders = feeders;
	}
	
	public synchronized CopyOnWriteArrayList<Nest> getNests() {
		return nestList;
	}
	
	public void setNests(CopyOnWriteArrayList<Nest> nests) {
		this.nestList = nests;
	}
	
	public synchronized GantryManager getGantryManager()
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
	
	public Vector<String> getGantryWaitList()
	{
		return gantryWaitList;
	}
	
	public Vector<Integer> getGantryFeedList()
	{
		return gantryFeedList;
	}
	
	
	public FCSAgent getFCSAgent() {
		return FCSAgent;
	}

	public void setFCSAgent(FCSAgent fCSAgent) {
		FCSAgent = fCSAgent;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Vector<Boolean> getOverFlow() {
		return overFlow;
	}

	public void setOverFlow(Vector<Boolean> overFlow) {
		this.overFlow = overFlow;
	}

	public boolean isFeederSlow() {
		return feederSlow;
	}

	public void setFeederSlow(boolean feederSlow) {
		this.feederSlow = feederSlow;
	}

}
