package Agents.VisionAgent;

import java.util.*;

import server.Server;
import java.util.concurrent.*;

import Agent.*;
import Agents.KitRobotAgents.*;
import Agents.PartsRobotAgent.*;
import Interface.VisionAgent.Vision;
// import data.Part.PartType;
import data.Part;
import data.Kit;
import data.*;

public class VisionAgent extends Agent implements Vision {
	
	/** NOTES **/
	// upon integration, make sure the messages are all cool (msgHereIsSchematic, msgPartsApproved, msgBadParts, etc.

	/////////////////////////////////////////////////////////////
	/** DATA **/
	
	String name;
	List<NestAgent> nestsList= Collections.synchronizedList( new ArrayList<NestAgent>() ); // list of all the nests that we need parts from
	List<Part> neededPartsList = Collections.synchronizedList( new ArrayList<Part>() ); // list of all the parts that should be in each kit
	
	Map<Integer, NestAgent> fullNestsMap = Collections.synchronizedMap( new TreeMap<Integer, NestAgent>() ); // list of all the nests that are full and ready to have their picture taken
	List<Part.PartType> fullNestsPartsList = Collections.synchronizedList( new ArrayList<Part.PartType>() ); // parallel array that holds all parts that each nest is supposed to hold
	
	enum State {IDLE, SCHEMATIC_RECEIVED, READY_TO_TAKE_PICTURE, PICTURE_TAKEN};
	enum Type {NESTS_INSPECTOR, KIT_INSPECTOR};
	
	Semaphore flashpermit; //Hack because CS200 team's picture animation can only take 1 picture at a time. We realize that the fact
	//that all the visionagents share this semaphore violates the no-shared data rule but due to the limitations of the gui
	//this was the most elegant solution
	
	State state;
	Type type;
	
	Kit currentKit;

	Part part;
	KitRobotAgent kitRobotAgent;
	PartsRobotAgent partsRobotAgent;
	NestAgent nest1;
	NestAgent nest2;
	Server server;
	
	boolean approved;
	
	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public VisionAgent (String type, KitRobotAgent kra, PartsRobotAgent pra, Server server) {
		approved=false;
		
		kitRobotAgent = kra;
		partsRobotAgent = pra;
		this.server = server;
		
		initializeVisionAgent(type);
	}
	
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/
	
	public void msgHereIsSchematic(List<Part> partsList, List<NestAgent> nestsList)  {
		// receive a list of all the parts that a kit needs, and a list of all the nests
		this.nestsList = nestsList;
		this.neededPartsList = partsList;
		for(Part p : partsList){
			fullNestsPartsList.add(p.type);
		}
		approved = false;
		state = State.SCHEMATIC_RECEIVED;
		stateChanged();
		
	}
	
	// sent by NestAgent
	public void msgImFull(NestAgent nest) {
		// here i am assuming that we will name the nests with a number.
		// i can do this differently if we later decide not to do this, but this makes it easier for me to organize parts
		
		fullNestsMap.put(nest.getNumber(), nest);
		stateChanged();
	}
	
	// sent by KitStandAgent
	public void msgTakePicture(Kit k) {
		// completedKits.add(k);
		this.currentKit = k;
		state = State.READY_TO_TAKE_PICTURE;
		stateChanged();
	}
	
	//sent by GUI
	public void msgAnimationDone(){
		flashpermit.drainPermits();
		flashpermit.release();
	}
	
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS**/
	private void initializeVisionAgent(String type) {
		if (type=="kit") {
			this.type = Type.KIT_INSPECTOR;
			print ("initialized to kit inspecting vision agent");
		}
		else if (type=="nests") {
			this.type = Type.NESTS_INSPECTOR;
			print ("initialized to nest inspecting vision agent");
		}
	}
	
	private void takePicture() {
		
		try{
		flashpermit.acquire();
		}catch (InterruptedException e){
			print("error with flashpermit");
		}
		server.execute("Take Picture", nest2.index);

		print ("taking a picture at " + nest2.index);
		
		state = State.PICTURE_TAKEN;

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
	}
	
	private void inspectNests() {
		boolean nest1Approved=false;
		boolean nest2Approved=false;
		
		// nest1.getPartType should return the string of the name that the nest should hold
		if (nest1.getPartType() == fullNestsPartsList.get( nest1.getNumber()-1) ) {
			nest1Approved=true;
			print(nest1.getName() + " approved");
		}
		else {
			nest2Approved=false;
			nest1.msgBadParts();
			print(nest1.getName() + " not approved");
		}
				
		if (nest2.getPartType() == fullNestsPartsList.get(nest2.getNumber()-1) ) { 
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
				partsRobotAgent.msgPartsApproved(nest1.getNumber());
				partsRobotAgent.msgPartsApproved(nest2.getNumber());
			}
/*			else {
				// nestAgent.msgBadParts();
			}
*/
		}
		else if (type==Type.KIT_INSPECTOR) {
			kitRobotAgent.msgKitInspected(approved);
		}
		
		state=State.SCHEMATIC_RECEIVED;
		fullNestsMap.remove(nest1.getNumber());
		fullNestsMap.remove(nest2.getNumber());
		nest1 = null;
		nest2 = null;
		
		stateChanged();
		
	}
	
	
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/
	
	protected boolean pickAndExecuteAnAction() {
		if (state==State.IDLE){
			// do nothing
			return true;
		}
		
		if (state==State.SCHEMATIC_RECEIVED) {
			if (type==Type.NESTS_INSPECTOR) {
				checkForConsecutiveNests();
			}
			return true;
		}
		
		if (state==State.READY_TO_TAKE_PICTURE) {
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

	
}
