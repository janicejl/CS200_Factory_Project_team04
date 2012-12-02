package Agents.GantryFeederAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.Server;
import Agent.Agent;
import Agents.PartsRobotAgent.LaneAgent;
import Interface.GantryFeederAgent.Feeder;
import Interface.PartsRobotAgent.Lane;
import Interface.GantryFeederAgent.Gantry;
import MoveableObjects.Bin;
import data.PartInfo;

public class FeederAgent extends Agent implements Feeder {

	
	enum LaneState {NeedPart,Nothing, ReadyForParts};
	
	class MyLane
	{
		Lane lane;
		PartInfo part_wanted;
		LaneState state;
	}
	
	enum FeederState {HaveParts,NoPartsInFeeder};
	enum FeederEvent {GotBinWithParts, IsFeederReadyForNewParts, PartsGone};
	
	FeederState state;
	Bin current_bin;
	List<FeederEvent> feeder_event_list = Collections.synchronizedList(new ArrayList<FeederEvent>());
	List<MyLane> my_lane_list = Collections.synchronizedList(new ArrayList<MyLane>());
	private Gantry gantry;
	int number;
	Server server;
	String name;
	
	
	
	public FeederAgent(String name, Lane l_lane, Lane r_lane, int number, Server sever)
	{
		this.name = name;
		this.number = number;
		this.server = sever;
		
		MyLane temp_lane = new MyLane();
		MyLane temp_lane_2 = new MyLane();
		
		temp_lane_2.lane = r_lane;
		temp_lane.lane = l_lane;
		
		temp_lane_2.state = LaneState.Nothing;
		temp_lane.state = LaneState.Nothing;
		
		my_lane_list.add(temp_lane_2);
		my_lane_list.add(temp_lane);
		state = FeederState.NoPartsInFeeder;
		
	}
	
	/**
	 * 
	 */
	public void msgNeedThisPart(PartInfo p, Lane lane)
	{
		System.out.println(name + ": Got a request for a part");
		for(MyLane my_lane_:my_lane_list)
		{
			if(my_lane_.lane.equals(lane))
			{
				my_lane_.state = LaneState.NeedPart;
				my_lane_.part_wanted = p;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgHereAreParts(Bin bin) 
	{
		System.out.println(name + ": Got a bin");
		feeder_event_list.add(FeederEvent.GotBinWithParts);
		current_bin = bin;
		System.out.println(bin);
		stateChanged();
	}
	
	
	
	//may not need this actually
	public void msgAmIReadyForParts()
	{
		System.out.println(name + ": Is feeder ready for part?");
		feeder_event_list.add(FeederEvent.IsFeederReadyForNewParts);
		stateChanged();
	}
	
	public void msgPartsGone()
	{
		System.out.println(name + ": No more parts in the feeder");
		feeder_event_list.add(FeederEvent.PartsGone);
		stateChanged();
	}
	
	
	public void msgIsLaneReadyForParts(Lane lane_)
	{
		System.out.println(name + ": Lane is ready to get parts");
		for(MyLane my_lane_:my_lane_list)
		{
			if(my_lane_.lane == lane_)
			{
				my_lane_.state = LaneState.ReadyForParts;
				stateChanged();
				return;
			}
		}
	}
	
	
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		
		
		for(FeederEvent event:feeder_event_list)
		{
			if(event == FeederEvent.GotBinWithParts)
			{
				state = FeederState.HaveParts;
				feeder_event_list.remove(event);
				CheckIfLaneIsReadyForParts();
				return true;
			}
		}
		
		if(state == FeederState.HaveParts)
		{
			for(MyLane my_lane_:my_lane_list)
			{	
				if(my_lane_.state == LaneState.ReadyForParts)
				{
					GivePartsToLane(my_lane_);
					return true;
				}
			}
			
			for(FeederEvent event:feeder_event_list)
			{
				if(event == FeederEvent.PartsGone)
				{
					feeder_event_list.remove(event);
					state = FeederState.NoPartsInFeeder;
					return true;
				}
			}
		}
		System.out.println(state);
		
		if(state == FeederState.NoPartsInFeeder)
		{
			for(MyLane my_lane_:my_lane_list)
			{	
				if(my_lane_.state == LaneState.NeedPart)
				{
					AskGantryToGetPart(my_lane_);
					return true;
				}
			}
			
			for(MyLane my_lane_:my_lane_list)
			{	
				if(my_lane_.state == LaneState.ReadyForParts)
				{
					AskGantryToGetPart(my_lane_);
					return true;
				}
			}
		}

		return false;
	}
	
	//ACTIONS

	private void AskGantryToGetPart(MyLane lane)
	{
		System.out.println(name + ": Asking gantry for parts");
		lane.state = LaneState.Nothing;
		gantry.msgNeedThisPart(lane.part_wanted, this);
	}
	
	
	
	private void CheckIfLaneIsReadyForParts()
	{
		System.out.println(name + ": Checking if lane is ready for parts");
		for(MyLane my_lane_:my_lane_list)
		{	
			if(my_lane_.part_wanted != null)
			{	
				if(my_lane_.part_wanted.getName() == current_bin.getPartInfo().getName())
				{
					my_lane_.lane.msgCanIPlacePart();
				}
			}
		}
	}
	
	private void GivePartsToLane(MyLane lane)
	{
		System.out.println(name + ": Giving parts to lane");
	//	server.execute("Feed Feeder", number);
		lane.lane.msgHereIsAPart(current_bin.getPartInfo());

	//	server.execute("Try Feed Lane", lane.lane.getNumber());
		for(int i=0;i<current_bin.getQuantity();i++)
		{
			server.execute("Try Feed Lane", lane.lane.getNumber());
		}
		lane.state = LaneState.Nothing;
		state = FeederState.NoPartsInFeeder;
	
	}
	
	
	//Extras	
	
	public void SetGantry(Gantry gantry)
	{
		this.gantry = gantry;
	}
	
	
	public int getNumber(){
		return number;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	//Data
	String name;
	PartInfo currentPart;
	PartInfo requestedPart;
	int partsInFeeder = 0;
	int lowParts;
	int number;
	Bin myBin;
	public EventLog log;
	
	
	enum FeederState{feeding, low, waitingLane, purging, waitingGantry, fed, waitAnimLane, waitAnimFeeder};
	//possible additional state: beingFed
	FeederState fstate = FeederState.low;
	
	MyLane left;
	MyLane right;
	Gantry gantry;
	//Gantry gc;
	Server app;
	
	private class MyLane{
		Lane fLane1;
		PartInfo partWanted;
		boolean readyForParts;
		boolean needsparts;
		
		public MyLane(Lane f1){
			this.fLane1 = f1;
			partWanted = new PartInfo("blank", "source");
			readyForParts = false;
			needsparts = false;
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
			left.needsparts = true;
		}
		else {
			right.partWanted = p;
			right.needsparts = true;
		}
		
		log.add(new LoggedEvent("msgNeedThisPart received from "+laneName+" lane."));
		print("msgNeedThisPart received from "+laneName+" lane.");
		stateChanged();
	}

	@Override
	public void msgHaveParts(Gantry g1) {
		gantry = g1;
		log.add(new LoggedEvent("msgHaveParts received from Gantry " + "."));
		print("msgHaveParts received from Gantry " + ".");
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

		if(fstate == FeederState.low && !currentPart.getName().equals("blank") ){
			print("fstate is low and current part is not blank, RequestParts(currentPart) called.");
			RequestParts(currentPart);
			return true;
		}
		
		else if(fstate == FeederState.waitingGantry && gantry != null){
			print("fstate is waitingGantry and gantry exists. AcceptParts() should be called.");
			AcceptParts();
			return true;
		}
		
		/*if(fstate == FeederState.beingFed){
			FeedFeeder();
			return true;
		}*/
		
	/*	else if(fstate == FeederState.waitingLane || fstate == FeederState.low){
			if(currentPart == left.partWanted && left.readyForParts && partsInFeeder>0){
				print("CurrentPart is left.partWanted and left is ready for parts. FeedParts(true) called.");
				FeedParts(true);
				return true;
			}
			else if (currentPart == right.partWanted && right.readyForParts && partsInFeeder>0){
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
			
			else if (currentPart != left.partWanted && currentPart != right.partWanted && !requestedPart.getName().equals("blank")&&(left.needsparts || right.needsparts)){
				print("currentPart is not left or right partWanted.RequestedParts(requestedPart) called.");
				print("requestedPart:" + requestedPart.getName());
				RequestParts(requestedPart);
				PurgeFeeder();
				return true;
			}
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
			app.execute("Try Feed Lane", left.fLane1.getNumber());
			left.fLane1.msgHereIsAPart(currentPart);
			partsInFeeder --;
			//DoGiveLeftLaneAPart();
			print("left lane was fed 1 part");
		}
		else{
			right.fLane1.msgHereIsAPart(currentPart);
			partsInFeeder--;
			//DoGiveRightLaneAPart();
			print("right lane was fed 1 part");
			app.execute("Try Feed Lane", right.fLane1.getNumber());
		}
		if(partsInFeeder == 0){
			currentPart = new PartInfo("blank","blanksource");
			if(divertLeft){
				left.needsparts = false;
				left.readyForParts = false;
				if(right.needsparts){
					requestedPart = right.partWanted;
				}
			}
			else{
				right.needsparts = false;
				right.readyForParts = false;
				if(left.needsparts){
					requestedPart = left.partWanted;
				}
			}
		}
			
		
	}
	
	private void RequestParts(PartInfo part){
		print("Requesting part of type " + part.getName() + " from gantry");
		gantry.msgNeedThisPart(part, this);
		fstate = FeederState.waitingGantry;
	}
	
	private void PurgeFeeder(){
		if(partsInFeeder > 0){
			myBin.setQuantity(partsInFeeder);
			myBin.setPartInfo(currentPart);
			partsInFeeder = 0;
			//DoPurgeFeeder();
			app.execute("Idle Bin", this.getNumber());
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
//	@Override
	/*public void setGantry(Gantry g1) {
		this.gantry = g1;
	}

	public void removeGantry(){
		this.gantry = null;
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
	}*/

	public void msgLaneIsReadyForParts(LaneAgent laneAgent) {
		// TODO Auto-generated method stub
		
	}

}