package Agents.GantryFeederAgents;

import server.Server;
import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.PartsRobotAgent.Lane;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import data.Part;
import data.Part.PartType;

public class FeederAgent extends Agent implements Feeder {

	//Data
	String name;
	PartType currentPart = PartType.none;
	PartType requestedPart = PartType.none;
	int partsInFeeder = 0;
	int lowParts;
	int number;
	Bin myBin;
	
	
	enum FeederState{feeding, low, waitingLane, purging, waitingGantry, beingFed};
	FeederState fstate = FeederState.waitingGantry;
	
	MyLane left;
	MyLane right;
	Gantry gantry;
	GantryController gc;
	Server app;
	
	private class MyLane{
		Lane fLane1;
		PartType partWanted;
		boolean readyForParts;
		
		public MyLane(Lane f1){
			this.fLane1 = f1;
			partWanted = PartType.none;
			readyForParts = false;
		}
	}
	
	public FeederAgent(String name, int lowParts, Lane left, Lane right, int number, Server app){
		this.name = name;
		this.lowParts = lowParts;
		this.gantry = null;
		this.left = new MyLane(left);
		this.right = new MyLane(right);
		this.number = number;
		this.app = app;
	}
	
	//Messages

	@Override
	public void msgNeedThisPart(PartType p, String laneName) {
		if(requestedPart.equals(PartType.none)){
			requestedPart = p;
		}
		if(laneName.equals("left")){
			left.partWanted = p;
		}
		else {
			right.partWanted = p;
		}
		
		stateChanged();
	}

	@Override
	public void msgHaveParts(Gantry g1) {
		gantry = g1;
		stateChanged();
	}

	@Override
	public void msgHereAreParts(Bin bin) {
		currentPart = bin.getPartType();
		partsInFeeder = bin.getQuantity();
		myBin = bin;
		fstate = FeederState.beingFed;	
		stateChanged();
	}
	
	//will be from GUI
	public void msgHereAreParts(PartType part, int quantity){
		currentPart = part;
		partsInFeeder = quantity;
		fstate = FeederState.beingFed;
		stateChanged();
	}

	@Override
	public void msgLaneIsFull(String laneName) {
		if(laneName.equals("left")){
			left.readyForParts = false;
		}
		else{
			right.readyForParts = false;
		}
		fstate = FeederState.waitingLane;
		
		stateChanged();
	}

	@Override
	public void msgLaneIsReadyForParts(String laneName) {
		if(laneName.equals("left")){
			left.readyForParts = true;
		}
		else{
			right.readyForParts = true;
		}
		
		stateChanged();
	}
	
	//Scheduler
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if(fstate == FeederState.low){
			RequestParts(currentPart);
			return true;
		}
		
		if(fstate == FeederState.beingFed){
			FeedFeeder();
			return true;
		}
		
		if(fstate == FeederState.waitingLane || fstate == FeederState.low){
			if(currentPart == left.partWanted && left.readyForParts){
				FeedParts(true);
				return true;
			}
			else if (currentPart == right.partWanted && right.readyForParts){
				FeedParts(false);
				return true;
			}
			
			else if (currentPart != left.partWanted && currentPart != right.partWanted){
				RequestParts(requestedPart);
				PurgeFeeder();
				return true;
			}
		}
		
		if(fstate == FeederState.waitingGantry && gantry != null){
			AcceptParts();
			return true;
		}
		
		return false;
	}
	
	//Actions
	
	private void FeedParts(boolean divertLeft){
		if(divertLeft){
			left.fLane1.msgHereIsAPart(new Part(currentPart));
			partsInFeeder --;
			//DoGiveLeftLaneAPart();
			app.execute("Feed Lane", left.fLane1.getNumber());
		}
		else{
			right.fLane1.msgHereIsAPart(new Part(currentPart));
			partsInFeeder--;
			//DoGiveRightLaneAPart();
			app.execute("Feed Lane", right.fLane1.getNumber());
		}
	}
	
	private void RequestParts(PartType part){
		gc.msgNeedThisPart(part, this);
		fstate = FeederState.waitingGantry;
	}
	
	private void PurgeFeeder(){
		myBin.setQuantity(partsInFeeder);
		myBin.setPartType(currentPart);
		partsInFeeder = 0;
		//DoPurgeFeeder();
	}
	
	private void AcceptParts(){
		gantry.msgReadyForParts();
		fstate = FeederState.waitingGantry;
		
	}
	
	private void FeedFeeder(){
		//DoFeedFeeder();
		app.execute("Feed Feeder", number);
		fstate = FeederState.waitingLane;
	}
	
	
	
	//Extras
	/**
	 * Hack to set gantry to be the Mock or real GantryAgent
	 * @param g1 Gantry that is being set 
	 */
	@Override
	public void setGantry(Gantry g1) {
		this.gantry = g1;
	}

	public void removeGantry(){
		this.gantry = null;
	}
	
	@Override
	public void setGantryController(GantryController gc) {
		this.gc = gc;
		
	}
	
	public String getName(){
		return this.name;
	}
	
	

}