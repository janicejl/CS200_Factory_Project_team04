package Mocks.KitRobotAgents;


import Interface.KitRobotAgent.KitConveyor;
import UnitTest.KitRobotAgents.EventLog;

public class MockConveyorAgent extends MockAgent implements KitConveyor{

	
	public EventLog log = new EventLog();
	
	
	public MockConveyorAgent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
