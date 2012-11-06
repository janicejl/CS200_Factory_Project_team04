package Agents.PartsRobotAgent;

import java.util.List;

import Agent.Agent;
import Interface.PartsRobotAgent.Vision;
import data.Part.PartType;

public class MockVisionAgentV0 extends Agent implements Vision{

	PartsRobotAgent partsrobot;
	
	String name = "VisionAgent";
	
	int nestindex=-1;
	
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
			nestindex = nest.index;
			stateChanged();
		
		
	}

	
	protected boolean pickAndExecuteAnAction() {
		if(nestindex>-1)
		{
			partsrobot.msgPartsApproved(nestindex);
			nestindex = -1;
			return true;
		}
		
		return false;
	}

	public String getName()
	{
		return name;
	}
}
