package Agents.GantryFeederAgents;

import java.util.Timer;
import java.util.TimerTask;

import server.Server;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.FeederLane;

public class FeederLaneAgent extends Agent implements FeederLane {

	//Data
	String name;
	int number;
	int quantity;
	int maxQuantity;
	Timer t;
	Feeder feeder;
	Server app;
	
	enum FeederState{feeding, stopped, pending};
	FeederState state;
	
	public FeederLaneAgent(String name, int number, Server app){
		this.name = name;
		this.number = number;
		this.app = app;
	}
	
	//Messages
	
	@Override
	public void msgHereIsAPart() {
		quantity++;
		state = FeederState.feeding;
		stateChanged();
	}

	@Override
	public void msgRemovePart() {
		quantity --;
		stateChanged();
	}
	
	//Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if(quantity == maxQuantity){
			StopFeeder();
			return true;
		}
		else if(state == FeederState.stopped && quantity < maxQuantity){
			StartFeeder();
			return true;
		}
		else if(quantity < 0){
			RemoveParts();
			return true;
		}
		return false;
	}
	
	
	//Actions
	
	private void StopFeeder(){
		feeder.msgLaneIsFull(name);
		state = FeederState.stopped;
	}
	
	private void StartFeeder(){
		feeder.msgLaneIsReadyForParts(name);
		state = FeederState.pending;
	}
	
	private void RemoveParts(){
		t.schedule(new TimerTask(){public void run(){msgRemovePart();}}, 5000);
	}

	
	//Extra
	
	@Override
	public void setMaxQuantity(int quantity) {
		this.maxQuantity = quantity;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setFeeder(Feeder f1){
		this.feeder = f1;
	}
	
}