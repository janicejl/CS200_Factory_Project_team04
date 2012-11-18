package Mocks.PartsRobotAgents;

import java.util.List;

import data.Part;
import Agents.PartsRobotAgent.TestGUI;
import Interface.PartsRobotAgent.*;
import Interface.VisionAgent.Vision;
import Mocks.KitRobotAgents.MockAgent;
import UnitTest.KitRobotAgents.EventLog;
import UnitTest.KitRobotAgents.LoggedEvent;
import data.PartInfo;

public class MockPartsRobot extends MockAgent implements PartsRobot{

	public EventLog log = new EventLog();

	
	public MockPartsRobot(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgMakeThisKit(List<PartInfo> kitrecipe, int ct) {
		log.add(new LoggedEvent("Received Recipe"));
		
	}


	@Override
	public void msgPartsApproved(int nestindex) {
		log.add(new LoggedEvent("Nest Has Part"));
		
	}


	@Override
	public void msgEmptyKit(int position) {
		log.add(new LoggedEvent("Empty Kit Available"));
		
	}


	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsPart(Part p) {
		log.add(new LoggedEvent("Received Part"));
	}


	@Override
	public void placeParts() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void kitFinished() {
		// TODO Auto-generated method stub
		
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
