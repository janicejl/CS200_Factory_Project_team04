package UnitTest.PartsRobotAgents;

import static org.junit.Assert.*;
import Mocks.PartsRobotAgents.*;
import Mocks.KitRobotAgents.*;
import Agents.VisionAgent.*;
import server.Server;
import Interface.PartsRobotAgent.*;
import Interface.VisionAgent.*;
import Interface.KitRobotAgent.*;

import org.junit.Test;

import java.util.*;

import junit.framework.Assert;
import junit.framework.TestCase;
import Agents.PartsRobotAgent.*;
import Agents.PartsRobotAgent.NestAgent.LaneStatus;
import Agents.PartsRobotAgent.NestAgent.PartsRobotStatus;
import Agents.PartsRobotAgent.NestAgent.AnimationStatus;
import Agents.PartsRobotAgent.NestAgent.NestStatus;
import Agents.KitRobotAgents.*;
import MoveableObjects.*;
import UnitTest.KitRobotAgents.LoggedEvent;
import data.*;
import data.Part.PartType;



public class NestAgentTest extends TestCase {

	public void testNestAgent()
	{
		MockLane lane = new MockLane("lane");
		MockVision camera = new MockVision("Vision");
		MockPartsRobot partsrobot = new MockPartsRobot("PartsRobot");
		NestAgent nest = new NestAgent(lane,camera,1);
		nest.setPartsRobotAgent(partsrobot);
		
		Assert.assertTrue("Proper Setup",nest.lanestate == LaneStatus.noAction);
		Assert.assertTrue("Proper Setup",nest.partsrobotstate == PartsRobotStatus.noAction);
		Assert.assertTrue("Proper Setup",nest.neststate == NestStatus.noParts);
		Assert.assertTrue("Proper Setup",nest.animationstate == NestAgent.AnimationStatus.noAction);
		
		nest.msgNeedThisPart(PartType.part1);
		Assert.assertTrue("Received proper type",nest.parttype == PartType.part1);
		Assert.assertTrue("Proper State",nest.partsrobotstate == PartsRobotStatus.wantsParts);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Asked Lane for Parts",lane.log.containsString("Nest Wants Part"));
		Assert.assertTrue("Parts robot state correct",nest.partsrobotstate == PartsRobotStatus.waitingForParts);
		
		nest.msgCanIPlacePart(lane);
		Assert.assertTrue("Lane Has Part",nest.lanestate == LaneStatus.hasPart);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Ready to take part",lane.log.containsString("Nest Ready for Part"));
		
		nest.msgHereIsPart(new Part(PartType.part1));
		Assert.assertTrue("Has Part",nest.nestslots[0]!=null);
		

		
		
		

		
	}
	
	
}
