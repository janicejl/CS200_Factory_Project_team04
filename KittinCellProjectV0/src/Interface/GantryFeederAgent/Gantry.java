package Interface.GantryFeederAgent;

import data.PartInfo;

public interface Gantry {
	
	public void msgNeedThisPart(PartInfo p, Feeder feeder);
	public void msgAnimationHasBin();
	public void msgReadyForParts();
	public void msgNeedBinPurged(Feeder feeder);
}