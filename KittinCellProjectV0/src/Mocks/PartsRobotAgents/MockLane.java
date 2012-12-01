package Mocks.PartsRobotAgents;
import data.Part;
import data.PartInfo;
import Interface.PartsRobotAgent.*;
import Mocks.KitRobotAgents.MockAgent;
import UnitTest.KitRobotAgents.*;

public class MockLane extends MockAgent implements Lane{

	public EventLog log = new EventLog();

	
	public MockLane(String name) {
		super(name);
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
		
	}

	@Override
	public void msgCanIPlacePart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgPurge() {
		log.add(new LoggedEvent("Purge Called"));

		
	}

}
