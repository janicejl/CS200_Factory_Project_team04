package Interface.PartsRobotAgent;

import java.util.List;
import Agents.PartsRobotAgent.*;
import Agents.VisionAgent.*;
import data.Part;

public interface PartsRobot {

	// messages
	public void msgMakeThisKit(List<Part.PartType> kitrecipe, int ct);
	public void msgPartsApproved(int nestindex);
	public void msgEmptyKit(int position);
	public void msgAnimationDone();
	public void msgHereIsPart(Part p);
	
	// other methods
	public void placeParts();
	public void kitFinished();
	public String getName();
	public void setTestGUI(TestGUI test);
	public void setVisionAgents(List <VisionAgent> cams);
	
}
