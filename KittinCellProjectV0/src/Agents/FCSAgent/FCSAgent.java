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
	
	public enum State {NEW_JOB_RECEIVED, WORKING, JOB_COMPLETED, IDLE};
	public State state;
	
	KitInfo currentKitInfo;
	
	public Vector<Bin> binsList = new Vector<Bin>();
	public int numKitsNeeded;
	
	Vector<Nest> nestsList = new Vector<Nest>();
	
	Server server; 

	/////////////////////////////////////////////////////////////
	/** CONSTRUCTOR **/
	
	public FCSAgent(Server server, PartsRobot partsRobotAgent, KitRobot kitRobotAgent) {
		state=State.IDLE;
		numKitsNeeded=0;
		this.server = server;
		this.partsRobot = partsRobotAgent;
		this.kitRobot = kitRobotAgent;
		print("created");
	}
	
	/////////////////////////////////////////////////////////////
	/** MESSAGES **/ // receive messages from the GUI control panel/ server
	
	// receive a message with all the kit configurations to be made
	public void msgHereIsKitConfig(KitInfo kitInfo, int amount) {
		currentKitInfo = kitInfo;
		numKitsNeeded=amount;
		print("kit config received. Build "+amount+" kits");
		state=State.NEW_JOB_RECEIVED;
		stateChanged();
	}
	
	// receive a message from server when one kit is completed
	public void msgKitCompleted() {
		numKitsNeeded--;
		print("one kit completed");
		stateChanged();
	}
	
	// receive a message from the server when the job is complete
	public void msgFinalJobCompleted(Vector<Nest> nestsList, Feeder feeder1, Feeder feeder) {
		state=State.JOB_COMPLETED;
		stateChanged();
	}

	/////////////////////////////////////////////////////////////
	/** ACTIONS **/
	
	// send kit configuration to parts robot and kit robot
	private void sendKitConfig(KitInfo info, int amount) {
		print("sending kit configuration data to "+partsRobot.getName()+", kitRobot, and gantry controller");
		for (PartInfo p: info.getParts()) {
			binsList.add(new Bin(p, amount));
		}
	
		partsRobot.msgMakeThisKit(info, amount);
		kitRobot.msgGetKits(amount);
	}
	
	/////////////////////////////////////////////////////////////
	/** SCHEDULER **/

	// doesn't really do much because all the FCS does is send and receive messages as it gets them
	public boolean pickAndExecuteAnAction() {
	
		if (state==State.NEW_JOB_RECEIVED) {
			sendKitConfig(currentKitInfo, numKitsNeeded);
			state=State.WORKING;
			stateChanged();
			return true;
		}
		
		if (server.isRunning() && numKitsNeeded==0 && state==State.WORKING) {
			print("sending out to get a job");
			server.execute("Get Job");
			return true;
		}
		
/*	this may not be necessary
 * 	if (state==State.JOB_COMPLETED) {
			// message the agents that the job is finished (?)
			// all nests
			// all feeders
		}
*/		
		return false;
	}


	
	

}
