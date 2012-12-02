package Interface.PartsRobotAgent;

import java.util.List;
import Interface.VisionAgent.*;
import Agents.PartsRobotAgent.*;
import Agents.VisionAgent.*;
import data.KitInfo;
import data.Part;
import data.PartInfo;

public interface PartsRobot {

	// messages
	public void msgMakeThisKit(KitInfo kit, int ct);
	public void msgPartsApproved(int nestindex);
	public void msgEmptyKit(int position);
	public void msgAnimationDone();
	public void msgHereIsPart(Part p);
	
	// other methods
	public void placeParts();
	public void kitFinished();
	public String getName();
	public void setTestGUI(TestGUI test);
	public void setMockVisionAgents(List <Vision> cams);
	public void msgBadKit(int position, List<Part> parts_to_add);
	
}
