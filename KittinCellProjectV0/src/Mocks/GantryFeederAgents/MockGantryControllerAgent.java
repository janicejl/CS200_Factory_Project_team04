package Mocks.GantryFeederAgents;

import java.util.Vector;

import Interface.GantryFeederAgent.Feeder;
import Interface.GantryFeederAgent.Gantry;
import Interface.GantryFeederAgent.GantryController;
import Mocks.KitRobotAgents.MockAgent;
import MoveableObjects.Bin;
import MoveableObjects.Part.PartType;
import UnitTest.GantryFeederAgents.EventLog;
import UnitTest.GantryFeederAgents.LoggedEvent;

public class MockGantryControllerAgent extends MockAgent implements
		GantryController {

	public EventLog log = new EventLog();
	
	public MockGantryControllerAgent(String name) {
		super(name);
	}

	@Override
	public void msgBinConfiguration(Vector<Bin> bins) {
		log.add(new LoggedEvent("msgBinConfiguration received from FCS."));
	}

	@Override
	public void msgGantryAdded(Gantry gantry) {
		log.add(new LoggedEvent("msgGantryAdded received from FCS."));
	}

	@Override
	public void msgNeedThisPart(PartType p, Feeder f1) {
		log.add(new LoggedEvent("msgNeedThisPart received from Feeder " + f1.getName() + " requesting part type " + p + "."));
	}

	@Override
	public void msgDoneDeliveringParts(Gantry gantry) {
		log.add(new LoggedEvent("msgDoneDeliveringParts received from Gantry " + gantry.getName() + "."));
	}

}