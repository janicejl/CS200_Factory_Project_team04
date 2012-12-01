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
	//public void msgLaneIsFull(Lane lane_);



	/*void msgNeedThisPart(PartInfo p, Lane lane);
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
	int getNumber();*/
}