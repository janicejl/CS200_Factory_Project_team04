package Interface.GantryFeederAgent;

import java.util.Vector;

import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;

public interface GantryController {

	void msgBinConfiguration(Vector<Bin> bins);
	void msgGantryAdded(Gantry gantry);
	void msgNeedThisPart(PartType p, Feeder f1);
	void msgDoneDeliveringParts(Gantry gantry);

}