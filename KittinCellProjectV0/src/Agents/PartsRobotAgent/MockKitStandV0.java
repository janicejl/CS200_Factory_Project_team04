package Agents.PartsRobotAgent;

import java.util.List;

import Interface.KitRobotAgent.*;
import Agent.*;
import data.Kit;
import data.Part;


public class MockKitStandV0 extends Agent implements KitStand {
	
	PartsRobotAgent partsrobot;
	boolean kit1 = true;
	
	public void setPartsRobot(PartsRobotAgent robot)
	{
		partsrobot = robot;
	}
	
	public void msgCanIPlaceKit(){
		
	}

	public void msgPlacingKit(Kit kit){}
	
	public void msgIsThereEmptyKit()
	{
		if(kit1)
		{
			print("Kit 1 Available");
			partsrobot.msgEmptyKit(1);
			kit1 = false;
		}
		else
		{
			print("Kit 2 Available");
			partsrobot.msgEmptyKit(2);
			kit1 = true;
		}
		
	}
	
	public boolean pickAndExecuteAnAction(){
		return false;
	}

	
	public void msgHereAreParts(List<Part> parts, int index) {
		
		
	}

	@Override
	public void msgKitIsDone(int index) {
		print("Removing kit at index" + index);
		
	}

	@Override
	public void msgKitMoved(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgKitRemovedFromInspection() {
		// TODO Auto-generated method stub
		
	}
	
}
