package Interface.PartsRobotAgent;

import data.Part;

public interface Lane {

	public void msgReadyForPart();


	public void msgNeedThisPart(Part.PartType type);


	public void msgPartAtEndOfLane();
	
	
	
	public void msgHereIsAPart(Part p);
		

	public void msgCanIPlacePart();
	
		
	public int getNumber();
		
	}