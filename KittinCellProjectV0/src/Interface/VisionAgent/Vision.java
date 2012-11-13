package Interface.VisionAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

import Agents.PartsRobotAgent.*;
import data.*;

public interface Vision{

	public void msgHereIsSchematic(List<Part> partsList, List<NestAgent> nestsList);
	public void msgImFull(NestAgent nest);
	public void msgTakePicture(Kit k);
	public void msgAnimationDone();
	
	public void setFlashPermit(Semaphore flashpermit);
	
	
}
