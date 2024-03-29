package Interface.PartsRobotAgent;


import java.awt.Component;
import java.util.ArrayList;

import Agents.VisionAgent.VisionAgent;
import data.*;

public interface Nest {
	// public int index=0;
	public PartInfo partinfo= null;
	
	// messages
	public void msgCanIPlacePart(Lane lane);
	public void msgHereIsPart(Part p);
	public void msgBadParts();
	public void msgGetPart();
	public void msgNeedThisPart(PartInfo p);
	public void msgAnimationDone();
	
	// other public methods
	public String getName();
	public Integer getNumber();
	public PartInfo getPartInfo();
	public void setVisionAgent(VisionAgent camera);
	public Integer getIndex();
	public boolean pickAndExecuteAnAction();
	public void msgPurge();
	
}
