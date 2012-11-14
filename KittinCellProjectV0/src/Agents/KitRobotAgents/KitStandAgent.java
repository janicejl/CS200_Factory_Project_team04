package Agents.KitRobotAgents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.Server;

import Agent.Agent;
import Agents.PartsRobotAgent.PartsRobotAgent;
import Interface.KitRobotAgent.KitRobot;
import Interface.KitRobotAgent.KitStand;
import Interface.PartsRobotAgent.Vision;
import data.Part;
import data.Kit;

public class KitStandAgent extends Agent implements KitStand, Serializable{

	
	class KitHolder
	{
		public Kit kit;
		public KitState state;
		public List<Part> parts_to_add;
		public int position;
		public KitHolder(Kit _kit, List<Part> _parts)
		{
			kit = _kit;
			parts_to_add = _parts;
			state = KitState.None;
		}
	}

	enum KitStandEvent {IsEmptySpot, IsEmptyKit,RemoveKit,KitRemoved, SpotJustOpened, WaitingForSpot};
	List<KitStandEvent> stand_events = Collections.synchronizedList( new ArrayList<KitStandEvent>());
	List<KitHolder> kit_holder_list =Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitHolder> inpspection_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<Integer> robot_waiting_for_kit = Collections.synchronizedList( new ArrayList<Integer>());
	//List<Integer> robot_waiting_for_kit = Collections.synchronizedList( new ArrayList<Integer>());
	enum KitState {BeingInspected,AddParts,Empty,None,KitFinished, NeedKit, WaitinForInspectionQueueToClear,BeingUsed}


	PartsRobotAgent parts_robot;
	KitRobot kit_robot;
	Vision vision;
	Server server;
	
	
	public KitStandAgent(Server _server)
	{
		server = _server;
	}
	


	public void msgInspectionSlotIsClear()
	{
		stand_events.add(KitStandEvent.KitRemoved);
		stateChanged();
	}
	
	public void msgCanIPlaceKit()
	{
		System.out.println("KitStand: Can a kit be placed?");
		stand_events.add(KitStandEvent.IsEmptySpot);
		stateChanged();
	}
	
	
	public void msgPlacingKit(Kit k)
	{
		System.out.println("KitStand:: kit being placed");
		
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.state == KitState.NeedKit)
			{
				kit_h.kit = k;
				kit_h.state = KitState.Empty;
				stateChanged();
				return;
			}
		}
	}
	
	public void msgKitIsDone(int position)
	{
		System.out.println("KitStand:: kit is done!");
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.position == position)
			{
				kit_h.state = KitState.KitFinished;
				stateChanged();
				return;
			}
			
		}
		System.out.println("Error at msgKitIsDone");
	}
	
	
	public void msgKitMoved(int position)
	{
		System.out.println("KitStand: Kit being moved");
		stand_events.add(KitStandEvent.SpotJustOpened);
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.position == position)
			{
				inpspection_list.add(kit_h);
				kit_h.state = KitState.BeingInspected;
				kit_holder_list.remove(kit_h);
				stateChanged();
				return;
	
			}
		}
		
		System.out.println("Error at msgKitMoved");
	}
	
	
	public void msgKitRemovedFromInspection()
	{
		System.out.println("KitStand: kit removed from inspection");
		stand_events.add(KitStandEvent.RemoveKit);
		stateChanged();
		
	}
	
	public void msgHereAreParts(List<Part> part_l, int position)
	{
		System.out.println("KitStand: Parts to add for a kit");
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.position == position)
			{
				kit_h.state = KitState.AddParts;
				kit_h.parts_to_add = part_l;
				stateChanged();
				return;
			}
		}
		System.out.println("Error at msgHereAreParts");
	}
	
	public void msgIsThereEmptyKit()
	{
		System.out.println("KitStand: Is there and empty kit?");
		stand_events.add(KitStandEvent.IsEmptyKit);
		stateChanged();
	}
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {

		if(!stand_events.isEmpty())
		{
			for(KitStandEvent event:stand_events)
			{
				if(event == KitStandEvent.IsEmptyKit)
				{
					stand_events.remove(event);
					CheckForEmptyKit();
					return true;
				}
			}
		}
		
		if(!stand_events.isEmpty())
		{
			for(KitStandEvent event:stand_events)
			{
				if(event == KitStandEvent.RemoveKit)
				{
					stand_events.remove(event);
					RemoveKit();
					return true;
				}
			}
		}
		
		if(!stand_events.isEmpty())
		{
			for(KitStandEvent event:stand_events)
			{
				if(event == KitStandEvent.KitRemoved)
				{
					stand_events.remove(event);
					CheckForQueuedFinishedKits();
					return true;
				}
			}
		}
		

		if(!stand_events.isEmpty())
		{
			for(KitStandEvent event:stand_events)
			{
				if(event == KitStandEvent.IsEmptySpot)
				{
					stand_events.remove(event);
					CheckEmptySpot();
					return true;
				}
			}
		}
		
		
		if(!kit_holder_list.isEmpty())
		{
			for(KitHolder kit:kit_holder_list)
			{
				if(kit.state == KitState.AddParts)
				{
					AppPartsToKit(kit);
					return true;
				}
			}
		}
			
		if(!inpspection_list.isEmpty())
		{
			for(KitHolder kit:inpspection_list)
			{
				if(kit.state == KitState.BeingInspected)
				{
					InspectKitByVision(kit);
					return true;
				}
			}
		}
			
		if(!kit_holder_list.isEmpty())
		{
			for(KitHolder kit:kit_holder_list)
			{
				if(kit.state == KitState.KitFinished)
				{
					MoveToBeInspected(kit);
					return true;
				}
			}
		}
		
		if(!stand_events.isEmpty())
		{
			for(KitStandEvent event:stand_events)
			{
				if(event == KitStandEvent.SpotJustOpened)
				{
					stand_events.remove(event);
					
					return true;
				}
			}
		}
		if(!kit_holder_list.isEmpty())
		{
			for(KitHolder kit_h:kit_holder_list)
			{
				if(kit_h.state == KitState.Empty)
				{
					CheckForEmptyKit();
					return true;
				}
			}
		}
	

		return false;
	}
		
	
	
	
	
	//ACTIONS/~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	

	private void CheckForQueuedFinishedKits()
	{
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.state == KitState.WaitinForInspectionQueueToClear)
			{
				kit_robot.msgMoveKitToInspection(kit_h.position);
				kit_h.state = KitState.None;
				return;
			}
		}
	}
	
	private void MoveToBeInspected(KitHolder kit_h)
	{
		System.out.println("KitStand: Telling robot to move kit");
		if(inpspection_list.size() == 0)
		{
			kit_robot.msgMoveKitToInspection(kit_h.position);
			kit_h.state = KitState.None;
		}
		else
		{
			
			kit_h.state = KitState.WaitinForInspectionQueueToClear;
		}
	}
	
	
	private void InspectKitByVision(KitHolder kit_h)
	{
		server.execute("Take Picture");
		//vision.msgTakePicture(kit_h.kit);
		kit_h.state = KitState.None;
		System.out.println("KitStand: Inspect kit by vision");
	}
	
	private void RemoveKit()
	{
		System.out.println("KitStand: Removing kit");
		msgInspectionSlotIsClear();
		inpspection_list.remove(0);
	}
	
	private void AppPartsToKit(KitHolder kit_h)
	{
		for(Part part:kit_h.parts_to_add)
		{
			kit_h.kit.addPart(part);
		}
		System.out.println("KitStand: Add Parts to kit");
		kit_h.state = KitState.None;
		kit_h.parts_to_add = null;
	}
	
	private void CheckForEmptyKit()
	{
		System.out.println("KitStand: Check for empty kit");
		for(KitHolder kit:kit_holder_list)
		{
			if(kit.state == KitState.Empty)
			{
				kit.state = KitState.BeingUsed;
				parts_robot.msgEmptyKit(kit.position);
				return;
			}
		}
		
		if(kit_holder_list.isEmpty())
		{
			//robot_waiting_for_kit.add(1);
		}
		
	}
	
	//what do i message the robot
	private void CheckEmptySpot()
	{
		
		System.out.println("KitStand: Check for an empty spot on stand");
		if(kit_holder_list.size() >= 2)
		{
			System.out.println("WTF");
		}
		else
		{
			KitHolder kit_h = new KitHolder(null,null);
			if(kit_holder_list.size() == 0)
			{
				kit_robot.msgPlaceKitAtPosition(0);
				kit_h.state = KitState.NeedKit;
				kit_h.position = 0;
				kit_holder_list.add(kit_h);
			}
			else
			{
				if(kit_holder_list.get(0).position == 0)
				{
					kit_robot.msgPlaceKitAtPosition(1);
					kit_h.state = KitState.NeedKit;
					kit_h.position = 1;
					kit_holder_list.add(kit_h);
				}
				else
				{
					kit_robot.msgPlaceKitAtPosition(0);
					kit_h.state = KitState.NeedKit;
					kit_h.position = 0;
					kit_holder_list.add(kit_h);
				}
			}
		}
		
		
	}


	//Setters



	public void SetRobotAgent(KitRobot robot)
	{
		kit_robot = robot;
	}

	public void SetPartsRobotAgent(PartsRobotAgent robot)
	{
		parts_robot = robot;
	}
	
	
	public void SetVisionAgent(Vision vision_)
	{
		vision = vision_;
	}
	
	
		
}
	
	
	

