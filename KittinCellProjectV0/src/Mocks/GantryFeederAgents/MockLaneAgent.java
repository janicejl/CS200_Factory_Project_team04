package Mocks.GantryFeederAgents;
import data.PartInfo;
import Interface.PartsRobotAgent.*;
import Mocks.KitRobotAgents.MockAgent;
import UnitTest.KitRobotAgents.*;

public class MockLaneAgent extends MockAgent implements Lane{

	public EventLog log = new EventLog();
	int index;
	
	public MockLaneAgent(String name, int index) {
		super(name);
		this.index = index;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgReadyForPart() {
		log.add(new LoggedEvent("Nest Ready for Part"));
	}

	@Override
	public void msgNeedThisPart(PartInfo type) {
		log.add(new LoggedEvent("Nest Wants Part"));
		
	}

	@Override
	public void msgPartAtEndOfLane() {
		log.add(new LoggedEvent("Part at End of Lane"));
		
	}

	@Override
	public void msgHereIsAPart(PartInfo p) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("msgHereIsAPart received from Feeder."));
	}

	@Override
	public void msgCanIPlacePart() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("msgCanIPlacePart received from Feeder"));
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("getNumber() called"));
		return index;
	}

}
