package Agents.FCSAgent;

import java.util.*;

import server.Server;
import data.Part;
import data.PartInfo;
import Agent.Agent;

import Agents.PartsRobotAgent.*;
import Agents.GantryFeederAgents.*;
import Agents.KitRobotAgents.*;
import Interface.FCSAgent.FCS;
import MoveableObjects.*;


public class FCSAgent extends Agent implements FCS {
	/////////////////////////////////////////////////////////////
	/** DATA **/
	int numKits;

	PartsRobotAgent partsRobotAgent;
	KitRobotAgent kitRobotAgent;
	GantryControllerAgent gantryControllerAgent;
	
	List<GantryAgent> gantriesList = Collections.synchronizedList( new ArrayList<GantryAgent>() );
	List<PartInfo> kitRecipe;
	Vector<Bin> binsList = new Vector<Bin>();
	
	Server server; // not sure how to use this yet

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(Server server, PartsRobotAgent partsRobotAgent, KitRobotAgent kitRobotAgent, GantryControllerAgent gantryControllerAgent) {
		
		this.server = server;
		this.partsRobotAgent = partsRobotAgent;
		this.kitRobotAgent = kitRobotAgent;
		this.gantryControllerAgent = gantryControllerAgent;
	}
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/ // receive messages from the GUI control panel
	
	// receive a message telling what the bins are
	public void msgHereAreBins(Vector<Bin> binsList) {
		this.binsList = binsList;
	}
	
	// receive a message to make X kits with what parts 
	public void msgStartKitProduction(List<PartInfo> kitRecipe, int numKits) {
		this.numKits = numKits;
		this.kitRecipe = kitRecipe;
		
		makeAKit(kitRecipe);
		giveConfigurationToGantryController();
		getKitsFromKitRobotAgent();
	}
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send a message to the PartsRobotAgent telling it what kits to make
	private void makeAKit(List<PartInfo> kitRecipe) {
		this.kitRecipe = kitRecipe;
		partsRobotAgent.msgMakeThisKit(kitRecipe, numKits);
	}
		
	// send a message to the GantryControllerAgent to give it the configurations
	private void giveConfigurationToGantryController() {
		gantryControllerAgent.msgBinConfiguration(binsList);
	}
	
	// send a message to the KitRobotAgent requesting kits
	private void getKitsFromKitRobotAgent() {
		kitRobotAgent.msgGetKits(numKits);
	}
	
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	// doesnt really need to do anything because all the FCS does is send and receive messages as it gets them
	protected boolean pickAndExecuteAnAction() {
		return false;
	}
	

}
