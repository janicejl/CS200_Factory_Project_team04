package UnitTest.GantryFeederAgents;

import static org.junit.Assert.*;

import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import server.Server;

import data.PartInfo;

import Agents.GantryFeederAgents.GantryControllerAgent;
import Mocks.GantryFeederAgents.MockFeederAgent;
import Mocks.GantryFeederAgents.MockGantryAgent;
import MoveableObjects.Bin;

public class GantryControllerTests extends TestCase {

	GantryControllerAgent gc;
	MockGantryAgent gantry1;
	MockFeederAgent feeder1;
	MockFeederAgent feeder2;
	Bin bin1;
	Bin bin2;
	PartInfo type1;
	PartInfo type2;
	Vector<Bin> bins;
	
	//MockFCSAgent fcs;
	
	@Before
	protected void setUp() throws Exception{
		super.setUp();
		gc = new GantryControllerAgent(new Server());
		gantry1 = new MockGantryAgent("Gantry 1");
		feeder1 = new MockFeederAgent("Feeder 1", 0);
		feeder2 = new MockFeederAgent("Feeder 2", 1);
		type1 = new PartInfo("Part 1", "source 1");
		type2 = new PartInfo("Part 2", "source 2");
		bin1 = new Bin(type1, 20);
		bin2 = new Bin(type2, 20);
		bins = new Vector<Bin>();
		bins.add(bin1);
		bins.add(bin2);
		gc.msgGantryAdded(gantry1);
	}
	
	@Test
	public void testGantryInteractions() {
		
		gc.msgBinConfiguration(bins);
		assertTrue("Gantry controller should have received msgBinConfiguration from FCS", gc.log.containsString("msgBinConfiguration"));
		
		gc.msgNeedThisPart(type1, feeder1);
		assertTrue("Gantry controller should have received msgNeedThisPart from Feeder 1", gc.log.containsString("msgNeedThisPart"));
		
		gc.pickAndExecuteAnAction();
		assertTrue("Gantry1 should have received msgGiveFeederParts from GC", gantry1.log.containsString("msgGiveFeederParts"));
		
		gc.msgDoneDeliveringParts(gantry1);
		assertTrue("Gantry Controller should have received msgDoneDeliveringParts from Gantry 1", gc.log.containsString("msgDoneDeliveringParts"));
		
		
	}
	
	@Test
	public void testMultipleFeederInteractions() {
		
		gc.msgBinConfiguration(bins);
		assertTrue("Gantry controller should have received msgBinConfiguration from FCS", 
				gc.log.containsString("msgBinConfiguration"));
		
		gc.msgNeedThisPart(type1, feeder1);
		assertTrue("Gantry controller should have received msgNeedThisPart from Feeder 1", 
				gc.log.containsString("msgNeedThisPart"));
		
		gc.log.clear();
		
		gc.msgNeedThisPart(type2, feeder2);
		assertTrue("Gantry controller should have received msgNeedThisPart from Feeder 2", 
				gc.log.containsString("msgNeedThisPart"));
		
		while(gc.pickAndExecuteAnAction());		
		//gc.pickAndExecuteAnAction();
		assertTrue("Gantry1 should have received msgGiveFeederParts from GC", gantry1.log.containsString("msgGiveFeederParts"));
		
		gc.msgDoneDeliveringParts(gantry1);
		assertTrue("Gantry Controller should have received msgDoneDeliveringParts from Gantry 1", gc.log.containsString("msgDoneDeliveringParts"));
		
		gc.log.clear();
		while(gc.pickAndExecuteAnAction());		
		//gc.pickAndExecuteAnAction();
		assertTrue("Gantry1 should have received msgGiveFeederParts from GC", gantry1.log.containsString("msgGiveFeederParts"));
		
		gc.msgDoneDeliveringParts(gantry1);
		assertTrue("Gantry Controller should have received msgDoneDeliveringParts from Gantry 1", gc.log.containsString("msgDoneDeliveringParts"));
		
		
	}
	
	

}
