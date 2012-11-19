package Agents.GantryFeederAgents;

import server.Server;
import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class GantryAgent extends Agent implements Gantry {

	//Data
	
	String name;
	Feeder currentFeeder;
	Bin currentBin;
	GantryController gc;
	Server app;
	enum FeederState{ready, pending, notReady};
	FeederState fstate;
	public EventLog log;
	
	public GantryAgent(String name, Server app){
		this.name = name;
		currentFeeder = null;
		currentBin = null;
		fstate = FeederState.notReady;
		this.app = app;
		log = new EventLog();
		
	}
	
	//Messages
	
	@Override
	public void msgGiveFeederParts(Feeder f1, Bin b) {
		this.currentFeeder = f1;
		this.currentBin = b;
		log.add(new LoggedEvent("msgGiveFeederParts received from GantryController"));
		stateChanged();
	}

	@Override
	public void msgReadyForParts() {
		fstate = FeederState.ready;
		log.add(new LoggedEvent("msgReadyForParts received from Feeder"));
		stateChanged();
	}
	
	
	//Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		if(currentFeeder != null && fstate == FeederState.ready){
			GivePartsToFeeder();
			return true;
		}
		else if(currentFeeder != null && fstate == FeederState.notReady){
			PrepareToGiveParts();
			return true;
		}
		
		return false;
	}

	//Actions
	
	private void GivePartsToFeeder(){
		currentFeeder.msgHereAreParts(currentBin);
		//DoFillFeeder()
		app.execute("Load Feeder", currentFeeder.getNumber());
		while(currentBin.getQuantity() > 0){
			app.execute("Feed Feeder", currentFeeder.getNumber(), currentBin.getPartInfo());
			currentBin.setQuantity(currentBin.getQuantity()-1);
		}
		app.execute("Idle Bin", currentFeeder.getNumber());
		currentFeeder = null;
		fstate = FeederState.notReady;
		gc.msgDoneDeliveringParts(this);
	}
	
	private void PrepareToGiveParts(){
		app.execute("Make PartsBox", currentBin.getPartInfo());
		currentFeeder.msgHaveParts(this);
		fstate = FeederState.pending;
	}
	
	//Extras
	
	public void setFeeder(Feeder f1){
		this.currentFeeder = f1;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setGantryController(GantryController gc){
		this.gc = gc;
	}
	
	
}