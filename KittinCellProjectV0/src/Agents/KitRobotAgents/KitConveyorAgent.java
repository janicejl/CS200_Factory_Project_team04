package Agents.KitRobotAgents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Agent.Agent;
import Interface.KitRobotAgent.KitConveyor;
import MoveableObjects.Kit;

public class KitConveyorAgent extends Agent implements KitConveyor{

	
	enum ConveyorEvents {GetKit};
	List<ConveyorEvents> conveyor_events = Collections.synchronizedList( new ArrayList<ConveyorEvents>());
	List<Kit> finished_kit_list = Collections.synchronizedList( new ArrayList<Kit>());
	KitRobotAgent kit_robot;
	
	public KitConveyorAgent()
	{
		
	}
	
	
	public void msgGiveMeAKit()
	{
		conveyor_events.add(ConveyorEvents.GetKit);
		stateChanged();
	}
	
	public void msgHereIsFinishedKit(Kit k)
	{
		finished_kit_list.add(k);
		stateChanged();
	}
	
	
	
	@Override
	protected boolean pickAndExecuteAnAction() {


		for(ConveyorEvents event:conveyor_events)
		{
			if(event == ConveyorEvents.GetKit)
			{
				conveyor_events.remove(event);
				GiveKitToRobot();
				return true;
			}
		}
		
		for(Kit kit:finished_kit_list)
		{
			FinishedKit(kit);
			return true;
		}
		
		
		
		return false;
	}
	
	
	//ACTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void FinishedKit(Kit kit)
	{
		//Move kit off the conveyor 
		finished_kit_list.remove(kit);
	}
	
	private void GiveKitToRobot()
	{
		//PLAY ANIMATION TO MOVE KIT ONTO CONVEYOR
		Kit kit = new Kit();
		kit_robot.msgHereIsAKit(kit);
	}

}
