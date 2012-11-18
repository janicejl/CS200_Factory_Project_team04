package Agents.PartsRobotAgent;


import data.PartInfo;
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
	
	public List<PartInfo> lanequeue = new ArrayList<PartInfo>();
	
	public PartInfo type = null;
	
	public LaneNestStatus neststate = LaneNestStatus.noAction;
	public OrderStatus orderstate = OrderStatus.noAction;
	public LaneStatus lanestate = LaneStatus.noParts;
	public FeederStatus feederstate = FeederStatus.noAction;
	
	public enum OrderStatus{noAction,partRequested,partOrdered};
	public enum LaneNestStatus{noAction,readyForPart,askedToTakePart};
	public enum FeederStatus{wantsToPlacePart,noAction};
	public enum LaneStatus{noParts,hasParts,partsAtEndOfLane};
	
	
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
		neststate = LaneNestStatus.readyForPart;
		print("Nest ready to give part");
		stateChanged();
	}

	@Override
	public void msgNeedThisPart(PartInfo type) {
		if(this.type != type)
			orderstate = OrderStatus.partRequested;
		this.type = type;
		print("Received Part Order");
		stateChanged();
		
	}
	
	public void msgHereIsAPart(PartInfo p){
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
				lanequeue.add(new PartInfo(type));
			}
			lanestate = LaneStatus.partsAtEndOfLane;
			stateChanged();
		}
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if(orderstate == OrderStatus.partRequested){
			askForPart();
			return true;
		}
		
		if(neststate ==  LaneNestStatus.readyForPart){
			givePart();
			return true;
		}
		
		if(lanestate == LaneStatus.partsAtEndOfLane && neststate != LaneNestStatus.askedToTakePart){
			askToGivePart();
			return true;
		}
		
		if(lanequeue.size()>maxsize){
			if(index%2==0){
				feeder.msgLaneIsFull("left");
			}
			else{
				feeder.msgLaneIsFull("right");
			}
		}
		
		if(feederstate == FeederStatus.wantsToPlacePart){
			if(lanequeue.size()>maxsize){
				if(index%2==0){
					feeder.msgLaneIsFull("left");
				}
				else{
					feeder.msgLaneIsFull("right");
				}	
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
		if(feeder!=null){
			if(index%2==0){
				feeder.msgNeedThisPart(type,"left");
			}
			else{
				feeder.msgNeedThisPart(type,"right");
			}
		}
		orderstate = OrderStatus.partOrdered;
	}
	private void acceptPart(){
		feeder.msgLaneIsReadyForParts(name);
		feederstate = FeederStatus.noAction;
	}
	private void askToGivePart(){
		nest.msgCanIPlacePart(this);
		neststate = LaneNestStatus.askedToTakePart;		
	}
	private void givePart(){
		nest.msgHereIsPart(lanequeue.get(0));
		lanequeue.remove(0);
		neststate = LaneNestStatus.noAction;
		if(!lanequeue.isEmpty()){
			lanestate = LaneStatus.hasParts;
		}
		else{
			lanestate = LaneStatus.noParts;
		}
		print("Giving Part and calling server execute function");
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
