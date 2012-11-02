package Interface.GantryFeederAgent;

import java.util.ArrayList;

import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;

public interface GantryController {
	
	void msgBinConfiguration(ArrayList<Bin> bins, ArrayList<Gantry> gantries);
	void msgGantryAdded(Gantry gantry);
	void msgNeedThisPart(PartType p, Feeder f1);
	void msgDoneDeliveringParts(Gantry gantry);

}
