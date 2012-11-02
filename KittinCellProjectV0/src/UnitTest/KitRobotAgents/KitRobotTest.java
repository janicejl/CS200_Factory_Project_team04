package UnitTest.KitRobotAgents;

import static org.junit.Assert.*;

import org.junit.Test;

import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Mocks.KitRobotAgents.MockConveyorAgent;
import Mocks.KitRobotAgents.MockKitStand;
import MoveableObjects.Kit;

public class KitRobotTest {

	@Test
	public void StandTest() {


		MockConveyorAgent conveyor = new MockConveyorAgent("conveyor");
		
		KitRobotAgent robot_agent = new KitRobotAgent();
		
		
		
		MockKitStand kit_stand = new MockKitStand("kit stand");
		robot_agent.SetStandAgent(kit_stand);
		robot_agent.SetConveyorAgent(conveyor);
	
		assertEquals("The kit stand should have no messages " + kit_stand.log.toString(),0,kit_stand.log.size());
		assertEquals("The kit conveyor should have no messages " + conveyor.log.toString(),0,conveyor.log.size());
		robot_agent.msgGetKits(1);
		
		robot_agent.pickAndExecuteAnAction();
		
		assertTrue("The stand recieved the wrong message",kit_stand.log.containsString("Can I place kit"));
		
		robot_agent.msgPlaceKitAtPosition(0);
		
		robot_agent.pickAndExecuteAnAction();

		assertEquals("The kit conveyor should have one message",1,conveyor.log.size());
		
		Kit kit = new Kit();
		robot_agent.msgHereIsAKit(kit);
		
		robot_agent.pickAndExecuteAnAction();
		
	
		
	}

}
