package Agents.KitRobotAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Agent.Agent;
import MoveableObjects.Kit;
import MoveableObjects.Part;

public class KitStandAgent extends Agent{

	
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
	
	
	enum KitStandEvent {IsEmptySpot, IsEmptyKit,RemoveKit};
	
	List<KitStandEvent> stand_events = Collections.synchronizedList( new ArrayList<KitStandEvent>());
	List<KitHolder> kit_holder_list =Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitHolder> inpspection_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	enum KitState {BeingInspected,AddParts,Empty,None,KitFinished}
//	PartsRobotAgent parts_robot;
	KitRobotAgent kit_robot;
	
	
	
	public KitStandAgent()
	{
		
	}
	
	public void msgCanIPlaceKit()
	{
		
		stand_events.add(KitStandEvent.IsEmptySpot);
		stateChanged();
	}
	
	public void msgPlacingKit(Kit k)
	{
		KitHolder kit_h = new KitHolder(k,null);
		kit_h.state = KitState.Empty;
		if(kit_holder_list.size() == 0)
		{
			kit_h.position = kit_holder_list.size();
		}
		else//need to figure out what position isnt taken
		{
			if(kit_holder_list.get(0).position == 1)
			{
				kit_h.position = 0;
			}
			else
			{
				kit_h.position = 1;
			}
		}
		kit_holder_list.add(kit_h);
		stateChanged();
	}
	
	public void msgKitIsDone(int position)
	{
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
		for(KitHolder kit_h:kit_holder_list)
		{
			if(kit_h.position == position)
			{
				inpspection_list.add(kit_h);
				kit_h.state = KitState.BeingInspected;
				kit_holder_list.remove(0);
				stateChanged();
				return;
	
			}
		}
		
		System.out.println("Error at msgKitMoved");
	}
	
	
	public void msgKitRemovedFromInspection()
	{
		stand_events.add(KitStandEvent.RemoveKit);
		stateChanged();
		
	}
	
	public void msgHereAreParts(List<Part> part_l, int position)
	{
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
					RemoveKit();
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

		return false;
	}
		
	
	
	
	
	//ACTIONS/~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	private void MoveToBeInspected(KitHolder kit_h)
	{
		kit_robot.msgMoveKitToInspection(kit_h.position);
		kit_h.state = KitState.None;
	}
	
	
	private void InspectKitByVision(KitHolder kit_h)
	{
		//vision_agent.TakePicture();
		kit_h.state = KitState.None;
	}
	
	private void RemoveKit()
	{
		inpspection_list.remove(0);		
	}
	
	private void AppPartsToKit(KitHolder kit_h)
	{
		//kit_h.kit.AddPartsPartsToKit(kit_h.parts_to_add); ANIMATION
		kit_h.state = KitState.None;
		kit_h.parts_to_add = null;
	}
	
	private void CheckForEmptyKit()
	{
		for(KitHolder kit:kit_holder_list)
		{
			if(kit.state == KitState.Empty)
			{
				//parts_robot.EmptyKitAt(kit.position);
			}
		}
		
	}
	
	
	//what do i message the robot
	private void CheckEmptySpot()
	{
		if(kit_holder_list.size() >= 2)
		{
			//message part robot there is no empty slot
		}
		else
		{
			if(kit_holder_list.size() == 0)
			{
				kit_robot.PlaceKitAtPosition(0);
			}
			else
			{
				if(kit_holder_list.get(0).position == 0)
				{
					kit_robot.PlaceKitAtPosition(1);
				}
				else
				{
					kit_robot.PlaceKitAtPosition(0);
				}
			}
		}
		
		
	}
	

}
