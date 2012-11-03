package Interface.PartsRobotAgent;

import java.util.List;

import Agents.PartsRobotAgent.NestAgent;
import MoveableObjects.Part;

public interface Vision {

	public void msgHereIsSchematic(List<Part.PartType> recipe, List<Part.PartType> nestassignments);
	public void msgImFull(Part.PartType type, NestAgent nest);

	
	
}
