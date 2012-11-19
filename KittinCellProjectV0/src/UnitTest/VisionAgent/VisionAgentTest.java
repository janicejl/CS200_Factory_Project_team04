package UnitTest.VisionAgent;

import java.util.*;
import data.Part;
import server.Server;
import Agents.VisionAgent.VisionAgent;
import Agents.VisionAgent.VisionAgent.State;
import Interface.PartsRobotAgent.Nest;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.MockNest;
import Mocks.PartsRobotAgents.MockPartsRobot; 
import junit.framework.TestCase;


public class VisionAgentTest extends TestCase {
	
	// test that a nest has good parts
	public void testGoodNest() {
		MockKitRobot kitRobot = new MockKitRobot("kti robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		
		VisionAgent vision = new VisionAgent("nests", kitRobot, partsRobot, server);
		
		assertTrue("check that vision agent is initialized to nest type", vision.type==VisionAgent.Type.NESTS_INSPECTOR);
		assertTrue("check that the vision agent is in idle state", vision.state==VisionAgent.State.IDLE);
		
		List<Part> partsList = Collections.synchronizedList( new ArrayList<Part>() );
			partsList.add(new Part("part1", "image"));
			partsList.add(new Part("part2", "image"));
			partsList.add(new Part("part3", "image"));
			partsList.add(new Part("part4", "image"));
		List<Nest> nestsList = Collections.synchronizedList( new ArrayList<Nest>() );
			nestsList.add(new MockNest("nest 1", 1));
			nestsList.add(new MockNest("nest 2", 2));
			nestsList.add(new MockNest("nest 3", 3));
			nestsList.add(new MockNest("nest 4", 4));
		
		vision.msgHereIsSchematic(partsList, nestsList);
		
		assertTrue("check that schematic was received", vision.state==State.SCHEMATIC_RECEIVED);
		
		vision.pickAndExecuteAnAction();
		
		assertTrue("check that consecutive nests were found", vision.state==State.READY_TO_TAKE_PICTURE);
		
		
	}
	
/*	// test that a nest has bad parts
	public void testBadNest() {
		
	}
	
	// test that a kit is good
	public void testGoodKit() {
		
	}
	
	// test that a kit is bad
	public void testBadKit() {
		
	}*/
	
}
