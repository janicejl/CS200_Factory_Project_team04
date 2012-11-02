package Agents.KitRobotAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.Server;

import Agent.Agent;
import Interface.KitRobotAgent.KitConveyor;
import Interface.KitRobotAgent.KitRobot;
import MoveableObjects.Kit;

public class KitConveyorAgent extends Agent implements KitConveyor{

	
	enum ConveyorEvents {GetKit};
	List<ConveyorEvents> conveyor_events = Collections.synchronizedList( new ArrayList<ConveyorEvents>());
	List<Kit> finished_kit_list = Collections.synchronizedList( new ArrayList<Kit>());
	KitRobot kit_robot;
	Server server;
	
	public KitConveyorAgent(Server _server)
	{
		server = _server;
	}
	
	
	public void msgGiveMeAKit()
	{
		System.out.println("KitConveyor: Conveyor got a request for a kit");
		conveyor_events.add(ConveyorEvents.GetKit);
		stateChanged();
	}
	
	public void msgHereIsFinishedKit(Kit k)
	{
		System.out.println("KitConveyor: Conveyor got a finished kit");
		finished_kit_list.add(k);
		stateChanged();
	}
	
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {

		if(!conveyor_events.isEmpty())
		{
			for(ConveyorEvents event:conveyor_events)
			{
				if(event == ConveyorEvents.GetKit)
				{
					conveyor_events.remove(event);
					GiveKitToRobot();
					return true;
				}
			}
		}
		if(!finished_kit_list.isEmpty())
		{
			for(Kit kit:finished_kit_list)
			{
				FinishedKit(kit);
				return true;
			}
		}
		
		return false;
	}
	
	
	//ACTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void FinishedKit(Kit kit)
	{
		//Move kit off the conveyor 
		System.out.println("KitConveyor: Moving finished kit off conveyor");
		finished_kit_list.remove(kit);
	}
	
	private void GiveKitToRobot()
	{
		//PLAY ANIMATION TO MOVE KIT ONTO CONVEYOR
		System.out.println("KitConveyor: Moving empty kit onto conveyor");
		server.execute("Spawn Kit", 1);
		Kit kit = new Kit();
		kit_robot.msgHereIsAKit(kit);
	}

	
	public void SetKitRobot(KitRobotAgent robot)
	{
		kit_robot = robot;
	}
}
