package Mocks.KitRobotAgents;

import Interface.KitRobotAgent.KitRobot;
import UnitTest.KitRobotAgents.EventLog;

public class MockKitRobot extends MockAgent implements KitRobot{

	
	public EventLog log = new EventLog();
	
	public MockKitRobot(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
