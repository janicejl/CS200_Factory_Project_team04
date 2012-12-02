package Mocks.GantryFeederAgents;

import data.PartInfo;
import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class MockGantryAgent extends MockAgent implements Gantry {

	public EventLog log = new EventLog();
	String name;
	
	public MockGantryAgent(String name) {
		super(name);
	}

	
	@Override
	public void msgReadyForParts() {
		log.add(new LoggedEvent("msgReadyForParts received from Feeder"));
	}


	@Override
	public void msgNeedThisPart(PartInfo p, Feeder feeder) {
		log.add(new LoggedEvent("msgNeedThisPart sent from Feeder " + feeder.getNumber()
				+ " requesting part " + p.getDescription()));
	}


	@Override
	public void msgAnimationHasBin() {
		log.add(new LoggedEvent("msgAnimationHasBin sent from Server"));
	}


	@Override
	public void msgNeedBinPurged(Feeder feeder) {
		log.add(new LoggedEvent("msgNeedBinPurged sent from Feeder " + feeder.getNumber()));
	}


	@Override
	public void msgGantryAtFeeder() {
		log.add(new LoggedEvent("msgGantryAtFeeder sent from Server"));
	}
	
	

	
}