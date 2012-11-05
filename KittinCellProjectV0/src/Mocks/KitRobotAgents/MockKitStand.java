package Mocks.KitRobotAgents;


import java.util.List;

import Interface.KitRobotAgent.KitStand;
import MoveableObjects.Kit;
import MoveableObjects.Part;
import UnitTest.KitRobotAgents.EventLog;
import UnitTest.KitRobotAgents.LoggedEvent;

public class MockKitStand extends MockAgent implements KitStand{

	
	
	public EventLog log = new EventLog();
	
	public MockKitStand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgCanIPlaceKit() {
		
		log.add(new LoggedEvent("Can I place kit"));
		
	}

	@Override
	public void msgPlacingKit(Kit kit) {
		
		log.add(new LoggedEvent("Kit being placed"));
	}

	@Override
	public void msgIsThereEmptyKit() {

		log.add(new LoggedEvent("Is there an empty kit"));
		
	}

	@Override
	public void msgHereAreParts(List<Part> parts, int index) {

		log.add(new LoggedEvent("Got parts"));
		
	}

	@Override
	public void msgKitIsDone(int index) {
		log.add(new LoggedEvent("Kit is done"));
		
	}

	@Override
	public void msgKitMoved(int i) {
		log.add(new LoggedEvent("Kit moved to inspection"));
		
	}

	@Override
	public void msgKitRemovedFromInspection() {
		log.add(new LoggedEvent("Kit done with inspection"));
		
	}



}
