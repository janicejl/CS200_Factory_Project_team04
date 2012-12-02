package Mocks.KitRobotAgents;

import Interface.KitRobotAgent.KitRobot;
import data.Kit;
import data.KitConfig;
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
	public void msgKitInspected(KitConfig kit_config) {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("message that kit was inspected was received"));
		if (kit_config.kit_state == KitConfig.KitState.GOOD) {
			log.add(new UnitTest.KitRobotAgents.LoggedEvent("Kit has been inspected and it was approved"));	
		}
		else {
			log.add(new UnitTest.KitRobotAgents.LoggedEvent("Kit has been inspected and it was not approved"));
		}
		
	}

	@Override
	public void msgGetKits(int count) {
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Received message to get " + count + " kits"));
		
	}

}
