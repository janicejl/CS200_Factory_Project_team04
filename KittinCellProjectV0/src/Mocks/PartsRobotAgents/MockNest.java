package Mocks.PartsRobotAgents;

import data.Part;
import data.PartInfo;
import Agent.*;
import Agents.VisionAgent.VisionAgent;
import Mocks.KitRobotAgents.*;
import UnitTest.KitRobotAgents.*;
import Interface.PartsRobotAgent.*;

public class MockNest extends MockAgent implements Nest{

	public EventLog log = new EventLog();
	public int index;
	
	public MockNest(String name) {
		super(name);
	}

	public MockNest(String name, int num) {
		super(name);
		index = num;
	}
	
	public void msgCanIPlacePart(Lane lane) {
		log.add(new LoggedEvent("Received Request to Place Part"));
	}

	
	public void msgHereIsPart(Part p) {
		log.add(new LoggedEvent("Received Part"));
		
	}

	@Override
	public void msgBadParts() {
		log.add(new LoggedEvent("Notified I Have Bad Parts"));
		
	}

	@Override
	public void msgGetPart() {
		log.add(new LoggedEvent("Asked for a Part"));
		
	}

	@Override
	public void msgNeedThisPart(PartInfo type) {
		log.add(new LoggedEvent("Received PartInfo " + type.getName()));
		
	}

	@Override
	public void msgAnimationDone() {
		log.add(new LoggedEvent("Notified Animation is Done"));
		
	}

	@Override
	public Integer getNumber() {
		return index;
	}

	@Override
	public PartInfo getPartInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVisionAgent(VisionAgent camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getIndex() {
		return index;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
