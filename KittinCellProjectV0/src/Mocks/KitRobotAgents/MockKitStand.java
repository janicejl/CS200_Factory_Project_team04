package Mocks.KitRobotAgents;

import restaurant.test.EventLog;
import Interface.KitRobotAgent.KitStand;

public class MockKitStand extends MockAgent implements KitStand{

	
	
	public EventLog log = new EventLog();
	
	public MockKitStand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
