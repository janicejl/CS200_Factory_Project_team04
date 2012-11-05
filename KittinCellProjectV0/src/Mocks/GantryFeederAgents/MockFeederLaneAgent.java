package Mocks.GantryFeederAgents;

import Interface.GantryFeederAgent.FeederLane;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;


public class MockFeederLaneAgent extends MockAgent implements FeederLane {

	public EventLog log = new EventLog();
	String name;
	
	public MockFeederLaneAgent(String name) {
		super(name);
	}

	@Override
	public void msgHereIsAPart() {
		log.add(new LoggedEvent("msgHereIsAPart received from Feeder."));
	}

	@Override
	public void msgRemovePart() {
		log.add(new LoggedEvent("msgRemovePart received from my internal timer."));
	}

	@Override
	public void setMaxQuantity(int quantity) {
		log.add(new LoggedEvent("maxQuantity updated to " + quantity + "."));
	}

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}