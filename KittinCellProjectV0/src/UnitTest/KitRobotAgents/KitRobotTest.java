package UnitTest.KitRobotAgents;

import static org.junit.Assert.*;

import org.junit.Test;

import Agents.KitRobotAgents.KitRobotAgent;
import Mocks.KitRobotAgents.MockKitStand;

public class KitRobotTest {

	@Test
	public void StandTest() {

		KitRobotAgent robot_agent = new KitRobotAgent();
		MockKitStand kit_stand = new MockKitStand("kit stand");
		
		assertEquals("The kit stand should have no messages " + kit_stand.log.toString(),0,kit_stand.log.size());
		
		
	}

}
