package Mocks.KitRobotAgents;


import Interface.KitRobotAgent.KitStand;
import MoveableObjects.Kit;
import UnitTest.KitRobotAgents.EventLog;

public class MockKitStand extends MockAgent implements KitStand{

	
	
	public EventLog log = new EventLog();
	
	public MockKitStand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgCanIPlaceKit() {
		System.out.println("hmmm");
		log.add(new UnitTest.KitRobotAgents.LoggedEvent("Can I place kit"));
		
	}

	@Override
	public void msgPlacingKit(Kit kit) {
		// TODO Auto-generated method stub
		
	}

}
