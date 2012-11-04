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
import MoveableObjects.Kit;

public class KitRobotAgent extends Agent implements KitRobot, Serializable{


	enum KitState {BeingBuilt,WaitForKit, MoveKit , MoveToInspection, WaitingForInspection, FinishedKit, DestroyKit};
	
	enum KitRobotEvent {
		CanPlaceKit};
	
	List<KitHolder> kit_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitHolder> inspection_list = Collections.synchronizedList( new ArrayList<KitHolder>());
	List<KitRobotEvent> event_list = Collections.synchronizedList( new ArrayList<KitRobotEvent>());
	int kits_needed;
	KitStand kit_stand;
	KitConveyor kit_conveyor;
	boolean b_ask_for_kit;
	Timer timer = new Timer();
	Random rn = new Random();
	Server server;
	
	class KitHolder
	{
		public int position_on_stand;
		public KitState state;
		public Kit kit;
		
	}
	
	
	public KitRobotAgent(Server _server)
	{
		b_ask_for_kit = true;
		server = _server;
	}
	
	
	
	public void msgGetKits(int count)
	{
		System.out.println("KitRobot: get kits");
		//THIS WILL PROBALBY NEED TO BE REWORKED, depends on a new type of kit being made or a 
		kits_needed = count;
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
		System.out.println("KitRobot: place kit an position");
		event_list.add(KitRobotEvent.CanPlaceKit);
		KitHolder kit_h = new KitHolder();
		kit_h.position_on_stand = position;
		kit_h.state = KitState.WaitForKit;
		kit_list.add(kit_h);
		stateChanged();
		
	}
	
	
	public void msgKitInspected(boolean bis_good)
	{
		System.out.println("KitRobot: kit has been inspected");
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
	public boolean pickAndExecuteAnAction() {

	
		
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
			
			
			if(kit_list.size() < 2 && kits_needed > 0 && b_ask_for_kit)
			{
				CanIPlaceKit();
				return true;
			}
			
		return false;
	}
	
	//ACTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	
	private void FinishedKit(KitHolder kit)
	{
		server.execute("Remove Finished");
		System.out.println("KitRobot: Finished a kit!");
		kit_stand.msgKitRemovedFromInspection();
		kit_conveyor.msgHereIsFinishedKit(kit.kit);
		kit_list.remove(kit);
	}
	
	private void MoveKitToInspection(KitHolder kit)
	{
		if(kit.position_on_stand  == 0)
		{
			server.execute("Check Kit 1");
			kit_stand.msgKitMoved(0);
		}
		else
		{
			server.execute("Check Kit 2");
			kit_stand.msgKitMoved(1);
		}
		kit.state = KitState.WaitingForInspection;
		System.out.println("KitRobot: Moving kit to inspection");
		
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
		kits_needed--;
		System.out.println("KitRobot: Give me a kit");
		kit_conveyor.msgGiveMeAKit();
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
		}, (rn.nextInt(5000)+5000));
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
