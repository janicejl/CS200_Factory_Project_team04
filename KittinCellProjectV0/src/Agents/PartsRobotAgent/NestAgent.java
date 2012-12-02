package Agents.PartsRobotAgent;

import  Agent.*;
import Agents.VisionAgent.VisionAgent;
import server.Server;
import data.*;
import java.util.*;


import Interface.PartsRobotAgent.*;
import Interface.VisionAgent.Vision;

public class NestAgent extends Agent implements Nest{

	//Data
	public PartInfo partinfo;
	public Part[] nestslots = new Part[8];
	public List<Part> extraparts = new ArrayList<Part>();
	PartsRobot partsrobot;
	Lane lane;
	Vision camera;
	String name;
	Server server;
		
	boolean allowextraparts = false;
	public int index;
	Random generator = new Random();
	

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
		boolean acceptedpart = false;
			for(int i = 0; i < 8; i++)
			{
				if(nestslots[i]== null)
				{
					nestslots[i] = p;
					acceptedpart =true;
					if(i == 0)
						neststate = NestStatus.needCheck;
					break;
				}
			}
			if(!acceptedpart){
				extraparts.add(p);
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
					if(!nestslots[i].info.getName().equals(p.getName()))
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
	public void msgToggleNestOverflow(){
		if(!allowextraparts){
		allowextraparts = true;
		}
		else{
			allowextraparts = false;
		}
		stateChanged();
	}
	

	
	//Scheduler:
	
	public boolean pickAndExecuteAnAction()
	{		
		
		if(neststate == NestStatus.badParts || animationstate == AnimationStatus.needPurge)
		{
			purgeNest();
			return true;
		}
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
		
		if(partsrobotstate == PartsRobotStatus.wantsParts)
		{
			askForParts();
			return true;
		}
		
		if(lanestate == LaneStatus.hasPart)
		{
			for(int i = 0; i<8; i++)
			{
				if(nestslots[i] == null)
				{
				acceptPart();
				return true;
				}
			}
		}
		if(lanestate == LaneStatus.hasPart){
			int x = generator.nextInt(2);
			if(x==1 && allowextraparts){
				acceptPart();
				return true;
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
		server.execute("Purge Nest", index);
		
		for(int i = 0; i<8; i++)
		{
			nestslots[i] = null;
		}
		extraparts.clear();
		lane.msgPurge();
		animationstate = AnimationStatus.noAction;

	}

	private void acceptPart()
	{
		
		print("Ready to take the part" + lanestate);
		lanestate = LaneStatus.noAction;
		lane.msgReadyForPart();
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
		boolean stillempty = true;
		for(int i = 0; i<8; i++){
			if(nestslots[i]!= null){
				stillempty = false;
			}
		}
		if(stillempty){
			askForParts();
		}
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
