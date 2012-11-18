package Interface.GantryFeederAgent;

import MoveableObjects.Bin;
import data.PartInfo;

public interface Feeder {
	
	void msgNeedThisPart(PartInfo p, String laneName);
	void msgHaveParts(Gantry g1);
	void msgHereAreParts(Bin b);
	void msgHereAreParts(PartInfo p, int quantity);
	void msgLaneIsFull(String laneName);
	void msgLaneIsReadyForParts(String laneName);
	
	
	void setGantry(Gantry g1);
	void removeGantry();
	void setGantryController(GantryController gc);
	String getName();
	int getNumber();
}