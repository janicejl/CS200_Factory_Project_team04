package Mocks.KitRobotAgents;


import Interface.KitRobotAgent.KitStand;
import UnitTest.KitRobotAgents.EventLog;

public class MockKitStand extends MockAgent implements KitStand{

	
	
	public EventLog log = new EventLog();
	
	public MockKitStand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
