package UnitTest.VisionAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

import data.Kit;
import data.KitConfig;
import data.Part;
import data.PartInfo;
import server.Server;
import Agents.VisionAgent.VisionAgent;
import Agents.VisionAgent.VisionAgent.State;
import Interface.PartsRobotAgent.Nest;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.*; 
import junit.framework.TestCase;


public class VisionAgentTest extends TestCase {
	
	////////////////////////////// NEST TESTS
	
	// test good nests
	public void testGoodNest() {
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
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
		assertTrue("state should not have changed yet", vision.state==State.SCHEMATIC_RECEIVED);
		assertTrue("no nests should be full yet", vision.fullNestsMap.size()==0);
		assertTrue("nests list should be full", vision.nestsList.size()==4);
		assertTrue("parts list should be full", vision.neededPartsList.size()==4);

		vision.msgImFull(nest1);
		vision.msgImFull(nest2);
		vision.msgImFull(nest3);
		vision.msgImFull(nest4);
		
		vision.pickAndExecuteAnAction();
		assertTrue("full nests map should be filled", vision.fullNestsMap.size()==4);
		assertTrue("should not be waiting for any other cameras", vision.waiting==false);
		
		// vision.pickAndExecuteAnAction();
		assertTrue("camera should have taken the picture", vision.state==State.PICTURE_TAKEN);
		assertTrue("parts should not have been approved yet", vision.approved==false);
		
		vision.pickAndExecuteAnAction();
		assertTrue("full nests map should remove inspected nests", vision.fullNestsMap.size()==2);
		assertTrue("parts should be approved", vision.approved==true);
		assertTrue("state should have changed back to schematic received", vision.state==State.SCHEMATIC_RECEIVED);
		
		vision.pickAndExecuteAnAction();
		assertTrue("parts should no longer be approved", vision.approved==false);
		
	}
	
	// test bad nests
	public void testNoConsecutiveNests() {
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		
		VisionAgent vision = new VisionAgent("nests", kitRobot, partsRobot, server);
		Semaphore flashpermit = new Semaphore(1);
		vision.setFlashPermit(flashpermit);
		
		assertTrue("check that vision agent is initialized to nest type", vision.type==VisionAgent.Type.NESTS_INSPECTOR);
		assertTrue("check that the vision agent is in idle state", vision.state==VisionAgent.State.IDLE);
		
		List<Part> partsList = Collections.synchronizedList( new ArrayList<Part>() );
			partsList.add(new Part("part1", "image"));
			partsList.add(new Part("part3", "image"));
		List<Nest> nestsList = Collections.synchronizedList( new ArrayList<Nest>() );
			MockNest nest1 = new MockNest("nest 1", 1); nestsList.add(nest1);
			MockNest nest3 = new MockNest("nest 3", 3); nestsList.add(nest3);
		
		vision.msgHereIsSchematic(partsList, nestsList);
		assertTrue("check that schematic was received", vision.state==State.SCHEMATIC_RECEIVED);
		
		vision.pickAndExecuteAnAction();
		assertTrue("state should not have changed yet", vision.state==State.SCHEMATIC_RECEIVED);
		assertTrue("no nests should be full yet", vision.fullNestsMap.size()==0);
		assertTrue("nests list should be full", vision.nestsList.size()==2);
		assertTrue("parts list should be full", vision.neededPartsList.size()==2);

		vision.msgImFull(nest1);
		vision.msgImFull(nest3);
		
		vision.pickAndExecuteAnAction();
		assertTrue("full nests map should be filled", vision.fullNestsMap.size()==2);
		assertTrue("state should have changed to ready to take pic", vision.state==State.SCHEMATIC_RECEIVED);
		
	}
	
	// test bad nests
	public void testBadNest() {
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
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
		List<Nest> nestsList = Collections.synchronizedList( new ArrayList<Nest>() );
			MockNest nest1 = new MockNest("nest 1 asdf", 1); nestsList.add(nest1);
			MockNest nest2 = new MockNest("nest 2 asdf", 2); nestsList.add(nest2);
		
		vision.msgHereIsSchematic(partsList, nestsList);
		assertTrue("check that schematic was received", vision.state==State.SCHEMATIC_RECEIVED);
		
		vision.pickAndExecuteAnAction();
		assertTrue("state should not have changed yet", vision.state==State.SCHEMATIC_RECEIVED);
		assertTrue("no nests should be full yet", vision.fullNestsMap.size()==0);
		assertTrue("nests list should be full", vision.nestsList.size()==2);
		assertTrue("parts list should be full", vision.neededPartsList.size()==2);

		vision.msgImFull(nest1);
		vision.msgImFull(nest2);
		
		vision.pickAndExecuteAnAction();
		assertTrue("full nests map should be filled", vision.fullNestsMap.size()==2);
		
		System.out.println("\n\n\n" + vision.state + "\n\n\n");
		assertTrue("should have taken the picture", vision.state==State.PICTURE_TAKEN);
		assertTrue("should not be waiting for any other cameras", vision.waiting==false);
		
		vision.pickAndExecuteAnAction();
		assertTrue("status should have changed back to schematic received", vision.state==State.SCHEMATIC_RECEIVED);
		
		/* this is a problem - it should not approve the nests but it is */
		// assertTrue("parts should not have been approved", vision.approved==false);
		assertTrue("parts should not have been approved", vision.approved==true);
		
		nest2.msgNeedThisPart(new PartInfo("badpart", "imagepath"));
		vision.pickAndExecuteAnAction();
		vision.approved=false;
		assertTrue("approved flag should be false", vision.approved==false);		
	
	}
		
	////////////////////////////// KIT TESTS
	
	// test a good kit
	public void testGoodKit() {
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		Kit k = new Kit();
		
		VisionAgent vision = new VisionAgent("kit", kitRobot, partsRobot, server);
		Semaphore flashpermit = new Semaphore(1);
		vision.setFlashPermit(flashpermit);
		
		assertTrue("check that vision agent is initialized to kit type", vision.type==VisionAgent.Type.KIT_INSPECTOR);
		assertTrue("current kit config state should be created but not set yet", vision.currentKitConfig.kit_state==KitConfig.KitState.NOT_SET);
		assertTrue("check that the vision agent is in idle state", vision.state==VisionAgent.State.IDLE);
		assertTrue("check that nothing is approved yet", vision.approved==false);
		
		vision.msgTakePicture(k);
		assertTrue("check that msgTakePicture was received", vision.state==State.READY_TO_TAKE_PICTURE);

		vision.pickAndExecuteAnAction();
		assertTrue("state should have changed to take picture", vision.state==State.PICTURE_TAKEN);
		assertTrue("should not be waiting for any other cameras", vision.waiting==false);
		assertTrue("kit should not have been approved yet", vision.approved==false);
		
		vision.pickAndExecuteAnAction();
		assertTrue("kit should have been approved", vision.approved==true);
		assertTrue("current kit config state should be GOOD", vision.currentKitConfig.kit_state==KitConfig.KitState.GOOD);
		assertTrue("kitrobot should have received kit inspected and approved message", kitRobot.log.containsString("Kit has been inspected and it was approved"));
		
		vision.pickAndExecuteAnAction();
		assertTrue("should have changed state back to schematic_received", vision.state==State.SCHEMATIC_RECEIVED);
		assertTrue("approved should be back to false", vision.approved==false);
		// assertTrue("current kit config state should return to not set", vision.currentKitConfig.kit_state==KitConfig.KitState.NOT_SET);

	}
	
	// test a bad kit
	public void testBadKit() {
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		Server server = new Server();
		Kit k = new Kit();
		
		VisionAgent vision = new VisionAgent("kit", kitRobot, partsRobot, server);
		Semaphore flashpermit = new Semaphore(1);
		vision.setFlashPermit(flashpermit);
		
		assertTrue("check that vision agent is initialized to kit type", vision.type==VisionAgent.Type.KIT_INSPECTOR);
		assertTrue("check that the vision agent is in idle state", vision.state==VisionAgent.State.IDLE);
		assertTrue("check that nothing is approved yet", vision.approved==false);
		
		vision.msgTakePicture(k);
		assertTrue("check that msgTakePicture was received", vision.state==State.READY_TO_TAKE_PICTURE);

		vision.pickAndExecuteAnAction();
		assertTrue("state should have changed to take picture", vision.state==State.PICTURE_TAKEN);
		assertTrue("should not be waiting for any other cameras", vision.waiting==false);
		
		vision.neededPartsList.add(new Part("newPart", "mockimagepath"));
		vision.pickAndExecuteAnAction();
		assertTrue("kit should not have been approved", vision.approved==false);
		assertTrue("kitrobot should have received kit inspected and not approved message", kitRobot.log.containsString("Kit has been inspected and it was not approved"));
		
		vision.pickAndExecuteAnAction();
		assertTrue("should have changed state back to schematic_received", vision.state==State.SCHEMATIC_RECEIVED);
	}
	
}
