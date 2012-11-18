package Agents.PartsRobotAgent;


import data.Part;
import data.Part.PartType;
import Agent.Agent;
import Agents.GantryFeederAgents.FeederAgent;
import Interface.PartsRobotAgent.Lane;
import Interface.PartsRobotAgent.Nest;
import java.util.*;

import server.Server;


public class LaneAgent extends Agent implements Lane{

	int maxsize = 20;
	String name;
	FeederAgent feeder;
	Nest nest;
	Server server;
	
	int index = 0;
	
	List<Part> lanequeue = new ArrayList<Part>();
	
	PartType type = PartType.none;
	
	NestStatus neststate = NestStatus.noAction;
	OrderStatus orderstate = OrderStatus.noAction;
	LaneStatus lanestate = LaneStatus.noParts;
	FeederStatus feederstate = FeederStatus.noAction;
	
	private enum OrderStatus{noAction,partRequested,partOrdered};
	private enum NestStatus{noAction,readyForPart,askedToTakePart};
	private enum FeederStatus{wantsToPlacePart,noAction};
	private enum LaneStatus{noParts,hasParts,partsAtEndOfLane}
	
	
	public LaneAgent(Nest mynest,FeederAgent feed,Server server,String name,int index){
		this.server = server;
		this.nest = mynest;
		this.feeder = feed;
		this.name = name;
		this.index = index;
	}
	
	public LaneAgent(Nest mynest, Server server,String name,int index){
		this.server =server;
		this.nest = mynest;
		this.name = name;
		this.index = index;
	}
	
	@Override
	public void msgReadyForPart() {
		neststate = NestStatus.readyForPart;
		print("Nest ready to give part");
		stateChanged();
	}

	@Override
	public void msgNeedThisPart(PartType type) {
		if(this.type != type)
			orderstate = OrderStatus.partRequested;
		this.type = type;
		print("Received Part Order");
		stateChanged();
		
	}
	
	public void msgHereIsAPart(Part p){
		lanequeue.add(p);
		lanestate = LaneStatus.hasParts;
		stateChanged();
	}

	public void msgCanIPlacePart(){
		feederstate  = FeederStatus.wantsToPlacePart;
		stateChanged();
	}
	
	@Override
	public void msgPartAtEndOfLane() {
		if(lanestate!= LaneStatus.partsAtEndOfLane){
			print("Part at end of lane");
			if(lanequeue.isEmpty()){
				lanequeue.add(new Part(type));
			}
			lanestate = LaneStatus.partsAtEndOfLane;
			stateChanged();
		}
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if(orderstate == OrderStatus.partRequested){
			askForPart();
			return true;
		}
		
		if(neststate ==  NestStatus.readyForPart){
			givePart();
			return true;
		}
		
		if(lanestate == LaneStatus.partsAtEndOfLane && neststate != NestStatus.askedToTakePart){
			askToGivePart();
			return true;
		}
		
		if(feederstate == FeederStatus.wantsToPlacePart){
			if(lanequeue.size()>maxsize){
				feeder.msgLaneIsFull(name);
				return false;
			}
			else{
				acceptPart();
				return true;
			}
		}
		
		return false;
	}
	private void askForPart(){
		if(feeder!=null)
		feeder.msgNeedThisPart(type, name);
		orderstate = OrderStatus.partOrdered;
	}
	private void acceptPart(){
		feeder.msgLaneIsReadyForParts(name);
		feederstate = FeederStatus.noAction;
	}
	private void askToGivePart(){
		nest.msgCanIPlacePart(this);
		neststate = NestStatus.askedToTakePart;		
	}
	private void givePart(){
		print("Giving Part");
		nest.msgHereIsPart(lanequeue.get(0));
		lanequeue.remove(0);
		neststate = NestStatus.noAction;
		if(!lanequeue.isEmpty()){
			lanestate = LaneStatus.hasParts;
		}
		else{
			lanestate = LaneStatus.noParts;
		}
		server.execute("Feed Nest",index);
		
	}
	public boolean hasParts(){
		if(lanestate == LaneStatus.hasParts){
			return true;
		}
		else{
			return false;
		}
	}
	public void setFeeder(FeederAgent feeder){
		this.feeder = feeder;
	}
	public String getName(){
		return name;
	}
	public int getNumber(){
		return index;
	}
	
}
