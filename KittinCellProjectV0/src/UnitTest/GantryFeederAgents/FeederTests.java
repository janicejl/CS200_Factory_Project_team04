package UnitTest.GantryFeederAgents;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import server.Server;

import Agents.GantryFeederAgents.FeederAgent;
import Mocks.GantryFeederAgents.MockGantryAgent;
import Mocks.GantryFeederAgents.MockGantryControllerAgent;
import Mocks.GantryFeederAgents.MockLaneAgent;

public class FeederTests extends TestCase{

	public FeederAgent feeder1;
	public MockLaneAgent lane1;
	public MockLaneAgent lane2;
	public MockGantryControllerAgent gc;
	public MockGantryAgent gantry1;
	
	protected void setUp() throws Exception{
		super.setUp();
		lane1 = new MockLaneAgent("left", 0);
		lane2 = new MockLaneAgent("right", 1);
		feeder1 = new FeederAgent("Feeder 1", 5, lane1, lane2, 0, new Server() );
		gc = new MockGantryControllerAgent("gc");
		gantry1 = new MockGantryAgent("Gantry 1");
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
