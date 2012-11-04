package Agents.PartsRobotAgent;

import Interface.PartsRobotAgent.*;
import Agent.*;
import MoveableObjects.*;
import java.util.*;

public class MockLaneAgentV0 extends Agent implements Lane {
	
	String name;
	
	Part.PartType parttype;
	boolean needparts = false;
	private enum LaneStatus {noAction,hasParts, giveParts}
	LaneStatus lanestate = LaneStatus.noAction;
	NestAgent nest;
	LinkedList<Part> partslist = new LinkedList<Part>();
	int count = 0;
	//LaneAgentGui gui;

	public MockLaneAgentV0(int index){
		name = "MockLaneAgent " + index;
	}
	
	public void setNest(NestAgent nest){
		this.nest = nest;
	}
	
	//Messages:

	public void msgNeedThisPart(Part.PartType type)
	{
		parttype = type;
		needparts = true;
		stateChanged();
	}
	public void msgReadyForPart()
	{
		lanestate = LaneStatus.giveParts;
		stateChanged();
	}

	//Hack for v0 since we’re not using a full laneAgent. This should be called by the animation/lanegui when a part has reached the end of the lane and is ready to enter the nest.
	public void msgPartAtEndOfLane(Part p)
	{
		partslist.add(p);
		if(partslist.size() == 1)
			lanestate = LaneStatus.hasParts;
		stateChanged();
	}
	
	
	//Scheduler:
	public boolean pickAndExecuteAnAction()
	{
		
		if(lanestate == LaneStatus.giveParts)
		{
			givePart();
			return true;
		}
		if(!partslist.isEmpty() && lanestate == LaneStatus.hasParts)
		{
			askToGivePart();
			return true;
		}
		if(needparts)
		{
			createParts();
			return true;
		}
		return false;
	}

	//Action: 

	private void createParts()
	{
		Part p = new Part(parttype);
		print("Getting parts");
		//AnimationCall, just for v0
		//gui.DoSendPartDownLane(p)
		//When done, animation should call the msgPartAtEndOfLane() method
		count++;
		if(count >= 2)
			needparts = false;
		msgPartAtEndOfLane(p);
		
	}

	private void askToGivePart()
	{
		print("Have part which I'd like to place");
		nest.msgCanIPlacePart(this);
		lanestate = LaneStatus.noAction;
	}
	
	private void givePart()
	{
		print("Giving part to Nest");
		Part p = partslist.remove();
		nest.msgHereIsPart(p);
		if(partslist.isEmpty())
			lanestate = LaneStatus.noAction;
		else
			lanestate = LaneStatus.hasParts;
	}
	
	public String getName()
	{
		return name;
	}
}