package UnitTest.GantryFeederAgents;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import data.PartInfo;

import server.Server;

import Agents.GantryFeederAgents.FeederAgent;
import Mocks.GantryFeederAgents.MockGantryAgent;
import Mocks.GantryFeederAgents.MockGantryControllerAgent;
import Mocks.GantryFeederAgents.MockLaneAgent;
import MoveableObjects.Bin;

public class FeederTests extends TestCase{

	public FeederAgent feeder1;
	public MockLaneAgent lane1;
	public MockLaneAgent lane2;
	public MockGantryControllerAgent gc;
	public MockGantryAgent gantry1;
	public PartInfo type1;
	public PartInfo type2;
	public Bin bin1;
	public Bin bin2;
	
	@Before
	protected void setUp() throws Exception{
		super.setUp();
		lane1 = new MockLaneAgent("left", 0);
		lane2 = new MockLaneAgent("right", 1);
		feeder1 = new FeederAgent("Feeder 1", 5, lane1, lane2, 0, new Server() );
		gc = new MockGantryControllerAgent("gc");
		gantry1 = new MockGantryAgent("Gantry 1");
		type1 = new PartInfo("Part 1", "source 1");
		type2 = new PartInfo("Part 2", "source 2");
		bin1 = new Bin(type1, 20);
		bin2 = new Bin(type2, 20);
		feeder1.setGantryController(gc);
	}
	
	@Test
	public void testSingleLaneInteraction() {

		
		feeder1.msgNeedThisPart(type1, "left");
		assertTrue("Feeder should have received msgNeedThisPart.", feeder1.log.containsString("msgNeedThisPart") && feeder1.log.containsString("left"));
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Gantry Controller should have received msgNeedThisPart from Feeder 1", gc.log.containsString("msgNeedThisPart"));
		assertTrue("Feeder 1 should have no parts.", feeder1.getQuantity() == 0);
		
		feeder1.msgHaveParts(gantry1);
		assertTrue("Feeder 1 should have received msgHaveParts from Gantry 1", feeder1.log.containsString("msgHaveParts"));
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Gantry 1 should have received msgReadyForParts()", gantry1.log.containsString("msgReadyForParts"));
		
		feeder1.msgHereAreParts(bin1);
		assertTrue("Feeder1 should have received msgHereAreParts from Gantry 1.", feeder1.log.containsString("msgHereAreParts"));
		
		feeder1.pickAndExecuteAnAction();
		assertFalse("Left Lane should not have received msgHereIsAPart.", lane1.log.containsString("msgHereIsAPart"));
		
		feeder1.msgLaneIsReadyForParts("left");
		assertTrue("Feeder 1 should have received msgLaneIsReadyForParts from the left lane.", 
				feeder1.log.containsString("msgLaneIsReadyForParts") && feeder1.log.containsString("left"));
		
		feeder1.pickAndExecuteAnAction();
		//System.out.println(feeder1.getState());
		assertTrue("Left Lane should have received msgHereIsAPart.", lane1.log.containsString("msgHereIsAPart"));
		assertTrue("Feeder should have 19 parts.", feeder1.getQuantity() == 19);
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Feeder should have 18 parts.", feeder1.getQuantity() == 18);
		
	}
	
	
	@Test
	public void testBothLaneInteraction() {

		
		feeder1.msgNeedThisPart(type1, "left");
		assertTrue("Feeder should have received msgNeedThisPart.", feeder1.log.containsString("msgNeedThisPart") && feeder1.log.containsString("left"));
		feeder1.msgNeedThisPart(type2, "right");
		assertTrue("Feeder should have received msgNeedThisPart.", feeder1.log.containsString("msgNeedThisPart") && feeder1.log.containsString("right"));
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Gantry Controller should have received msgNeedThisPart from Feeder 1", gc.log.containsString("msgNeedThisPart"));
		assertTrue("Feeder 1 should have no parts.", feeder1.getQuantity() == 0);
		
		feeder1.msgHaveParts(gantry1);
		assertTrue("Feeder 1 should have received msgHaveParts from Gantry 1", feeder1.log.containsString("msgHaveParts"));
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Gantry 1 should have received msgReadyForParts()", gantry1.log.containsString("msgReadyForParts"));
		
		feeder1.msgHereAreParts(bin1);
		assertTrue("Feeder1 should have received msgHereAreParts from Gantry 1.", feeder1.log.containsString("msgHereAreParts"));
		
		feeder1.pickAndExecuteAnAction();
		assertFalse("Left Lane should not have received msgHereIsAPart.", lane1.log.containsString("msgHereIsAPart"));
		
		feeder1.msgLaneIsReadyForParts("left");
		assertTrue("Feeder 1 should have received msgLaneIsReadyForParts from the left lane.", 
				feeder1.log.containsString("msgLaneIsReadyForParts") && feeder1.log.containsString("left"));
		
		feeder1.pickAndExecuteAnAction();
		//System.out.println(feeder1.getState());
		assertTrue("Left Lane should have received msgHereIsAPart.", lane1.log.containsString("msgHereIsAPart"));
		assertTrue("Feeder should have 19 parts.", feeder1.getQuantity() == 19);
		
		feeder1.pickAndExecuteAnAction();
		assertTrue("Feeder should have 18 parts.", feeder1.getQuantity() == 18);
		
	}


}
