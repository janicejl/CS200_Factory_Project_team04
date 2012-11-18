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

	public void testNestAgent1part()
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
		Assert.assertTrue("Only 1 Part",nest.nestslots[1]== null);
		Assert.assertTrue("Nest State needs check",nest.neststate == NestStatus.needCheck);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Asked for Picture",camera.log.containsString("Received Message that Nest is Full"));
		Assert.assertTrue("Proper Nest State",nest.neststate == NestStatus.noAction);
		
		nest.msgGetPart();
		Assert.assertTrue("Ready to give part",nest.partsrobotstate == PartsRobotStatus.readyforpart);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Gave part to partsrobot",partsrobot.log.containsString("Received Part"));
		Assert.assertTrue("No longer has part",nest.nestslots[0]==null);

		
	}
	
	public void testNestAgent2parts()
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
		nest.msgHereIsPart(new Part(PartType.part1));
		Assert.assertTrue("Has Part",nest.nestslots[0]!=null);
		Assert.assertTrue("Only 2 Parts",nest.nestslots[1]!= null);
		Assert.assertTrue("Nest State needs check",nest.neststate == NestStatus.needCheck);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Asked for Picture",camera.log.containsString("Received Message that Nest is Full"));
		Assert.assertTrue("Proper Nest State",nest.neststate == NestStatus.noAction);
		
		nest.msgGetPart();
		Assert.assertTrue("Ready to give part",nest.partsrobotstate == PartsRobotStatus.readyforpart);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Gave part to partsrobot",partsrobot.log.containsString("Received Part"));
		Assert.assertTrue("No longer has part",nest.nestslots[1]==null);
		Assert.assertTrue("Asked for another Picture",camera.log.size() == 2);
		Assert.assertTrue("Proper Nest State",nest.neststate == NestStatus.noAction);
		
		nest.msgGetPart();
		Assert.assertTrue("Ready to give part",nest.partsrobotstate == PartsRobotStatus.readyforpart);

		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Gave part to partsrobot",partsrobot.log.containsString("Received Part"));
		Assert.assertTrue("No longer has part",nest.nestslots[0]==null);
		
		
	}
	
	public void testPurge(){
		MockLane lane = new MockLane("lane");
		MockVision camera = new MockVision("Vision");
		MockPartsRobot partsrobot = new MockPartsRobot("PartsRobot");
		NestAgent nest = new NestAgent(lane,camera,1);
		nest.setPartsRobotAgent(partsrobot);
		nest.msgHereIsPart(new Part(PartType.part1));
		nest.msgHereIsPart(new Part(PartType.part1));
		nest.msgHereIsPart(new Part(PartType.part1));
		Assert.assertTrue("has 3 parts",nest.nestslots[2]!=null);
		Assert.assertTrue("Only 3 parts",nest.nestslots[3]==null);
		Assert.assertTrue("Nest State needs check",nest.neststate == NestStatus.needCheck);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Asked for Picture",camera.log.containsString("Received Message that Nest is Full"));
		Assert.assertTrue("Proper Nest State",nest.neststate == NestStatus.noAction);
		
		nest.msgBadParts();
		Assert.assertTrue("Proper Nest State for bad parts",nest.neststate == NestStatus.badParts);
		
		while(nest.pickAndExecuteAnAction());
		Assert.assertTrue("Purge took place",nest.nestslots[0]==null);
		Assert.assertTrue("Animation Call made for purge",nest.animationstate == AnimationStatus.purging);
		
		nest.msgAnimationDone();
		Assert.assertTrue("Animation state changed",nest.animationstate == AnimationStatus.noAction);
		
		
	}
	
	
}
