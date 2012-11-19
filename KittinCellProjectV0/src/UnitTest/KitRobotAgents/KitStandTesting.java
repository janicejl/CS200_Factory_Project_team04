package UnitTest.KitRobotAgents;

import static org.junit.Assert.*;

import org.junit.Test;

import data.Kit;

import server.Server;

import Agents.KitRobotAgents.KitStandAgent;
import Interface.PartsRobotAgent.PartsRobot;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.MockPartsRobot;

public class KitStandTesting {

	@Test
	public void KitStandTestWithKitRobot() {
		
		Server server = new Server();
		KitStandAgent kit_agent = new KitStandAgent(server);
		
		MockKitRobot robot = new MockKitRobot("robot");
		kit_agent.SetRobotAgent(robot);
		kit_agent.msgCanIPlaceKit();
		
		assertEquals("Mock robot should have no messages",0,robot.log.size());
		
		kit_agent.pickAndExecuteAnAction();
		
		
		assertEquals("Mock robot should only have 1 message",1,robot.log.size());
		
		assertTrue("The mock robot got the wrong messaged fired",robot.log.containsString("Place kit at position"));
		
	}
	
	
	public void KitStandWithPartRobot()
	{
		MockPartsRobot parts = new MockPartsRobot("parts");
		Server server = new Server();
		KitStandAgent kit_agent = new KitStandAgent(server);
		
		assertEquals("Mock robot should have no messages",0,parts.log.size());
		
		
		Kit k = new Kit();
		kit_agent.msgPlacingKit(k);
		
		kit_agent.pickAndExecuteAnAction();
		
		kit_agent.msgIsThereEmptyKit();
		
		kit_agent.pickAndExecuteAnAction();
		
		
		assertEquals("Mock robot should have 1 message",1,parts.log.size());
		
		assertTrue("The robot got the wrong message fired",parts.log.containsString("Kit empty"));
		
		
	}

}
