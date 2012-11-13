package Interface.PartsRobotAgent;


import Agents.VisionAgent.VisionAgent;
import data.*;
import data.Part.PartType;

public interface Nest {
	
	// messages
	public void msgCanIPlacePart(Lane lane);
	public void msgHereIsPart(Part p);
	public void msgBadParts();
	public void msgGetPart();
	public void msgNeedThisPart(Part.PartType type);
	public void msgAnimationDone();
	
	// other public methods
	public String getName();
	public Integer getNumber();
	public PartType getPartType();
	public void setVisionAgent(VisionAgent camera);
	
}
