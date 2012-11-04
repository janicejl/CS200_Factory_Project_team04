package Interface.GantryFeederAgent;

import MoveableObjects.Bin;

public interface Gantry {
	
	void msgGiveFeederParts(Feeder f1, Bin b);
	void msgReadyForParts();
}
