package UnitTest.GantryFeederAgents;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import data.PartInfo;

import server.Server;

import Agents.GantryFeederAgents.GantryAgent;
import Mocks.GantryFeederAgents.MockFeederAgent;
import Mocks.GantryFeederAgents.MockGantryControllerAgent;
import MoveableObjects.Bin;

public class GantryTests extends TestCase {

	GantryAgent gantry;
	MockFeederAgent feeder1;
	PartInfo type1;
	Bin bin1;
	
	@Before
	protected void setUp() throws Exception{
		super.setUp();
		gantry = new GantryAgent("Gantry 1", new Server());
		feeder1 = new MockFeederAgent("Feeder 1", 0);
		type1 = new PartInfo("Part 1", "source 1");
		bin1 = new Bin(type1, 20);	
	}
	
	@Test
	public void testFeederInteraction() {
		gantry.msgGiveFeederParts(feeder1, bin1);
		assertTrue("Gantry should have received msgGiveFeederParts from GC", gantry.log.containsString("msgGiveFeederParts"));
		
		gantry.pickAndExecuteAnAction();
		assertTrue("Feeder should have received msgHaveParts from gantry.", feeder1.log.containsString("msgHaveParts"));
		
		gantry.msgReadyForParts();
		assertTrue("Gantry should have received msgReadyForParts from feeder.", gantry.log.containsString("msgReadyForParts"));
		
		while(gantry.pickAndExecuteAnAction());
		assertTrue("Feeder should have received msgHereAreParts from gantry.", feeder1.log.containsString("msgHereAreParts"));
		assertTrue("Gantry Controller should have received msgDoneDeliveringParts", gc.log.containsString("msgDoneDeliveringParts"));
		
	}

}
