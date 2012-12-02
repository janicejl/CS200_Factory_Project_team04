package Interface.VisionAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

import Agents.PartsRobotAgent.*;
import Interface.PartsRobotAgent.Nest;
import data.*;

public interface Vision{

	public void msgHereIsSchematic(List<Part> partsList, List<Nest> nestsList);
	public void msgImFull(Nest nest);
	public void msgTakePicture(Kit k);
	public void msgCameraAvailable();
	
	public void setFlashPermit(Semaphore flashpermit);
	public void msgChangeNumberOfNests();
	
	
}
