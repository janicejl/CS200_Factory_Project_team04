package Mocks.KitRobotAgents;

import Interface.KitRobotAgent.KitRobot;
import data.Kit;
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

	@Override
	public void msgKitInspected(boolean bis_good) {
		if (bis_good == true) {
			log.add(new UnitTest.KitRobotAgents.LoggedEvent("Kit has been inspected and it was approved"));	
		}
		else {
			log.add(new UnitTest.KitRobotAgents.LoggedEvent("Kit has been inspected and it was not approved"));
		}
		
	}

	@Override
	public void msgGetKits(int count) {
		// TODO Auto-generated method stub
		
	}

}
