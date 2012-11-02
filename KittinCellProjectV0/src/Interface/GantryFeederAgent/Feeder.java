package Interface.GantryFeederAgent;

import MoveableObjects.Part.PartType;

public interface Feeder {
	
	void msgNeedThisPart(PartType p, String laneName);
	void msgHaveParts(Gantry g1);
	void msgHereAreParts(B);
	void msgLaneIsFull(String laneName);
	void msgLaneIsReadyForParts(String laneName);
	
	
	void setGantry(Gantry g1);
	void removeGantry();
	void setGantryController(GantryController gc);
}
