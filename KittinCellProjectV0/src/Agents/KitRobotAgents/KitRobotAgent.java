package Agents.KitRobotAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Agent.Agent;
import MoveableObjects.Kit;
import MoveableObjects.Part;

public class KitRobotAgent extends Agent{


	enum KitState {WaitForKit, MoveKit , MoveToInspection, WaitingForInspection, FinishedKit, DestroyKit};
	
	enum KitRobotEvent {
		CanPlaceKit};
	
	List<KitHolder> kit_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitHolder> inspection_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitRobotEvent> event_list = Collections.synchronizedList( new ArrayList<KitRobotEvent>());
	int kits_needed;
	KitStandAgent kit_stand;
	KitConveyorAgent kit_conveyor;
	
	
	class KitHolder
	{
	//	public List<Part> part_list;
		public int position_on_stand;
		public KitState state;
		public Kit kit;
		
	}
	
	
	public KitRobotAgent()
	{
		
		
	}
	
	
	
	public void msgGetKits(int count)
	{
		//THIS WILL PROBALBY NEED TO BE REWORKED, depends on a new type of kit being made or a 
		kits_needed = 0;
		stateChanged();
	}
	
	public void msgHereIsAKit(Kit k)
	{	
		for(KitHolder kit_h:kit_list)
		{
			if(kit_h.state == KitState.WaitForKit)
			{
				kit_h.state = KitState.MoveKit;
				kit_h.kit = k;
				stateChanged();
				return;	
			}
		}
		
	}
	
	public void msgMoveKitToInspection(int i)
	{
		for(KitHolder kit_h:kit_list)
		{
			if(kit_h.position_on_stand == i)
			{
				kit_h.state = KitState.MoveToInspection;
				stateChanged();
				return;			
			}
		}
	}
	//telling where to place a kit
	public void PlaceKitAtPosition(int position)
	{
		event_list.add(KitRobotEvent.CanPlaceKit);
		KitHolder kit_h = new KitHolder();
		kit_h.position_on_stand = position;
		kit_h.state = KitState.WaitForKit;
		stateChanged();
		
	}
	
	
	public void msgKitInspected(boolean bis_good)
	{
		for(KitHolder kit_h:kit_list)
		{
			if(kit_h.state == KitState.WaitingForInspection)
			{
				if(bis_good)
				{
					kit_h.state = KitState.FinishedKit;
					stateChanged();
					return;
				}
				else
				{
					kit_h.state = KitState.DestroyKit;
					stateChanged();
					return;
				}
			}
		}
	}
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {


		
			if(kit_list.size() < 2 && kits_needed > 0)
			{
				CanIPlaceKit();
				return true;
			}
		
			if(!event_list.isEmpty())
			{
				for(KitRobotEvent event:event_list)
				{
					if(event == KitRobotEvent.CanPlaceKit)
					{
						event_list.remove(event);
						GiveMeAKit();
						return true;
					}
				}
			}
			
			if(!kit_list.isEmpty())
			{
				for(KitHolder kit:kit_list)
				{
					if(kit.state == KitState.MoveKit)
					{
						MoveKitToStand(kit);
						return true;
					
					}
				}
			}
			
			if(!kit_list.isEmpty())
			{
				for(KitHolder kit:kit_list)
				{
					if(kit.state == KitState.MoveToInspection)
					{
						MoveKitToInspection(kit);
						return true;
					}
					
				}
			}
			
			if(!kit_list.isEmpty())
			{
				for(KitHolder kit:kit_list)
				{
					if(kit.state == KitState.FinishedKit)
					{
						FinishedKit(kit);
						return true;
					}
				}
			}
		return false;
	}
	
	//ACTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	
	private void FinishedKit(KitHolder kit)
	{
		//MOVE KIT TO CONVEYOR
		kit_conveyor.msgHereIsFinishedKit(kit.kit);
		kit_list.remove(kit);
	}
	
	private void MoveKitToInspection(KitHolder kit)
	{
		//PLAY ANIMATION TO MOVE THE KIT FROM THE STAND TO INSPECTION STAND	
		kit.state = KitState.WaitingForInspection;
	}
	
	
	private void MoveKitToStand(KitHolder kit)
	{
		//PLAY ANIMATION TO MOVE KIT FROM CONVEYOR TO STAND
	}
	
	
	private void GiveMeAKit()
	{
		kits_needed--;
		kit_conveyor.msgGiveMeAKit();
		
	}
	
	
	private void CanIPlaceKit()
	{
		kit_stand.msgCanIPlaceKit();
	}
	


}
