package Interface.GantryFeederAgent;

import Agents.GantryFeederAgents.GantryAgent;
import Interface.PartsRobotAgent.Lane;
import MoveableObjects.Bin;
import data.PartInfo;

public interface Feeder {
	
	public void msgNeedThisPart(PartInfo p, Lane lane);
	public void msgHereAreParts(Bin bin);
	public void msgAmIReadyForParts();
	public void msgPartsGone();
	public void msgIsLaneReadyForParts(Lane lane_);
	public int getNumber();
	
}