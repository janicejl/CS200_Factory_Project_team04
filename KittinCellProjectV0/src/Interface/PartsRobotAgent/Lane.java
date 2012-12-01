package Interface.PartsRobotAgent;

import data.PartInfo;

public interface Lane {

	public void msgReadyForPart();


	public void msgNeedThisPart(PartInfo type);


	public void msgPartAtEndOfLane();
	
	
	
	public void msgHereIsAPart(PartInfo p);
		

	public void msgCanIPlacePart();
	
		
	public int getNumber();


	public void msgPurge();
		
	}