package Agents.FCSAgent;

import java.util.*;

import Agent.Agent;
import Agents.PartsRobotAgent.*;
import Agents.GantryFeederAgents.*;
import Agents.KitRobotAgents.*;
import MoveableObjects.*;


public class FCSAgent extends Agent {
	/////////////////////////////////////////////////////////////
	/** DATA **/
	int numKits;
	
	PartsRobotAgent partsRobotAgent;
	KitRobotAgent kitRobotAgent;
	GantryControllerAgent gantryControllerAgent;
	
	List<GantryAgent> gantriesList = Collections.synchronizedList( new ArrayList<GantryAgent>() );
	Vector<Bin> binsList = new Vector<Bin>();
	
	// kit recipe
	

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(PartsRobotAgent partsRobotAgent, KitRobotAgent kitRobotAgent, GantryControllerAgent gantryControllerAgent) {
		this.partsRobotAgent = partsRobotAgent;
		this.kitRobotAgent = kitRobotAgent;
		this.gantryControllerAgent = gantryControllerAgent;
		
		
	}
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/

	// receive messages from the GUI control panel

	// make X amount of kits
	public void msgMakeXKits(int numKits) {
		this.numKits = numKits;
	}
	
	// receive a message to make a certain Kit 
	public void msgMakeThisKit() {
		
	}
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send a message to the PartsRobotAgent telling it what kits to make
	public void makeAKit() {
		partsRobotAgent.msgMakeThisKit(kitrecipe, numKits);
	}
		
	// send a message to the GantryControllerAgent to give it the configurations
	public void giveConfigurationToGantryController() {
		gantryControllerAgent.msgBinConfiguration(binsList);
	}
	
	// send a message to the KitRobotAgent requesting kits
	public void getKitsFromKitRobotAgent() {
		kitRobotAgent.msgGetKits(numKits);
	}
	
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	protected boolean pickAndExecuteAnAction() {

		return false;
	}

	
	/////////////////////////////////////////////////////////////
	/** OTHER **/
	

}
