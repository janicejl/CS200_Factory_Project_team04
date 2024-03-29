package Agents.KitRobotAgents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import server.Server;

import Agent.Agent;
import Interface.KitRobotAgent.KitConveyor;
import Interface.KitRobotAgent.KitRobot;
import Interface.KitRobotAgent.KitStand;
import data.Kit;
import data.KitConfig;
import data.Part;
import data.PartInfo;

public class KitRobotAgent extends Agent implements KitRobot, Serializable{


	enum KitState {BeingBuilt,WaitForKit, MoveKit , MoveToInspection, WaitingForInspection, FinishedKit, DestroyKit, MissingParts, BadParts, KitBeingFixed};
	
	enum KitRobotEvent {
		CanPlaceKit, AnimationComplete, KitArrivedAtInspection};
	
	List<KitHolder> kit_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitHolder> inspection_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitRobotEvent> event_list = Collections.synchronizedList( new ArrayList<KitRobotEvent>());
	List<KitAmountHolder> kits_needed = Collections.synchronizedList(new ArrayList<KitAmountHolder>());
	KitStand kit_stand;
	KitConveyor kit_conveyor;
	boolean b_ask_for_kit;
	Timer timer = new Timer();
	Random rn = new Random();
	Server server;
/*	TimerTask timer_task = new TimerTask(){ public void run(){ 
    	
    	b_ask_for_kit = true;
    	stateChanged();
    }
	};*/
		
	
	
	class KitHolder
	{
		public int position_on_stand;
		public KitState state;
		public Kit kit;
		public 	List<Part> parts_needed = Collections.synchronizedList(new ArrayList<Part>());

		
	}
	
	
	public KitRobotAgent(Server _server)
	{
		b_ask_for_kit = true;
		server = _server;
	}
	
	class KitAmountHolder
	{
		int kits_to_be_made;
	}
	
	
	
	public void msgGetKits(int count)
	{
		System.out.println("KitRobot: get kits");
		if(kit_list.size() == 0 )
		{
			b_ask_for_kit = true;
		}
		KitAmountHolder kit_h = new KitAmountHolder();
		kit_h.kits_to_be_made = count;
		//THIS WILL PROBALBY NEED TO BE REWORKED, depends on a new type of kit being made or a 
		kits_needed.add(kit_h);
		stateChanged();
	}
	
	public void msgHereIsAKit(Kit k)
	{	
		System.out.println("KitRobot: here is a kit");
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
		System.out.println("KitRobot: move kit to inspection");
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
	public void msgPlaceKitAtPosition(int position)
	{
		System.out.println("KitRobot: place kit at position " + position);
		event_list.add(KitRobotEvent.CanPlaceKit);
		KitHolder kit_h = new KitHolder();
		kit_h.position_on_stand = position;
		kit_h.state = KitState.WaitForKit;
		kit_list.add(kit_h);
		stateChanged();
		
	}
	
	// this will have to change to an enum or something
	public void msgKitInspected(KitConfig kit_config)
	{
		System.out.println("KitRobot: kit has been inspected");
		for(KitHolder kit_h:inspection_list)
		{
			if(kit_h.state == KitState.WaitingForInspection)
			{
				if(kit_config.kit_state == KitConfig.KitState.GOOD)
				{
					kit_h.state = KitState.FinishedKit;
					stateChanged();
					return;
				}
				else if(kit_config.kit_state == KitConfig.KitState.MISSING_PARTS)
				{
					kit_h.state = KitState.MissingParts;
					kit_h.parts_needed = kit_config.missing_part_list;
					stateChanged();
					return;
				}
				else if(kit_config.kit_state == KitConfig.KitState.BAD_PARTS)
				{
					kit_h.state = KitState.BadParts;
					stateChanged();
					return;
				}
				else if(kit_config.kit_state == KitConfig.KitState.NOT_SET)
				{
					System.out.println("Error at KitRobot msgInspected");
					return;
				}
			}
		}
	}
	
	
	public void msgKitAtInspection()
	{
		event_list.add(KitRobotEvent.KitArrivedAtInspection);
		stateChanged();
	}
	
	
	
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
			if(kit_list.size() + inspection_list.size()  < 2 && kit_list.size() < 2 && kits_needed.size() > 0 && b_ask_for_kit)
			{
				System.out.println("whyyy you no");
				CanIPlaceKit();
				return true;
			}
			
			
			if(!event_list.isEmpty())
			{
				for(KitRobotEvent event:event_list)
				{
					if(event == KitRobotEvent.KitArrivedAtInspection)
					{
						event_list.remove(event);
						TellKitStandKitArrivedAtInspection();
						return true;
					}
				}
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
			
			//if(!inspection_list.isEmpty())
			//{
				for(KitHolder kit:inspection_list)
				{
					if(kit.state == KitState.MissingParts)
					{
						MoveKitBackToStand(kit);
						return true;
					}
				}
		//	}
			
			if(!inspection_list.isEmpty())
			{
				for(KitHolder kit:inspection_list)
				{
					if(kit.state == KitState.BadParts)
					{
						MoveKitBackToStand(kit);
						return true;
					}
				}
			}
			
			if(!inspection_list.isEmpty())	
			{
				for(KitHolder kit:inspection_list)
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
	
	
	private void TellKitStandKitArrivedAtInspection()
	{
		System.out.println("KitRobot: Telling kitstand kit arrived at inspection");
		kit_stand.msgKitMoved(inspection_list.get(0).position_on_stand);
		KitConfig kit_config = new KitConfig();
		kit_config.kit_state = KitConfig.KitState.GOOD;
		//this.msgKitInspected(kit_config);
	}
	
	
	
	
	private void MoveKitBackToStand(KitHolder kit)
	{
		System.out.println("KitRobot: Moving kit back to the stand");
		if(kit.position_on_stand == 0)
		{
			server.execute("Redo 1");
		}
		else
		{
			server.execute("Redo 2");
		}
		// will need to add back to regualr list i believe	
		kit.state = KitState.KitBeingFixed;
		kit_list.add(kit);
		inspection_list.remove(kit);
		kit_stand.msgPlacingBadKit(kit.kit, kit.position_on_stand,kit.parts_needed);
	}
	
	private void FinishedKit(KitHolder kit)
	{
		server.execute("Remove Finished");
		System.out.println("KitRobot: Finished a kit!");
		kit_conveyor.msgHereIsFinishedKit(kit.kit);
		kit_list.remove(kit);
		inspection_list.remove(kit);
		kit_stand.msgKitRemovedFromInspection();
	}
	
	private void MoveKitToInspection(KitHolder kit)
	{
		if(inspection_list.size() == 0)
		{
			if(kit.position_on_stand  == 0)
			{
				server.execute("Check Kit 1");
			}
			else
			{
				server.execute("Check Kit 2");
			}
			kit_list.remove(kit);
			inspection_list.add(kit);
			
			kit.state = KitState.WaitingForInspection;
			System.out.println("KitRobot: Moving kit to inspection");
			
		}
		else
		{
			
		}
	}
	
	
	private void MoveKitToStand(KitHolder kit)
	{
		if(kit.position_on_stand  == 0)
		{
			server.execute("Load Stand 1");
		}
		else
		{
			server.execute("Load Stand 2");
		}
		
		System.out.println("KitRobot: Move kit to stand");
		kit.state = KitState.BeingBuilt;
		kit_stand.msgPlacingKit(kit.kit);
	}
	
	
	private void GiveMeAKit()
	{
		
		KitAmountHolder kit_h = kits_needed.get(0);
		
		if(kit_h.kits_to_be_made <= 1)
		{
			kits_needed.remove(kit_h);
			kit_conveyor.msgGiveMeAKit();

		}
		else
		{
			System.out.println("KitRobot: Give me a kit");
			kit_conveyor.msgGiveMeAKit();
		}
		kit_h.kits_to_be_made--;
		
	}
	
	private void CanIPlaceKit()
	{
		System.out.println("KitRobot: Can I place kit");
		kit_stand.msgCanIPlaceKit();
		b_ask_for_kit = false;
		timer.schedule(new TimerTask(){
		    public void run(){ 
		    	
		    	b_ask_for_kit = true;
		    	stateChanged();
		    }
		}, (rn.nextInt(1000)+1000));
	}
	
	public void SetConveyorAgent(KitConveyor conv)
	{
		kit_conveyor =  conv;
	}
	
	public void SetStandAgent(KitStand stand)
	{
		kit_stand = stand;
	}



	
}
