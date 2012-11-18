package Mocks.PartsRobotAgents;
import data.Part;
import data.Part.PartType;
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
	public void msgNeedThisPart(PartType type) {
		log.add(new LoggedEvent("Nest Ready for Part"));
		
	}

	@Override
	public void msgPartAtEndOfLane(Part p) {
		log.add(new LoggedEvent("Part at End of Lane"));
		
	}

}