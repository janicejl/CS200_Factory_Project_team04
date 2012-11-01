package Agents.PartsRobotAgent;

import Interface.PartsRobotAgent.*;
import Agent.*;
import MoveableObjects.*;
import java.util.*;

public class MockLaneAgentV0 extends Agent implements LaneAgent {
	
	Part.PartType parttype;
	boolean needparts = false;
	boolean giveparts = false;
	NestAgent nest;
	LinkedList<Part> partslist = new LinkedList<Part>();
	int count = 0;
	//LaneAgentGui gui;

	public MockLaneAgentV0(NestAgent nest){
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
		giveparts = true;
		stateChanged();
	}

	//Hack for v0 since we’re not using a full laneAgent. This should be called by the animation/lanegui when a part has reached the end of the lane and is ready to enter the nest.
	public void msgPartAtEndOfLane(Part p)
	{
		partslist.add(p);
		stateChanged();
	}
	
	
	//Scheduler:
	public boolean pickAndExecuteAnAction()
	{
		
		if(giveparts)
		{
			givePart();
			return true;
		}
		if(!partslist.isEmpty())
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
		Part p = new Part();
		p.type = parttype;
		print("Getting parts");
		//AnimationCall, just for v0
		//gui.DoSendPartDownLane(p)
		//When done, animation should call the msgPartAtEndOfLane() method
		count++;
		if(count >= 8)
			needparts = false;
		msgPartAtEndOfLane(p);
		
	}

	private void askToGivePart()
	{
		print("Have part which I'd like to place");
		nest.msgCanIPlacePart(this);
	}
	private void givePart()
	{
		print("Giving part to Nest");
		nest.msgHereIsPart(partslist.removeFirst());
	}
}