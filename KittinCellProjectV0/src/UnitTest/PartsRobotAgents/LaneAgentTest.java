package UnitTest.PartsRobotAgents;

import Agents.PartsRobotAgent.LaneAgent;
import Agents.PartsRobotAgent.LaneAgent.FeederStatus;
import Agents.PartsRobotAgent.LaneAgent.LaneNestStatus;
import Agents.PartsRobotAgent.LaneAgent.LaneStatus;
import Agents.PartsRobotAgent.LaneAgent.OrderStatus;
import Mocks.PartsRobotAgents.MockNest;
import server.Server;
import org.junit.Test;

import data.Part.PartType;

import java.util.*;

import junit.framework.TestCase;
import junit.framework.Assert;
import static org.junit.Assert.*;

public class LaneAgentTest extends TestCase{

	public void testLaneNestInteraction(){
		MockNest nest = new MockNest("Nest");
		
		LaneAgent lane = new LaneAgent(nest,new Server(),"lane1",0);
		Assert.assertTrue("Lane Set up Properly",lane.neststate == LaneNestStatus.noAction);
		Assert.assertTrue("Lane Set up Properly",lane.orderstate == OrderStatus.noAction);
		Assert.assertTrue("Lane Set up Properly",lane.feederstate == FeederStatus.noAction);
		Assert.assertTrue("Lane Set up Properly",lane.lanestate == LaneStatus.noParts);
		
		lane.msgNeedThisPart(PartType.part1);
		Assert.assertTrue("Needs to ask for Parts",lane.orderstate == OrderStatus.partRequested);
		
		while(lane.pickAndExecuteAnAction());
		Assert.assertTrue("Ordered parts",lane.orderstate == OrderStatus.partOrdered);
		lane.msgPartAtEndOfLane();
		Assert.assertTrue("Has a part",!lane.lanequeue.isEmpty());
		Assert.assertTrue("lanestate correct",lane.lanestate == LaneStatus.partsAtEndOfLane);
		
		while(lane.pickAndExecuteAnAction());
		Assert.assertTrue("Messaged nest that lane has part",nest.log.containsString("Received Request to Place Part"));
		Assert.assertTrue("Nest status correct",lane.neststate == LaneNestStatus.askedToTakePart);
		
		lane.msgReadyForPart();
		Assert.assertTrue("Nest ready for part",lane.neststate == LaneNestStatus.readyForPart);
		while(lane.pickAndExecuteAnAction());
		Assert.assertTrue("Placed part",nest.log.containsString("Received Part"));
		
		
		
		
		
		
	}
	
	
}
