package Agents.GantryFeederAgents;

import server.Server;
import Agent.Agent;
import Interface.GantryFeederAgent.Feeder;
import Interface.PartsRobotAgent.Lane;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;
import data.PartInfo;

public class FeederAgent extends Agent implements Feeder {

	//Data
	String name;
	PartInfo currentPart;
	PartInfo requestedPart;
	int partsInFeeder = 0;
	int lowParts;
	int number;
	Bin myBin;
	public EventLog log;
	
	
	enum FeederState{feeding, low, waitingLane, purging, waitingGantry, fed};
	//possible additional state: beingFed
	FeederState fstate = FeederState.low;
	
	MyLane left;
	MyLane right;
	Gantry gantry;
	GantryController gc;
	Server app;
	
	private class MyLane{
		Lane fLane1;
		PartInfo partWanted;
		boolean readyForParts;
		
		public MyLane(Lane f1){
			this.fLane1 = f1;
			partWanted = new PartInfo("blank", "source");
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
		this.currentPart = new PartInfo("blank", "source1");
		this.requestedPart = new PartInfo("blank", "source2");
		this.log = new EventLog();
	}
	
	//Messages

	@Override
	public void msgNeedThisPart(PartInfo p, String laneName) {
		if(requestedPart.getName().equals("blank")){
			requestedPart = p;
		}
		if(laneName.equals("left")){
			left.partWanted = p;
		}
		else {
			right.partWanted = p;
		}
		
		log.add(new LoggedEvent("msgNeedThisPart received from "+laneName+" lane."));
		print("msgNeedThisPart received from "+laneName+" lane.");
		stateChanged();
	}

	@Override
	public void msgHaveParts(Gantry g1) {
		gantry = g1;
		log.add(new LoggedEvent("msgHaveParts received from Gantry " + g1.getName() + "."));
		print("msgHaveParts received from Gantry " + g1.getName() + ".");
		stateChanged();
	}

	@Override
	public void msgHereAreParts(Bin bin) {
		currentPart = bin.getPartInfo();
		partsInFeeder = bin.getQuantity();
		myBin = bin;
		fstate = FeederState.waitingLane;	
		log.add(new LoggedEvent("msgHereAreParts received from gantry."));
		print("msgHereAreParts received from gantry.");
		stateChanged();
	}
	
	//will be from GUI
	public void msgHereAreParts(PartInfo part, int quantity){
		currentPart = part;
		partsInFeeder = quantity;
		fstate = FeederState.waitingLane;
		log.add(new LoggedEvent("msgHereAreParts received from GUI."));
		print("msgHereAreParts received from GUI.");
		stateChanged();
	}

	@Override
	public void msgLaneIsFull(String laneName) {
		if(laneName.equals("left")){
			left.readyForParts = false;
			requestedPart = right.partWanted;
		}
		else{
			right.readyForParts = false;
			requestedPart = left.partWanted;
		}
		if(fstate != FeederState.waitingGantry){
			fstate = FeederState.waitingLane;
		}
		log.add(new LoggedEvent("msgLaneIsFull received from " + laneName + " lane."));
		print("msgLaneIsFull received from " + laneName + " lane.");
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
		log.add(new LoggedEvent("msgLaneIsReadyForParts received from " + laneName + " lane."));
		print("msgLaneIsReadyForParts received from " + laneName + " lane.");
		stateChanged();
	}
	
	//Scheduler
	
	@Override
	public boolean pickAndExecuteAnAction() {
		print("Feeder running");
		if(fstate == FeederState.low && !currentPart.getName().equals("blank") ){
			print("fstate is low and current part is not blank, RequestParts(currentPart) called.");
			RequestParts(currentPart);
			return true;
		}
		
		/*if(fstate == FeederState.beingFed){
			FeedFeeder();
			return true;
		}*/
		
		else if(fstate == FeederState.waitingLane || fstate == FeederState.low){
			print("fstate is waitingLane or low.");
			if(currentPart == left.partWanted && left.readyForParts){
				print("CurrentPart is left.partWanted and left is ready for parts. FeedParts(true) called.");
				FeedParts(true);
				return true;
			}
			else if (currentPart == right.partWanted && right.readyForParts){
				print("CurrentPart is right.partWanted and right is ready for parts. FeedParts(false) called.");
				FeedParts(false);
				return true;
			}
			
			else if(!left.readyForParts && right.readyForParts && !currentPart.getName().equals(right.partWanted.getName()) && !requestedPart.equals("blank")){
				print("Left is not ready for parts. Right is ready for parts. Current part is not equal to right.partWanted. RequestParts(requestedPart) called.");
				RequestParts(requestedPart);
				PurgeFeeder();
				return true;
			}
			

			else if(!right.readyForParts && left.readyForParts && currentPart!= left.partWanted && !requestedPart.getName().equals("blank")){
				print("Right is not ready for parts. Left is ready for parts. Current part is not equal to left.partWanted. RequestParts(requestedPart) called.)");
				RequestParts(requestedPart);
				PurgeFeeder();
				return true;
			}
			
			else if (currentPart != left.partWanted && currentPart != right.partWanted && !requestedPart.getName().equals("blank")){
				print("currentPart is not left or right partWanted.RequestedParts(requestedPart) called.");
				print("requestedPart:" + requestedPart.getName());
				RequestParts(requestedPart);
				PurgeFeeder();
				return true;
			}
		}
		
		else if(fstate == FeederState.waitingGantry && gantry != null){
			print("fstate is waitingGantry and gantry exists. AcceptParts() should be called.");
			AcceptParts();
			return true;
		}
		
		print("Nothing chosen by feeder scheduler.");
		//print("left ready: " + left.readyForParts);
		//print("right ready: " + right.readyForParts);
		//print("currentPart:" + currentPart.getName());
		//print("left part:" + left.partWanted.getName());
		//print("right part:" + right.partWanted.getName());
		
		return false;
	}
	
	//Actions
	
	private void FeedParts(boolean divertLeft){
		if(divertLeft){
			left.fLane1.msgHereIsAPart(currentPart);
			partsInFeeder --;
			//DoGiveLeftLaneAPart();
			print("left lane was fed 1 part");
			app.execute("Feed Lane", left.fLane1.getNumber());
		}
		else{
			right.fLane1.msgHereIsAPart(currentPart);
			partsInFeeder--;
			//DoGiveRightLaneAPart();
			print("right lane was fed 1 part");
			app.execute("Feed Lane", right.fLane1.getNumber());
		}
		stateChanged();
	}
	
	private void RequestParts(PartInfo part){
		gc.msgNeedThisPart(part, this);
		fstate = FeederState.waitingGantry;
		stateChanged();
	}
	
	private void PurgeFeeder(){
		if(partsInFeeder > 0){
			myBin.setQuantity(partsInFeeder);
			myBin.setPartInfo(currentPart);
			partsInFeeder = 0;
			//DoPurgeFeeder();
			app.execute("Idle Bin", this.getNumber());
			stateChanged();
		}
		
	}
	
	private void AcceptParts(){
		print("Accept Parts was called.");
		gantry.msgReadyForParts();
		fstate = FeederState.fed;
		stateChanged();
		
	}
	
	/*private void FeedFeeder(){
		//DoFeedFeeder();
		app.execute("Feed Feeder", number);
		fstate = FeederState.waitingLane;
	}*/
	
	
	
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
	
	public int getNumber(){
		return this.number;
	}
	
	public FeederState getState(){
		return fstate;
	}
	
	public int getQuantity(){
		return this.partsInFeeder;
	}

}