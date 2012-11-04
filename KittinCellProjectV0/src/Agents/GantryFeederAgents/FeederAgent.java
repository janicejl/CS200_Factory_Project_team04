package Agents.GantryFeederAgents;

import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.FeederLane;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;

public class FeederAgent extends Agent implements Feeder {

	//Data
	String name;
	PartType currentPart = PartType.none;
	PartType requestedPart = PartType.none;
	int partsInFeeder = 0;
	int lowParts;
	Bin myBin;
	
	
	enum FeederState{feeding, low, waitingLane, purging, waitingGantry};
	FeederState fstate = FeederState.waitingGantry;
	
	MyLane left;
	MyLane right;
	Gantry gantry;
	GantryController gc; 
	
	private class MyLane{
		FeederLane fLane1;
		PartType partWanted;
		boolean readyForParts;
		
		public MyLane(FeederLane f1){
			this.fLane1 = f1;
			partWanted = PartType.none;
			readyForParts = false;
		}
	}
	
	public FeederAgent(String name, int lowParts, FeederLane left, FeederLane right){
		this.name = name;
		this.lowParts = lowParts;
		this.gantry = null;
		this.left = new MyLane(left);
		this.right = new MyLane(right);
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
		fstate = FeederState.waitingLane;	
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
			left.fLane1.msgHereIsAPart();
			partsInFeeder --;
			//DoGiveLeftLaneAPart();
		}
		else{
			right.fLane1.msgHereIsAPart();
			partsInFeeder--;
			//DoGiveRightLaneAPart();
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