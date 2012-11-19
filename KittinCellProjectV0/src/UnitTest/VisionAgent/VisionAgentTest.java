package UnitTest.VisionAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

import data.Part;
import server.Server;
import Agents.VisionAgent.VisionAgent;
import Agents.VisionAgent.VisionAgent.State;
import Interface.PartsRobotAgent.Nest;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.*; 
import junit.framework.TestCase;


public class VisionAgentTest extends TestCase {
	
	// test that a nest has good parts
	public void testGoodNest() {
		MockKitRobot kitRobot = new MockKitRobot("kti robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		
		VisionAgent vision = new VisionAgent("nests", kitRobot, partsRobot, server);
		Semaphore flashpermit = new Semaphore(1);
		vision.setFlashPermit(flashpermit);
		
		assertTrue("check that vision agent is initialized to nest type", vision.type==VisionAgent.Type.NESTS_INSPECTOR);
		assertTrue("check that the vision agent is in idle state", vision.state==VisionAgent.State.IDLE);
		
		List<Part> partsList = Collections.synchronizedList( new ArrayList<Part>() );
			partsList.add(new Part("part1", "image"));
			partsList.add(new Part("part2", "image"));
			partsList.add(new Part("part3", "image"));
			partsList.add(new Part("part4", "image"));
		List<Nest> nestsList = Collections.synchronizedList( new ArrayList<Nest>() );
			MockNest nest1 = new MockNest("nest 1", 1); nestsList.add(nest1);
			MockNest nest2 = new MockNest("nest 2", 2); nestsList.add(nest2);
			MockNest nest3 = new MockNest("nest 3", 3); nestsList.add(nest3);
			MockNest nest4 = new MockNest("nest 4", 4); nestsList.add(nest4);
		
		
		vision.msgHereIsSchematic(partsList, nestsList);
		assertTrue("check that schematic was received", vision.state==State.SCHEMATIC_RECEIVED);
		
		vision.pickAndExecuteAnAction();
		assertTrue("no nests should be full yet", vision.state==State.SCHEMATIC_RECEIVED);
		assertTrue("no nests should be full yet", vision.fullNestsMap.size()==0);
		assertTrue("nests list should be full", vision.nestsList.size()==4);
		assertTrue("parts list should be full", vision.neededPartsList.size()==4);

		vision.msgImFull(nest1);
		vision.msgImFull(nest2);
		vision.msgImFull(nest3);
		vision.msgImFull(nest4);
		
		vision.pickAndExecuteAnAction();
		assertTrue("full nests map should be filled", vision.fullNestsMap.size()==0);
		
		
		
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
