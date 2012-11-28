package Agents.VisionAgent;

import java.util.*;

import server.Server;
import java.util.concurrent.*;

import Agent.*;
import Interface.VisionAgent.Vision;
import Interface.KitRobotAgent.*;
import Interface.PartsRobotAgent.*;
import data.*;

public class VisionAgent extends Agent implements Vision {
	
	/** NOTES **/
	// upon integration, make sure the messages are all cool (msgHereIsSchematic, msgPartsApproved, msgBadParts, etc.

	/////////////////////////////////////////////////////////////
	/** DATA **/
	
	String name;
	public List<Nest> nestsList= Collections.synchronizedList( new ArrayList<Nest>() ); // list of all the nests that we need parts from
	public List<Part> neededPartsList = Collections.synchronizedList( new ArrayList<Part>() ); // list of all the parts that should be in each kit
	
	public Map<Integer, Nest> fullNestsMap = Collections.synchronizedMap( new TreeMap<Integer, Nest>() ); // list of all the nests that are full and ready to have their picture taken
	public List<PartInfo> fullNestsPartsList = Collections.synchronizedList( new ArrayList<PartInfo>() ); // parallel array that holds all parts that each nest is supposed to hold
	
	public enum State {IDLE, SCHEMATIC_RECEIVED, READY_TO_TAKE_PICTURE, PICTURE_TAKEN};
	public enum Type {NESTS_INSPECTOR, KIT_INSPECTOR};
	
	Semaphore flashpermit;
	
	public State state;
	public Type type;
	
	Kit currentKit;

	Part part;
	//KitRobotAgent kitRobotAgent;
	KitRobot kitRobot;
	PartsRobot partsRobot; // no interface for this yet
	public Nest nest1; // no interface for this yet
	public Nest nest2;
	Server server;
	
	public boolean approved;
	public boolean waiting;
	
	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public VisionAgent (String type, KitRobot kr, PartsRobot pr, Server server) {
		approved = false;
		waiting = false;
		
		kitRobot = kr;
		partsRobot = pr;
		this.server = server;
		
		initializeVisionAgent(type);
	}
	
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/
	
	// sent by kit robot
	public void msgHereIsSchematic(List<Part> partsList, List<Nest> nestsList)  {
		// receive a list of all the parts that a kit needs, and a list of all the nests
		this.nestsList = nestsList;
		this.neededPartsList = partsList;
		if(partsList.size()>0){
			for(Part p : partsList){
				fullNestsPartsList.add(p.info);
			}
		}
		approved = false;
		state = State.SCHEMATIC_RECEIVED;
		print("schematic received from PartsRobot");
		stateChanged();
		
	}
	
	// sent by NestAgent
	public void msgImFull(Nest nest) {
		fullNestsMap.put(nest.getNumber(), nest);
		print("received message msgImFull from nest "+ nest.getName());
		 stateChanged();
	}
	
	// sent by KitStandAgent
	public void msgTakePicture(Kit k) {
		// completedKits.add(k);
		this.currentKit = k;
		state = State.READY_TO_TAKE_PICTURE;
		print("received message to take picture of kit from kitStandAgent ");
		stateChanged();
	}
	
	// sent by GUI
	public void msgCameraBusy() {
		waiting = true;
		print("received message that the camera is busy from the GUI");
		stateChanged();
	}
	
	//sent by GUI
	public void msgCameraAvailable(){
		waiting = false;
		flashpermit.drainPermits();
		flashpermit.release();
		print("received message that the camera is available again from the GUI");
		stateChanged();
	}	
	
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS**/
	private void initializeVisionAgent(String type) {
		state = State.IDLE;
		
		if (type=="kit") {
			this.type = Type.KIT_INSPECTOR;
			name = "Kit Vision Agent";

			print ("initialized to kit inspecting vision agent");
		}
		else if (type=="nests") {
			this.type = Type.NESTS_INSPECTOR;
			name = "Nest Vision Agent";

			print ("initialized to nest inspecting vision agent");
		}
	}
	
	private void takePicture() {
		
		//try{
		//flashpermit.acquire();
		//}catch (InterruptedException e){
		//	print("error with flashpermit");
		//}
		print("BOOOM");
		if (type==Type.NESTS_INSPECTOR) {
			server.execute("Take Picture", nest2.getIndex());
			print ("taking a picture at " + nest2.getIndex());
		}
		if (type==Type.KIT_INSPECTOR) {
			server.execute("Take Picture");
			print ("taking a picture at kit stand");
		}
		
		state = State.PICTURE_TAKEN;
		print("Picture Taken");
		stateChanged();
	}	
	
	private void checkForConsecutiveNests() {
		
		for (int i=1; i<9; i++) {
			if (fullNestsMap.containsKey(i) && fullNestsMap.containsKey(i+1) && i%2==1) {
				nest1 = fullNestsMap.get(i);
				nest2 = fullNestsMap.get(i+1);
			
				state = State.READY_TO_TAKE_PICTURE;
				print( "consecutive nests found; ready to take picture" );
				stateChanged();

			}
		}
	}
	
	private void inspectKit() {
		// check that the kit has all the parts (this is in the partsList)
		print("inspecting kit");
		for (Part p: neededPartsList) {
			if (currentKit.peekParts().contains(p)) {
				neededPartsList.remove(p);
			}
		}

		if (neededPartsList.size()==0) {
			print("kit approved");
			approved = true;

		}
		else {
			print("kit not approved");
			approved = false;
		}	
		approveOrDenyParts();
	}
	
	private void inspectNests() {
		boolean nest1Approved=false;
		boolean nest2Approved=false;
		
		// nest1.getPartType should return the string of the name that the nest should hold
		if (nest1.getPartInfo() == fullNestsPartsList.get( nest1.getNumber()-1) ) {
			nest1Approved=true;
			print(nest1.getName() + " approved");
		}
		else {
			nest2Approved=false;
			nest1.msgBadParts();
			print(nest1.getName() + " not approved");
		}
				
		if (nest2.getPartInfo() == fullNestsPartsList.get(nest2.getNumber()-1) ) { 
			nest2Approved=true;
			print(nest2.getName() + " approved");
		}
		else {
			nest2Approved=false;
			nest2.msgBadParts();
			print(nest2.getName() + " not approved");
		}
			
		if (nest1Approved && nest2Approved) {
			approved=true;
			print("consecutive nests approved");
		}
		else {
			approved=false;
			print("consecutive not approved");
		}
		approveOrDenyParts();
		
	}
	
	private void approveOrDenyParts() {
		if (type==Type.NESTS_INSPECTOR) {
			if (approved) {
				partsRobot.msgPartsApproved(nest1.getNumber());
				partsRobot.msgPartsApproved(nest2.getNumber());
			}
/*			else {
				// nestAgent.msgBadParts();
			}
*/
			fullNestsMap.remove(nest1.getNumber());
			fullNestsMap.remove(nest2.getNumber());
			nest1 = null;
			nest2 = null;
		}
		else if(type == Type.KIT_INSPECTOR)
		{
			kitRobot.msgKitInspected(true);
		}
		
		state=State.SCHEMATIC_RECEIVED;
		
		stateChanged();
		
	}
	
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/
	
	public boolean pickAndExecuteAnAction() {
	
		print("running");
	
		if (state==State.IDLE){
			// do nothing
			return false;
		}
		
		if (state==State.SCHEMATIC_RECEIVED) {
//			print("Running");
			

			approved = false;
			if (type==Type.NESTS_INSPECTOR) {
				checkForConsecutiveNests();
			}
		}
		
		if (state==State.READY_TO_TAKE_PICTURE && waiting == false) {
			takePicture();
			return true;
		}
		
		if (state==State.PICTURE_TAKEN) {
			if (type==Type.KIT_INSPECTOR) {
				inspectKit();
			}
			else if (type==Type.NESTS_INSPECTOR) {
				inspectNests();
			}
			return true;
		}
		return false;
	}

	
	/////////////////////////////////////////////////////////////
	/** OTHER **/
	public void setFlashPermit(Semaphore flashpermit){
		this.flashpermit = flashpermit;
	}

	public String getName(){
		return name;
	}

	
}
