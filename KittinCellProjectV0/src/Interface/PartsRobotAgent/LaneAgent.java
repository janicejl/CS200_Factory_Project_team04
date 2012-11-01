package Interface.PartsRobotAgent;
import MoveableObjects.*;

public interface LaneAgent {


public void msgReadyForPart();


public void msgNeedThisPart(Part.PartType type);


//Hack for v0 since we’re not using a full laneAgent. This should be called by the animation/lanegui when a part has reached the end of the lane and is ready to enter the nest.
public void msgPartAtEndOfLane(Part p);
	
}

