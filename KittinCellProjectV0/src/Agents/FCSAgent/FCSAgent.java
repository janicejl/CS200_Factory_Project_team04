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
	
	// public List<Gantry> gantriesList = Collections.synchronizedList( new ArrayList<Gantry>() );
	// public List<PartInfo> kitRecipe = Collections.synchronizedList( new ArrayList<PartInfo>() );
	public Vector<Bin> binsList = new Vector<Bin>();
	public int numKitsNeeded;
	
	
	Server server; 

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(Server server, PartsRobot partsRobotAgent, KitRobot kitRobotAgent, GantryController gantryControllerAgent) {
		numKitsNeeded=0;
		this.server = server;
		this.partsRobot = partsRobotAgent;
		this.kitRobot = kitRobotAgent;
		this.gantryController = gantryControllerAgent;
		print("created");
	}
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/ // receive messages from the GUI control panel
	
	// receive a message telling what the bins are
	public void msgHereIsKitConfig(KitInfo kitInfo, int amount) {
		sendKitConfig(kitInfo, amount);
		numKitsNeeded=amount;
		print("kit config received. Build "+amount+" kits");
	}
	
	public void msgKitCompleted() {
		numKitsNeeded--;
		print("one kit completed");
	}
	//public void msgNewConfig
		
	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send kit configuration to parts robot and kit robot
	private void sendKitConfig(KitInfo info, int amount) {
		print("sending kit configuration data to "+partsRobot.getName()+", kitRobot, and gantry controller");
		for (PartInfo p: info.getParts()) {
			binsList.add(new Bin(p, amount));
		}
	
		gantryController.msgBinConfiguration(binsList);
		
		partsRobot.msgMakeThisKit(info, amount);
		kitRobot.msgGetKits(amount);
		
		
	}
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	// doesn't really do much because all the FCS does is send and receive messages as it gets them
	public boolean pickAndExecuteAnAction() {
	
		if (server.isRunning() && numKitsNeeded==0) {
			print("sending out to get a job");
			server.execute("Get Job");
			stateChanged();
			return true;
		}
		
		stateChanged();
		return false;
	}


	
	

}
