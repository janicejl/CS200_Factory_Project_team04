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
	
	public MockNest(String name) {
		super(name);
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
		log.add(new LoggedEvent("Received PartType " + type));
		
	}

	@Override
	public void msgAnimationDone() {
		log.add(new LoggedEvent("Notified Animation is Done"));
		
	}

	@Override
	public Integer getNumber() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
