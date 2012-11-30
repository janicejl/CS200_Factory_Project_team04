package Interface.GantryFeederAgent;

import Interface.PartsRobotAgent.Lane;
import MoveableObjects.Bin;
import data.PartInfo;

public interface Feeder {
	
	void msgNeedThisPart(PartInfo p, Lane lane);
	void msgHaveParts(Gantry g1);
	void msgHereAreParts(Bin b);
	void msgHereAreParts(PartInfo p, int quantity);
	void msgLaneIsFull(String laneName);
	void msgLaneIsReadyForParts(String laneName);
	//void msgLaneAnimationDone();
	//void msgFeederAnimationDone();
	
	
	void setGantry(Gantry g1);
	void removeGantry();
	String getName();
	int getNumber();
}