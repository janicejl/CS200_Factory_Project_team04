package UnitTest.KitRobotAgents;

import static org.junit.Assert.*;

import org.junit.Test;

import Agents.KitRobotAgents.KitConveyorAgent;
import Agents.KitRobotAgents.KitRobotAgent;
import Agents.KitRobotAgents.KitStandAgent;
import Mocks.KitRobotAgents.MockConveyorAgent;
import Mocks.KitRobotAgents.MockKitStand;

public class KitRobotTest {

	@Test
	public void StandTest() {


		MockConveyorAgent conveyor = new MockConveyorAgent("conveyor");
		
		KitRobotAgent robot_agent = new KitRobotAgent();
		
		
		
		MockKitStand kit_stand = new MockKitStand("kit stand");
		robot_agent.SetStandAgent(kit_stand);
		robot_agent.SetConveyorAgent(conveyor);
		robot_agent.startThread();
		assertEquals("The kit stand should have no messages " + kit_stand.log.toString(),0,kit_stand.log.size());
		
		robot_agent.msgGetKits(3);
		
		int timer = 0;
		int timeout = 1000 * 7;
		while (timer < timeout) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			timer += 50;
		}
		
		assertEquals("The kitstand should have one message it has " + kit_stand.log.size(),2,kit_stand.log.size());
		
		
		
	}

}
