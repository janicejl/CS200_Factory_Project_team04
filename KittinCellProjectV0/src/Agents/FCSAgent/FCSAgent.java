package Agents.FCSAgent;

import java.util.*;

import server.Server;
import data.KitInfo;
import data.Part;
import data.PartInfo;
import Agent.Agent;


import Interface.FCSAgent.FCS;
import Interface.GantryFeederAgent.*;
import Interface.PartsRobotAgent.*;
import Interface.KitRobotAgent.*;
import MoveableObjects.*;


public class FCSAgent extends Agent implements FCS {
	/////////////////////////////////////////////////////////////
	/** DATA **/
	int numKits;

	PartsRobot partsRobot;
	KitRobot kitRobot;
	GantryController gantryController;
	
	List<Gantry> gantriesList = Collections.synchronizedList( new ArrayList<Gantry>() );
	List<PartInfo> kitRecipe;
	Vector<Bin> binsList = new Vector<Bin>();
	
	Server server; 

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(Server server, PartsRobot partsRobotAgent, KitRobot kitRobotAgent, GantryController gantryControllerAgent) {
		
		this.server = server;
		this.partsRobot = partsRobotAgent;
		this.kitRobot = kitRobotAgent;
		this.gantryController = gantryControllerAgent;
	}
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/ // receive messages from the GUI control panel
	
	// receive a message telling what the bins are
	public void msgHereIsKitConfig(KitInfo kitInfo, int amount) {
		sendKitConfig(kitInfo, amount);
	}
	
	// receive a message from GUI to add a new bin
	public void msgAddBin(Bin bin) {
		binsList.add(bin);
	}
	
/*	// receive a message to make X kits with what parts 
	public void msgStartKitProduction(List<PartInfo> kitRecipe, int numKits) {
		this.numKits = numKits;
		this.kitRecipe = kitRecipe;
		
		makeAKit(kitRecipe);
		giveConfigurationToGantryController();
		getKitsFromKitRobotAgent();
	}*/
	
	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send kit configuration to parts robot and kit robot
	private void sendKitConfig(KitInfo info, int amount) {
		partsRobot.msgMakeThisKit(info, amount);
		kitRobot.msgGetKits(amount);
	}
	
	
/*	
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
	*/
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	// doesnt really need to do anything because all the FCS does is send and receive messages as it gets them
	protected boolean pickAndExecuteAnAction() {
		return false;
	}


	
	

}
