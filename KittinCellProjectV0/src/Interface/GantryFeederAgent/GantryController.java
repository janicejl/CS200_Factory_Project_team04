package Interface.GantryFeederAgent;

import java.util.Vector;

import MoveableObjects.Bin;
import data.PartInfo;

public interface GantryController {

	void msgBinConfiguration(Vector<Bin> bins);
	void msgGantryAdded(Gantry gantry);
	void msgNeedThisPart(PartInfo p, Feeder f1);
	void msgDoneDeliveringParts(Gantry gantry);

}