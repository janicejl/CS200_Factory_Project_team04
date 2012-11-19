package UnitTest.FCSAgent;

import data.KitInfo;
import data.PartInfo;
import server.Server;
import Agents.FCSAgent.FCSAgent;
import Agents.VisionAgent.VisionAgent;
import Mocks.GantryFeederAgents.MockGantryControllerAgent;
import Mocks.KitRobotAgents.MockKitRobot;
import Mocks.PartsRobotAgents.MockPartsRobot;
import junit.framework.TestCase;

public class FCSAgentTest extends TestCase {
	
	
	public void testFCS() {
		
		Server server = new Server();
		MockPartsRobot partsRobot = new MockPartsRobot("parts robot");
		MockKitRobot kitRobot = new MockKitRobot("kit robot");
		MockGantryControllerAgent gantryController = new MockGantryControllerAgent("gantry controller");
		
		FCSAgent fcs = new FCSAgent(server, partsRobot, kitRobot, gantryController);
		
		assertTrue("check that the fcs has no kits needed yet", fcs.numKitsNeeded==0);
		assertTrue("check that the bins vector is empty", fcs.binsList.size()==0);
		
		
		KitInfo info = new KitInfo("info");
		PartInfo part1 = new PartInfo("part1", "imagepath");
		PartInfo part2 = new PartInfo("part2", "imagepath");
		PartInfo part3 = new PartInfo("part3", "imagepath");
		info.add(part1);
		info.add(part2);
		info.add(part3);
		
		int numKitsNeeded = 10;
		fcs.msgHereIsKitConfig(info, numKitsNeeded);
		
		assertTrue("check that the fcs has received the config", fcs.numKitsNeeded==numKitsNeeded);
		assertTrue("check that the fcs has received the config", fcs.binsList.size()==info.getSize());
		assertTrue("check that the fcs has sent the recipe to the parts robot", partsRobot.log.containsString("Received Recipe"));
		assertTrue("check that the fcs has sent kits request to the kit robot", kitRobot.log.containsString("Received message to get " + numKitsNeeded + " kits"));
		
		fcs.msgKitCompleted();
		
		fcs.pickAndExecuteAnAction();
		assertTrue("check that the fcs has reduced the num of kits needed by 1", fcs.numKitsNeeded==9);
		
		
		
		
	}
}