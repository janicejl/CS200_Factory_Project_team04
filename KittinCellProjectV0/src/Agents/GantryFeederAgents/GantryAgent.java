package Agents.GantryFeederAgents;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;

public class GantryAgent extends Agent implements Gantry {

	//Data
	
	String name;
	Feeder currentFeeder;
	Bin currentBin;
	GantryController gc;
	enum FeederState{ready, pending, notReady};
	FeederState fstate;
	
	
	public GantryAgent(String name){
		this.name = name;
		currentFeeder = null;
		currentBin = null;
		fstate = FeederState.notReady;
	}
	
	//Messages
	
	@Override
	public void msgGiveFeederParts(Feeder f1, Bin b) {
		this.currentFeeder = f1;
		this.currentBin = b;
		stateChanged();
	}

	@Override
	public void msgReadyForParts() {
		fstate = FeederState.ready;
	}
	
	
	//Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if(fstate == FeederState.ready){
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
		currentFeeder = null;
		fstate = FeederState.notReady;
		gc.msgDoneDeliveringParts(this);
	}
	
	private void PrepareToGiveParts(){
		currentFeeder.msgHaveParts(this);
		fstate = FeederState.pending;
	}
	
	//Extras
	
	public void setFeeder(Feeder f1){
		this.currentFeeder = f1;
	}
	
	public String getName(){
		return this.getName();
	}
	
	public void setGantryController(GantryController gc){
		this.gc = gc;
	}
	
	
}