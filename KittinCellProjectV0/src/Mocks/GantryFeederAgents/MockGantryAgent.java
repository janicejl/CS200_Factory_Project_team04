package Mocks.GantryFeederAgents;

import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class MockGantryAgent extends MockAgent implements Gantry {

	public EventLog log = new EventLog();
	String name;
	GantryController gc;
	
	public MockGantryAgent(String name) {
		super(name);
	}

	@Override
	public void msgGiveFeederParts(Feeder f1, Bin b) {
		log.add(new LoggedEvent("msgGiveFeederParts received from GantryController."));
	}

	@Override
	public void msgReadyForParts() {
		log.add(new LoggedEvent("msgReadyForParts received from Feeder"));
	}

	@Override
	public void setGantryController(GantryController gc) {
		log.add(new LoggedEvent("Gantry Controller has been set for " + this.name +"."));
		this.gc = gc;
	}

}