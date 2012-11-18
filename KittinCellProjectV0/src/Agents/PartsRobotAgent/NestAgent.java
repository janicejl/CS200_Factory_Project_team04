package Agents.PartsRobotAgent;

import  Agent.*;
import Agents.VisionAgent.VisionAgent;
import server.Server;
import data.*;


import Interface.PartsRobotAgent.*;
import Interface.VisionAgent.Vision;

public class NestAgent extends Agent implements Nest{

	//Data
	public PartInfo partinfo;
	public Part[] nestslots = new Part[8];
	PartsRobot partsrobot;
	Lane lane;
	Vision camera;
	String name;
	Server server;
		
	public int index;

	public enum LaneStatus {hasPart, gettingParts, noAction}
	public enum PartsRobotStatus {wantsParts, waitingForParts, noAction,readyforpart}
	public enum NestStatus {badParts, noParts, needCheck, noAction}
	public enum AnimationStatus {purging, needPurge, needSettle, noAction}
	
	public LaneStatus lanestate = LaneStatus.noAction;
	public PartsRobotStatus partsrobotstate= PartsRobotStatus.noAction;
	public NestStatus neststate = NestStatus.noParts;
	public AnimationStatus animationstate = AnimationStatus.noAction;
	
	public NestAgent(Lane lane, Vision camera, int index)
	{
		this.lane = lane;
		this.camera = camera;
		this.index = index;
		name = "NestAgent " + index;
		for(int i = 0; i<8; i++){
			nestslots[i]=null;
		}
	}
	
	public NestAgent(int index,Server server)
	{
		this.lane = null;
		this.camera = null;
		this.index = index;
		this.server = server;
		name = "NestAgent " + index;
		for(int i = 0; i<8; i++){
			nestslots[i]=null;
		}
	}	

	public void setPartsRobotAgent(PartsRobot robot){
		this.partsrobot = robot;
	}
	//Messages
	public void msgCanIPlacePart(Lane lane)
	{
			lanestate = LaneStatus.hasPart;
			stateChanged();
	}
	public void msgHereIsPart(Part p)
	{
			for(int i = 0; i < 8; i++)
			{
				if(nestslots[i]== null)
				{
					nestslots[i] = p;
					if(i == 0)
						neststate = NestStatus.needCheck;
					break;
				}
			}
			stateChanged();
	}

	public void msgBadParts()
	{
			neststate = NestStatus.badParts;
			stateChanged();
	}

	public void msgGetPart()
	{
		partsrobotstate = PartsRobotStatus.readyforpart;
		stateChanged();
	}

	public void msgNeedThisPart(PartInfo p)
	{
			for(int i = 0; i<8; i++)
			{
				if(nestslots[i] != null){
					if(!nestslots[i].info.equals(partinfo))
						animationstate = AnimationStatus.needPurge;
				}
			}
					
			partinfo = p;
			partsrobotstate = PartsRobotStatus.wantsParts;
			
			stateChanged();
	}

	public void msgAnimationDone(){
		if(animationstate == AnimationStatus.purging){
			neststate = NestStatus.noParts;
			partsrobotstate = PartsRobotStatus.wantsParts;
			animationstate = AnimationStatus.noAction;

		}
		stateChanged();
	}

	
	//Scheduler:
	
	public boolean pickAndExecuteAnAction()
	{
		int ct=0;
		for(int i = 0; i<8; i++){
			if(nestslots[i]!=null){
				ct++;
			}
		}
		print("Number of items in nest " + ct);
		
		
		if(partsrobotstate == PartsRobotStatus.readyforpart)
		{
			givePart();
			return true;
		}
		if(animationstate == AnimationStatus.needSettle)
		{
			settleNest();
			return true;
		}
		if((neststate == NestStatus.badParts || animationstate == AnimationStatus.needPurge)&&animationstate != AnimationStatus.purging)
		{
			purgeNest();
			return true;
		}
		if(partsrobotstate == PartsRobotStatus.wantsParts)
		{
			askForParts();
			return true;
		}
		
		if(lanestate == LaneStatus.hasPart)
		{
			for(int i = 0; i<7; i++)
			{
				if(nestslots[i] == null)
				{
				acceptPart();
				return true;
				}
			}
		}
		
		if(neststate == NestStatus.needCheck)
		{
			askForInspection();
			return true;
			
		}
			
		if(nestslots[0] == null && nestslots[1]!= null)
		{
			settleNest();
			return true;
		}
		print("current" + lanestate);
		return false;
	}

	//Actions

	private void purgeNest(){
		print("Purging");
		//Animation Call
		//server.DoPurge(); // Run the purge animation
		
		animationstate = AnimationStatus.purging;
		for(int i = 0; i<8; i++)
		{
			nestslots[i] = null;
		}
	}

	private void acceptPart()
	{
		
		print("Ready to take the part");
		lane.msgReadyForPart();
		lanestate = LaneStatus.noAction;
	}
	private void askForInspection()
	{
		print("I am full and need inspection");
		neststate = NestStatus.noAction;
		if(camera!= null)
		camera.msgImFull(this);
	}
	
	private void settleNest()
	{
		//NestSettlingAnimation
		//gui.DoSettleNest(); // Settle Nest so that the parts behind in the nest move to the front
		for(int i = 0; i<7; i++){
			if(nestslots[i] == null && nestslots[i+1]!= null){
				nestslots[i] = nestslots[i+1];
				nestslots[i+1] = null;
				if(i==0)
					neststate = NestStatus.needCheck;
			}
			
		}
		//if(lanestate == LaneStatus.hasPart && nestslots[7]==null){
		//	acceptPart();
		//}
	}
	private void givePart()
	{
		print("Giving PartsRobot part");
		partsrobot.msgHereIsPart(nestslots[0]);
		nestslots[0] = null;
		partsrobotstate = PartsRobotStatus.waitingForParts;
		settleNest();
	}
		
	private void askForParts()
	{
		print("Please give me a part");
		if(lane!= null)
		lane.msgNeedThisPart (partinfo);
		partsrobotstate = PartsRobotStatus.waitingForParts;
	}
	
	public String getName(){
		return name;
	}

	public Integer getNumber() {
		return index;
	}

	public PartInfo getPartInfo() {
		return partinfo;
	}
	
	public void setVisionAgent(VisionAgent camera){
		this.camera = camera;
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setLane(Lane newlane){
		this.lane = newlane;
	}
	
}
