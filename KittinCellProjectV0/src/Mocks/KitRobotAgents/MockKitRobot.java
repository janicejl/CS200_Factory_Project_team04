package Mocks.KitRobotAgents;

import Interface.KitRobotAgent.KitRobot;
import MoveableObjects.Kit;
import UnitTest.KitRobotAgents.EventLog;

public class MockKitRobot extends MockAgent implements KitRobot{

	
	public EventLog log = new EventLog();
	
	public MockKitRobot(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAKit(Kit kit) {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Here is a kit for the robot"));
		
	}

	@Override
	public void msgMoveKitToInspection(int position) {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Move this kit to inspection"));
		
	}

	@Override
	public void msgPlaceKitAtPosition(int i) {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Place kit at position " + i) );
		
	}

}
