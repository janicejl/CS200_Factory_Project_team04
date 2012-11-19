package UnitTest.VisionAgent;

import server.Server;
import Agents.VisionAgent.VisionAgent;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.MockPartsRobot;

import junit.framework.TestCase;


public class VisionAgentTest extends TestCase {
	
	// test that a nest has good parts
	public void testGoodPartsNest() {
		MockKitRobot kitRobot = new MockKitRobot("kti robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		
		VisionAgent vision = new VisionAgent("nests", kitRobot, partsRobot, server);
		
		assertTrue("check that vision agent is initialized to nest type", vision.type==VisionAgent.Type.NESTS_INSPECTOR);
		
		
	}
	
/*	// test that a nest has bad parts
	public void badPartsNestTest() {
		
	}
	
	// test that a kit is good
	public void goodKitTest() {
		
	}
	
	// test that a kit is bad
	public void badKitTest() {
		
	}*/
	
}
