package Agents.PartsRobotAgent;

import  Agent.*;
import MoveableObjects.*;
import Interface.PartsRobotAgent.*;

public class NestAgent extends Agent{

	//Data
	Part.PartType parttype;
	Part[] nestslots = new Part[9];
	PartsRobotAgent partsrobot;
	LaneAgent lane;
	VisionAgent camera;
	String name;
	
	//GUIObject gui;
	
	int index;

	private enum LaneStatus {hasPart, gettingParts, noAction}
	private enum PartsRobotStatus {wantsParts, waitingForParts, noAction,readyforpart}
	private enum NestStatus {badParts, noParts, needCheck, noAction}
	private enum AnimationStatus {purging, needPurge, needSettle, noAction}
	
	LaneStatus lanestate = LaneStatus.noAction;
	PartsRobotStatus partsrobotstate= PartsRobotStatus.noAction;
	NestStatus neststate = NestStatus.noParts;
	AnimationStatus animationstate = AnimationStatus.noAction;
	
	public NestAgent(LaneAgent lane, VisionAgent camera, int index)
	{
		this.lane = lane;
		this.camera = camera;
		this.index = index;
		name = "NestAgent " + index;
	}
	
	public void setPartsRobotAgent(PartsRobotAgent robot){
		this.partsrobot = robot;
	}
	//Messages
	public void msgCanIPlacePart(LaneAgent lane)
	{
			lanestate = LaneStatus.hasPart;
			stateChanged();
	}
	public void msgHereIsPart(Part p)
	{
			for(int i = 0; i < 9; i++)
			{
				if(nestslots[i]== null)
				{
					nestslots[i] = p;
					if(i == 0)
						neststate = NestStatus.needCheck;
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

	public void msgNeedThisPart(Part.PartType type)
	{
			for(int i = 0; i<9; i++)
			{
				if(nestslots[1] != null){
					if(nestslots[i].type != type)
						animationstate = AnimationStatus.needPurge;
				}
			}
					
			parttype = type;
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
		if(neststate == NestStatus.badParts || animationstate == AnimationStatus.needPurge)
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
			for(int i = 0; i<9; i++)
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
		return false;
	}

	//Actions

	private void purgeNest(){
		print("Purging");
		//Animation Call
		//gui.DoPurge(); // Run the purge animation
		
		animationstate = AnimationStatus.purging;
		for(int i = 0; i<9; i++)
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
		camera.msgImFull(parttype,this);
		neststate = NestStatus.noAction;
	}
	
	private void settleNest()
	{
		//NestSettlingAnimation
		//gui.DoSettleNest(); // Settle Nest so that the parts behind in the nest move to the front
		for(int i = 0; i<8; i++){
			if(nestslots[i] == null){
				nestslots[i] = nestslots[i+1];
				nestslots[i+1] = null;
				if(i==0)
					neststate = NestStatus.needCheck;
			}
		}
	}
	private void givePart()
	{
		partsrobot.msgHereIsPart(nestslots[0]);
		nestslots[0] = null;
		partsrobotstate = PartsRobotStatus.waitingForParts;
	}
		
	private void askForParts()
	{
		print("Please give me a part");
		lane.msgNeedThisPart (parttype);
		partsrobotstate = PartsRobotStatus.waitingForParts;
	}
	
	public String getName(){
		return name;
	}
	

	
}