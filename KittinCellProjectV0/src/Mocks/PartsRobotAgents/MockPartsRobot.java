package Mocks.PartsRobotAgents;

import java.util.List;

import data.Part;
import data.Part.PartType;
import Agents.PartsRobotAgent.TestGUI;
import Interface.PartsRobotAgent.PartsRobot;
import Interface.VisionAgent.Vision;
import Mocks.KitRobotAgents.MockAgent;
import UnitTest.KitRobotAgents.EventLog;
import UnitTest.KitRobotAgents.LoggedEvent;

public class MockPartsRobot extends MockAgent implements PartsRobot{

	
	public EventLog log = new EventLog();
	
	public MockPartsRobot(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMakeThisKit(List<PartType> kitrecipe, int ct) {
		log.add(new LoggedEvent("Make this kind of kit"));
		
	}

	@Override
	public void msgPartsApproved(int nestindex) {
		log.add(new LoggedEvent("Parts approved"));
		
	}

	@Override
	public void msgEmptyKit(int position) {
		log.add(new LoggedEvent("Kit empty"));
		
	}

	@Override
	public void msgAnimationDone() {
		log.add(new LoggedEvent("Animation completed"));
		
	}

	@Override
	public void msgHereIsPart(Part p) {
		log.add(new LoggedEvent("Here is your part"));
		
	}

	@Override
	public void placeParts() {
		log.add(new LoggedEvent("Place parts into kit"));
		
	}

	@Override
	public void kitFinished() {
		log.add(new LoggedEvent("Kit Finished"));		
	}

	@Override
	public void setTestGUI(TestGUI test) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMockVisionAgents(List<Vision> cams) {
		// TODO Auto-generated method stub
		
	}

}
