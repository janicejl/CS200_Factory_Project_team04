import java.util.*;
import java.util.concurrent.*;

import Agent.Agent;
import Agents.PartsRobotAgent.NestAgent;
import Agents.PartsRobotAgent.PartsRobotAgent;
import MoveableObjects.Kit;
import MoveableObjects.Part;




public class VisionAgent extends Agent {
	
	/** NOTES **/
	// synchronize lists
	// upon integration, make sure the messages are all cool (msgHereIsSchematic, msgPartsApproved, msgBadParts, etc.)
	// uncomment DoTakePicture() method
	// have a list of the kits that need to be checked - NO< BECAUSE WE WILL ONLY HAVE 2 MAx

	/////////////////////////////////////////////////////////////
	/** DATA **/
	
	String name;
	List<NestAgent> nestsList= Collections.synchronizedList( new ArrayList<NestAgent>() ); // list of all the nests that we need parts from
	List<Part> neededPartsList = Collections.synchronizedList( new ArrayList<Part>() ); // list of all the parts that should be in each kit
	
	Map<Integer, NestAgent> fullNestsMap = Collections.synchronizedMap( new TreeMap<Integer, NestAgent>() ); // list of all the nests that are full and ready to have their picture taken
	List<Part> fullNestsPartsList = Collections.synchronizedList( new ArrayList<Part>() ); // parallel array that holds all parts that each nest is supposed to hold
	
	enum State {IDLE, SCHEMATIC_RECEIVED, READY_TO_TAKE_PICTURE, PICTURE_TAKEN};
	enum Type {NESTS_INSPECTOR, KIT_INSPECTOR};
	
	State state;
	Type type;
	
	Kit kit;

	Part part;
	KitRobotAgent kitRobotAgent;
	PartsRobotAgent partsRobotAgent;
	NestAgent nest1;
	NestAgent nest2;
	
	boolean approved;
	
	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public VisionAgent (String type, KitRobotAgent kra, PartsRobotAgent pra) {
		approved=false;
		
		kitRobotAgent = kra;
		partsRobotAgent = pra;
		
		initializeVisionAgent(type);
	}
	
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/
	
	// sent by PartsRobotAgent
	public void msgHereIsSchematic(List<Part> partsList, List<NestAgent> nestsList) {
		// receive a list of all the parts that a kit needs, and a list of all the nests
		this.nestsList = nestsList;
		this.neededPartsList = partsList;
		approved = false;
		state = State.SCHEMATIC_RECEIVED;
		stateChanged();
		
	}
	
	// sent by NestAgent
	public void msgImFull(NestAgent nest) {
		// here i am assuming that we will name the nests with a number.
		// i can do this differently if we later decide not to do this, but this makes it easier for me to organize parts
		
		
		fullNestsMap.put(nest.getNumber(), nest);
	}
	
	// sent by KitStandAgent
	public void msgTakePicture(Kit k) {
		// completedKits.add(k);
		this.kit = k;
		state = State.READY_TO_TAKE_PICTURE;
		stateChanged();
	}
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS**/
	private void initializeVisionAgent(String type) {
		if (type=="kit") {
			this.type = Type.KIT_INSPECTOR;
		}
		else if (type=="nests") {
			this.type = Type.NESTS_INSPECTOR;
		}
	}
	
	private void takePicture() {
		DoTakePicture();
		state = state.PICTURE_TAKEN;
		stateChanged();
	}
	
	private void checkForConsecutiveNests() {
		for (int i=0; i<fullNestsMap.size(); i++) {
			if (fullNestsMap.containsKey(i) && fullNestsMap.containsKey(i+1) && i%2==0) {
				nest1 = fullNestsMap.get(i);
				nest2 = fullNestsMap.get(i+1);
				state = State.READY_TO_TAKE_PICTURE;
				stateChanged();
			}
		}
	}
	
	private void inspectKit() {
		// check that the kit has all the parts (this is in the partsList)
		for (Part p: neededPartsList) {
			if (kit.peekParts().contains(p)) {
				neededPartsList.remove(p);
			}
		}

		if (neededPartsList.size()==0)
			approved = true;
		else
			approved = false;
		
	}
	
	private void inspectNests() {
		boolean nest1Approved=false;
		boolean nest2Approved=false;
		
		// nest1.getPartType should return the string of the name that the nest should hold
		if (nest1.getPartType() == fullNestsPartsList.at( nest1.getNumber()).partType ) {
			nest1Approved=true;
		}
		else {
			nest2Approved=false;
			nest1.msgBadParts();
		}
				
		if (nest2.getPartType() == fullNestsPartsList.at( nest2.getNumber()).partType ) { 
			nest2Aproved=true;
		}
		else {
			nest2Approved=false;
			nest2.msgBadParts();
		}
			
		if (nest1Approved && nest2Approved) {
			approved=true;
		}
		else {
			approved=false;
		}
		
	}
	
	private void approveOrDenyParts() {
		if (type==Type.NESTS_INSPECTOR) {
			if (approved) {
				partsRobotAgent.msgPartsApproved(nest1);
				partsRobotAgent.msgPartsApproved(nest2);
			}
/*			else {
				// nestAgent.msgBadParts();
			}
*/
		}
		else if (type==Type.KIT_INSPECTOR) {
			kitRobotAgent.msgKitInspected(approved);
		}
		
		state=State.IDLE;
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

	
}
