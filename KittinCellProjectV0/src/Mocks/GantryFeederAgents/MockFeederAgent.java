package Mocks.GantryFeederAgents;

import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import MoveableObjects.Bin;
import data.Part.PartType;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class MockFeederAgent extends MockAgent implements Feeder {

	public EventLog log = new EventLog();
	GantryController gc;
	
	public MockFeederAgent(String name) {
		super(name);
	}

	@Override
	public void msgNeedThisPart(PartType p, String laneName) {
		log.add(new LoggedEvent("msgNeedThisPart received from lane " + laneName + " requesting part type " + p + "."));
	}

	@Override
	public void msgHaveParts(Gantry g1) {
		log.add(new LoggedEvent("msgHaveParts received from gantry " + g1.getName() + "."));
		
	}

	@Override
	public void msgHereAreParts(Bin b) {
		log.add(new LoggedEvent("msgHereAreParts received from gantry."));
	}

	@Override
	public void msgHereAreParts(PartType p, int quantity) {
		log.add(new LoggedEvent("msgHereAreParts received from GUI"));
	}
	
	@Override
	public void msgLaneIsFull(String laneName) {
		log.add(new LoggedEvent("msgLaneIsFull received from lane " + laneName + "."));
	}

	@Override
	public void msgLaneIsReadyForParts(String laneName) {
		log.add(new LoggedEvent("msgLaneIsReadyForParts received from lane " + laneName +"."));
	}
	
	@Override
	public void setGantry(Gantry g1) {
		log.add(new LoggedEvent("Gantry " + g1.getName() + "set to gantry."));
		
	}

	@Override
	public void removeGantry() {
		log.add(new LoggedEvent("Gantry removed."));
		
	}

	@Override
	public void setGantryController(GantryController gc) {
		this.gc = gc;
	}

	

}