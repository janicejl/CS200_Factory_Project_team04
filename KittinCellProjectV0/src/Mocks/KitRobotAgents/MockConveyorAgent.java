package Mocks.KitRobotAgents;

import restaurant.test.EventLog;
import Interface.KitRobotAgent.KitConveyor;

public class MockConveyorAgent extends MockAgent implements KitConveyor{

	
	public EventLog log = new EventLog();
	
	
	public MockConveyorAgent(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
