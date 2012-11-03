package Agents.PartsRobotAgent;

import java.util.List;

import Agent.Agent;
import Interface.PartsRobotAgent.Vision;
import MoveableObjects.Part.PartType;

public class MockVisionAgentV0 extends Agent implements Vision{

	PartsRobotAgent partsrobot;
	
	String name = "VisionAgent";
	
	public void setPartsRobot(PartsRobotAgent robot)
	{
		partsrobot = robot;
	}
	
	public void msgHereIsSchematic(List<PartType> recipe,
			List<PartType> nestassignments) {
		
	}


	public void msgImFull(PartType type, NestAgent nest) {
			print("Inspecting part... Part good");
			print("Part available at nest " + nest.index);
			partsrobot.msgPartsApproved(nest.index);
		
		
	}

	
	protected boolean pickAndExecuteAnAction() {
		return false;
	}

	public String getName()
	{
		return name;
	}
}
