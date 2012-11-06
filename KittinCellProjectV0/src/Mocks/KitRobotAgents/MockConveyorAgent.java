package Mocks.KitRobotAgents;


import Interface.KitRobotAgent.KitConveyor;
import data.Kit;
import UnitTest.KitRobotAgents.EventLog;

public class MockConveyorAgent extends MockAgent implements KitConveyor{

	
	public EventLog log = new EventLog();
	
	
	public MockConveyorAgent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgHereIsFinishedKit(Kit kit) {

		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Finished Kit"));
		
	}


	@Override
	public void msgGiveMeAKit() {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Give robot a kit"));
		
	}

}
