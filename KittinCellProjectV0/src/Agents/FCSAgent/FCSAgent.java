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
	int numKitsNeeded;
	
	
	Server server; 

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(Server server, PartsRobot partsRobotAgent, KitRobot kitRobotAgent, GantryController gantryControllerAgent) {
		numKitsNeeded=0;
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
		numKitsNeeded=amount;
	}
	
	public void msgKitCompleted() {
		numKitsNeeded--;
	}
		
	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send kit configuration to parts robot and kit robot
	private void sendKitConfig(KitInfo info, int amount) {
		partsRobot.msgMakeThisKit(info, amount);
		kitRobot.msgGetKits(amount);
		
		for (PartInfo p: info.getParts()) {
			binsList.add(new Bin(p, amount));
		}
	
		gantryController.msgBinConfiguration(binsList);
	}
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	// doesnt really need to do anything because all the FCS does is send and receive messages as it gets them
	protected boolean pickAndExecuteAnAction() {
		return false;
	}


	
	

}
