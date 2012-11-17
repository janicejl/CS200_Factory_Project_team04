package Mocks.PartsRobotAgents;

import java.util.List;
import java.util.concurrent.Semaphore;

import data.Kit;
import data.Part;
import Mocks.KitRobotAgents.MockAgent;
import UnitTest.KitRobotAgents.*;
import Agents.PartsRobotAgent.NestAgent;
import Interface.VisionAgent.*;
import Interface.PartsRobotAgent.*;


public class MockVision extends MockAgent implements Vision{

	public EventLog log = new EventLog();

	
	public MockVision(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsSchematic(List<Part> partsList,
			List<Nest> nestsList) {
		log.add(new LoggedEvent("Received Schematic"));
		
	}

	@Override
	public void msgImFull(Nest nest) {
		log.add(new LoggedEvent("Received Message that Nest is Full"));
		
	}

	@Override
	public void msgTakePicture(Kit k) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void setFlashPermit(Semaphore flashpermit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCameraAvailable() {
		// TODO Auto-generated method stub
		
	}

}
