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
import Agents.PartsRobotAgent.PartsRobotAgent.AnimationStatus;
import Agents.PartsRobotAgent.PartsRobotAgent.CurrentKit;
import Agents.PartsRobotAgent.PartsRobotAgent.KitStatus;
import Agents.PartsRobotAgent.PartsRobotAgent.NestStatus;
import Agents.KitRobotAgents.*;
import MoveableObjects.*;
import UnitTest.KitRobotAgents.LoggedEvent;
import data.*;

public class PartsRobotTest extends TestCase{
	
	
	
	public void testPartsRobotWith4Parts()
	{
		PartsRobotAgent partsrobotagent;
		MockNest nest1 = new MockNest("nest1");
		MockNest nest2 = new MockNest("nest2");
		MockNest nest3 = new MockNest("nest3");
		MockNest nest4 = new MockNest("nest4");
		List<Nest> nests = new ArrayList<Nest>();
		nests.add(nest1);nests.add(nest2);nests.add(nest3);nests.add(nest4);
		MockVision camera1 = new MockVision("camera1");
		MockVision camera2 = new MockVision("camera2");
		List<Vision> cameras = new ArrayList<Vision>();
		cameras.add(camera1);cameras.add(camera2);
		MockKitStand kitstand = new MockKitStand("Kit Stand");
		partsrobotagent = new PartsRobotAgent(nests,cameras,kitstand);
		Assert.assertTrue("Camera size = 2",partsrobotagent.cameras.size() == 2);
		Assert.assertTrue("Nests size = 4",partsrobotagent.nests.size() == 4);
		Assert.assertTrue("2nd nest equals nest2",partsrobotagent.nests.get(1).nest == nest2);
		Assert.assertTrue("Recipe is Empty",partsrobotagent.recipe.isEmpty());
		KitInfo kitinfo = new KitInfo("Test Kit");
		PartInfo p1 = new PartInfo("parttype1","image");
		PartInfo p2 = new PartInfo("parttype2","image");
		PartInfo p3 = new PartInfo("parttype3","image");
		PartInfo p4 = new PartInfo("parttype4","image");
		kitinfo.add(p1);kitinfo.add(p2);kitinfo.add(p3);kitinfo.add(p4);
		
		partsrobotagent.msgMakeThisKit(kitinfo, 3);
		Assert.assertTrue("Recipe read properly",partsrobotagent.recipe.get(1)==p2);
		Assert.assertTrue("Partsneeded Set correctly",partsrobotagent.kit1.partsneeded.get(2)==p3);
		Assert.assertTrue("Kit status correct",partsrobotagent.kit2.state==KitStatus.notAvailable);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Camera received recipe",camera1.log.size()==1);
		Assert.assertTrue("Cameras received recipe",camera1.log.containsString("Received Schematic"));
		Assert.assertTrue("Nest 4 received proper part type",nest4.log.containsString("Received PartInfo parttype4"));
		Assert.assertTrue("KitStand asked for 2 kits",kitstand.log.size()==2);
		Assert.assertTrue("KitStand received correct message",kitstand.log.containsString("Is there an empty kit"));
		Assert.assertTrue("Confirmed that recipe sent to camera",partsrobotagent.camerahasrecipe);
		
		partsrobotagent.msgEmptyKit(0);
		Assert.assertTrue("Received Empty Kit message",partsrobotagent.kit1.state == KitStatus.available);
		Assert.assertTrue("Did not receive two Empty Kit messages",partsrobotagent.kit2.state == KitStatus.pending);
		
		partsrobotagent.msgEmptyKit(1);
		Assert.assertTrue("Received Empty Kit message",partsrobotagent.kit2.state == KitStatus.available);
		
		partsrobotagent.msgPartsApproved(1);
		Assert.assertTrue("Parts Approved",partsrobotagent.nests.get(0).state == NestStatus.hasPart);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Moving to Part",partsrobotagent.kit1.partsneeded.size()==3);
		Assert.assertTrue("Gripper is occupied",partsrobotagent.grippers[0].full);
		Assert.assertTrue("Gripper destination nest is correct",partsrobotagent.grippers[0].nestindex == 1);
		Assert.assertTrue("Moving to Nest",partsrobotagent.animationstate == AnimationStatus.movingToNest);
		
		partsrobotagent.msgAnimationDone();
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Asked nest for part",nest1.log.containsString("Asked for a Part"));
		
		
		partsrobotagent.msgHereIsPart(new Part(p1));
		Assert.assertTrue("Has part",partsrobotagent.grippers[0].p!= null);
		Assert.assertTrue("Nest no longer has part",partsrobotagent.nests.get(0).state!= NestStatus.hasPart);
		
		partsrobotagent.msgPartsApproved(2);
		Assert.assertTrue("Parts Approved",partsrobotagent.nests.get(1).state == NestStatus.hasPart);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Moving to Part",partsrobotagent.kit1.partsneeded.size()==2);
		Assert.assertTrue("Gripper is occupied",partsrobotagent.grippers[1].full);
		Assert.assertTrue("Gripper destination nest is correct",partsrobotagent.grippers[1].nestindex == 2);
		Assert.assertTrue("Moving to Nest",partsrobotagent.animationstate == AnimationStatus.movingToNest);
				
		partsrobotagent.msgAnimationDone();
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Asked nest for part",nest2.log.containsString("Asked for a Part"));
		
		
		partsrobotagent.msgHereIsPart(new Part(p2));
		Assert.assertTrue("Has part",partsrobotagent.grippers[1].p!= null);
		
		partsrobotagent.msgPartsApproved(3);
		Assert.assertTrue("Parts Approved",partsrobotagent.nests.get(2).state == NestStatus.hasPart);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Moving to Part",partsrobotagent.kit1.partsneeded.size()==1);
		Assert.assertTrue("Gripper is occupied",partsrobotagent.grippers[2].full);
		Assert.assertTrue("Gripper destination nest is correct",partsrobotagent.grippers[2].nestindex == 3);
		Assert.assertTrue("Moving to Nest",partsrobotagent.animationstate == AnimationStatus.movingToNest);
				
		partsrobotagent.msgAnimationDone();
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Asked nest for part",nest3.log.containsString("Asked for a Part"));
		
		
		partsrobotagent.msgHereIsPart(new Part(p3));
		Assert.assertTrue("Has part",partsrobotagent.grippers[2].p!= null);
		
		partsrobotagent.msgPartsApproved(4);
		Assert.assertTrue("Parts Approved",partsrobotagent.nests.get(3).state == NestStatus.hasPart);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Moving to Part",partsrobotagent.kit1.partsneeded.isEmpty());
		Assert.assertTrue("Gripper is occupied",partsrobotagent.grippers[3].full);
		Assert.assertTrue("Gripper destination nest is correct",partsrobotagent.grippers[3].nestindex == 4);
		Assert.assertTrue("Moving to Nest",partsrobotagent.animationstate == AnimationStatus.movingToNest);
				
		partsrobotagent.msgAnimationDone();
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Asked nest for part",nest4.log.containsString("Asked for a Part"));
		
	
		partsrobotagent.msgHereIsPart(new Part(p4));
		Assert.assertTrue("Has part",partsrobotagent.grippers[3].p!= null);
		Assert.assertTrue("All grippers full",partsrobotagent.allGrippersFull());
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Moving to Kit Stand",partsrobotagent.animationstate == AnimationStatus.movingToStand);
		
		partsrobotagent.msgMovementDone();
		Assert.assertTrue("At Kit Stand",partsrobotagent.animationstate == AnimationStatus.atStand);
		
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Grippers Empty",partsrobotagent.grippersEmpty());
		Assert.assertTrue("KitStand received parts",kitstand.log.containsString("Got parts"));
		
		partsrobotagent.msgPartsDropped();
		while(partsrobotagent.pickAndExecuteAnAction());
		Assert.assertTrue("Kit is finished",partsrobotagent.currentkit == CurrentKit.kit2);
		Assert.assertTrue("Asked for new kit1",partsrobotagent.kit1.state == KitStatus.pending);
		Assert.assertTrue("Notified stand kit is done",kitstand.log.containsString("Kit is done"));
		Assert.assertTrue("Lowered count",partsrobotagent.count == 2);

		
		
		
	}
	

	
		

}
