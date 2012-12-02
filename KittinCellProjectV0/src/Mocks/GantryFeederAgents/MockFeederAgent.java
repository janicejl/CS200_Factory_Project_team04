package Mocks.GantryFeederAgents;

import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.GantryController;
import Interface.PartsRobotAgent.Lane;
import MoveableObjects.Bin;
import data.PartInfo;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class MockFeederAgent extends MockAgent implements Feeder {

	public EventLog log = new EventLog();
	GantryController gc;
	int index;
	
	public MockFeederAgent(String name, int index) {
		super(name);
		this.index = index;
	}

	@Override
	public void msgNeedThisPart(PartInfo p, Lane lane) {
		log.add(new LoggedEvent("msgNeedThisPart received from lane " + lane.getNumber() + " requesting part type " + p + "."));
	}

	@Override
	public void msgHereAreParts(Bin b) {
		log.add(new LoggedEvent("msgHereAreParts received from gantry."));
	}

	@Override
	public void msgIsLaneReadyForParts(Lane lane) {
		log.add(new LoggedEvent("msgIsLaneReadyForParts received from lane " + lane.getNumber() +"."));
	}
	
	@Override
	public int getNumber() {
		return index;
	}

	@Override
	public void msgAmIReadyForParts() {
		log.add(new LoggedEvent("msgAmIReadyForParts received from self."));
	}

	@Override
	public void msgPartsGone() {
		log.add(new LoggedEvent("msgPartsGone received from "));
	}

	

}